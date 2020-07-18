package com.kambiud.puzzle15.view

import com.kambius.puzzle15.controller.GameEvent
import com.kambius.puzzle15.view.View

import cats.Applicative

class ViewStub[F[_]](implicit F: Applicative[F]) extends View[F] {
  override def show(event: GameEvent): F[Unit] = F.unit
}

object ViewStub {
  def apply[F[_]: Applicative] = new ViewStub[F]
}
