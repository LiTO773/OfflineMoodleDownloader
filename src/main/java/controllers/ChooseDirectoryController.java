package controllers;

import controllers.SharedMethods.FilePicker;
import helpers.SceneChanger;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import models.CurrentMoodle;
import models.Moodle;

import java.io.File;

public class ChooseDirectoryController {
    public Label directoryChosen;
    public Button nextScene;

    public void openDirectoryChooser(ActionEvent actionEvent) {
        File selectedDirectory = FilePicker.picker(actionEvent);

        if(selectedDirectory == null){
            directoryChosen.setText("No Directory selected");
        }else{
            directoryChosen.setText(selectedDirectory.getAbsolutePath());
            nextScene.setDisable(false);
            CurrentMoodle.getMoodle().setDiskLocation(selectedDirectory.getAbsolutePath());
        }
    }

    public void nextScene(ActionEvent actionEvent) {
        SceneChanger sc = new SceneChanger(actionEvent);

        sc.changeScene("MoodleActions/Confirm.fxml");
    }
}
