package com.kambius.puzzle15.view

import cats.effect.IO
import cats.syntax.option._
import com.kambius.puzzle15.controller.{Exit, GameEvent, GeneralError, MoveError, Moved, Shuffled, Solved, Started}
import com.kambius.puzzle15.core.{Board, Direction}

/**
  * Simple terminal view implementation that is expected to be controlled via CLI commands.
  */
class CliView extends View {
  def boardToStr(board: Board): String = {
    val numSize = board.maxNumber.toString.length
    def tileToStr(row: Int, col: Int) =
      board(row, col)
        .fold(e => throw new IllegalStateException(e.message), identity)
        .fold(" " * numSize)(_.formatted(s"%${numSize}d"))

    val hrLine = "-" * (board.cols * (numSize + 1) + 1) + "\n"

    (0 until board.rows)
      .map { row =>
        (0 until board.cols)
          .map(col => tileToStr(row, col))
          .mkString("|", "|", "|\n")
      }
      .mkString(hrLine, hrLine, hrLine)
  }

  def directionToStr(direction: Direction): String =
    direction match {
      case Direction.Up    => "UP"
      case Direction.Down  => "DOWN"
      case Direction.Left  => "LEFT"
      case Direction.Right => "RIGHT"
    }

  private[view] def showBoardScreen(board: Board, header: String, footer: Option[String] = None): IO[Unit] =
    IO {
      print(
        s"""${CliView.CleanScreenStr}$header
         /
         /${boardToStr(board)}
         /${footer.fold("")(_ + "\n")}Your action: """.stripMargin('/')
      )
    }

  override def show(event: GameEvent): IO[Unit] =
    event match {
      case Started(board) =>
        showBoardScreen(
          board,
          "WELCOME TO 15 PUZZLE GAME!",
          """To make move use WASD keys and press ENTER.
          |To reshuffle tiles use R key.
          |To quit press Q key.""".stripMargin.some
        )

      case Solved(board) =>
        showBoardScreen(
          board,
          "CONGRATULATIONS, YOU SOLVED THE PUZZLE!",
          """To reshuffle tiles use R key.
          |To quit press Q key.""".stripMargin.some
        )

      case Moved(board, direction) =>
        showBoardScreen(board, s"Moved ${directionToStr(direction)}")

      case MoveError(board, direction) =>
        showBoardScreen(board, s"It's impossible to move in ${directionToStr(direction)} direction!")

      case Shuffled(board) =>
        showBoardScreen(board, s"Board has been reshuffled.")

      case GeneralError(board, message) =>
        showBoardScreen(board, message)

      case Exit(board) =>
        showBoardScreen(board, "BYE!")
    }
}

object CliView {

  /**
    * ASCII Control character for cleaning the terminal.
    * @see [[https://en.wikipedia.org/wiki/Control_character]] for more details.
    */
  final val CleanScreenStr = "\u001b[H\u001b[2J"
}
