package com.library.patterns.factory;

import com.library.model.BookType;

import java.util.HashMap;
import java.util.Map;

/**
 * BookFactoryRegistry — maps a BookType enum to its factory.
 *
 * GRASP — Pure Fabrication:
 *   No real-world equivalent; invented purely to keep BookController
 *   from knowing any concrete factory class. The controller passes a
 *   BookType and gets back the right factory without any if/else.
 *
 *   BookController code:
 *     BookFactory factory = BookFactoryRegistry.getFactory(selectedType);
 *     Book book = factory.createBook(id, title, author, extra);
 *   That's it. No "if type == PHYSICAL ... else if type == EBOOK ..."
 */
public class BookFactoryRegistry {

    private static final Map<BookType, BookFactory> registry = new HashMap<>();

    static {
        registry.put(BookType.PHYSICAL,   new PhysicalBookFactory());
        registry.put(BookType.EBOOK,      new EBookFactory());
        registry.put(BookType.REFERENCE,  new ReferenceBookFactory());
    }

    public static BookFactory getFactory(BookType type) {
        return registry.get(type);
    }

    private BookFactoryRegistry() {}
}
