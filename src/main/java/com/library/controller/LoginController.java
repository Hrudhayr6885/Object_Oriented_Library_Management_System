package com.library.controller;

import com.library.MainApp;
import com.library.util.PatternLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * LoginController — MVC Controller for the Login screen.
 *
 * Responsibilities:
 *   - Capture user name + role
 *   - Pass the logged-in user info to MainController
 *   - Switch scene to Main.fxml
 *
 * No business logic here — just UI routing (Single Responsibility).
 */
public class LoginController {

    @FXML private TextField nameField;
    @FXML private ComboBox<String> roleCombo;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        roleCombo.getItems().addAll("Member", "Librarian", "Admin");
        roleCombo.setValue("Member");
        errorLabel.setVisible(false);
    }

    @FXML
    private void handleLogin() {
        String name = nameField.getText().trim();
        String role = roleCombo.getValue();

        if (name.isEmpty()) {
            errorLabel.setText("Please enter your name.");
            errorLabel.setVisible(true);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/Main.fxml"));
            Scene scene = new Scene(loader.load(), 1100, 720);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/style.css").toExternalForm());

            MainController mainCtrl = loader.getController();
            mainCtrl.initUser(name, role);

            PatternLogger.log("SINGLETON",
                "Login → MainController called LibraryManager.getInstance()");

            MainApp.primaryStage.setResizable(true);
            MainApp.primaryStage.setScene(scene);
            MainApp.primaryStage.setTitle("Library Management System — " + role);

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error loading main screen.");
            errorLabel.setVisible(true);
        }
    }
}
