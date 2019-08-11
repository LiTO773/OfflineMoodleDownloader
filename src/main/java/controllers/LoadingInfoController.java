package controllers;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import models.Moodle;
import com.google.gson.JsonObject;
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
    private class LoginService extends Service<JsonObject> {
        @Override
        protected Task<JsonObject> createTask() {
            return new Task<JsonObject>() {
                @Override
                protected JsonObject call() throws Exception {
                    return loginSupplier.login(url, username, password);
                }

                @Override
                protected void succeeded() {
                    // The task succeeded, setup the Moodle correctly
                    JsonObject tokenID = getValue();

                    //Moodle currentMoodle = new Moodle()
                }

                @Override
                protected void failed() {
                    Throwable error = getException();

                    // TODO Implement the error message switch
                    System.out.println(error.getMessage());
                }
            };
        }
    }
}
