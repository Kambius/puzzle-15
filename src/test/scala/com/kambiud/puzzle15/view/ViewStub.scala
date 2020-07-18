package com.kambiud.puzzle15.view

import cats.effect.IO
import com.kambius.puzzle15.controller.GameEvent
import com.kambius.puzzle15.view.View

object ViewStub extends View {
  override def show(event: GameEvent): IO[Unit] = IO.unit
}
