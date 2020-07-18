# puzzle-15
[![Build Status](https://travis-ci.org/Kambius/puzzle-15.svg?branch=master)](https://travis-ci.org/Kambius/puzzle-15)

Simple [15 Puzzle](https://en.wikipedia.org/wiki/15_puzzle) implementation written in Scala.

### Controls
* `WASD` keys and RETURN to make a move
* `Q` key to exit the game
* `R` key to reshuffle the board 

### Commands
To run the game in SBT:
`sbt run`

To run tests:
`sbt test`

To run full source check including code formatting check:
`sbt validate`

To build binaries:
`sbt universal:packageBin`