package controllers.SharedMethods.ListComparisonUtils;

import helpers.MessageDialog;
import helpers.PathOperations;
import models.*;
import models.enums.DeletedActions;
import models.enums.NewActions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListComparison {
    // Stores the Moodle affected (for parameters like the name and disk location)
    private Moodle moodle;

    // Checks if the lists are different
    private boolean listsDifferent;

    // Action variables, needed when downloading/deleting files
    private boolean canDelete;
    private boolean askDelete;
    private boolean canDownload;
    private boolean askDownload;

    // List that stores files which had an usual behavior
    private ArrayList<String> notDeleted = new ArrayList<>();

    // The actual lists
    private ArrayListIgnoreDownload<Course> originalListIgnore;
    private ArrayListIgnoreDownload<Course> newListIgnore;

    public ListComparison(List<Course> originalList, List<Course> newList, Moodle moodle) {
        this.moodle = moodle;

        listsDifferent = areDifferentLists(originalList, newList);

        DeletedActions deletedAction = CurrentMoodle.getDeletedFileAction();
        NewActions newAction = CurrentMoodle.getNewFileAction();
        this.canDelete = deletedAction == DeletedActions.DELETE;
        this.askDelete = deletedAction == DeletedActions.ASK;
        this.canDownload = newAction == NewActions.DOWNLOAD;
        this.askDownload = newAction == NewActions.ASK;

        this.originalListIgnore = new ArrayListIgnoreDownload<>(originalList);
        this.newListIgnore = new ArrayListIgnoreDownload<>(newList);
    }

    /**
     * Compares 2 courses lists and deletes or downloads the files.
     * @return An array with all the files the were not deleted due to some error. If it is null, then the lists are the same.
     */
    public ArrayList<String> downloadAndUpdate() {
        // Check if the lists are different
        if (!listsDifferent) {
            return null;
        }

        // Keep track of how many times it was unable to download, if it reaches 3, throw an error
        int failedDownloadAttempts = 0;

        // Keep track of the path, by adding the current course/section/folder
        ArrayList<String> path = new ArrayList<>();
        path.add(moodle.getName());

        // Loop every list and delete/ignore the files
        if (deleteBoolean()) {
            deleteLoop(originalListIgnore, newListIgnore, path);
        }

        return notDeleted;
    }

    private void deleteLoop(ArrayListIgnoreDownload oldList, ArrayListIgnoreDownload newList, ArrayList<String> path) {
        oldList.stream().forEach(o -> {
            Downloadable d = (Downloadable) o;
            path.add(d.getName());

            int result = delete(newList, d, path);
            if (result != -1 && !(oldList.get(result) instanceof models.File)) { // if it was an instanceof File there is no need to repeat the loop
                // move to the next file structure
                ArrayListIgnoreDownload subOld = new ArrayListIgnoreDownload(((DBCollection) oldList.get(result)).getCollection());
                ArrayListIgnoreDownload subNew = new ArrayListIgnoreDownload(((DBCollection) newList.get(result)).getCollection());

                deleteLoop(subOld, subNew, path);
            }
            path.remove(path.size() - 1);
        });
    }

    // Returns -1 if the content was ignored/deleted, else returns the position
    private int delete(ArrayListIgnoreDownload newList, Downloadable original, ArrayList<String> path) {
        int index = newList.indexOf(original);
        Downloadable newD = (Downloadable) newList.get(index);

        System.out.println(newD.isDownloadable());
        if (index == -1 || (original.isDownloadable() && !newD.isDownloadable())) {
            String generatedPath = PathOperations.path(moodle.getDiskLocation(), path);
            try {
                Files.walk(new File(generatedPath).toPath())
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                e.printStackTrace();
                notDeleted.add(generatedPath);
            }
            return -1;
        }
        return index;
    }

    private boolean deleteBoolean() {
        // TODO: implement this in javafx
        if (askDelete) {
            System.out.println("Can i delete?");
            canDelete = true; // user chooses this value
            askDelete = false;
        }
        return canDelete;
    }

    private boolean downloadBoolean() {
        if (askDownload) {
            System.out.println("Can i download?");
            canDownload = true;
            askDownload = false;
        }
        return canDownload;
    }

    public static boolean areDifferentLists(List<Course> originalList, List<Course> newList) {
        if(originalList.size() != newList.size()) return true;

        return !originalList.stream().allMatch(c -> newList.contains(c));
    }
}