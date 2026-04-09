package com.library.model;

/**
 * Discriminator used by the Factory Method pattern.
 * BookFactoryRegistry maps each type to its concrete factory.
 */
public enum BookType {
    PHYSICAL,   // PhysicalBookFactory → PhysicalBook
    EBOOK,      // EBookFactory        → EBook
    REFERENCE   // ReferenceBookFactory → ReferenceBook (in-library only)
}
