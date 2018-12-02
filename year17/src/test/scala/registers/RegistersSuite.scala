package registers

import common.Decoder.decode
import org.scalatest.FunSuite
import registers.RegistersDef._

import scala.io.Source
import scala.util.Success

class RegistersSuite extends FunSuite {
  import Parser.commandsDecoder
  import Solver._

  val inputExample: String = Source.fromResource("registers-example.txt").mkString
  val input: String        = Source.fromResource("registers-input.txt").mkString

  val commands: Commands = decode[Commands](input).get

  val commandsExample = List(
    Command(Inc("b", 5), Predicate("a", Gt, 1)),
    Command(Inc("a", 1), Predicate("b", Lt, 5)),
    Command(Dec("c", -10), Predicate("a", Ge, 1)),
    Command(Inc("c", -20), Predicate("c", Eq, 10))
  )

  test("commands decoder") {
    assert(commandsDecoder(inputExample) == Success(commandsExample))
  }

  test("inc instruction: empty registers") {
    val rs = Map.empty[Id, Value]
    val op = Inc("b", 5)
    assert(execInstruction(rs, op) == Map("b" -> 5))
  }

  test("inc instruction: non empty registers") {
    val rs = Map("b" -> 5)
    val op = Inc("b", 5)
    assert(execInstruction(rs, op) == Map("b" -> 10))
  }

  test("dec instruction: empty registers") {
    val rs = Map.empty[Id, Value]
    val op = Dec("b", 5)
    assert(execInstruction(rs, op) == Map("b" -> -5))
  }

  test("dec instruction: non empty registers") {
    val rs = Map("b" -> 5)
    val op = Dec("b", 5)
    assert(execInstruction(rs, op) == Map("b" -> 0))
  }

  test("condition: empty registers Gt") {
    val rs = Map.empty[Id, Value]
    val p  = Predicate("b", Gt, 5)
    assert(!condition(rs, p))
  }

  test("condition: empty registers Lt") {
    val rs = Map.empty[Id, Value]
    val p  = Predicate("b", Lt, 5)
    assert(condition(rs, p))
  }

  test("condition: non empty registers Eq") {
    val rs = Map("b" -> 5)
    val p  = Predicate("b", Eq, 5)
    assert(condition(rs, p))
  }

  test("condition: non empty registers Ne") {
    val rs = Map("b" -> 5)
    val p  = Predicate("b", Ne, 5)
    assert(!condition(rs, p))
  }

  test("condition: non empty registers Gt") {
    val rs = Map("b" -> 5)
    val p  = Predicate("b", Gt, 5)
    assert(!condition(rs, p))
  }

  test("condition: non empty registers Lt") {
    val rs = Map("b" -> 5)
    val p  = Predicate("b", Lt, 5)
    assert(!condition(rs, p))
  }

  test("condition: non empty registers Ge") {
    val rs = Map("b" -> 5)
    val p  = Predicate("b", Ge, 5)
    assert(condition(rs, p))
  }

  test("condition: non empty registers Le") {
    val rs = Map("b" -> 5)
    val p  = Predicate("b", Le, 5)
    assert(condition(rs, p))
  }

  test("interpret command 1") {
    val rs     = Map.empty[Id, Value]
    val c1     = Command(Inc("b", 5), Predicate("a", Gt, 1))
    val result = interpretCommand(rs, c1)
    assert(result.isEmpty)
  }

  test("interpret command 2") {
    val rs       = Map.empty[Id, Value]
    val c2       = Command(Inc("a", 1), Predicate("b", Lt, 5))
    val expected = Map("a" -> 1)
    val result   = interpretCommand(rs, c2)
    assert(result == expected)
  }

  test("interpret command 3") {
    val rs       = Map("a" -> 1)
    val c3       = Command(Dec("c", -10), Predicate("a", Ge, 1))
    val expected = Map("a" -> 1, "c" -> 10)
    val result   = interpretCommand(rs, c3)
    assert(result == expected)
  }

  test("interpret command 4") {
    val rs       = Map("a" -> 1, "c" -> 10)
    val c4       = Command(Inc("c", -20), Predicate("c", Eq, 10))
    val expected = Map("a" -> 1, "c" -> -10)
    val result   = interpretCommand(rs, c4)
    assert(result == expected)
  }

  test("interpret commands") {
    val expected = List(Map("a" -> 1, "c" -> -10), Map("a" -> 1, "c" -> 10), Map("a" -> 1), Map(), Map())
    assert(interpret(commandsExample) == expected)
  }

  test("solution1 example") {
    assert(solution1(commandsExample) == 1)
  }

  test("solution1") {
    assert(solution1(commands) == 6061)
  }

  test("solution2 example") {
    assert(solution2(commandsExample) == 10)
  }

  test("solution2") {
    assert(solution2(commands) == 6696)
  }

}
