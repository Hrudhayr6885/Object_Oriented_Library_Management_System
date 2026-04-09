package com.library.model;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Transaction — Records a single borrow/return event.
 *
 * Created by LibraryFacade.borrowBook() and closed by
 * LibraryFacade.returnBook(). Fine is calculated on return
 * if the return date exceeds dueDate.
 *
 * Fine rate: ₹5 per overdue day (kept simple for demo).
 */
public class Transaction {

    private static final double FINE_PER_DAY = 5.0;

    private final StringProperty id;
    private final StringProperty bookId;
    private final StringProperty memberId;
    private final ObjectProperty<LocalDate> issueDate;
    private final ObjectProperty<LocalDate> dueDate;
    private final BooleanProperty returned;
    private final DoubleProperty fine;

    public Transaction(String id, String bookId, String memberId) {
        this.id        = new SimpleStringProperty(id);
        this.bookId    = new SimpleStringProperty(bookId);
        this.memberId  = new SimpleStringProperty(memberId);
        this.issueDate = new SimpleObjectProperty<>(LocalDate.now());
        this.dueDate   = new SimpleObjectProperty<>(LocalDate.now().plusDays(14));
        this.returned  = new SimpleBooleanProperty(false);
        this.fine      = new SimpleDoubleProperty(0.0);
    }

    /**
     * Mark as returned, compute fine if overdue.
     * @return fine amount charged (0 if on time)
     */
    public double processReturn() {
        returned.set(true);
        long daysLate = LocalDate.now().toEpochDay() - dueDate.get().toEpochDay();
        if (daysLate > 0) {
            double calculatedFine = daysLate * FINE_PER_DAY;
            fine.set(calculatedFine);
            return calculatedFine;
        }
        return 0.0;
    }

    public String getStatusDisplay() {
        if (returned.get()) return fine.get() > 0 ? "Returned (Fine ₹" + fine.get() + ")" : "Returned";
        return LocalDate.now().isAfter(dueDate.get()) ? "OVERDUE" : "Active";
    }

    // JavaFX property accessors
    public StringProperty idProperty()       { return id; }
    public StringProperty bookIdProperty()   { return bookId; }
    public StringProperty memberIdProperty() { return memberId; }
    public ObjectProperty<LocalDate> issueDateProperty() { return issueDate; }
    public ObjectProperty<LocalDate> dueDateProperty()   { return dueDate; }
    public BooleanProperty returnedProperty() { return returned; }
    public DoubleProperty  fineProperty()     { return fine; }

    public String getId()       { return id.get(); }
    public String getBookId()   { return bookId.get(); }
    public String getMemberId() { return memberId.get(); }
    public LocalDate getIssueDate() { return issueDate.get(); }
    public LocalDate getDueDate()   { return dueDate.get(); }
    public boolean isReturned() { return returned.get(); }
    public double getFine()     { return fine.get(); }

    @Override
    public String toString() { return getId() + " | Book: " + bookId.get() + " | Member: " + memberId.get(); }
}
