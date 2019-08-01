package controllers;

import connections.MoodleWSConnection;
import helpers.MessageDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import models.Course;
import models.CustomException;

import java.io.IOException;
import java.util.Set;

public class ChooseCourses {
    public VBox checkboxes;
    private Set<Course> courses;

    @FXML
    public void initialize() {
        MoodleWSConnection moodleWSConnection = new MoodleWSConnection();
        // Load all the courses
        try {
            courses = moodleWSConnection.getCourses();

            // Populate the View with the courses
            for (Course c: courses) {
                CheckBox cb = new CheckBox(c.getName());
                cb.setSelected(true);

                // Change the download property if the checkbox gets selected or not
                cb.selectedProperty().addListener((observable, oldValue, newValue) -> c.setDownload(newValue));

                checkboxes.getChildren().add(cb);
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
        for (Course c: courses) {
            System.out.println(c);
        }
    }
}
