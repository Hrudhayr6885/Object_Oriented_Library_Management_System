# 📚 Object-Oriented Library Management System

> **Course:** UE23CS352B — Object Oriented Analysis & Design  
> **University:** PES University, Bengaluru  
> **Semester:** 6th Semester, Section D  
> **Faculty:** Prof. Sowmya A M

A JavaFX desktop application for managing a library — built as a mini-project to demonstrate four Gang-of-Four design patterns (Singleton, Factory Method, Facade, Observer) and all five SOLID principles in a real, working system.

---

##  Features :

- 🔐 **Role-based login** — Member, Librarian, and Admin dashboards
- 📖 **Multi-type book management** — Physical Books, EBooks, and Reference Books
- 🔄 **Borrow & Return workflow** — availability checks, due-date tracking, fine calculation
- 🔔 **Live notifications** — members are automatically notified when a waited-on book becomes available
- 💰 **Fine management** — automatic overdue fine calculation (₹/day) with a pay-fine flow
- 🔍 **Book search** — search by title or author across the full catalog
- 🧪 **Demo data pre-loaded** — books and members are seeded on startup so the app is instantly usable
- 🪵 **Pattern logger** — every design-pattern event is logged to the console so you can see patterns firing live

---

## 🏗️ Design Patterns

### 1. Singleton — `LibraryManager`
**Package:** `com.library.patterns.singleton`

Ensures exactly **one** `LibraryManager` instance exists for the entire application. All four controllers call `LibraryManager.getInstance()` — they all get the same object, guaranteeing a single source of truth for books, members, and transactions.

```java
// Proof — both lines print the same hashCode
System.out.println(LibraryManager.getInstance().hashCode()); // from BooksController
System.out.println(LibraryManager.getInstance().hashCode()); // from TransactionsController
```

---

### 2. Factory Method — `BookFactory`
**Package:** `com.library.patterns.factory`

`BookController` never writes `new PhysicalBook()`. It calls `factory.createBook(...)` on whichever factory `BookFactoryRegistry` returns. Adding a new book type (e.g., `AudioBook`) means writing one new class — **zero changes** to any existing controller or factory.

| Factory | Produces |
|---|---|
| `PhysicalBookFactory` | `PhysicalBook` (with shelf location) |
| `EBookFactory` | `EBook` (with download URL) |
| `ReferenceBookFactory` | `ReferenceBook` (non-borrowable) |

---

### 3. Facade — `LibraryFacade`
**Package:** `com.library.patterns.facade`

Borrowing a book involves five subsystem steps. Without a Facade, all five would clutter `MembersController`. With it, the controller makes **one call**:

```java
Transaction txn = libraryFacade.borrowBook(memberId, bookId);
```

Inside `LibraryFacade.borrowBook()`, the five steps are neatly hidden:
1. Validate book availability
2. Verify member exists
3. Create a `Transaction` record
4. Update book status → `BORROWED`
5. Persist everything to `LibraryManager`

---

### 4. Observer — `BookObserver` / `MemberNotifier`
**Package:** `com.library.patterns.observer`

When a book is returned, `book.setStatus(AVAILABLE)` fires the observer chain automatically. Any `MemberNotifier` registered on that book is called — the `TransactionsController` doesn't know or care about notifications. Adding a new notification type (e.g., `EmailNotifier`) means writing one new class that implements `BookObserver`. Nothing else changes.

---

## 🧱 SOLID Principles

| Principle | Where Applied |
|---|---|
| **S** — Single Responsibility | `LoginController` only handles auth routing; `FineCalculator` logic lives only in `Transaction` |
| **O** — Open/Closed | `BookFactory` interface never changes; new book types extend the system without modifying existing code |
| **L** — Liskov Substitution | `PhysicalBook`, `EBook`, `ReferenceBook` are all usable wherever `Book` is expected |
| **I** — Interface Segregation | `BookObserver` exposes exactly one method; roles are separated across focused interfaces |
| **D** — Dependency Inversion | `BooksController` depends on `BookFactory` interface, never on `PhysicalBookFactory` directly |

---

## 📁 Project Structure

