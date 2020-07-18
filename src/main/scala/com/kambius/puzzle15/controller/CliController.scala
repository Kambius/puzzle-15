package com.kambius.puzzle15.controller

import com.kambius.puzzle15.controller.CliController.DirectionKeys
import com.kambius.puzzle15.core.{Board, Direction}
import com.kambius.puzzle15.view.View

import scala.io.StdIn.readLine

import cats.effect.Sync
import cats.syntax.functor._

/**
  * Simple controller implementation that reads user input from terminal.
  * @param view view that displays game events
  */
class CliController[F[_]](view: View[F])(implicit F: Sync[F]) extends Controller[F](view) {
  override protected def handleInput(board: Board): F[(Board, Seq[GameEvent])] =
    F.delay(readLine().toLowerCase).map(handleKey(_, board))

  def handleKey(key: String, board: Board): (Board, Seq[GameEvent]) =
    key match {
      case "q" =>
        exit(board)

      case "r" =>
        shuffle(board)

      case k =>
        DirectionKeys
          .get(k)
          .fold(
            generalError(
              board,
              """Invalid input! To make move use WASD keys.
              |To reshuffle tiles use R key.
              |To quit press Q key.""".stripMargin
            )
          )(move(board, _))
    }
}

object CliController {
  final val DirectionKeys: Map[String, Direction] =
    Map("w" -> Direction.Up, "a" -> Direction.Left, "s" -> Direction.Down, "d" -> Direction.Right)
}
