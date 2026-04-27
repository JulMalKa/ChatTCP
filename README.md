# Java Chat App

A multi-user chat application built on a client-server architecture over TCP. The server handles multiple clients simultaneously using threads, and clients can chat publicly or send private messages.

## Getting Started

**1. Compile the files:**
```bash
javac ChatServer.java ChatClient.java
```

**2. Start the server:**
```bash
java ChatServer
```

**3. Start a client (in a separate terminal):**
```bash
java ChatClient
```

You can run multiple clients at the same time by opening additional terminals and repeating step 3.

In IntelliJ: Edit Configurations → ChatClient → check **Allow multiple instances**.

## Commands

| Command | Description |
|---|---|
| `/list` | Show all currently connected users |
| `/msg <user> <text>` | Send a private message to a user |
| `/all <text>` | Send a message to everyone |
| `/quit` | Disconnect from the server |
| `/help` | Display the list of commands |

## Project Structure

```
ChatServer.java   # Server – listens on port 12345, manages connected clients
ChatClient.java   # Client – connects to the server, sends and receives messages
```

