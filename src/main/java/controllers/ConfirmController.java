package controllers;

import suppliers.MoodleWSConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Course;
import models.CurrentMoodle;
import models.CustomException;
import models.Moodle;

import java.io.IOException;

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
            if (c.isDownload()) {
                courses += "- " + c.getName() + "\n";
            }
        }

        moodleCourses.setText(courses);
    }

    public void download(ActionEvent actionEvent) {
        /*MoodleWSConnection moodleWSConnection = new MoodleWSConnection();
        try {
            // moodleWSConnection.fillMoodle();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CustomException e) {
            e.printStackTrace();
        }*/
    }
}
