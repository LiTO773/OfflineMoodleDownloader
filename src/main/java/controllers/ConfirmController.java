package controllers;

import helpers.MessageDialog;
import helpers.SceneChanger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import models.*;
import suppliers.DownloadSupplier;

import java.awt.*;
import java.io.File;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static helpers.FileNameHelper.*;

public class ConfirmController {

    public Label progressLabel;
    public ProgressBar progressBar;
    public Button button;

    private DownloadSupplier downloadSupplier = new DownloadSupplier();
    private FileDownloadService fileDownloadService = new FileDownloadService();
    private String moodleFolderPath = "";

    public void registerAndDownload(ActionEvent actionEvent) {
        button.setDisable(true);
        progressLabel.setText("Saving Moodle Locally");
        CurrentMoodle.saveMoodle();
        // first try to save all Moodles
        if (!CurrentMoodle.writeAllMoodles()) {
            // TODO better error handling
            MessageDialog.errorDialog(Errors.IO_ERROR);
            return;
        }
        progressBar.setProgress(1.00);

        // Then download the files
        progressLabel.setText("Downloading");
        fileDownloadService.restart();
    }

    private class FileDownloadService extends Service<Void> {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {

                    Moodle currentMoodle = CurrentMoodle.getMoodle();
                    System.out.println(currentMoodle);
                    // Create the folder to store Moodle
                    moodleFolderPath = Paths.get(currentMoodle.getDiskLocation(), safeFileName(currentMoodle.getName())).toString();
                    createFolder(moodleFolderPath);

                    // Step for the Course
                    double courseStep = 1 / (double) currentMoodle.getCourses().size();
                    double currentProgress = 0.0;

                    for (Course cou : currentMoodle.getCourses()) {
                        if (!cou.isDownloadable()) {
                            currentProgress += courseStep;
                            continue;
                        }
                        // Create the folder to store the course
                        String courseFolderPath = Paths.get(moodleFolderPath, safeFileName(cou.getName())).toString();
                        createFolder(courseFolderPath);

                        // Step for the Section
                        double sectionStep = courseStep / cou.getCollection().size();

                        for (Section s: cou.getCollection()) {
                            if (!s.isDownloadable()) {
                                currentProgress += sectionStep;
                                continue;
                            }
                            // Create the folder to store the section
                            String sectionFolderPath = Paths.get(courseFolderPath, safeFileName(s.getName())).toString();
                            createFolder(sectionFolderPath);

                            // Set the step size
                            double fileStep = courseStep / s.getCollection().size();

                            for (Content con: s.getCollection()) {
                                currentProgress += fileStep;

                                if (con instanceof Folder) {
                                    if (!((Folder) con).isDownloadable()) continue;
                                    // Create the folder to store the folder
                                    String folderFolderPath = Paths.get(sectionFolderPath, safeFileName(((Folder) con).getName())).toString();
                                    createFolder(folderFolderPath);

                                    for (models.File f: ((Folder) con).getCollection()) {
                                        downloadSupplier.download(f.getFileName(), folderFolderPath, f.getFileURL());
                                    }
                                } else {
                                    models.File f = (models.File) con;
                                    if (!f.isDownloadable()) continue;

                                    downloadSupplier.download(f.getFileName(), sectionFolderPath, f.getFileURL());
                                }
                                progressBar.setProgress(currentProgress);
                            }
                        }
                    }
                    return null;
                }

                @Override
                protected void succeeded() {
                    try {
                        Desktop.getDesktop().open(new File(moodleFolderPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SceneChanger sc = new SceneChanger((Stage) progressLabel.getScene().getWindow());
                    sc.changeScene("MoodleActions/SuccessfulDownload.fxml");
                }

                @Override
                protected void failed() {
                    // The error message can be re-used
                    System.out.println(getException().toString());
                    getException().printStackTrace();
                    MessageDialog.errorDialog(getException().getMessage());
//                    returnToPreviousScene();
                }
            };
        }
    }
}

/*
Moodle m = CurrentMoodle.getMoodle()
        for (Course c: ) {

        }
 */