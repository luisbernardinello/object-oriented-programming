![Unesp](https://img.shields.io/badge/BCC-UNESP-Bauru.svg)
![License](https://img.shields.io/badge/Code%20License-MIT-blue.svg)

## 📑 Overview

This repository contains three projects developed for the Object-Oriented Programming course at UNESP Bauru. Each project demonstrates different aspects of OOP principles and their application across various programming environments and problem domains.

## 🏗️ Project Components

### 1. 8bit Chess

A chess game developed in Python using the Pygame library featuring animated pixelated sprites and classic chess mechanics.

- **Key Features:**

  - Complete chess rule implementation
  - Pixelated retro-style graphics and animations
  - Turn-based gameplay with move validation
  - Special moves (castling, en passant, pawn promotion)
  - Check and checkmate detection

- **Technologies:**
  - Python
  - Pygame
  - Sprite-based animation system
  - Custom event handling

### 2. Blood Eagle

A 2D fighting platform game developed entirely in Java without using any external game libraries or engines, showcasing core OOP principles in game development.

- **Key Features:**

  - Two playable viking characters with unique abilities
  - Platforming mechanics with collision detection
  - Combat system with health tracking
  - Pixelated art style with custom animations
  - Game state management and victory conditions

- **Technologies:**
  - Pure Java implementation
  - Custom game loop and physics
  - Tilemap rendering system
  - Sprite atlas implementation
  - Double buffering for smooth rendering

### 3. TCP Chat Room

A network-based chat application using Java, demonstrating client-server architecture and network programming concepts.

- **Key Features:**

  - Multi-user chat functionality
  - Real-time message broadcasting
  - User connection management
  - Graphical user interface
  - Client-server architecture

- **Technologies:**
  - Java
  - Java Sockets for network communication
  - Swing for GUI components
  - Multi-threading for concurrent connections
  - Event-driven programming

## Implementation Details

### Object-Oriented Design Principles

All projects demonstrate fundamental OOP concepts:

- **Encapsulation:** Data and behaviors grouped into appropriate classes
- **Inheritance:** Class hierarchies for code reuse and specialization
- **Polymorphism:** Different implementations of shared interfaces
- **Abstraction:** Simplified interfaces hiding complex implementations

## 🧰 Getting Started

### Prerequisites

- **For 8bit Chess:**

  - Python 3.7+
  - Pygame library

- **For Blood Eagle:**

  - Java Development Kit (JDK) 8 or higher
  - No additional libraries required

- **For TCP Chat Room:**
  - Java Development Kit (JDK) 8 or higher
  - Basic understanding of network concepts

### Installation and Running

#### 8bit Chess

```bash
# Navigate to the project directory
cd 8bit-chess-pygame

# Install pygame if not already installed
pip install pygame

# Run the game
python main.py
```

#### Blood Eagle

```bash
# Navigate to the project directory
cd blood-eagle-java

# Compile the Java files
javac *.java

# Run the game
java Main
```

#### TCP Chat Room

```bash
# Navigate to the project directory
cd tcp-chat-room-java

# Compile the server
javac Server.java

# Compile the client
javac Client.java

# Run the server
java Server

# Run client(s) in separate terminal(s)
java Client
```

## 📋 Project Structure

Each project follows a structured organization:

- **8bit Chess:**

  - Game engine components
  - Chess logic implementation
  - Sprite and animation systems
  - User interface elements

- **Blood Eagle:**

  - Core game engine
  - Entity component system
  - Physics and collision detection
  - Input handling and game states

- **TCP Chat Room:**
  - Server implementation
  - Client implementation
  - UI components
  - Network communication protocol

## 📚 References

- Booch, G. (1993). _Object Oriented Analysis and Design with Applications_. [Link](books/object-oriented-analysis-grady-booch.pdf)
- Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1994). _Design Patterns: Elements of Reusable Object-Oriented Software_. Addison-Wesley.
- Eckel, B. (2006). _Thinking in Java_ (4th ed.). Prentice Hall.
- McConnell, S. (2004). _Code Complete_ (2nd ed.). Microsoft Press.

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

<h4 align="center">
<img src="https://socialify.git.ci/luisbernardinello/object-oriented-programming/image?font=Raleway&language=1&name=1&owner=1&pattern=Solid&theme=Auto" alt="object-oriented-programming" width="498" height="270" />
</h4>
