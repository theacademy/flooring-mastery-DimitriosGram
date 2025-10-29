# 🏠 Flooring Mastery Project

This project was developed as part of the **Wiley Edge Java training program**.  
It demonstrates a complete **N-tier MVC architecture** with clear separation of concerns, **Spring Dependency Injection**, and **unit testing** in a console-based flooring order management system.

---

## 🚀 Features
- **Display Orders** — View all orders for a specific date  
- **Add Orders** — Create and validate new flooring orders  
- **Edit Orders** — Update customer, product, or state details  
- **Remove Orders** — Safely delete existing orders  
- **File Persistence** — Data loaded and saved from text files  
- **Optional Export** — Backup all order data into a separate export file  
- **Exception Handling** — Graceful management of file and input errors  
- **Unit Testing** — Includes JUnit 5 tests using stubbed DAO classes  

---

## 🧩 Architecture
- **View Layer:** Handles user interaction through console I/O  
- **Controller Layer:** Manages program flow and delegates tasks  
- **Service Layer:** Performs business logic and data validation  
- **DAO Layer:** Handles all data persistence and file I/O  
- **Spring Context:** Injects dependencies for loose coupling and testability  

---

## 🛠️ Technologies Used
- **Java 11**  
- **Spring Framework (Core & Context)**  
- **JUnit 5** for unit testing  
- **Maven** for build and dependency management  

---

## 🧪 Testing
Unit tests are implemented for the service layer using:
- **Stub DAO classes** for isolated logic verification  
- Assertions for order creation, calculation accuracy, and validation  

To run all tests:
```bash
mvn test
