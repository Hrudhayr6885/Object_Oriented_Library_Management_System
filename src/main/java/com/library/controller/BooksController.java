package com.library.controller;

import com.library.model.Book;
import com.library.model.BookType;
import com.library.patterns.facade.LibraryFacade;
import com.library.patterns.singleton.LibraryManager;
import com.library.util.PatternLogger;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * BooksController — MVC Controller for Book management.
 *
 * FACTORY METHOD is clearly visible here:
 *   This controller NEVER writes "new PhysicalBook(...)" or
 *   "new EBook(...)". It calls facade.addBook(..., selectedType, ...)
 *   and the Facade + Factory handles object creation.
 *
 * SINGLETON is visible here:
 *   The TableView binds to LibraryManager.getInstance().getAllBooks()
 *   — the exact same list that TransactionController reads.
 */
public class BooksController {

    // ── Add-book form ────────────────────────────────────────────────
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private ComboBox<BookType> typeCombo;
    @FXML private TextField extraField;
    @FXML private Label extraLabel;
    @FXML private Label statusLabel;

    // ── Search ───────────────────────────────────────────────────────
    @FXML private TextField searchField;

    // ── Table ────────────────────────────────────────────────────────
    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, String> colId;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, BookType>   colType;
    @FXML private TableColumn<Book, ?> colStatus;
    @FXML private TableColumn<Book, String> colExtra;

    private final LibraryFacade facade = new LibraryFacade();
    private final LibraryManager lm    = LibraryManager.getInstance();

    @FXML
    public void initialize() {
        PatternLogger.log("SINGLETON",
            "BooksController → instance #" +
            Integer.toHexString(System.identityHashCode(lm)));

        // Set up type dropdown
        typeCombo.getItems().addAll(BookType.values());
        typeCombo.setValue(BookType.PHYSICAL);
        updateExtraLabel();
        typeCombo.setOnAction(e -> updateExtraLabel());

        // Wire table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colExtra.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getExtraInfo()));

        // Bind to Singleton's live list — auto-updates when books are added
        booksTable.setItems(lm.getAllBooks());

        statusLabel.setVisible(false);
    }

    @FXML
    private void handleAddBook() {
        String title  = titleField.getText().trim();
        String author = authorField.getText().trim();
        String extra  = extraField.getText().trim();
        BookType type = typeCombo.getValue();

        if (title.isEmpty() || author.isEmpty()) {
            showStatus("Title and Author are required.", true);
            return;
        }

        // ── FACTORY METHOD IN ACTION ─────────────────────────────────
        // Notice: No "new PhysicalBook()" or "new EBook()" here.
        // The controller passes the type; Facade + Factory decide the class.
        PatternLogger.log("FACTORY",
            "BooksController requested book type: " + type +
            " — delegating to BookFactoryRegistry");

        facade.addBook(title, author, type, extra);
        showStatus("Book '" + title + "' added as " + type + ".", false);

        titleField.clear(); authorField.clear(); extraField.clear();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            booksTable.setItems(lm.getAllBooks());
        } else {
            booksTable.getItems().setAll(facade.searchBooks(query));
        }
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
        booksTable.setItems(lm.getAllBooks());
    }

    private void updateExtraLabel() {
        switch (typeCombo.getValue()) {
            case PHYSICAL   -> extraLabel.setText("Shelf Location:");
            case EBOOK      -> extraLabel.setText("File URL:");
            case REFERENCE  -> extraLabel.setText("Notes (optional):");
        }
    }

    private void showStatus(String msg, boolean error) {
        statusLabel.setText(msg);
        statusLabel.getStyleClass().removeAll("label-success", "label-danger");
        statusLabel.getStyleClass().add(error ? "label-danger" : "label-success");
        statusLabel.setVisible(true);
    }
}
