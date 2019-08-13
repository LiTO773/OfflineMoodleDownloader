package controllers;

import helpers.MessageDialog;
import helpers.SceneChanger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import models.Course;
import models.CurrentMoodle;
import models.Errors;
import models.Moodle;
import suppliers.CourseSupplier;
import suppliers.LoginSupplier;
import suppliers.MoodleCourseSupplier;
import suppliers.MoodleLoginSupplier;

public class LoadingInfoController {

    // Parameters from the previous scene
    private String name;
    private boolean getNameAutomatically;
    private String url;
    private String username;
    private String password;

    // Currently only Moodle is supported, so it's directly defined as MoodleLoginSupplier()
    private LoginSupplier loginSupplier = new MoodleLoginSupplier();
    private LoginService loginService = new LoginService();
    private CourseSupplier courseSupplier = new MoodleCourseSupplier();
    private CourseService courseService = new CourseService();
    private CourseInfoService courseInfoService = new CourseInfoService();

    // FXML elements
    public Label loginLabel;
    public ProgressBar loginProgress;
    public Label coursesLabel;
    public ProgressBar coursesProgress;
    public Label coursesInfoLabel;
    public ProgressBar coursesInfoProgress;

    public LoadingInfoController(String name, boolean getNameAutomatically, String url, String username, String password) {
        this.name = name;
        this.getNameAutomatically = getNameAutomatically;
        this.url = url;
        this.username = username;
        this.password = password;

        // Starts the LoginService Task
        loginService.restart();
    }

    // Login in a separate thread
    private class LoginService extends Service<Map<String, String>> {
        @Override
        protected Task<Map<String, String>> createTask() {
            return new Task<Map<String, String>>() {
                @Override
                protected Map<String, String> call() throws Exception {
                    return loginSupplier.login(url, username, password);
                }

                @Override
                protected void succeeded() {
                    // The task succeeded, setup the Moodle correctly
                    Map<String, String> response = getValue();

                    Moodle currentMoodle = new Moodle(
                            getNameAutomatically ? response.get("sitename") : name,
                            url,
                            username,
                            response.get("token"),
                            Integer.parseInt(response.get("userid"))
                    );
                    CurrentMoodle.setMoodle(currentMoodle);

                    // Indicate the completion of the task in the UI
                    loginLabel.setText(loginLabel.getText() + " ✅");
                    loginProgress.setProgress(1.00);
                    coursesProgress.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

                    // Move to the next step
                    courseService.restart();
                }

                @Override
                protected void failed() {
                    Throwable error = getException();

                    // Display error
                    switch (error.getMessage()) {
                        case "\"enablewsdescription\"":
                            MessageDialog.errorDialog(Errors.INCOMPATIBLE_MOODLE);
                            break;
                        case "\"missingparam\"":
                            MessageDialog.errorDialog(Errors.MISSING_PARAMS);
                            break;
                        case "\"invalidlogin\"":
                            MessageDialog.errorDialog(Errors.INVALID_CREDENTIALS);
                            break;
                        default:
                            MessageDialog.errorDialog("Moodle returned this error: " + error);
                            break;
                    }

                    returnToPreviousScene();
                }
            };
        }
    }

    // Get all courses without files, modules or folders
    private class CourseService extends Service<List<Course>> {
        @Override
        protected Task<List<Course>> createTask() {
            return new Task<List<Course>>() {
                @Override
                protected List<Course> call() throws Exception {
                    return courseSupplier.getAllCourses();
                }

                @Override
                protected void succeeded() {
                    // All courses were successfully obtained
                    CurrentMoodle.getMoodle().setCourses(getValue());

                    // Indicate the completion of the task in the UI
                    coursesLabel.setText(coursesLabel.getText() + " ✅");
                    coursesProgress.setProgress(1.00);

                    // Move to the next step
                    courseInfoService.restart();
                }

                @Override
                protected void failed() {
                    MessageDialog.errorDialog(Errors.COURSES_ERROR);
                    returnToPreviousScene();
                }
            };
        }
    }

    // Fill the courses
    private class CourseInfoService extends Service<Void> {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    // Since the progress bar needs to be updated, the loop logic is done here
                    Moodle currentMoodle = CurrentMoodle.getMoodle();
                    double step = 1 / (double) currentMoodle.getCourses().size();
                    double currentProgress = 0.0;

                    for (Course c : currentMoodle.getCourses()) {
                        courseSupplier.getCourseInfo(c);
                        currentProgress += step;
                        System.out.println(currentProgress);
                        coursesInfoProgress.setProgress(currentProgress);
                    }

                    return null;
                }

                @Override
                protected void succeeded() {
                    // All courses were successfully filled
                    // Indicate the completion of the task in the UI
                    coursesInfoLabel.setText(coursesInfoLabel.getText() + " ✅");

                    // Move to the next scene
                    SceneChanger sc = new SceneChanger((Stage) loginLabel.getScene().getWindow());
                    sc.changeScene("MoodleActions/ChooseCourses.fxml");
                }

                @Override
                protected void failed() {
                    // The error message can be re-used
                    MessageDialog.errorDialog(Errors.COURSES_ERROR);
                    returnToPreviousScene();
                }
            };
        }
    }


    private void returnToPreviousScene() {
        SceneChanger sc = new SceneChanger((Stage) loginLabel.getScene().getWindow());
        MoodleInfoController nextSceneController = new MoodleInfoController(name, getNameAutomatically, url, username, password);
        sc.changeSceneWithFactory("MoodleActions/MoodleInfo.fxml", nextSceneController);
    }
}
