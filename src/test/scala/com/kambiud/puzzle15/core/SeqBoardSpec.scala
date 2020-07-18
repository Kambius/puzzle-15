package com.kambiud.puzzle15.core

import com.kambius.puzzle15.core.{Direction, SeqBoard}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import cats.syntax.option._
import cats.syntax.either._

import scala.util.Random

class SeqBoardSpec extends AnyFlatSpec with Matchers {
  "A SeqBoard" should "be created in solved state" in {
    val boardE = SeqBoard.create(3, 2)
    boardE.isRight shouldBe true
    val board = boardE.toOption.get
    board.isSolved shouldBe true
  }

  it should "contain sorted sequence of tiles in solved state" in {
    val board = SeqBoard.create(3, 2).toOption.get
    board(0, 0) shouldBe Right(Some(1))
    board(0, 1) shouldBe Right(Some(2))
    board(1, 0) shouldBe Right(Some(3))
    board(1, 1) shouldBe Right(Some(4))
    board(2, 0) shouldBe Right(Some(5))
    board(2, 1) shouldBe Right(None)

    board shouldBe SeqBoard.fromSeq(3, 2, (1 to 5).map(_.some) :+ None).toOption.get
  }

  it should "shuffle tiles" in {
    val board = SeqBoard.create(3, 3).toOption.get
    board.shuffle(new Random(0)).isSolved shouldBe false
    board.shuffle(new Random(0)) should not be board.shuffle(new Random(1))
  }

  it should "move tile right" in {
    val board      = SeqBoard.create(3, 2).toOption.get
    val nextBoardE = board.move(Direction.Right)
    nextBoardE.isRight shouldBe true
    val nextBoard = nextBoardE.toOption.get
    nextBoard(2, 1) shouldBe 5.some.asRight
    nextBoard(2, 0) shouldBe None.asRight
  }

  it should "move tile down" in {
    val board      = SeqBoard.create(3, 2).toOption.get
    val nextBoardE = board.move(Direction.Down)
    nextBoardE.isRight shouldBe true
    val nextBoard = nextBoardE.toOption.get
    nextBoard(2, 1) shouldBe 4.some.asRight
    nextBoard(1, 1) shouldBe None.asRight
  }

  it should "move tile left" in {
    val board      = SeqBoard.fromSeq(3, 2, None +: (1 to 5).map(_.some)).toOption.get
    val nextBoardE = board.move(Direction.Left)
    nextBoardE.isRight shouldBe true
    val nextBoard = nextBoardE.toOption.get
    nextBoard(0, 0) shouldBe 1.some.asRight
    nextBoard(0, 1) shouldBe None.asRight
  }

  it should "move tile up" in {
    val board      = SeqBoard.fromSeq(3, 2, None +: (1 to 5).map(_.some)).toOption.get
    val nextBoardE = board.move(Direction.Up)
    nextBoardE.isRight shouldBe true
    val nextBoard = nextBoardE.toOption.get
    nextBoard(0, 0) shouldBe 2.some.asRight
    nextBoard(1, 0) shouldBe None.asRight
  }

  it should "calculate max number" in {
    SeqBoard.create(4, 4).toOption.get.maxNumber shouldBe 15
  }

  it should "calculate solved flag" in {
    val board = SeqBoard.create(3, 3).flatMap(_.move(Direction.Down)).toOption.get
    board.isSolved shouldBe false
    val nextBoardE = board.move(Direction.Up)
    nextBoardE.isRight shouldBe true
    nextBoardE.toOption.get.isSolved shouldBe true
  }

  it should "not allow to create invalid board" in {
    SeqBoard.create(3, 0).isLeft shouldBe true
    SeqBoard.create(0, 2).isLeft shouldBe true
    SeqBoard.create(-1, 1).isLeft shouldBe true
    SeqBoard.create(1, 1).isLeft shouldBe true
  }

  it should "not allow to create invalid board from seq" in {
    SeqBoard.fromSeq(3, 3, Seq(None, 1.some)).isLeft shouldBe true
    SeqBoard.fromSeq(1, 2, Seq(None, 3.some)).isLeft shouldBe true
    SeqBoard.fromSeq(1, 2, Seq(None, None)).isLeft shouldBe true
    SeqBoard.fromSeq(1, 1, Seq(None)).isLeft shouldBe true
  }

  it should "not allow to use invalid index" in {
    val board = SeqBoard.create(2, 1).toOption.get
    board(-1, 0).isLeft shouldBe true
    board(0, -1).isLeft shouldBe true
    board(2, 0).isLeft shouldBe true
    board(0, 1).isLeft shouldBe true
  }

  it should "not allow to make invalid move left" in {
    val board      = SeqBoard.create(3, 2).toOption.get
    val nextBoardE = board.move(Direction.Left)
    nextBoardE.isLeft shouldBe true
  }

  it should "not allow to make invalid move up" in {
    val board      = SeqBoard.create(3, 2).toOption.get
    val nextBoardE = board.move(Direction.Up)
    nextBoardE.isLeft shouldBe true
  }

  it should "not allow to make invalid move right" in {
    val board      = SeqBoard.fromSeq(3, 2, None +: (1 to 5).map(_.some)).toOption.get
    val nextBoardE = board.move(Direction.Right)
    nextBoardE.isLeft shouldBe true
  }

  it should "not allow to make invalid move down" in {
    val board      = SeqBoard.fromSeq(3, 2, None +: (1 to 5).map(_.some)).toOption.get
    val nextBoardE = board.move(Direction.Down)
    nextBoardE.isLeft shouldBe true
  }
}
