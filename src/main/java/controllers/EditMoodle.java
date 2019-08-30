package controllers;

import controllers.SharedMethods.CoursesPopulator;
import controllers.SharedMethods.FilePicker;
import helpers.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import models.CurrentMoodle;
import models.Moodle;

import java.io.File;

public class EditMoodle {
    public TextField moodleName;
    public Button directoryButton;
    public TreeView coursesTree;
    public Button applyButton;

    private int moodlePos;
    private Moodle moodleClone;

    public EditMoodle(int pos) {
        moodlePos = pos;
        // A clone is made in order to compare the changes
        moodleClone = CurrentMoodle.getAllMoodles().get(pos).clone();
    }

    @FXML
    public void initialize() {
        // Populate name
        moodleName.setText(moodleClone.getName());

        // Populate directory
        directoryButton.setText(moodleClone.getDiskLocation());

        // Populate table
        coursesTree.setRoot(CoursesPopulator.populator(moodleClone));
        coursesTree.setCellFactory(CheckBoxTreeCell.forTreeView());
    }

    public void openDirectoryChooser(ActionEvent actionEvent) {
        File selectedDirectory = FilePicker.picker(actionEvent, moodleClone.getDiskLocation());

        if(selectedDirectory == null){
            directoryButton.setText("No Directory selected");
            applyButton.setDisable(true);
        }else{
            directoryButton.setText(selectedDirectory.getAbsolutePath());
            applyButton.setDisable(false);
            moodleClone.setDiskLocation(selectedDirectory.getAbsolutePath());
        }
    }

    public void back(ActionEvent actionEvent) {
        SceneChanger sc = new SceneChanger(actionEvent);
        sc.changeScene("MoodleActions/MainMenu.fxml");
    }

    public void delete(ActionEvent actionEvent) {
        // TODO check if the user wants to remove only the moodle or everything
    }

    public void apply(ActionEvent actionEvent) {
    }
}
