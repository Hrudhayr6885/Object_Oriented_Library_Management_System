package com.library.model;

/**
 * PhysicalBook — Concrete product of PhysicalBookFactory.
 *
 * FACTORY METHOD PATTERN (Product):
 *   PhysicalBookFactory.createBook() returns an instance of this class.
 *   BookController never writes "new PhysicalBook(...)".
 *   It calls factory.createBook(...) and gets back a Book reference.
 *
 * Extra field: shelfLocation — specific to physical books.
 */
public class PhysicalBook extends Book {

    private final String shelfLocation;

    public PhysicalBook(String id, String title, String author, String shelfLocation) {
        super(id, title, author, BookType.PHYSICAL);
        this.shelfLocation = shelfLocation;
    }

    @Override
    public String getExtraInfo() {
        return "Shelf: " + shelfLocation;
    }

    public String getShelfLocation() { return shelfLocation; }
}
