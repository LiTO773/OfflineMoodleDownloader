package controllers.SharedMethods;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;

public class FilePicker {
    public static File picker(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        // Get the stage (needed to display the DirectoryChooser)
        Node source = (Node) event.getSource();
        Window stage = source.getScene().getWindow();

        // Allow the selection of the directory
        return directoryChooser.showDialog(stage);
    }

    public static File picker(ActionEvent event, String dir) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(dir));

        // Get the stage (needed to display the DirectoryChooser)
        Node source = (Node) event.getSource();
        Window stage = source.getScene().getWindow();

        // Allow the selection of the directory
        return directoryChooser.showDialog(stage);
    }
}
