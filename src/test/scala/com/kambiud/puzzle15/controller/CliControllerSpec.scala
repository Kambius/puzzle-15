package com.kambiud.puzzle15.controller

import com.kambiud.puzzle15.view.ViewStub
import com.kambius.puzzle15.controller.{CliController, Exit, GeneralError, MoveError, Moved, Shuffled, Solved}
import com.kambius.puzzle15.core.{Direction, SeqBoard}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CliControllerSpec extends AnyFlatSpec with Matchers {
  private val fstBoard =
    SeqBoard.create(3, 3).toOption.get
  private val sndBoard =
    SeqBoard.create(3, 3).flatMap(_.move(Direction.Down)).flatMap(_.move(Direction.Right)).toOption.get
  private val controller =
    new CliController(ViewStub)

  "A CliController" should "handle exit key" in {
    controller.handleKey("q", fstBoard) shouldBe ((fstBoard, Seq(Exit(fstBoard))))
  }

  it should "handle reshuffle key" in {
    val (nextBoard, events) = controller.handleKey("r", fstBoard)
    nextBoard should not be fstBoard
    nextBoard.isSolved shouldBe false
    events shouldBe Seq(Shuffled(nextBoard))
  }

  it should "handle move right key" in {
    val nextBoard = fstBoard.move(Direction.Right).toOption.get
    controller.handleKey("d", fstBoard) shouldBe ((nextBoard, Seq(Moved(nextBoard, Direction.Right))))
  }

  it should "handle move down key" in {
    val nextBoard = fstBoard.move(Direction.Down).toOption.get
    controller.handleKey("s", fstBoard) shouldBe ((nextBoard, Seq(Moved(nextBoard, Direction.Down))))
  }

  it should "handle move left key" in {
    val nextBoard = sndBoard.move(Direction.Left).toOption.get
    controller.handleKey("a", sndBoard) shouldBe ((nextBoard, Seq(Moved(nextBoard, Direction.Left))))
  }

  it should "handle move up key" in {
    val nextBoard = sndBoard.move(Direction.Up).toOption.get
    controller.handleKey("w", sndBoard) shouldBe ((nextBoard, Seq(Moved(nextBoard, Direction.Up))))
  }

  it should "handle invalid move" in {
    controller.handleKey("w", fstBoard) shouldBe ((fstBoard, Seq(MoveError(fstBoard, Direction.Up))))
  }

  it should "handle invalid input" in {
    val (nextBoard, events) = controller.handleKey("?", fstBoard)
    nextBoard shouldBe fstBoard
    events.size shouldBe 1
    events.head shouldBe a[GeneralError]
  }

  it should "create solved event" in {
    val trdBoard = fstBoard.move(Direction.Down).toOption.get
    controller.handleKey("w", trdBoard) shouldBe ((fstBoard, Seq(Moved(fstBoard, Direction.Up), Solved(fstBoard))))
  }
}
