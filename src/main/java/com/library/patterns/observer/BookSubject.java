package com.library.patterns.observer;

/**
 * OBSERVER PATTERN — Subject interface.
 *
 * Book implements this to maintain a list of observers and notify
 * them on state changes. Keeping it as an interface follows the
 * Dependency Inversion Principle — high-level code depends on this
 * abstraction, not on the concrete Book class.
 */
public interface BookSubject {
    void registerObserver(BookObserver observer);
    void removeObserver(BookObserver observer);
    void notifyObservers();
}
