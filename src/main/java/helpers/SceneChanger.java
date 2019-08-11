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
    private Parent resource;
    private Stage app;

    /**
     * Constructor of the object SceneChanger
     * @param event
     */
    public SceneChanger(ActionEvent event) {
        this.app = (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public SceneChanger(Stage stage) {
        this.app = stage;
    }

    /**
     * Method that changes the scene
     * @param sceneName
     */
    public void changeScene(String sceneName) {
        try {
            resource = FXMLLoader.load(getClass().getClassLoader().getResource(sceneName));
            Scene scene = new Scene(resource);
            app.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void changeSceneWithFactory(String sceneName, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(sceneName));
            loader.setControllerFactory(t -> controller);
            resource = loader.load();
            app.setScene(new Scene(resource));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
