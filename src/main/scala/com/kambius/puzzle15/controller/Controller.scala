package com.kambius.puzzle15.controller

import cats.effect.IO
import com.kambius.puzzle15.core.{Board, Direction}
import com.kambius.puzzle15.view.View
import cats.instances.vector._
import cats.syntax.foldable._

/**
  * Abstract base controller for the game that is used to handle user input and propagate game events to view.
  * @param view view that displays game events
  */
abstract class Controller(view: View) {
  protected def handleInput(board: Board): IO[(Board, Seq[GameEvent])]

  def start(initialBoard: Board): IO[Unit] =
    view.show(Started(initialBoard)) *> gameLoop(initialBoard)

  protected def exit(board: Board): (Board, Seq[GameEvent]) =
    (board, Seq(Exit(board)))

  protected def shuffle(board: Board): (Board, Seq[GameEvent]) = {
    val nextBoard = board.shuffle()
    (nextBoard, Seq(Shuffled(nextBoard)))
  }

  protected def move(board: Board, direction: Direction): (Board, Seq[GameEvent]) =
    board.move(direction) match {
      case Left(_) =>
        (board, Seq(MoveError(board, direction)))

      case Right(board) =>
        (board, Seq(Moved(board, direction)) ++ Option.when(board.isSolved)(Solved(board)))
    }

  protected def generalError(board: Board, message: String): (Board, Seq[GameEvent]) =
    (board, Seq(GeneralError(board, message)))

  protected def gameLoop(board: Board): IO[Unit] =
    handleInput(board).flatMap {
      case (nextBoard, events) =>
        val nextIteration = if (!events.exists(_.isInstanceOf[Exit])) gameLoop(nextBoard) else IO.unit
        events.toVector.traverse_(view.show) *> nextIteration
    }
}

/**
  * Events produced by [[Controller]] that are displayed in [[com.kambius.puzzle15.view.View]]
  */
sealed trait GameEvent {
  def board: Board
}
final case class Started(board: Board)                         extends GameEvent
final case class Solved(board: Board)                          extends GameEvent
final case class Moved(board: Board, direction: Direction)     extends GameEvent
final case class MoveError(board: Board, direction: Direction) extends GameEvent
final case class Shuffled(board: Board)                        extends GameEvent
final case class GeneralError(board: Board, message: String)   extends GameEvent
final case class Exit(board: Board)                            extends GameEvent
