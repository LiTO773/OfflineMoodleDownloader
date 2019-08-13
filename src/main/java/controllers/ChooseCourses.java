package controllers;

import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import models.Downloadable;
import suppliers.MoodleWSConnection;
import helpers.MessageDialog;
import helpers.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import models.Course;
import models.CurrentMoodle;
import models.CustomException;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ChooseCourses {
    public TreeView coursesTree;
    private List<Course> courses;

    @FXML
    public void initialize() {
        // Setup the TreeView
        MoodleWSConnection moodleWSConnection = new MoodleWSConnection();
        // Load all the courses
        try {
            courses = moodleWSConnection.getCourses();

            // Populate the View with the courses
            for (Course c: courses) {
                CheckBox cb = new CheckBox(c.getName());
                cb.setSelected(true);

                // Change the download property if the checkbox gets selected or not
                //cb.selectedProperty().addListener((observable, oldValue, newValue) -> c.setDownloadable(newValue));

                //checkboxes.getChildren().add(cb);
            }
        } catch (IOException e) {
            e.printStackTrace();
            MessageDialog.errorDialog(e.getMessage());
        } catch (CustomException e) {
            e.printStackTrace();
            MessageDialog.errorDialog(e.getMessage());
        }
    }

    public void nextScene(ActionEvent actionEvent) {
        CurrentMoodle.getMoodle().setCourses(courses);
        SceneChanger sc = new SceneChanger(actionEvent);
        sc.changeScene("MoodleActions/Confirm.fxml");
    }
}
