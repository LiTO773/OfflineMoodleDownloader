package controllers.SharedMethods;

import helpers.FileSizeCalculator;
import javafx.scene.control.CheckBoxTreeItem;
import models.*;

public class CoursesPopulator {

    public static CheckBoxTreeItem<String> populator(Moodle moodle) {
        CheckBoxTreeItem<String> rootItem = createTreeItem(moodle, true);

        for (Course course: moodle.getCourses()) {
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

        return rootItem;
    }

    private static CheckBoxTreeItem<String> createTreeItem(Downloadable d, boolean expanded) {
        // Get the file size
        String size = "";
        if (d instanceof File) {
            int sizeInt = ((File) d).getFileSize();
            size = FileSizeCalculator.calculate(sizeInt);
        } else if (d instanceof DBCollection) {
            size = ((DBCollection) d).sizeString();
        }
        CheckBoxTreeItem<String> item = new CheckBoxTreeItem<>(d.getName() + " (" + size + ")");
        item.setSelected(d.isDownloadable());
        // If it is selected or indeterminate, then set download to true
        item.indeterminateProperty().addListener((obs, oldVal, newVal) -> d.setDownloadable(obs.getValue() || item.selectedProperty().get()));
        item.selectedProperty().addListener((obs, oldVal, newVal) -> d.setDownloadable(obs.getValue() || item.indeterminateProperty().get()));
        item.setExpanded(expanded);

        return item;
    }
}
