package controllers;

import controllers.SharedMethods.CoursesPopulator;
import controllers.SharedMethods.DifferenceChecker;
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
        moodleClone = new Moodle(CurrentMoodle.getAllMoodles().get(pos));
    }

    @FXML
    public void initialize() {
        // Add check to see if the name is empty or not
        moodleName.textProperty().addListener((observable, oldValue, newValue) -> {
            applyButton.setDisable(newValue.trim().isEmpty() || moodleClone.getDiskLocation() == null);
        });

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
            moodleClone.setDiskLocation(null);
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
        // TODO this
        Moodle actualMoodle = CurrentMoodle.getAllMoodles().get(moodlePos);
        actualMoodle.setName(moodleName.getText());

        // Check for courses changes
        System.out.println(DifferenceChecker.areDifferentLists(actualMoodle.getCourses(), moodleClone.getCourses()));

        // Check if the location changed
        if (!moodleClone.getDiskLocation().equals(actualMoodle.getDiskLocation())) {
            // Files will need to be moved


        }
    }
}
