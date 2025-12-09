## Introduction

Welcome to the Ludo Game in Java.  This game aims to create a fully functional Ludo game play experience for multiplayer Ludo against computers.  Computer and human players are both supported.  Any number of the four players may be computers.  

## Project File Descriptions:
The project is broken down into multiple files.  Each file is described below in high level.  More specific documentation is available within each file.

### Logic Files

#### Computerized Player

This file represents the computer player.  All the actions, such as making a move and managing the color the player is playing for, are taken in this file.  This ensures fairness in the game by forcing the computer to use the same moving logic in Game Logic.  This decoupling also makes maintenance and improvements easier.

#### Game Logic

Game Logic represents the core functionality of game.  All the rules and validations are contained and managed in this file.

### UI Files

#### Board Square

This file represents a single square on the board.  Manages its attributes, such as square classification, position, and color.

#### Game Status Information

Shows the user the current status of the game - the player currently playing, the dice roll, and the button to roll the dice.

#### Ludo Board View

Represents a full Ludo board.  Assembles itself using the board squares.  Sets the classifications and positions of each square.

#### Player Token

Represents each individual player token.  Manages position on the board, the token's spawn, and color information.   

#### Start Screen

Allows the user to select options to setup the game.  Select the number and color of the computer players and get the game set up.

#### Start Screen Listener

A listener class to listen to the start screen and notify the system when the game is ready to start.

### Main File

#### Ludo Game Runner

The main entry to the program.  Contains the main method.

### Other Files

#### Configuration Files

One file per play color represents the playpaths for the players.  

## Example run

An example run of the project with 4 computer players:

https://github.com/user-attachments/assets/727c89ea-89d5-4d88-83e4-d882a012732e

