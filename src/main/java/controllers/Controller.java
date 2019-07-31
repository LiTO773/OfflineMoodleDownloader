package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.DataDB;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.CustomException;
import models.Errors;
import models.TokenID;

import java.io.IOException;
import java.sql.SQLException;

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

        // Create a correct URL
        String moodleURL = null;
        try {
            moodleURL = MoodleWSController.httpsURL(moodleURLField.getText());
        } catch (CustomException e) {
            e.printStackTrace();
            ErrorController.errorDialog(e.getMessage());
            return;
        }

        // Check if there is already an instance in the DB
        DataDB db = null;
        try {
            db = new DataDB();
            if (db.moodleExists(moodleURL, nameField.getText().trim())) {
                ErrorController.errorDialog(Errors.MOODLE_EXISTS);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorController.errorDialog(Errors.SQL_ERROR);
            return;
        }

        // Send a request to Moodle
        TokenID tokenID = null;
        try {
            tokenID = sendLoginRequest(
                moodleURL,
                nameField.getText(),
                passwordField.getText()
            );
        } catch (Exception e) {
            e.printStackTrace();
            ErrorController.errorDialog(e.getMessage());
            return;
        }

        // Add the information to the DB
        try {
            db.newMoodle(
                moodleNameField.getText().trim(),
                moodleURL,
                nameField.getText(),
                tokenID.getToken(),
                tokenID.getUserid()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorController.errorDialog(e.getMessage());
            return;
        }
    }

    private TokenID sendLoginRequest(String moodleURL, String username, String password) throws CustomException, IOException {
        MoodleWSController moodleWS = new MoodleWSController();
        TokenID tokenID = new TokenID();

        // Gets the user's token (as a response)
        String tokenResponse = moodleWS.getTokenResponse(moodleURL, username, password);

        // Check if the response contains the user's token:
        JsonObject jsonResponse = new JsonParser().parse(tokenResponse).getAsJsonObject();
        if (jsonResponse.has("errorcode")) {
            // An error happened, find out which one and report to the user
            String error = jsonResponse.get("errorcode").toString();
            switch (error) {
                case "enablewsdescription":
                    throw new CustomException(Errors.INCOMPATIBLE_MOODLE);
                case "missingparam":
                    throw new CustomException(Errors.MISSING_PARAMS);
                case "invalidlogin":
                    throw new CustomException(Errors.INVALID_CREDENTIALS);
                default:
                    throw new CustomException("Moodle returned this error: " + error);
            }
        }

        // Everything went correctly
        String tokenRaw = jsonResponse.get("token").toString();
        String token = tokenRaw.substring(1, tokenRaw.length() - 1); // tokenRaw has extra ""
        tokenID.setToken(token);

        System.out.println(token);

        // Get the userid
        int userid = moodleWS.getUserID(moodleURL, token);

        // Everything went correctly, it can now be added to the DB
        System.out.println(userid);
        tokenID.setUserid(userid);

        return tokenID;
    }
}
