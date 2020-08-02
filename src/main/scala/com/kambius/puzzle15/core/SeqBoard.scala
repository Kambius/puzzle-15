package com.kambius.puzzle15.core

import cats.syntax.either._
import cats.syntax.option._

/**
  * Board implementation which stores tiles in one-dimensional sequence row by row.
  * @param rows number of rows
  * @param cols number of columns
  * @param tiles one-dimensional sequence of tiles in row by row format
  */
final case class SeqBoard private (
  rows: Int,
  cols: Int,
  private val tiles: Seq[Option[Int]]
) extends Board {
  private def posToIdx(row: Int, col: Int): Int =
    row * cols + col

  override def apply(row: Int, col: Int): Either[InvalidPositionError, Option[Int]] =
    if (isValidPos(row, col))
      tiles(posToIdx(row, col)).asRight
    else
      InvalidPositionError(row, col).asLeft

  override def move(direction: Direction): Either[InvalidMoveError, SeqBoard] = {
    val eIdx = tiles.indexOf(None)
    val eRow = eIdx / cols
    val eCol = eIdx % cols

    val tileRow = eRow - direction.rowOffset
    val tileCol = eCol - direction.colOffset

    if (isValidPos(tileRow, tileCol)) {
      val nextTiles = tiles
        .updated(posToIdx(eRow, eCol), tiles(posToIdx(tileRow, tileCol)))
        .updated(posToIdx(tileRow, tileCol), None)
      copy(tiles = nextTiles).asRight
    } else InvalidMoveError(direction).asLeft
  }

  override lazy val isSolved: Boolean = tiles.last.isEmpty && tiles.init.zipWithIndex.forall {
    case (cell, idx) => cell.contains(idx + 1)
  }
}

object SeqBoard {

  /**
    * Creates board in solved state with specified dimensions
    * @param rows number of rows
    * @param cols number of columns
    * @return created board or error in case of invalid parameters
    */
  def create(rows: Int, cols: Int): Either[InvalidInitParamsError, Board] =
    if (rows > 0 && cols > 0 && !(rows == 1 && cols == 1))
      SeqBoard(rows, cols, (1 until rows * cols).map(_.some) :+ none).asRight
    else
      InvalidInitParamsError(s"Invalid board size: ($rows, $cols)").asLeft

  /**
    * Creates board in specified state and dimensions from one-dimensional sequence of tiles in row by row format
    * @param rows number of rows
    * @param cols number of columns
    * @return created board or error in case of invalid parameters
    */
  def fromSeq(rows: Int, cols: Int, tiles: Seq[Option[Int]]): Either[InvalidInitParamsError, SeqBoard] =
    if (rows <= 0 || cols <= 0 || (rows == 1 && cols == 1))
      InvalidInitParamsError(s"Invalid board size: ($rows, $cols)").asLeft
    else if (tiles.size != rows * cols)
      InvalidInitParamsError(s"Tiles size (${tiles.size}) != board size (${rows * cols})").asLeft
    else if (
      tiles.flatten.sorted.zipWithIndex.exists(ci => ci._1 != ci._2 + 1) || tiles.flatten.toSet.size != rows * cols - 1
    )
      InvalidInitParamsError(s"Invalid tiles: $tiles").asLeft
    else
      SeqBoard(rows, cols, tiles).asRight
}
