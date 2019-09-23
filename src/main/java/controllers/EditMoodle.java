package controllers;

import controllers.SharedMethods.CoursesPopulator;
import controllers.SharedMethods.DifferenceChecker;
import controllers.SharedMethods.FilePicker;
import helpers.MessageDialog;
import helpers.PathOperations;
import helpers.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import models.CurrentMoodle;
import models.Moodle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

public class EditMoodle {
    public TextField moodleName;
    public Button directoryButton;
    public TreeView coursesTree;
    public Button applyButton;

    private int moodlePos;
    private Moodle moodleClone;
    private Moodle actualMoodle;

    public EditMoodle(int pos) {
        moodlePos = pos;
        // A clone is made in order to compare the changes
        actualMoodle = CurrentMoodle.getAllMoodles().get(moodlePos);
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
        ButtonBar.ButtonData answer = MessageDialog.deleteEverythingDialog();

        String location = PathOperations.path(actualMoodle.getDiskLocation(), actualMoodle.getName());

        if (answer == ButtonBar.ButtonData.YES || answer == ButtonBar.ButtonData.NO) {
            CurrentMoodle.clear();

            // Remove the Moodle from the DB
            if (!CurrentMoodle.removeMoodle(moodlePos)) {
                MessageDialog.errorDialog("An error occurred and it wasn't possible to delete the Moodle from the database.");
                back(actionEvent);
                return;
            }

            // Remove Moodle's files
            if (answer == ButtonBar.ButtonData.YES) {
                try {
                    Files.walk(new File(location).toPath())
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                } catch (IOException e) {
                    MessageDialog.errorDialog("Unable to delete Moodle from disk. Please try removing it manually.");
                }
            }
            back(actionEvent);
        }
    }

    public void apply(ActionEvent actionEvent) {
        // Check if the name changed
        // If it did, update the moodle's name and the folder's name
        String currentName = actualMoodle.getName();
        String newName = moodleName.getText();
        File currentFolder = new File(PathOperations.path(actualMoodle.getDiskLocation(), currentName));
        File newFolder = new File(PathOperations.path(actualMoodle.getDiskLocation(), newName));
        if (!currentName.equals(newName)) {
            if (currentFolder.renameTo(newFolder)) {
                System.out.println("Folder name changed successfully!");
                actualMoodle.setName(PathOperations.safeFileName(newName));
            } else {
                MessageDialog.errorDialog("An error occurred when renaming the Moodle! Please check if the name doesn't contain any special characters and/or if the original folder is still in the same location.");
                return;
            }
        }


        // Check if the location changed
        // If so, move thr contents to the new location
        String currentLocation = actualMoodle.getDiskLocation();
        String newLocation = moodleClone.getDiskLocation();
        System.out.println(currentFolder);
        System.out.println(newLocation);
        if (!currentLocation.equals(newLocation)) {
            // Files will need to be moved
            String newDirectory = PathOperations.path(newLocation, newName);
            try {
                new File(newDirectory).mkdirs();
                Files.move(currentFolder.toPath(), new File(newDirectory).toPath(), StandardCopyOption.REPLACE_EXISTING);
                actualMoodle.setDiskLocation(newLocation);
            } catch (IOException e) {
                MessageDialog.errorDialog("An error while trying to move to a new location! Please check if the directory has correct permissions.");
                return;
            }
        }

        // Check for courses changes
        System.out.println(DifferenceChecker.areDifferentLists(actualMoodle.getCourses(), moodleClone.getCourses()));

        // Save the changes
        // TODO error handling
        CurrentMoodle.writeAllMoodles();

        back(actionEvent);
    }
}
