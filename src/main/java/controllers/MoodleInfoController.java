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
    public CheckBox nameCheckbox;

    public void doLogin(ActionEvent actionEvent) {
        // Check if all the parameters where filled
        boolean empty = moodleURLField.getText().trim().isEmpty() ||
                nameField.getText().trim().isEmpty() ||
                passwordField.getText().trim().isEmpty();

        if (empty) {
            MessageDialog.errorDialog(Errors.TEXTFIELD_EMPTY);
            return;
        }

        // Create a correct URL (add https:// or http:// if necessary)
        String moodleURL;
        try {
            moodleURL = MoodleWSConnection.httpsURL(moodleURLField.getText());
        } catch (CustomException e) {
            e.printStackTrace();
            MessageDialog.errorDialog(e.getMessage());
            return;
        }

        // Save the arguments
        CurrentMoodle.setParams(moodleURL, nameField.getText(), passwordField.getText());

        // Check if there is already an instance in the DB
        DataDB db;
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

        // Move to the Loading Scene
        SceneChanger sc = new SceneChanger(actionEvent);
        sc.changeScene("MoodleActions/LoadingInfo.fxml");
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
