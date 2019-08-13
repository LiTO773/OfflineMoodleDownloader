package controllers;

import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import models.*;
import suppliers.MoodleWSConnection;
import helpers.MessageDialog;
import helpers.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ChooseCourses {
    public TreeView<String> coursesTree;
    private List<Course> courses;

    @FXML
    public void initialize() {
        // Setup the TreeView
        Moodle currentMoodle = CurrentMoodle.getMoodle();
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
        CurrentMoodle.getMoodle().setCourses(courses);
        SceneChanger sc = new SceneChanger(actionEvent);
        sc.changeScene("MoodleActions/Confirm.fxml");
    }

    private CheckBoxTreeItem<String> createTreeItem(Downloadable d, boolean expanded) {
        CheckBoxTreeItem<String> item = new CheckBoxTreeItem<>(d.getName());
        item.setSelected(true);
        item.selectedProperty().addListener((obs, oldVal, newVal) -> d.setDownloadable(newVal));
        item.setExpanded(expanded);

        return item;
    }
}
