package com.library.controller;

import com.library.patterns.singleton.LibraryManager;
import com.library.util.PatternLogger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

/**
 * MainController — MVC Controller for the main application window.
 *
 * Owns the outer shell: top bar (user info) + TabPane + Pattern Log.
 * Each tab loads its own FXML and controller independently.
 *
 * SINGLETON DEMO:
 *   All 4 controllers (Books, Members, Transactions, this one)
 *   call LibraryManager.getInstance(). The Pattern Log will show
 *   the same hashcode each time — proving one instance.
 */
public class MainController {

    @FXML private Label userLabel;
    @FXML private Label roleLabel;
    @FXML private TextArea patternLog;
    @FXML private Tab booksTab;
    @FXML private Tab membersTab;
    @FXML private Tab transactionsTab;

    @FXML
    public void initialize() {
        // Subscribe the TextArea to PatternLogger
        // Every pattern event now appears live in the UI log
        PatternLogger.addListener(entry ->
            Platform.runLater(() -> {
                patternLog.appendText(entry + "\n");
                patternLog.setScrollTop(Double.MAX_VALUE);
            })
        );

        loadTabContent(booksTab,        "/fxml/Books.fxml");
        loadTabContent(membersTab,      "/fxml/Members.fxml");
        loadTabContent(transactionsTab, "/fxml/Transactions.fxml");

        // Prove Singleton — same instance from MainController
        LibraryManager lm = LibraryManager.getInstance();
        PatternLogger.log("SINGLETON",
            "MainController → instance #" + Integer.toHexString(System.identityHashCode(lm)) +
            "  (watch: all controllers print same #)");
    }

    public void initUser(String name, String role) {
        userLabel.setText("Welcome, " + name);
        roleLabel.setText("Role: " + role);
    }

    private void loadTabContent(Tab tab, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            tab.setContent(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
