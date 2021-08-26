Header Mafia, Josh Sanyal & Arnold Zhou, May 24th


Introduction

In this game, a group of players can create custom games of Mafia with four different roles (mafioso, doctor, detective, townsperson), number of players in each role, and win-conditions for each role. They then play the game which includes performing different actions throughout the night based on a player’s role, chatting with other players throughout the day, and voting to lynch certain players. Once a player or group of players reaches a win condition, the game ends.


Instructions

Starting Screen
You can click the following main menu options: create/connect to game, instructions

Create/Connect to Game Screen
Click on “start server” and enter your name into the pop-up to create a game. Once the game is created, other players can click “discover servers”, select the server with the IP address of the game creator, and click “connect to selected”.

Game Screen
Once all players have joined the server, everyone can chat utilizing typing through the game’s chat panel. 
The game can be started by the game creator by clicking on “start game” and selecting the number of players for each role (all leftover players will be normal townsperson).
Click “show role” to display your role once the game has started.
Voting on lynchings will be done through pop-ups and players votes will be communicated to other player’s in the chat panel. A player may click “cancel” to not vote. 
Mafioso can vote on who they kill through pop-ups during the night phase. These votes will be communicated to other mafia, and the player with the most votes will be killed unless they’re saved by the doctor.
Doctor can select who they save through pop-ups during the night phase. Multiple doctors will be able to save multiple people, but they can save the same person as they don’t know who the other doctor votes for.
Detective can investigate a player through pop-ups during the night phase. Once they select a player, they will receive the player’s role through the game’s chat panel. Multiple detective can investigate different players.


Features List

Must-Have:
(DONE) Standard mafia roles (townsfolk, detective, doctor, mafioso) 
(DONE) Ability to chat (type) with other players (networking)
(DONE) Lynching system based on group vote
(DONE) Night phase where doctor, detective, mafia act
(DONE) Win-Conditions for mafia & townsfolk

Want to Have:
(NOT DONE) Additional roles such as a joker 
(DONE) Ability to set how many of each role
(NOT DONE) Set different win conditions for additional roles
(SEMI DONE) Nice GUI for night/day phases
(DONE) Ability to spectate game once lynched/dead

Stretch Features:
(NOT DONE) Create stories based on the gameplay
(NOT DONE) When a player is lynched they are sent to a second game with a different role
(NOT DONE) Audiovisual communication between players
(SEMI DONE) Player customization (names, skins, etc.)
(NOT DONE) Ability to private message players


Class List

* Player (contains player roles, names, etc.)
* Role (interface for storing the methods of different roles)
* Mafioso, Doctor, Detective, Townsperson. (extend Role, contain actions of each player)
* Game (has players, stores win-conditions, etc.)
* MafiaClient (handles GUI for gameplay, set game options)
* Main (needed to start the game)
* MainMenu (handles GUI for start screen with different menu options)
* RectButton (button for the options on the start screen)
* EndScreen (handles GUI for the screen once the game is over)


Credit List

Arnold: MafiaClient, Main, MainMenu, RectButton, EndScreen
Josh: MafiaClient, Game, Player, Specific Roles, MainMenu
Mr. Shelby: Networking code (All java files in the networking package)