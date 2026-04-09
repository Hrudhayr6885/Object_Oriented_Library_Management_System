package com.library.model;

/**
 * ReferenceBook — Concrete product of ReferenceBookFactory.
 *
 * FACTORY METHOD PATTERN (Product):
 *   In-library use only; cannot be borrowed. Demonstrates Liskov
 *   Substitution — all three book types are treated as Book by
 *   SearchCatalog, but only PhysicalBook and EBook are Borrowable.
 *
 * SOLID — Interface Segregation:
 *   ReferenceBook does NOT implement a Borrowable interface.
 *   It is never passed to the borrow flow.
 */
public class ReferenceBook extends Book {

    public ReferenceBook(String id, String title, String author) {
        super(id, title, author, BookType.REFERENCE);
    }

    @Override
    public String getExtraInfo() {
        return "In-library use only";
    }
}
