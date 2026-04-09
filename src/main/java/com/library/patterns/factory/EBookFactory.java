package com.library.patterns.factory;

import com.library.model.Book;
import com.library.model.EBook;
import com.library.util.PatternLogger;

/**
 * FACTORY METHOD PATTERN — Concrete Creator for EBooks.
 */
public class EBookFactory implements BookFactory {

    @Override
    public Book createBook(String id, String title, String author, String fileUrl) {
        PatternLogger.log("FACTORY",
            "EBookFactory.createBook() → new EBook('" + title + "', url=" + fileUrl + ")");
        return new EBook(id, title, author, fileUrl);
    }
}
