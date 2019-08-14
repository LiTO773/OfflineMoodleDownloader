import helpers.MessageDialog;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.CurrentMoodle;
import models.Errors;
import models.Moodle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MoodleActions/MoodleInfo.fxml"));
        primaryStage.setTitle("OfflineMoodleDownloader");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        if (!CurrentMoodle.loadAllMoodles()) {
            // Assume that no file exists and create a new one
            if (!CurrentMoodle.writeAllMoodles()) {
                // There was a problem writing
                MessageDialog.errorDialog(Errors.IO_ERROR);
            }
        }

        // DEBUG
        for (Moodle m: CurrentMoodle.getAllMoodles().getClone()) {
            System.out.println(m.getName());
        }

        launch(args);
    }
}
