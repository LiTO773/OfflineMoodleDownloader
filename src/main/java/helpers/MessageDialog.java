package helpers;

import javafx.scene.control.Alert;

public class MessageDialog {
    public static void errorDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Oh no! There was a problem.");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }

    public static void infoDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hey look at this!");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }
}
