package controllers;

import helpers.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import models.CurrentMoodle;
import models.enums.DeletedActions;
import models.enums.NewActions;
import models.enums.RefreshTime;

public class Settings {
    public ComboBox timeOptions;
    public ComboBox newOptions;
    public ComboBox deletedOptions;
    public RadioButton lightRadio;
    public RadioButton darkRadio;

    @FXML
    public void initialize() {
        // Populate ComboBoxes
        for (RefreshTime time: RefreshTime.values()) {
            timeOptions.getItems().add(RefreshTime.name(time));
        }

        for (NewActions action: NewActions.values()) {
            newOptions.getItems().add(action.getText());
        }

        for (DeletedActions action: DeletedActions.values()) {
            deletedOptions.getItems().add(action.getText());
        }

        // Select the configured time
        timeOptions.getSelectionModel().select(RefreshTime.name(CurrentMoodle.getRefreshTime()));
        newOptions.getSelectionModel().select(CurrentMoodle.getNewFileAction().getText());
        deletedOptions.getSelectionModel().select(CurrentMoodle.getDeletedFileAction().getText());
    }

    public void apply(ActionEvent actionEvent) {
        // Change and save
        int selectedIndex = timeOptions.getSelectionModel().getSelectedIndex();
        CurrentMoodle.setRefreshTime(RefreshTime.values()[selectedIndex]);

        selectedIndex = newOptions.getSelectionModel().getSelectedIndex();
        CurrentMoodle.setNewFileAction(NewActions.values()[selectedIndex]);

        selectedIndex = deletedOptions.getSelectionModel().getSelectedIndex();
        CurrentMoodle.setDeletedFileAction(DeletedActions.values()[selectedIndex]);

        CurrentMoodle.writeAllMoodles();
        goBack(actionEvent);
    }

    public void goBack(ActionEvent actionEvent) {
        SceneChanger sc = new SceneChanger(actionEvent);
        sc.changeScene("MoodleActions/MainMenu.fxml");
    }
}
