package com.library.util;

import com.library.model.BookType;
import com.library.patterns.facade.LibraryFacade;
import com.library.patterns.singleton.LibraryManager;

/**
 * SampleDataLoader — loads demo-ready data on startup.
 *
 * Having pre-loaded data means the demo can start immediately
 * without having to add books/members live (which wastes time).
 * The demo audience sees a realistic system from the first screen.
 */
public class SampleDataLoader {

    public static void load() {
        LibraryFacade facade = new LibraryFacade();

        // ── Physical Books ────────────────────────────────────────────
        facade.addBook("Clean Code",                      "Robert C. Martin",    BookType.PHYSICAL, "A1-S3");
        facade.addBook("Design Patterns",                 "GoF",                 BookType.PHYSICAL, "B2-S1");
        facade.addBook("Effective Java",                  "Joshua Bloch",        BookType.PHYSICAL, "A2-S4");
        facade.addBook("The Pragmatic Programmer",        "Hunt & Thomas",       BookType.PHYSICAL, "A3-S2");
        facade.addBook("Introduction to Algorithms",      "CLRS",                BookType.PHYSICAL, "C1-S1");
        facade.addBook("Computer Networks",               "Andrew Tanenbaum",    BookType.PHYSICAL, "C2-S3");
        facade.addBook("Operating System Concepts",       "Silberschatz",        BookType.PHYSICAL, "C3-S1");
        facade.addBook("Database System Concepts",        "Korth & Sudarshan",   BookType.PHYSICAL, "C3-S4");
        facade.addBook("Software Engineering",            "Ian Sommerville",     BookType.PHYSICAL, "D1-S2");
        facade.addBook("Artificial Intelligence",         "Russell & Norvig",    BookType.PHYSICAL, "D2-S1");
        facade.addBook("Code Complete",                   "Steve McConnell",     BookType.PHYSICAL, "A1-S5");
        facade.addBook("Domain-Driven Design",            "Eric Evans",          BookType.PHYSICAL, "B1-S3");

        // ── EBooks ───────────────────────────────────────────────────
        facade.addBook("Head First Java",                 "Kathy Sierra",        BookType.EBOOK,    "lib.edu/hfj.pdf");
        facade.addBook("Refactoring",                     "Martin Fowler",       BookType.EBOOK,    "lib.edu/refactoring.pdf");
        facade.addBook("Spring in Action",                "Craig Walls",         BookType.EBOOK,    "lib.edu/spring.pdf");
        facade.addBook("Java: The Complete Reference",    "Herbert Schildt",     BookType.EBOOK,    "lib.edu/jcr.pdf");
        facade.addBook("Learning Python",                 "Mark Lutz",           BookType.EBOOK,    "lib.edu/python.pdf");
        facade.addBook("You Don't Know JS",               "Kyle Simpson",        BookType.EBOOK,    "lib.edu/ydkjs.pdf");
        facade.addBook("The Clean Coder",                 "Robert C. Martin",    BookType.EBOOK,    "lib.edu/cleancoder.pdf");
        facade.addBook("Continuous Delivery",             "Humble & Farley",     BookType.EBOOK,    "lib.edu/cd.pdf");

        // ── Reference Books (in-library use only) ────────────────────
        facade.addBook("Java Concurrency in Practice",    "Brian Goetz",         BookType.REFERENCE, "");
        facade.addBook("IEEE Software Standards",         "IEEE",                BookType.REFERENCE, "");
        facade.addBook("Data Structures Handbook",        "Various Authors",     BookType.REFERENCE, "");
        facade.addBook("ACM Digital Library Index",       "ACM",                 BookType.REFERENCE, "");

        // ── Members ───────────────────────────────────────────────────
        facade.registerMember("Alice Sharma",    "alice@pes.edu",    "9900001111");
        facade.registerMember("Bob Nair",        "bob@pes.edu",      "9900002222");
        facade.registerMember("Charlie Reddy",   "charlie@pes.edu",  "9900003333");
        facade.registerMember("Diana Fernandez", "diana@pes.edu",    "9900004444");
        facade.registerMember("Ethan Patel",     "ethan@pes.edu",    "9900005555");
        facade.registerMember("Fatima Khan",     "fatima@pes.edu",   "9900006666");
        facade.registerMember("George Thomas",   "george@pes.edu",   "9900007777");
        facade.registerMember("Hannah Iyer",     "hannah@pes.edu",   "9900008888");

        System.out.println("\n── Sample data loaded (" +
                LibraryManager.getInstance().getAllBooks().size() + " books, " +
                LibraryManager.getInstance().getAllMembers().size() + " members) ──\n");
    }

    private SampleDataLoader() {}
}
