package com.library.patterns.singleton;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.util.PatternLogger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

/**
 * LibraryManager — SINGLETON PATTERN
 * ════════════════════════════════════
 *
 * WHAT IS SINGLETON?
 *   Ensures a class has exactly ONE instance for the entire
 *   application lifetime and provides a global access point.
 *
 * WHY HERE?
 *   All controllers (Books, Members, Transactions) need to read
 *   and write the same data. If each created its own LibraryManager,
 *   adding a book in BooksController would be invisible to
 *   TransactionController. One instance = one source of truth.
 *
 * HOW IT WORKS:
 *   1. Constructor is private → nobody can call "new LibraryManager()"
 *   2. Static field holds the one instance
 *   3. getInstance() creates it on first call, returns it every time
 *
 * DEMO PROOF:
 *   LibraryManager.getInstance().hashCode() prints the SAME number
 *   no matter which controller calls it.
 */
public class LibraryManager {

    // ── Step 1: The single private instance ─────────────────────────
    private static LibraryManager instance;

    // ── Observable lists — JavaFX UI auto-refreshes on changes ──────
    private final ObservableList<Book>        books        = FXCollections.observableArrayList();
    private final ObservableList<Member>      members      = FXCollections.observableArrayList();
    private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    // ── Step 2: Private constructor — blocks external instantiation ──
    private LibraryManager() {
        PatternLogger.log("SINGLETON", "LibraryManager instance CREATED (this happens only once)");
    }

    // ── Step 3: Global access point ─────────────────────────────────
    public static synchronized LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        PatternLogger.log("SINGLETON",
            "getInstance() called → returning instance #" +
            Integer.toHexString(System.identityHashCode(instance)));
        return instance;
    }

    // ── Book operations ──────────────────────────────────────────────

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(String bookId) {
        books.removeIf(b -> b.getId().equals(bookId));
    }

    public Optional<Book> getBook(String bookId) {
        return books.stream().filter(b -> b.getId().equals(bookId)).findFirst();
    }

    public ObservableList<Book> getAllBooks() {
        return books;
    }

    public ObservableList<Book> getAvailableBooks() {
        return books.filtered(b -> b.getStatus() == BookStatus.AVAILABLE &&
                                   b.getType() != com.library.model.BookType.REFERENCE);
    }

    // ── Member operations ────────────────────────────────────────────

    public void addMember(Member member) {
        members.add(member);
    }

    public Optional<Member> getMember(String memberId) {
        return members.stream().filter(m -> m.getId().equals(memberId)).findFirst();
    }

    public ObservableList<Member> getAllMembers() {
        return members;
    }

    // ── Transaction operations ───────────────────────────────────────

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public Optional<Transaction> getTransaction(String transactionId) {
        return transactions.stream()
                .filter(t -> t.getId().equals(transactionId)).findFirst();
    }

    public ObservableList<Transaction> getAllTransactions() {
        return transactions;
    }

    public ObservableList<Transaction> getActiveTransactions() {
        return transactions.filtered(t -> !t.isReturned());
    }

    // ── ID generator ─────────────────────────────────────────────────
    public String nextBookId()   { return "B" + String.format("%03d", books.size() + 1); }
    public String nextMemberId() { return "M" + String.format("%03d", members.size() + 1); }
    public String nextTxnId()    { return "T" + String.format("%03d", transactions.size() + 1); }
}
