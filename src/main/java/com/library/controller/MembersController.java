package com.library.controller;

import com.library.model.Member;
import com.library.patterns.facade.LibraryFacade;
import com.library.patterns.singleton.LibraryManager;
import com.library.util.PatternLogger;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * MembersController — MVC Controller for Member management.
 *
 * FACADE is clearly visible here:
 *   registerMember() is a single facade call. The Facade internally:
 *     1. Generates a member ID
 *     2. Creates the Member object
 *     3. Saves it to LibraryManager
 *   None of those steps appear in this controller.
 *
 * SINGLETON is visible here:
 *   booksTable binds to LibraryManager.getInstance().getAllMembers()
 *   — same instance as BooksController and TransactionsController.
 */
public class MembersController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private Label statusLabel;

    @FXML private TableView<Member> membersTable;
    @FXML private TableColumn<Member, String> colId;
    @FXML private TableColumn<Member, String> colName;
    @FXML private TableColumn<Member, String> colEmail;
    @FXML private TableColumn<Member, String> colPhone;
    @FXML private TableColumn<Member, Double> colFine;

    @FXML private ComboBox<Member> memberCombo;
    @FXML private Label fineDisplayLabel;

    private final LibraryFacade facade = new LibraryFacade();
    private final LibraryManager lm    = LibraryManager.getInstance();

    @FXML
    public void initialize() {
        PatternLogger.log("SINGLETON",
            "MembersController → instance #" +
            Integer.toHexString(System.identityHashCode(lm)));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colFine.setCellValueFactory(new PropertyValueFactory<>("fineAmount"));

        membersTable.setItems(lm.getAllMembers());

        // Fine payment section
        memberCombo.setItems(lm.getAllMembers());
        memberCombo.setOnAction(e -> refreshFineDisplay());

        statusLabel.setVisible(false);
        fineDisplayLabel.setText("");
    }

    @FXML
    private void handleRegister() {
        String name  = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            showStatus("Name and Email are required.", true);
            return;
        }

        // ── FACADE IN ACTION ─────────────────────────────────────────
        // Single call hides: ID generation + object creation + storage
        PatternLogger.log("FACADE",
            "MembersController.handleRegister() → calling facade.registerMember()");
        Member m = facade.registerMember(name, email, phone);

        memberCombo.setItems(lm.getAllMembers());
        showStatus("Member '" + m.getName() + "' registered with ID " + m.getId(), false);
        nameField.clear(); emailField.clear(); phoneField.clear();
    }

    @FXML
    private void handlePayFine() {
        Member selected = memberCombo.getValue();
        if (selected == null) return;

        if (selected.getFineAmount() == 0.0) {
            showStatus("No fine pending for " + selected.getName(), false);
            return;
        }

        facade.payFine(selected.getId());
        refreshFineDisplay();
        showStatus("Fine cleared for " + selected.getName(), false);
        membersTable.refresh();
    }

    private void refreshFineDisplay() {
        Member m = memberCombo.getValue();
        if (m != null) {
            fineDisplayLabel.setText("Pending fine: ₹" + m.getFineAmount());
        }
    }

    private void showStatus(String msg, boolean error) {
        statusLabel.setText(msg);
        statusLabel.getStyleClass().removeAll("label-success", "label-danger");
        statusLabel.getStyleClass().add(error ? "label-danger" : "label-success");
        statusLabel.setVisible(true);
    }
}
