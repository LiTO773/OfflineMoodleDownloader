package controllers;

import helpers.MessageDialog;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.util.Map;

import models.CurrentMoodle;
import models.Errors;
import models.Moodle;
import suppliers.LoginSupplier;
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

                    System.out.println(currentMoodle);

                    CurrentMoodle.setMoodle(currentMoodle);
                }

                @Override
                protected void failed() {
                    Throwable error = getException();

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
                }
            };
        }
    }
}
