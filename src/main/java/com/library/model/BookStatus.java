package com.library.model;

/**
 * Represents the availability state of a book.
 * Used by the Observer pattern — when status changes to AVAILABLE,
 * the Book notifies all registered observers.
 */
public enum BookStatus {
    AVAILABLE,
    BORROWED,
    OVERDUE
}
