package controllers;

import javafx.scene.control.Alert;

public class ErrorController {
    public static void errorDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Oh no! There was a problem.");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }
}
