package com.kambius.puzzle15.core

import scala.util.Random

/**
  * Enumeration of move directions
  * @param rowOffset vertical move direction
  * @param colOffset horizontal move
  */
sealed abstract class Direction(val rowOffset: Int, val colOffset: Int)
object Direction {
  case object Up    extends Direction(-1, 0)
  case object Down  extends Direction(1, 0)
  case object Left  extends Direction(0, -1)
  case object Right extends Direction(0, 1)

  val All: Seq[Direction] = Seq(Up, Down, Left, Right)
}

/**
  * Trait for different board implementations
  */
trait Board {

  /**
    * Number of columns of the board
    */
  def cols: Int

  /**
    * Number of rows of the board
    */
  def rows: Int

  /**
    * Returns the value of the tile at given (row, col)
    * @param row row of the tile which is in [0; rows) range
    * @param col col of the tile which is in [0; cols) range
    * @return value of the tile at given (row, col) or error in case of invalid position
    */
  def apply(row: Int, col: Int): Either[InvalidPositionError, Option[Int]]

  /**
    * Returns new board with move applied
    * @param direction direction of move (empty tile is moved in opposite direction)
    * @return new board with move applied or error in case of invalid move direction
    */
  def move(direction: Direction): Either[InvalidMoveError, Board]

  /**
    * Returns true if all tiles are sorted and empty tile is the last one
    */
  def isSolved: Boolean

  /**
    * Returns max tile number
    */
  def maxNumber: Int = rows * cols - 1

  /**
    * Returns new board with tiles shuffled
    * @param random random generator used for shuffling
    */
  def shuffle(random: Random = scala.util.Random): Board =
    Seq
      .fill(math.pow((rows * cols).toDouble, 2).toInt)(Direction.All(random.nextInt(4)))
      .foldLeft(this)((b, d) => b.move(d).getOrElse(b))

  /**
    * Returns if specified position is valid for this board
    * @param row row of the tile
    * @param col col of the tile
    */
  protected def isValidPos(row: Int, col: Int): Boolean =
    0 <= row && row < rows && 0 <= col && col < cols
}

sealed trait BoardError {
  def message: String
}
final case class InvalidPositionError(row: Int, col: Int) extends BoardError {
  override val message: String = s"Invalid position: ($row, $col)"
}
final case class InvalidMoveError(direction: Direction) extends BoardError {
  override val message: String = s"Invalid move in direction: $direction"
}
final case class InvalidInitParamsError(override val message: String) extends BoardError
