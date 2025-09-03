package zbot.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import zbot.Zbot;

/**
 * A GUI for Zbot using FXML.
 */
public class Main extends Application {

    private Zbot zbot = new Zbot("./data/zbot.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Zbot");
            fxmlLoader.<MainWindow>getController().setZbot(zbot);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

