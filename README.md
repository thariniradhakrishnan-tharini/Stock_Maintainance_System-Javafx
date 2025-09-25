# 📦 Stock Management System (JavaFX)

![JavaFX Logo](data:image/jpeg;base64,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)

## 📖 Overview  
The **Stock Management System** is a **JavaFX-based desktop application** designed to help small businesses or individuals manage their inventory effectively.  
It allows users to **add, update, remove, search, and display summaries** of products with a simple and user-friendly GUI.  

This project demonstrates **Object-Oriented Programming (OOP) principles** and **JavaFX UI development**, making it a strong academic and portfolio project.

---

## ✨ Key Features  

- ✅ **Add Products** – Enter product details (name & stock) and assign an auto-generated ID  
- ✅ **Update Products** – Modify the stock levels of existing products  
- ✅ **Remove Products** – Delete products by ID after confirmation  
- ✅ **Search Products** – Find products by ID or name quickly  
- ✅ **Stock Levels** – Check current stock availability  
- ✅ **Summary Display** – View all products in a **TableView**  
- ✅ **User-Friendly Dialogs** – Alerts for success, warnings, and confirmations  
- ✅ **Clean UI** – Simple JavaFX layout using VBox, BorderPane, and Dialogs  

---

## 🛠️ Tech Stack  

- **Programming Language:** Java  
- **GUI Framework:** JavaFX  
- **Paradigm:** Object-Oriented Programming (Encapsulation, Abstraction, Polymorphism)  
- **IDE Support:** IntelliJ IDEA / Eclipse / VS Code  
### 1️⃣ Prerequisites  
Make sure you have the following installed:  
- **Java JDK 8+**  
- **JavaFX SDK** (if not bundled with JDK)  
- An IDE like **IntelliJ IDEA**, **Eclipse**, or **VS Code**  

---

### 2️⃣ Clone the Repository  
```bash
git clone https://github.com/your-username/Stock-Management-System-JavaFX.git
cd Stock-Management-System-JavaFX
