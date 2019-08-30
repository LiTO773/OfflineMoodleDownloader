package controllers;

import helpers.SceneChanger;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.CurrentMoodle;
import models.Moodle;
import models.MoodleStorage;

import java.util.List;

public class MainMenu {
    public ListView moodleList;

    @FXML
    public void initialize() {

        // Populate the list with Moodles
        MoodleStorage ms = CurrentMoodle.getAllMoodles();

        List<Moodle> moodles = ms.getClone();

        for (Moodle m: moodles) {
            moodleList.getItems().add(m.getName());
        }

        moodleList.getItems().add("➕ Add Moodle");

        moodleList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int index = moodleList.getSelectionModel().getSelectedIndex();

                SceneChanger sc = new SceneChanger(event);
                // Check if "Add Moodle" was clicked
                if (index == moodles.size()) {
                    sc.changeScene("MoodleActions/MoodleInfo.fxml");
                } else {
                    EditMoodle editMoodle = new EditMoodle(index);
                    sc.changeSceneWithFactory("MoodleActions/EditMoodle.fxml", editMoodle);
                }
            }
        });
    }
}
