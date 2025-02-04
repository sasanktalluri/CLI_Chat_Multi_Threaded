
# Multithreaded Chat Application (Java)

This is a **multithreaded chat application** implemented in Java using Socket Programming. It allows multiple clients to connect to a server and exchange messages in real time. Each client runs in a separate thread, ensuring smooth communication.

## Features
- **Supports multiple clients simultaneously**
- **Real-time message broadcasting**
- **Multithreaded server architecture**
- **Graceful client disconnection using `logout`**
- **Thread-safe handling of active clients**

---

## Technologies Used
- **Java SE** (Sockets, Multithreading, I/O Streams)

---

## Setup
### **1️⃣ Compile the Code**
```sh
javac Server.java
javac Client.java
```

### **2️⃣ Start the Server**
```sh
java Server
```

### **3️⃣ Run Multiple Clients** (In different terminals)
```sh
java Client
```
