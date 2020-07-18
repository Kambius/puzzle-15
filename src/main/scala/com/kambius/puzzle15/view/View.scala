package com.kambius.puzzle15.view

import com.kambius.puzzle15.controller.GameEvent

/**
  * Base trait for views
  */
trait View[F[_]] {
  def show(event: GameEvent): F[Unit]
}
