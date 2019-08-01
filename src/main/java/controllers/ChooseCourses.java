package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TreeTableView;
import models.Downloadable;

public class ChooseCourses {
    public TreeTableView<Downloadable> tableView;

    @FXML
    public void initialize() {
        // Load all the courses
        // TODO: Function that gets all courses and adds them to the CurrentMoodle
    }
}
