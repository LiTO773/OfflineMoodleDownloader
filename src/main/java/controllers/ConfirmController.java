package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Course;
import models.CurrentMoodle;
import models.Moodle;

public class ConfirmController {
    public Label moodleInfo;
    public Label moodleLocation;
    public Label moodleCourses;

    @FXML
    public void initialize() {
        Moodle currentMoodle = CurrentMoodle.getMoodle();
        String info = "Name: " + currentMoodle.getName() +
                "\nURL: " + currentMoodle.getUrl() +
                "\nUsername: " + currentMoodle.getUsername();
        moodleInfo.setText(info);

        moodleLocation.setText(currentMoodle.getDiskLocation());

        String courses = "";
        for (Course c: currentMoodle.getCourses()) {
            courses += "- " + c.getName() + "\n";
        }

        moodleCourses.setText(courses);
    }
}
