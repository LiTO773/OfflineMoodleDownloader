package helpers;

import javafx.scene.control.Alert;

public class ErrorDialog {
    public static void errorDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Oh no! There was a problem.");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }
}
