package controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;

public class ChooseDirectoryController {
    public Label directoryChosen;

    public void openDirectoryChooser(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        // Get the stage (needed to display the DirectoryChooser)
        Node source = (Node) actionEvent.getSource();
        Window stage = source.getScene().getWindow();

        // Allow the selection of the directory
        File selectedDirectory =
                directoryChooser.showDialog(stage);

        if(selectedDirectory == null){
            directoryChosen.setText("No Directory selected");
        }else{
            directoryChosen.setText(selectedDirectory.getAbsolutePath());
        }
    }
}
