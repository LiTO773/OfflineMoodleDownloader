package helpers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

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

    public static ButtonBar.ButtonData deleteEverythingDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Before you delete");
        alert.setHeaderText(null);
        alert.setContentText("Please check the behavior you want when deleting the Moodle.");
        alert.getDialogPane().setMinWidth(600);

        ButtonType deleteEverything = new ButtonType("Delete Moodle and files", ButtonBar.ButtonData.YES);
        ButtonType leaveFiles = new ButtonType("Only delete the Moodle", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(deleteEverything, leaveFiles, cancelButton);
        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() ? result.get().getButtonData() : ButtonBar.ButtonData.CANCEL_CLOSE;
    }
}
