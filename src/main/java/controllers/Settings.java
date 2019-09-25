package controllers;

import helpers.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import models.CurrentMoodle;
import models.RefreshTime;

public class Settings {
    public ComboBox timeOptions;

    @FXML
    public void initialize() {
        RefreshTime currentRF = CurrentMoodle.getRefreshTime();

        // Populate the times
        for (RefreshTime time: RefreshTime.values()) {
            timeOptions.getItems().add(RefreshTime.name(time));
        }

        // Select the configured time
        timeOptions.getSelectionModel().select(currentRF.ordinal());
    }

    public void apply(ActionEvent actionEvent) {
        // Change and save
        int selectedIndex = timeOptions.getSelectionModel().getSelectedIndex();
        CurrentMoodle.setRefreshTime(RefreshTime.values()[selectedIndex]);
        CurrentMoodle.writeAllMoodles();

        goBack(actionEvent);
    }

    public void goBack(ActionEvent actionEvent) {
        SceneChanger sc = new SceneChanger(actionEvent);
        sc.changeScene("MoodleActions/MainMenu.fxml");
    }
}
