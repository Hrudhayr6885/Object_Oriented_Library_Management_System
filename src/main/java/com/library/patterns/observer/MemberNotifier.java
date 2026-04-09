package com.library.patterns.observer;

import com.library.model.Book;
import com.library.model.Member;
import com.library.util.PatternLogger;

/**
 * MemberNotifier — OBSERVER PATTERN (Concrete Observer)
 * ═══════════════════════════════════════════════════════
 *
 * WHAT IS OBSERVER?
 *   Defines a one-to-many dependency. When the Subject (Book)
 *   changes state, all registered Observers are notified
 *   automatically — without the Subject knowing who they are.
 *
 * WHY HERE?
 *   When a borrowed book is returned and becomes AVAILABLE,
 *   members who were waiting for it should be notified.
 *   TransactionController should NOT need to know about the
 *   notification system. It just calls book.setStatus(AVAILABLE).
 *   The Observer chain fires automatically.
 *
 * HOW IT WORKS:
 *   1. Member clicks "Notify me" → MemberNotifier is registered
 *      on that Book: book.registerObserver(new MemberNotifier(member))
 *   2. When another member returns the book, LibraryFacade calls
 *      book.setStatus(AVAILABLE)
 *   3. Book.setStatus() internally calls notifyObservers()
 *   4. MemberNotifier.onBookAvailable() fires — logs the notification
 *
 * ADDING A NEW NOTIFICATION TYPE (e.g., EmailNotifier):
 *   → Write class EmailNotifier implements BookObserver { ... }
 *   → Register it: book.registerObserver(new EmailNotifier(email))
 *   → Zero changes to Book, TransactionController, or MemberNotifier.
 *   This is Open/Closed Principle in action.
 */
public class MemberNotifier implements BookObserver {

    private final Member waitingMember;

    public MemberNotifier(Member waitingMember) {
        this.waitingMember = waitingMember;
    }

    @Override
    public void onBookAvailable(Book book) {
        PatternLogger.log("OBSERVER",
            "  ✓ MemberNotifier fired → '" + waitingMember.getName() +
            "' notified: \"" + book.getTitle() + "\" is now available!");
    }

    public Member getWaitingMember() { return waitingMember; }
}
