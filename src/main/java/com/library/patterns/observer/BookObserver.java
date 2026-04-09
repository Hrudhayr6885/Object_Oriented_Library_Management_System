package com.library.patterns.observer;

import com.library.model.Book;

/**
 * OBSERVER PATTERN — Observer interface.
 *
 * Any class that wants to be notified when a Book becomes available
 * implements this interface. Currently: MemberNotifier.
 *
 * Adding a new notification type (e.g., EmailNotifier) = write one
 * new class implementing this interface. Zero existing code changes.
 * This is the Open/Closed Principle in action.
 */
public interface BookObserver {
    void onBookAvailable(Book book);
}
