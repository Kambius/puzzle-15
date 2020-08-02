package com.kambiud.puzzle15.view

import com.kambius.puzzle15.core.SeqBoard
import com.kambius.puzzle15.view.CliView

import cats.effect.IO

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CliViewSpec extends AnyFlatSpec with Matchers {
  private val view = new CliView[IO]

  "A CliView" should "display small board in" in {
    view.boardToStr(SeqBoard.create(3, 3).toOption.get) shouldBe
      """-------
        /|1|2|3|
        /-------
        /|4|5|6|
        /-------
        /|7|8| |
        /-------
        /""".stripMargin('/')
  }

  it should "display big board in" in {
    view.boardToStr(SeqBoard.create(4, 3).toOption.get) shouldBe
      """----------
        /| 1| 2| 3|
        /----------
        /| 4| 5| 6|
        /----------
        /| 7| 8| 9|
        /----------
        /|10|11|  |
        /----------
        /""".stripMargin('/')
  }
}
