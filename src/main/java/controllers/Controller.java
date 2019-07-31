package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.CustomException;
import models.Errors;

import java.io.IOException;

public class Controller {
    public TextField moodleNameField;
    public TextField moodleURLField;
    public TextField nameField;
    public PasswordField passwordField;

    public void doLogin(ActionEvent actionEvent) {
        // Check if all the parameters where filled
        boolean empty = moodleNameField.getText().trim().isEmpty() ||
                moodleURLField.getText().trim().isEmpty() ||
                nameField.getText().trim().isEmpty() ||
                passwordField.getText().trim().isEmpty();

        if (empty) {
            ErrorController.errorDialog(Errors.TEXTFIELD_EMPTY);
            return;
        }

        // Send a request to Moodle
        try {
            sendLoginRequest(
                moodleURLField.getText(),
                nameField.getText(),
                passwordField.getText()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendLoginRequest(String moodle, String username, String password) {
        MoodleWSController moodleWS = new MoodleWSController();

        String moodleURL = null;
        try {
            moodleURL = moodleWS.httpsURL(moodle);
        } catch (CustomException e) {
            e.printStackTrace();
            ErrorController.errorDialog(e.getMessage());
            return;
        }

        // Gets the user's token (as a response)
        String tokenResponse = null;
        try {
            tokenResponse = moodleWS.getTokenResponse(moodleURL, username, password);
        } catch (IOException e) {
            e.printStackTrace();
            ErrorController.errorDialog(Errors.IO_ERROR);
            return;
        } catch (CustomException e) {
            e.printStackTrace();
            ErrorController.errorDialog(e.getMessage());
            return;
        }

        // Check if the response contains the user's token:
        JsonObject jsonResponse = new JsonParser().parse(tokenResponse).getAsJsonObject();
        if (jsonResponse.has("errorcode")) {
            // An error happened, find out which one and report to the user
            String error = jsonResponse.get("errorcode").toString();
            switch (error) {
                case "enablewsdescription":
                    ErrorController.errorDialog(Errors.INCOMPATIBLE_MOODLE);
                    break;
                case "missingparam":
                    ErrorController.errorDialog(Errors.MISSING_PARAMS);
                    break;
                case "invalidlogin":
                    ErrorController.errorDialog(Errors.INVALID_CREDENTIALS);
                    break;
                default:
                    ErrorController.errorDialog("Moodle returned this error: " + error);
                    break;
            }
            return;
        }

        // Everything went correctly
        String tokenRaw = jsonResponse.get("token").toString();
        String token = tokenRaw.substring(1, tokenRaw.length() - 1); // tokenRaw has extra ""

        System.out.println(token);

        // Get the userid
        int userid = -1;
        try {
            userid = moodleWS.getUserID(moodleURL, token);
        } catch (IOException e) {
            e.printStackTrace();
            ErrorController.errorDialog(Errors.IO_ERROR);
            return;
        } catch (CustomException e) {
            e.printStackTrace();
            ErrorController.errorDialog(e.getMessage());
            return;
        }

        // Everything went correctly, it can now be added to the DB
        System.out.println(userid);
    }
}
