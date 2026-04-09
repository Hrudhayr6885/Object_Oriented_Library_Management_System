package com.library;

import com.library.util.SampleDataLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application entry point.
 *
 * Start order:
 *   1. SampleDataLoader pre-fills books + members (demo ready)
 *   2. Login screen shown — user selects role
 *   3. LoginController switches to Main.fxml on "Enter"
 */
public class MainApp extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // Load demo data before UI appears
        SampleDataLoader.load();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(loader.load(), 480, 360);
        scene.getStylesheets().add(
                getClass().getResource("/styles/style.css").toExternalForm());

        stage.setTitle("Library Management System");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
