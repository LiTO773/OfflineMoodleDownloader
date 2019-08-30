package controllers;

import controllers.SharedMethods.CoursesPopulator;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import models.*;
import helpers.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ChooseCourses {
    public TreeView<String> coursesTree;

    @FXML
    public void initialize() {
        // Setup the TreeView
        Moodle currentMoodle = CurrentMoodle.getMoodle();
        coursesTree.setRoot(CoursesPopulator.populator(currentMoodle));
        coursesTree.setCellFactory(CheckBoxTreeCell.forTreeView());
    }

    public void nextScene(ActionEvent actionEvent) {
        SceneChanger sc = new SceneChanger(actionEvent);
        sc.changeScene("MoodleActions/ChooseDirectory.fxml");
    }


}
