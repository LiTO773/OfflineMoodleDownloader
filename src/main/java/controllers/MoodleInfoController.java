package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import connections.MoodleWSConnection;
import database.DataDB;
import helpers.MessageDialog;
import helpers.SceneChanger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import models.*;

import java.io.IOException;
import java.sql.SQLException;

public class MoodleInfoController {
    public TextField moodleURLField;
    public TextField moodleName;
    public TextField nameField;
    public PasswordField passwordField;
    public Button nextSceneButton;
    public CheckBox nameCheckbox;

    public JsonObject jsonObject;

    public void doLogin(ActionEvent actionEvent) {
        // Check if all the parameters where filled
        boolean empty = moodleURLField.getText().trim().isEmpty() ||
                nameField.getText().trim().isEmpty() ||
                passwordField.getText().trim().isEmpty();

        if (empty) {
            MessageDialog.errorDialog(Errors.TEXTFIELD_EMPTY);
            return;
        }

        // Create a correct URL
        String moodleURL = null;
        try {
            moodleURL = MoodleWSConnection.httpsURL(moodleURLField.getText());
        } catch (CustomException e) {
            e.printStackTrace();
            MessageDialog.errorDialog(e.getMessage());
            return;
        }

        // Check if there is already an instance in the DB
        DataDB db = null;
        try {
            db = new DataDB();
            if (db.moodleExists(moodleURL, nameField.getText().trim())) {
                MessageDialog.errorDialog(Errors.MOODLE_EXISTS);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            MessageDialog.errorDialog(Errors.SQL_ERROR);
            return;
        }

        // Send a request to Moodle
        TokenID tokenID = null;
        String siteName = null;
        try {
            tokenID = sendLoginRequest(
                moodleURL,
                nameField.getText(),
                passwordField.getText()
            );
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialog.errorDialog(e.getMessage());
            return;
        }

        // Everything went correctly, save the Moodle in CurrentMoodle
        String name;
        if(nameCheckbox.isSelected()){
            name = jsonObject.get("sitename").toString();
            name = name.substring(1, name.length() - 1);
        } else {
            name = moodleName.getText();
        }

        Moodle moodle = new Moodle(
                name,
                moodleURL,
                nameField.getText(),
                tokenID.getToken(),
                tokenID.getUserid()
        );
        MessageDialog.infoDialog("The Moodle you inserted is valid 🎉. Please continue the setup.");
        CurrentMoodle.setMoodle(moodle);
        nextSceneButton.setDisable(false);
    }

    private TokenID sendLoginRequest(String moodleURL, String username, String password) throws CustomException, IOException {
        MoodleWSConnection moodleWS = new MoodleWSConnection();
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
        jsonObject = moodleWS.getUserID(moodleURL, token);
        tokenID.setUserid(Integer.parseInt(jsonObject.get("userid").toString()));

        return tokenID;
    }


    public void nextScene(ActionEvent actionEvent) {
        SceneChanger sc = new SceneChanger(actionEvent);

        sc.changeScene("MoodleActions/ChooseDirectory.fxml");
    }

    public void checkCheckbox(ActionEvent actionEvent) {
        if(nameCheckbox.isSelected()){
            moodleName.setDisable(true);
            moodleName.setText("");
        } else {
            moodleName.setDisable(false);
        }
    }
}
