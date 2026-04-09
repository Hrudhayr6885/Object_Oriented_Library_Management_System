package com.library.model;

/**
 * EBook — Concrete product of EBookFactory.
 *
 * FACTORY METHOD PATTERN (Product):
 *   Same Book interface, different data. The factory decides which
 *   class to instantiate; the controller just uses the Book reference.
 *
 * Extra field: fileUrl — specific to digital books.
 */
public class EBook extends Book {

    private final String fileUrl;

    public EBook(String id, String title, String author, String fileUrl) {
        super(id, title, author, BookType.EBOOK);
        this.fileUrl = fileUrl;
    }

    @Override
    public String getExtraInfo() {
        return "URL: " + fileUrl;
    }

    public String getFileUrl() { return fileUrl; }
}
