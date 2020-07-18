package com.kambius.puzzle15

import cats.effect.{ExitCode, IO, IOApp}
import com.kambius.puzzle15.controller.CliController
import com.kambius.puzzle15.core.SeqBoard
import com.kambius.puzzle15.view.CliView
import pureconfig._
import pureconfig.generic.semiauto._

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      config     <- IO(ConfigSource.default.loadOrThrow[Config])
      controller <- IO(new CliController(new CliView))
      board <- IO {
        SeqBoard
          .create(config.rows, config.cols)
          .fold(e => throw new IllegalStateException(e.message), _.shuffle())
      }
      _ <- controller.start(board)
    } yield ExitCode.Success
}

final case class Config(rows: Int, cols: Int)
object Config {
  implicit val configReader: ConfigReader[Config] = deriveReader
}
