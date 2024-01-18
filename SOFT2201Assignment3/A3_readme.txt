## Commands

* Run: `gradle run` to load default config from resources folder or
`gradle run --args="'insert_config_file_path'"` to load custom config.

* There are no special quirks needed to run the application

* The javadocs can be found in the `Javadocs` folder in the project directory

## Features

All the required features have been successfully implemented as specified in the assignment spec.
These include :

* **Pockets** :
  * Position and Radius configurable through JSON files

* **More Coloured Balls** :
  * A total of 8 different coloured balls available

* **Visual Cue Stick** :
  * A visible, controllable cue stick appears when dragging the cue ball to shoot

* **Difficulty Levels** :
  * 3 difficulty levels can be chosen from using their respective buttons
  * Can be changed mid-game
  * New loaded level starts from the beginning, i.e. everything is resetted and reloaded

* **Time** :
  * A `m:ss` formatted time counter shows the time elapsed.
  * Accurately reverts when undo button is clicked.
  * Stops when game is won.
  * Resets when game is lost, i.e. cue ball goes in pocket.

* **Score** :
  * Incremented by specific amount depending on colour of ball pocketed
  * Resets when game is lost, i.e. cue ball goes in pocket.

* **Undo** :
  * Triggered by clicking the Undo button on screen
  * Reverts game to the previous move
  * Reverts the time elapsed, score and the position of the balls
  * Allows undo while balls are still in movement
  * Allows undo from a win/loss scenario & accurately hides/shows the win message
  * Accurately brings back any pocketed balls, if necessary

* **Cheat**
  * Balls of specific colours can be removed by clicking on their labelled buttons on the screen
  * Balls are instantly removed
  * Respective scores are added to the main score as if they were all pocketed once
  * Can be used mid-movement too
  * Only relevant buttons are showed for the colours of balls on the table
  * Once a colour is not on the table anymore, i.e. either through the cheat button or normal play, the button is not available on screen anymore for that colour
  * Even for different levels, buttons are only showed for the respective colours actually on the table

## Design Patterns

There were 3 additional design patterns implemented in this assignment :

1) Memento : The participating classes, as found within the Memento directory, were :
   * Memento
   * Caretaker
   * Originator
2) Observer : The participating classes, as found within the Observer directory, were :
   * Observer
   * BallObserver
   * NotConcreteBall
   * Ball
3) Singleton : The participating classes, as found within the Memento directory, were :
   * ConfigReader