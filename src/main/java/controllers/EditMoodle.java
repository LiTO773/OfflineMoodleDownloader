package controllers;

import controllers.SharedMethods.CoursesPopulator;
import helpers.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import models.CurrentMoodle;
import models.Moodle;

public class EditMoodle {
    public TextField moodleName;
    public Button directoryButton;
    public TreeView coursesTree;

    private Moodle moodle;

    public EditMoodle(int pos) {
        moodle = CurrentMoodle.getAllMoodles().get(pos);
    }

    @FXML
    public void initialize() {
        // Populate name
        moodleName.setText(moodle.getName());

        // Populate directory
        directoryButton.setText(moodle.getDiskLocation());

        // Populate table
        coursesTree.setRoot(CoursesPopulator.populator(moodle));
        coursesTree.setCellFactory(CheckBoxTreeCell.forTreeView());
    }

    public void openDirectoryChooser(ActionEvent actionEvent) {
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
