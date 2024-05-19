# 2d-game-engine-
My semester project, in which I created my own 2D game engine and a game on it called ZERO POINT

Plot:
The game will feature a universe where the Zero Point research center exists, focused on the development of chemical weapons and modern technologies in physics, chemistry, and weapon development. During an unsuccessful experiment in one of the research rooms, there was an explosion and a leak of a secret virus. Most of the complex's employees perished and, under the influence of the virus, which affects the dead brain, turned into zombies who will be the main enemies of the player.

Game Objective:
The goal of the game is to escape from the destroyed complex by following the emergency protocol, which states that all employees in case of emergency must go to complex X for further instructions. The player will have to progress upwards through the floors. Each floor represents a level. The game will feature 2 levels. Passing a level means opening the doors leading to the next floor, marked as "EXIT."

The player will encounter surviving scientists who will gradually tell them what happened and help them overcome obstacles, such as advising where to find a key or where a weapon lies. They will also give some items that will help the player escape, such as a key, a first aid kit, or a weapon, or these items can be found on the map. It will be possible to create additional items from the collected items. The player will have an inventory and crafting option. Crafting recipes can either be found in documents in the lab or learned from the complex's employees.

Items in the Game:
Weapon: pistol, superWeapon
Door key, levelKey
Crafting material
Ammo for the pistol
First aid kit, which adds 20 health points to the player
Threats:
The main threat to the player will be enemies (zombies). These enemies are infected complex employees (zombies) who will attack the player and reduce health upon hitting (20 points).

Saving and Loading the Game:
The player will be able to save and load the game from the last save point. The following will be saved:

Player's position on the map
Player's health status
Inventory status
Opened doors
Levels:
The game will feature 2 levels. Progressing to the next level means finding the key to the final door of the level. Levels will be loaded from a file describing the positions of NPCs and the player, item locations, items, and doors.

Crafting:
During the game, the player will collect items and will need special items for crafting. For example, pistol + material = super pistol. The recipe will be given to the player by an NPC.

Controls:
A – move left
D – move right
W – move forward
S – move backward
SHIFT – speed up (+2 to speed)
F – open inventory
E – action (e.g., open door)
H – heal
R – reload pistol
Space – shoot (attack)
The player will have a health indicator (100 units).
![image](https://github.com/KlykauCode/2d-game-engine-/assets/157822018/aa889d78-c2ea-4a2d-9ff3-44ade61eccfe)
![image](https://github.com/KlykauCode/2d-game-engine-/assets/157822018/06f29ecb-9ffa-46a3-9dcf-ca4d840a17d3)
![image](https://github.com/KlykauCode/2d-game-engine-/assets/157822018/d39cfa2e-b46a-48d5-bb26-bb535456104f)
![image](https://github.com/KlykauCode/2d-game-engine-/assets/157822018/e18d6fae-1443-48f8-b282-7c2411228a36)
![image](https://github.com/KlykauCode/2d-game-engine-/assets/157822018/14b522b2-9473-4828-9018-593e91ab4105)
![image](https://github.com/KlykauCode/2d-game-engine-/assets/157822018/d98c455b-083f-4f27-bc7d-6f679f9963be)


Program Documentation

Zero Point Program Documentation
A more detailed description of classes and methods is provided in the UML diagram.
Classes:
The classes are located in different folders and communicate with each other using the MVC pattern.

Controller:

AudioManager - Class for managing audio files

Direction (enum) – Collection defining directions

GameController – Main class that controls the entire game

GameSaverLoader – Save and load the game from a save file

GameStateController – Class that manages the game states

InputHandler - Class for managing user input

PhysicsEngine – Class for determining object collisions

Sprite – Class for managing images

SaveController – Class for storing data to be loaded

**TextureManager

TextureManager – Class for managing textures, tiles, maps

CraftingRecipe – Class for crafting logic

Collidable – Interface for collidable game objects

Interactable – Interface for objects that can be interacted with

Animatable – Interface for characters with animations

Model:

GameObject – Parent abstract class for all game objects

Door – Class for all doors in the game

CollisionBox – Trigger class for collision

Item – Parent class for all items in the game

Detail – Item for crafting

Heal – First aid kit

Key – Class for keys in the game

Weapon – Class for weapons

SuperWeapon – Class for the weapon obtained through crafting

Ammo – Class for weapon ammunition

Character – Parent class for characters in the game

Player – Class for managing and updating the player

Enemy – Class for the main character's enemies

FriendlyNPC – Class for all allies of the character

Inventory – Class for the player's inventory

Recipe – Class for crafting recipes

View:

GameOverUI – Class for setting the UI when the game is over

MenuUI – Class for setting the UI for PAUSE, START states

InventoryUI – Class for setting the UI for the INVENTORY state

UI – Class for setting the UI for other elements such as ammo, weapon, and health icons

GamePanel – Class for setting up the scene and its configuration

GUIConfig – Configuration settings for the graphical interface

Technologies and Libraries Used:

Java

JavaFX

JUnit

Mocking

Application States:

GameOver

Pause

Play

StartMenu

Inventory

