package controllers;

import helpers.MessageDialog;
import helpers.SceneChanger;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import models.CurrentMoodle;
import models.Errors;

public class ConfirmController {

    public Label progressLabel;
    public ProgressBar progressBar;

    public void registerAndDownload(ActionEvent actionEvent) {
        CurrentMoodle.saveMoodle();
        // first try to save all Moodles
        if (!CurrentMoodle.writeAllMoodles()) {
            // TODO better error handling
            MessageDialog.errorDialog(Errors.IO_ERROR);
            return;
        }

        // Then download the files
    }
}