```
src/main/java/com/library/
│
├── MainApp.java                        # Entry point — launches JavaFX
│
├── controller/                         # MVC Controllers (UI logic only)
│   ├── LoginController.java
│   ├── MainController.java
│   ├── BooksController.java
│   ├── MembersController.java
│   └── TransactionsController.java
│
├── model/                              # Business data classes
│   ├── Book.java                       # Abstract base (also acts as Observer Subject)
│   ├── PhysicalBook.java
│   ├── EBook.java
│   ├── ReferenceBook.java
│   ├── BookType.java
│   ├── BookStatus.java
│   ├── Member.java
│   └── Transaction.java
│
├── patterns/
│   ├── singleton/
│   │   └── LibraryManager.java         # ★ Singleton
│   ├── factory/
│   │   ├── BookFactory.java            # ★ Factory Method (interface)
│   │   ├── BookFactoryRegistry.java
│   │   ├── PhysicalBookFactory.java
│   │   ├── EBookFactory.java
│   │   └── ReferenceBookFactory.java
│   ├── facade/
│   │   └── LibraryFacade.java          # ★ Facade
│   └── observer/
│       ├── BookObserver.java           # ★ Observer (interface)
│       ├── BookSubject.java
│       └── MemberNotifier.java
│
└── util/
    ├── PatternLogger.java              # Logs pattern events to console
    └── SampleDataLoader.java          # Seeds demo books & members on startup

src/main/resources/
├── fxml/                              # JavaFX UI layouts
│   ├── Login.fxml
│   ├── Main.fxml
│   ├── Books.fxml
│   ├── Members.fxml
│   └── Transactions.fxml
└── styles/
    └── style.css                      # BootstrapFX-based styling

Docs/
├── Use case diagram.jpeg
├── Class diagram.jpeg
├── ActivityDiagram.jpg
└── StateDiagram.jpg
```

---

## 🚀 Getting Started

### Prerequisites

| Tool | Version |
|---|---|
| Java | 17 or higher |
| Maven | 3.8 or higher |
| IntelliJ IDEA | Recommended (any edition) |

### Run in IntelliJ IDEA

1. **Clone the repository**
   ```bash
   git clone https://github.com/<your-username>/Object_Oriented_Library_Management_System.git
   cd Object_Oriented_Library_Management_System
   ```

2. **Open in IntelliJ IDEA**  
   `File → Open → select the project root folder`  
   IntelliJ detects `pom.xml` and imports Maven dependencies automatically.

3. **Wait for indexing to finish**, then run `MainApp.java`  
   Right-click → *Run 'MainApp.main()'*

### Run from Terminal

```bash
mvn clean install
mvn javafx:run
```

> **First run note:** Maven downloads JavaFX 17 and BootstrapFX on first run (~50 MB). An internet connection is required once.

### Default Login

The app seeds demo data automatically on startup. Simply enter any name, pick a role, and click **Enter Library**.

| Role | Access |
|---|---|
| **Member** | Browse books, borrow, return, pay fines, set book watch |
| **Librarian** | All of the above + add/remove books, register members |
| **Admin** | Full access + view all transactions and reports |

---

## 🗂️ UML Diagrams

All diagrams are in the [`Docs/`](./Docs/) folder.

| Diagram | File |
|---|---|
| Use Case Diagram | `Docs/Use case diagram.jpeg` |
| Class Diagram | `Docs/Class diagram.jpeg` |
| Activity Diagram | `Docs/ActivityDiagram.jpg` |
| State Diagram | `Docs/StateDiagram.jpg` |

---

## 👥 Team

| Name | SRN | Module |
|---|---|---|
| Hrudhay R | PES2UG23CS226 | patterns/facade/ · Facade Pattern · ISP |
| G Praneeth | PES2UG23CS202 | model/ · Singleton Pattern · SRP |
| Kashyap K | PES2UG23CS263 | controller/ · Factory Method Pattern · OCP |
| Rohan S Nayak | PES2UG24CS820 | patterns/observer/ · Observer Pattern · DIP |

---

## 🛠️ Tech Stack

- **Language:** Java 17
- **UI Framework:** JavaFX 17 + FXML
- **Styling:** BootstrapFX 0.4.0
- **Build Tool:** Maven 3
- **Storage:** In-memory (`ObservableList` via `LibraryManager` Singleton)

---

## 📄 License

This project is submitted as an academic mini-project for **UE23CS352B** at PES University, January–May 2026.
