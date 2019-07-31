import database.DataDB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("OfflineMoodleDownloader");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Start the db
        try {
            DataDB db = new DataDB();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong with SQL, aborting");
            System.exit(1);
        }

        launch(args);
    }
}
