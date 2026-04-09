package com.library.model;

import com.library.patterns.observer.BookObserver;
import com.library.patterns.observer.BookSubject;
import com.library.util.PatternLogger;
import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Book — Core domain model AND the Subject in the Observer pattern.
 *
 * OBSERVER PATTERN (Subject side):
 *   Book maintains a list of BookObserver objects. When its status
 *   changes to AVAILABLE (i.e., a book is returned), it automatically
 *   calls notifyObservers(). The TransactionController simply calls
 *   book.setStatus(AVAILABLE) — it never knows about MemberNotifier
 *   or any other observer. They are completely decoupled.
 *
 * JavaFX Properties are used so TableView columns bind automatically.
 */
public abstract class Book implements BookSubject {

    // JavaFX properties — enable automatic TableView binding
    private final StringProperty id;
    private final StringProperty title;
    private final StringProperty author;
    private final ObjectProperty<BookStatus> status;
    private final ObjectProperty<BookType> type;

    // Observer list — maintained by this Subject
    private final List<BookObserver> observers = new ArrayList<>();

    protected Book(String id, String title, String author, BookType type) {
        this.id     = new SimpleStringProperty(id);
        this.title  = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.status = new SimpleObjectProperty<>(BookStatus.AVAILABLE);
        this.type   = new SimpleObjectProperty<>(type);
    }

    // ── Observer Pattern ────────────────────────────────────────────

    @Override
    public void registerObserver(BookObserver observer) {
        observers.add(observer);
        PatternLogger.log("OBSERVER",
            "Observer registered on book '" + getTitle() + "' — watching for availability");
    }

    @Override
    public void removeObserver(BookObserver observer) {
        observers.remove(observer);
    }

    /**
     * Called automatically when status → AVAILABLE.
     * TransactionController never calls this directly — it just sets status.
     */
    @Override
    public void notifyObservers() {
        PatternLogger.log("OBSERVER",
            "'" + getTitle() + "' is now AVAILABLE → notifying " + observers.size() + " observer(s)");
        for (BookObserver observer : observers) {
            observer.onBookAvailable(this);
        }
    }

    // ── Status setter — triggers Observer notification ───────────────

    public void setStatus(BookStatus newStatus) {
        this.status.set(newStatus);
        if (newStatus == BookStatus.AVAILABLE && !observers.isEmpty()) {
            notifyObservers();
        }
    }

    // ── Abstract method — subclasses provide extra info for display ──

    public abstract String getExtraInfo();

    // ── JavaFX Property accessors ────────────────────────────────────

    public StringProperty idProperty()     { return id; }
    public StringProperty titleProperty()  { return title; }
    public StringProperty authorProperty() { return author; }
    public ObjectProperty<BookStatus> statusProperty() { return status; }
    public ObjectProperty<BookType>   typeProperty()   { return type; }

    public String getId()     { return id.get(); }
    public String getTitle()  { return title.get(); }
    public String getAuthor() { return author.get(); }
    public BookStatus getStatus() { return status.get(); }
    public BookType   getType()   { return type.get(); }

    @Override
    public String toString() { return getTitle() + " (" + getId() + ")"; }
}
