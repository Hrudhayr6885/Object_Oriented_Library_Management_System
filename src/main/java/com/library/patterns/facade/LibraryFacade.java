package com.library.patterns.facade;

import com.library.model.*;
import com.library.patterns.factory.BookFactory;
import com.library.patterns.factory.BookFactoryRegistry;
import com.library.patterns.observer.MemberNotifier;
import com.library.patterns.singleton.LibraryManager;
import com.library.util.PatternLogger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * LibraryFacade — FACADE PATTERN
 * ════════════════════════════════
 *
 * WHAT IS FACADE?
 *   Provides a simplified, unified interface to a complex set of
 *   subsystem classes. Clients talk to the Facade only; the Facade
 *   delegates to the right subsystem internally.
 *
 * WHY HERE?
 *   Borrowing a book involves multiple subsystem steps:
 *     1. Check book availability
 *     2. Verify member exists
 *     3. Create a Transaction record
 *     4. Update book status → BORROWED
 *     5. Save everything to LibraryManager
 *   If MembersController did all 5 steps directly, it would be tightly
 *   coupled to 4 different classes. Any change to any step breaks the
 *   controller. With Facade, the controller makes ONE call:
 *     libraryFacade.borrowBook(memberId, bookId)
 *   The 5 steps are hidden inside this class.
 *
 * DEMO PROOF:
 *   Open MembersController — borrowBook is 1 line.
 *   Open LibraryFacade.borrowBook() — 5 steps are visible.
 *   Ask: "Without Facade, all 5 steps would be in the controller.
 *         Facade keeps each layer clean and focused."
 *
 * GRASP principles:
 *   - Controller: Facade handles system-level operations
 *   - Low Coupling: Controllers depend only on LibraryFacade
 *   - High Cohesion: Each method hides one complete workflow
 */
public class LibraryFacade {

    private final LibraryManager libraryManager;

    public LibraryFacade() {
        this.libraryManager = LibraryManager.getInstance();
    }

    // ── Borrow a Book ────────────────────────────────────────────────

    /**
     * Hides 5 subsystem steps behind a single call.
     * @return the created Transaction, or null if validation fails
     */
    public Transaction borrowBook(String memberId, String bookId) {
        PatternLogger.log("FACADE", "borrowBook() called — starting 4-step workflow:");

        // Step 1: Validate book
        Book book = libraryManager.getBook(bookId).orElse(null);
        if (book == null || book.getStatus() != BookStatus.AVAILABLE) {
            PatternLogger.log("FACADE", "  ✗ Step 1: Book not available — aborting");
            return null;
        }
        PatternLogger.log("FACADE", "  ✓ Step 1: Book '" + book.getTitle() + "' is AVAILABLE");

        // Step 2: Validate member
        Member member = libraryManager.getMember(memberId).orElse(null);
        if (member == null) {
            PatternLogger.log("FACADE", "  ✗ Step 2: Member not found — aborting");
            return null;
        }
        PatternLogger.log("FACADE", "  ✓ Step 2: Member '" + member.getName() + "' verified");

        // Step 3: Create transaction
        Transaction txn = new Transaction(libraryManager.nextTxnId(), bookId, memberId);
        libraryManager.addTransaction(txn);
        PatternLogger.log("FACADE", "  ✓ Step 3: Transaction " + txn.getId() + " created (due: " + txn.getDueDate() + ")");

        // Step 4: Update book status
        book.setStatus(BookStatus.BORROWED);
        PatternLogger.log("FACADE", "  ✓ Step 4: Book status → BORROWED  ——  borrowBook() complete");

        return txn;
    }

    // ── Return a Book ────────────────────────────────────────────────

    /**
     * Processes a return: calculates fine, updates statuses, fires Observer.
     * @return fine amount charged (0.0 if returned on time)
     */
    public double returnBook(String transactionId) {
        PatternLogger.log("FACADE", "returnBook() called — starting return workflow:");

        Transaction txn = libraryManager.getTransaction(transactionId).orElse(null);
        if (txn == null || txn.isReturned()) {
            PatternLogger.log("FACADE", "  ✗ Transaction not found or already returned");
            return 0.0;
        }

        // Step 1: Calculate fine
        double fine = txn.processReturn();
        PatternLogger.log("FACADE", "  ✓ Step 1: Fine calculated = ₹" + fine);

        // Step 2: Charge member if fine exists
        if (fine > 0) {
            libraryManager.getMember(txn.getMemberId())
                    .ifPresent(m -> m.addFine(fine));
            PatternLogger.log("FACADE", "  ✓ Step 2: Fine ₹" + fine + " charged to member");
        }

        // Step 3: Update book status → AVAILABLE (this fires the Observer chain)
        PatternLogger.log("FACADE", "  ✓ Step 3: Setting book status → AVAILABLE (Observer will fire if anyone is waiting)");
        libraryManager.getBook(txn.getBookId())
                .ifPresent(b -> b.setStatus(BookStatus.AVAILABLE));

        PatternLogger.log("FACADE", "  ✓ returnBook() complete");
        return fine;
    }

    // ── Register a Member ────────────────────────────────────────────

    public Member registerMember(String name, String email, String phone) {
        PatternLogger.log("FACADE", "registerMember('" + name + "') → creating Member via Facade");
        Member member = new Member(libraryManager.nextMemberId(), name, email, phone);
        libraryManager.addMember(member);
        return member;
    }

    // ── Add a Book ───────────────────────────────────────────────────

    /**
     * Uses Factory Method pattern internally to create the right Book type.
     * The controller passes a BookType; Facade picks the factory.
     */
    public Book addBook(String title, String author, BookType type, String extra) {
        PatternLogger.log("FACADE", "addBook() → delegating to Factory for type: " + type);
        BookFactory factory = BookFactoryRegistry.getFactory(type);
        Book book = factory.createBook(libraryManager.nextBookId(), title, author, extra);
        libraryManager.addBook(book);
        PatternLogger.log("FACADE", "  ✓ Book added: " + book.getClass().getSimpleName() + " '" + title + "'");
        return book;
    }

    // ── Search Books ─────────────────────────────────────────────────

    public List<Book> searchBooks(String query) {
        String q = query.toLowerCase();
        return libraryManager.getAllBooks().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(q) ||
                             b.getAuthor().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    // ── Register Observer on a Book ──────────────────────────────────

    public void watchBook(String bookId, String memberId) {
        Book book = libraryManager.getBook(bookId).orElse(null);
        Member member = libraryManager.getMember(memberId).orElse(null);
        if (book != null && member != null) {
            book.registerObserver(new MemberNotifier(member));
        }
    }

    // ── Pay Fine ─────────────────────────────────────────────────────

    public void payFine(String memberId) {
        libraryManager.getMember(memberId).ifPresent(m -> {
            PatternLogger.log("FACADE", "payFine() → clearing ₹" + m.getFineAmount() + " for " + m.getName());
            m.clearFine();
        });
    }
}
