package helpers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * SceneChanger is in charge of, you guessed it, changing scenes.
 * It only exists to avoid a lot of duplicate code.
 */
public class SceneChanger {
    ActionEvent event;

    /**
     * Constructor of the object SceneChanger
     * @param event
     */
    public SceneChanger(ActionEvent event) {
        this.event = event;
    }

    /**
     * Method that changes the scene
     * @param sceneName
     */
    public void changeScene(String sceneName) {
        Parent resource;
        try {
            resource = FXMLLoader.load(getClass().getClassLoader().getResource(sceneName));
            Scene scene = new Scene(resource);
            Stage app = (Stage) ((Node) this.event.getSource()).getScene().getWindow();
            app.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
