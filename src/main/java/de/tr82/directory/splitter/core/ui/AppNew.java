package de.tr82.directory.splitter.core.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppNew extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("MainDialog.fxml"));
        primaryStage.setTitle("Directory Splitter");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }
}
