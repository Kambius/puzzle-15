package com.kambius.puzzle15.view

import cats.effect.IO
import com.kambius.puzzle15.controller.GameEvent

/**
  * Base trait for views
  */
trait View {
  def show(event: GameEvent): IO[Unit]
}
