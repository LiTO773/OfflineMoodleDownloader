package controllers;

import javafx.fxml.FXML;
import suppliers.MoodleWSConnection;
import helpers.MessageDialog;
import helpers.SceneChanger;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import models.*;

import java.sql.SQLException;

public class MoodleInfoController {
    // These variables are only used when the user returns to the scene
    private String name = "";
    private boolean getNameAutomatically = true;
    private String url = "";
    private String username = "";
    private String password = "";

    // FXML
    public TextField moodleURLField;
    public TextField moodleName;
    public TextField usernameField;
    public PasswordField passwordField;
    public CheckBox nameCheckbox;

    public MoodleInfoController() {
    }

    // Constructor used when the user returns to the scene
    public MoodleInfoController(String name, boolean getNameAutomatically, String url, String username, String password) {
        this.name = name;
        this.getNameAutomatically = getNameAutomatically;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @FXML
    public void initialize() {
        moodleURLField.setText(url);
        usernameField.setText(username);
        passwordField.setText(password);
        if (!getNameAutomatically) {
            moodleName.setDisable(false);
            moodleName.setText(name);
            nameCheckbox.setSelected(false);
        }
    }

    public void doLogin(ActionEvent actionEvent) {
        // Check if all the parameters where filled
        boolean empty = (moodleName.getText().trim().isEmpty() && !nameCheckbox.isSelected()) ||
                moodleURLField.getText().trim().isEmpty() ||
                usernameField.getText().trim().isEmpty() ||
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

        // Check if there is already an instance in the DB
        if (CurrentMoodle.moodleExistsByURLandUsername(moodleURL, usernameField.getText())) {
            MessageDialog.errorDialog(Errors.MOODLE_EXISTS);
            return;
        }

        // Move to the Loading Scene
        SceneChanger sc = new SceneChanger(actionEvent);
        LoadingInfoController nextSceneController = new LoadingInfoController(
                moodleName.getText().trim(),
                nameCheckbox.isSelected(),
                moodleURL,
                usernameField.getText(),
                passwordField.getText()
        );
        sc.changeSceneWithFactory("MoodleActions/LoadingInfo.fxml", nextSceneController);
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
