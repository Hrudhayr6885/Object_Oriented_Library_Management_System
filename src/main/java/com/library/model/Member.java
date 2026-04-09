package com.library.model;

import javafx.beans.property.*;

/**
 * Member — Domain model for a library member.
 *
 * JavaFX properties allow TableView columns to auto-refresh
 * when data changes (e.g., fine amount updates after return).
 *
 * GRASP — Information Expert:
 *   Member knows its own fine amount. Fine logic lives here
 *   (addFine, clearFine) rather than scattered in controllers.
 */
public class Member {

    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty phone;
    private final DoubleProperty  fineAmount;

    public Member(String id, String name, String email, String phone) {
        this.id         = new SimpleStringProperty(id);
        this.name       = new SimpleStringProperty(name);
        this.email      = new SimpleStringProperty(email);
        this.phone      = new SimpleStringProperty(phone);
        this.fineAmount = new SimpleDoubleProperty(0.0);
    }

    public void addFine(double amount) {
        fineAmount.set(fineAmount.get() + amount);
    }

    public void clearFine() {
        fineAmount.set(0.0);
    }

    // JavaFX property accessors
    public StringProperty idProperty()     { return id; }
    public StringProperty nameProperty()   { return name; }
    public StringProperty emailProperty()  { return email; }
    public StringProperty phoneProperty()  { return phone; }
    public DoubleProperty  fineAmountProperty() { return fineAmount; }

    public String getId()       { return id.get(); }
    public String getName()     { return name.get(); }
    public String getEmail()    { return email.get(); }
    public String getPhone()    { return phone.get(); }
    public double getFineAmount() { return fineAmount.get(); }

    @Override
    public String toString() { return getName() + " (" + getId() + ")"; }
}
