package controllers;

import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import models.*;
import helpers.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ChooseCourses {
    public TreeView<String> coursesTree;
    public Button nextButton;

    public Moodle currentMoodle = CurrentMoodle.getMoodle();

    @FXML
    public void initialize() {
        // Setup the TreeView
        CheckBoxTreeItem<String> rootItem = createTreeItem(currentMoodle, true);

        for (Course course: currentMoodle.getCourses()) {
            CheckBoxTreeItem<String> courseItem = createTreeItem(course, false);

            for (Section section: course.getCollection()) {
                CheckBoxTreeItem<String> sectionItem = createTreeItem(section, false);

                for (Content content: section.getCollection()) {
                    if (content instanceof Folder) {
                        Folder folder = (Folder) content;
                        CheckBoxTreeItem<String> folderItem = createTreeItem(folder, false);

                        for (File file: folder.getCollection()) {
                            CheckBoxTreeItem<String> fileItem = createTreeItem(file, false);
                            folderItem.getChildren().add(fileItem);
                        }
                        sectionItem.getChildren().add(folderItem);
                    } else {
                        // It's a file
                        CheckBoxTreeItem<String> fileItem = createTreeItem((File) content, false);
                        sectionItem.getChildren().add(fileItem);
                    }
                }

                courseItem.getChildren().add(sectionItem);
            }

            rootItem.getChildren().add(courseItem);
        }

        coursesTree.setRoot(rootItem);
        coursesTree.setCellFactory(CheckBoxTreeCell.forTreeView());
    }

    public void nextScene(ActionEvent actionEvent) {
        SceneChanger sc = new SceneChanger(actionEvent);
        sc.changeScene("MoodleActions/ChooseDirectory.fxml");
    }

    private CheckBoxTreeItem<String> createTreeItem(Downloadable d, boolean expanded) {
        CheckBoxTreeItem<String> item = new CheckBoxTreeItem<>(d.getName());
        item.setSelected(false);
        item.selectedProperty().addListener((obs, oldVal, newVal) -> nextButton.setDisable(setButtonState(d, newVal)));
        item.setExpanded(expanded);

        return item;
    }

    private boolean setButtonState(Downloadable d, boolean val){
        d.setDownloadable(val);

        for(Course course: currentMoodle.getCourses()){
            if(course.isDownloadable()){
                return false;
            }
            for (Section section: course.getCollection()) {
                if(section.isDownloadable()){
                    return false;
                }

                for (Content content: section.getCollection()) {
                    if (content instanceof Folder){
                        Folder folder = (Folder) content;

                        for (File file: folder.getCollection()) {
                            if(file.isDownloadable()){
                                return false;
                            }
                        }
                    } else {
                        File file = (File) content;
                        if(file.isDownloadable()){
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }
}
