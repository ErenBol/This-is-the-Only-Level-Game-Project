# This Is The Only Level – Java Game Project 🎮

A Java-based puzzle-platformer game inspired by *This Is The Only Level*, where the player must complete a single level through multiple unique stage variations. Each stage tweaks the mechanics or controls, challenging the player's understanding and adaptability.

🔗 [Gameplay Video](https://youtu.be/cq1saX8ti2Y)

---

## 🧩 Game Concept

The game consists of **one main level** played through **5 unique stages**, each introducing a different twist on player movement, gravity, or puzzle interaction. The objective in each stage is to:

1. Open the door (usually by pressing a button),
2. Avoid spikes and obstacles,
3. Reach the exit pipe.

Touching spikes resets the stage and increments the death counter.

---

## 🕹️ Stage Mechanics

| Stage | Description |
|-------|-------------|
| 1     | Standard movement with arrow keys. Press the button, open the door, exit. |
| 2     | Reversed arrow key controls. |
| 3     | Continuous auto-jumping with increased gravity. |
| 4     | Button must be pressed **5 times** to open the door. |
| 5     | Low gravity and floaty space-like jump until collision. |

---

## 🧠 Game Environment

- **Player**: Elephant (player-controlled).
- **Obstacles**: Rectangular blocks.
- **Spikes**: Deadly traps that reset the stage.
- **Pipes**: Entry and exit points.
- **Button**: Used to unlock the door.
- **Door**: Must be opened to finish a stage.

---

## 📋 UI Features

- **Clue & Help Button**: Display hints and detailed help for each stage.
- **Game Timer**: Tracks total playtime.
- **Death Counter**: Counts player deaths.
- **Stage/Level Counter**: Tracks current progress.
- **Reset & Restart Buttons**: Reset full game or current stage.

---

## 🧮 Game Mechanics Overview

- **Collision Detection**:  
  Prevents the player from passing through obstacles using AABB logic:  
  ```java
  playerMaxX > objMinX && playerMinX < objMaxX &&
  playerMaxY > objMinY && playerMinY < objMaxY
