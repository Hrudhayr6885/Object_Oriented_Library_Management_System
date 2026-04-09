package com.library.patterns.factory;

import com.library.model.Book;

/**
 * BookFactory — FACTORY METHOD PATTERN (Creator interface)
 * ══════════════════════════════════════════════════════════
 *
 * WHAT IS FACTORY METHOD?
 *   Defines an interface for creating an object, but lets subclasses
 *   decide which class to instantiate. The creator never uses
 *   "new ConcreteProduct()" directly.
 *
 * WHY HERE?
 *   The system supports 3 book types: Physical, EBook, Reference.
 *   Each has different fields (shelf location, file URL, etc.).
 *   If BookController called "new PhysicalBook(...)" directly,
 *   adding a new type (AudioBook) would require changing the
 *   controller — violating the Open/Closed Principle.
 *   With this factory, adding AudioBook = write AudioBookFactory.
 *   BookController never changes.
 *
 * CONCRETE FACTORIES:
 *   PhysicalBookFactory  → creates PhysicalBook
 *   EBookFactory         → creates EBook
 *   ReferenceBookFactory → creates ReferenceBook
 */
public interface BookFactory {
    /**
     * Factory method — each concrete factory implements this
     * to create its specific type of Book.
     *
     * @param id     unique book ID
     * @param title  book title
     * @param author book author
     * @param extra  type-specific info (shelf / URL / "")
     */
    Book createBook(String id, String title, String author, String extra);
}
