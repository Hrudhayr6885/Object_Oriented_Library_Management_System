package com.library.patterns.factory;

import com.library.model.Book;
import com.library.model.PhysicalBook;
import com.library.util.PatternLogger;

/**
 * FACTORY METHOD PATTERN — Concrete Creator for Physical Books.
 *
 * createBook() returns a PhysicalBook. The caller (BookController)
 * only knows about the BookFactory interface and the Book base type.
 */
public class PhysicalBookFactory implements BookFactory {

    @Override
    public Book createBook(String id, String title, String author, String shelfLocation) {
        PatternLogger.log("FACTORY",
            "PhysicalBookFactory.createBook() → new PhysicalBook('" + title + "', shelf=" + shelfLocation + ")");
        return new PhysicalBook(id, title, author, shelfLocation);
    }
}
