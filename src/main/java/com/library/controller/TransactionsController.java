package com.library.controller;

import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.patterns.facade.LibraryFacade;
import com.library.patterns.singleton.LibraryManager;
import com.library.util.PatternLogger;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * TransactionsController — MVC Controller for Issue / Return / Observer demo.
 *
 * This controller demonstrates ALL 4 patterns in one place:
 *
 *   SINGLETON : lm = LibraryManager.getInstance() — same instance
 *   FACTORY   : borrowing calls facade which calls factory for the book
 *   FACADE    : facade.borrowBook() and facade.returnBook() are single calls
 *   OBSERVER  : "Watch Book" button registers a MemberNotifier on a Book.
 *               When that book is returned, the Observer fires automatically.
 *               This controller NEVER calls MemberNotifier directly.
 */
public class TransactionsController {

    // ── Issue section ────────────────────────────────────────────────
    @FXML private ComboBox<Member> issueMemberCombo;
    @FXML private ComboBox<Book>   issueBookCombo;
    @FXML private Label            issueStatusLabel;

    // ── Return section ───────────────────────────────────────────────
    @FXML private ComboBox<Transaction> returnTxnCombo;
    @FXML private Label                 returnStatusLabel;

    // ── Observer demo section ────────────────────────────────────────
    @FXML private ComboBox<Member> watchMemberCombo;
    @FXML private ComboBox<Book>   watchBookCombo;
    @FXML private Label            watchStatusLabel;

    // ── Transaction table ────────────────────────────────────────────
    @FXML private TableView<Transaction> txnTable;
    @FXML private TableColumn<Transaction, String> colTxnId;
    @FXML private TableColumn<Transaction, String> colBookId;
    @FXML private TableColumn<Transaction, String> colMemberId;
    @FXML private TableColumn<Transaction, ?>      colIssueDate;
    @FXML private TableColumn<Transaction, ?>      colDueDate;
    @FXML private TableColumn<Transaction, Double> colFine;

    private final LibraryFacade  facade = new LibraryFacade();
    private final LibraryManager lm     = LibraryManager.getInstance();

    @FXML
    public void initialize() {
        PatternLogger.log("SINGLETON",
            "TransactionsController → instance #" +
            Integer.toHexString(System.identityHashCode(lm)) +
            "  ← compare with BooksController and MembersController above");

        // Populate dropdowns from Singleton's live lists
        issueMemberCombo.setItems(lm.getAllMembers());
        issueBookCombo.setItems(lm.getAvailableBooks());
        returnTxnCombo.setItems(lm.getActiveTransactions());
        watchMemberCombo.setItems(lm.getAllMembers());
        watchBookCombo.setItems(lm.getAllBooks());

        // Table columns
        colTxnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colIssueDate.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colFine.setCellValueFactory(new PropertyValueFactory<>("fine"));

        // Status column — computed from transaction state
        txnTable.setItems(lm.getAllTransactions());

        issueStatusLabel.setVisible(false);
        returnStatusLabel.setVisible(false);
        watchStatusLabel.setVisible(false);
    }

    // ── Issue Book ───────────────────────────────────────────────────
    @FXML
    private void handleIssueBook() {
        Member m = issueMemberCombo.getValue();
        Book   b = issueBookCombo.getValue();

        if (m == null || b == null) {
            showLabel(issueStatusLabel, "Select a member and a book.", true);
            return;
        }

        // ── FACADE in action — one call, multiple subsystem steps ────
        PatternLogger.log("FACADE",
            "TransactionsController → facade.borrowBook('" +
            m.getName() + "', '" + b.getTitle() + "')");

        Transaction txn = facade.borrowBook(m.getId(), b.getId());

        if (txn != null) {
            showLabel(issueStatusLabel,
                "Issued '" + b.getTitle() + "' to " + m.getName() +
                " | Due: " + txn.getDueDate(), false);
            refreshDropdowns();
        } else {
            showLabel(issueStatusLabel, "Could not issue book — check availability.", true);
        }
    }

    // ── Return Book ──────────────────────────────────────────────────
    @FXML
    private void handleReturnBook() {
        Transaction txn = returnTxnCombo.getValue();
        if (txn == null) {
            showLabel(returnStatusLabel, "Select a transaction to return.", true);
            return;
        }

        // ── FACADE in action ─────────────────────────────────────────
        // facade.returnBook() internally:
        //   1. Calculates fine  2. Charges member  3. Sets book.setStatus(AVAILABLE)
        // Step 3 triggers the Observer chain — THIS controller never calls MemberNotifier
        PatternLogger.log("FACADE",
            "TransactionsController → facade.returnBook('" + txn.getId() + "')");

        double fine = facade.returnBook(txn.getId());

        String msg = fine > 0
            ? "Book returned. Fine charged: ₹" + fine
            : "Book returned on time. No fine.";
        showLabel(returnStatusLabel, msg, fine > 0);

        refreshDropdowns();
        txnTable.refresh();
    }

    // ── Watch Book (Observer demo) ────────────────────────────────────
    @FXML
    private void handleWatchBook() {
        Member m = watchMemberCombo.getValue();
        Book   b = watchBookCombo.getValue();

        if (m == null || b == null) {
            showLabel(watchStatusLabel, "Select member and book to watch.", true);
            return;
        }

        // ── OBSERVER in action ────────────────────────────────────────
        // Registers a MemberNotifier observer ON the book object.
        // When this book's status → AVAILABLE, MemberNotifier fires.
        // This controller never interacts with MemberNotifier again.
        PatternLogger.log("OBSERVER",
            "TransactionsController → facade.watchBook('" +
            m.getName() + "', '" + b.getTitle() + "')");

        facade.watchBook(b.getId(), m.getId());
        showLabel(watchStatusLabel,
            m.getName() + " is now watching '" + b.getTitle() +
            "'. Return the book to trigger the notification.", false);
    }

    private void refreshDropdowns() {
        issueBookCombo.setItems(lm.getAvailableBooks());
        returnTxnCombo.setItems(lm.getActiveTransactions());
    }

    private void showLabel(Label label, String msg, boolean error) {
        label.setText(msg);
        label.getStyleClass().removeAll("label-success", "label-danger");
        label.getStyleClass().add(error ? "label-danger" : "label-success");
        label.setVisible(true);
    }
}
