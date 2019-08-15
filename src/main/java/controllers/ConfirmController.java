package controllers;

import helpers.MessageDialog;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import models.*;
import suppliers.DownloadSupplier;

import java.io.File;

import java.nio.file.Paths;
import java.util.List;

import static helpers.FileNameHelper.*;

public class ConfirmController {

    public Label progressLabel;
    public ProgressBar progressBar;
    public Button button;

    private DownloadSupplier downloadSupplier = new DownloadSupplier();
    private FileDownloadService fileDownloadService = new FileDownloadService();

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
                    // Step for the Progress bar
                    double step = 1 / (double) currentMoodle.getCourses().size();
                    double currentProgress = 0.0;

                    // Create the folder to store Moodle
                    String moodleFolderPath = Paths.get(currentMoodle.getDiskLocation(), safeFileName(currentMoodle.getName())).toString();
                    createFolder(moodleFolderPath);

                    for (Course cou : currentMoodle.getCourses()) {
                        // Create the folder to store the course
                        String courseFolderPath = Paths.get(moodleFolderPath, safeFileName(cou.getName())).toString();
                        createFolder(courseFolderPath);
                        for (Section s: cou.getCollection()) {
                            // Create the folder to store the section
                            String sectionFolderPath = Paths.get(courseFolderPath, safeFileName(s.getName())).toString();
                            createFolder(sectionFolderPath);

                            // Set the step size
                            double fileStep = step / s.getCollection().size();
                            for (Content con: s.getCollection()) {
                                currentProgress += fileStep;

                                if (con instanceof Folder) {
                                    // Create the folder to store the folder
                                    String folderFolderPath = Paths.get(sectionFolderPath, safeFileName(((Folder) con).getName())).toString();
                                    createFolder(folderFolderPath);

                                    for (models.File f: ((Folder) con).getCollection()) {
                                        downloadSupplier.download(f.getFileName(), folderFolderPath, f.getFileURL());
                                    }
                                } else {
                                    models.File f = (models.File) con;
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
                    // the unicode is this emoji -> 😃
                    MessageDialog.infoDialog("Hurray, you have a backup of your Moodle on your computer \uD83D\uDE03");
                }

                @Override
                protected void failed() {
                    // The error message can be re-used
                    System.out.println(getException().toString());
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