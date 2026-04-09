package com.library.patterns.factory;

import com.library.model.Book;
import com.library.model.ReferenceBook;
import com.library.util.PatternLogger;

/**
 * FACTORY METHOD PATTERN — Concrete Creator for Reference Books.
 *
 * Reference books are in-library use only. They are created the
 * same way as other books from the controller's perspective,
 * but the product (ReferenceBook) blocks borrowing at the Facade level.
 */
public class ReferenceBookFactory implements BookFactory {

    @Override
    public Book createBook(String id, String title, String author, String extra) {
        PatternLogger.log("FACTORY",
            "ReferenceBookFactory.createBook() → new ReferenceBook('" + title + "') [in-library only]");
        return new ReferenceBook(id, title, author);
    }
}
