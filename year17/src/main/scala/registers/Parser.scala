package registers

import common.Decoder
import registers.RegistersDef._

import scala.util.{ Failure, Success, Try }

object Parser {
  import common.util._

  implicit val commandsDecoder: Decoder[Commands] = (input: String) => {
    def instruction(id: String, i: String, value: String): Try[Instruction] = i match {
      case "inc" => Try(value.toInt).map(Inc(id, _))
      case "dec" => Try(value.toInt).map(Dec(id, _))
      case _     => Failure(new Exception(s"Instruction.unknown: $i"))
    }

    def predicate(id: String, relOp: String, value: String): Try[Predicate] =
      for {
        v <- Try(value.toInt)
        ro <- relOp match {
          case "==" => Success(Eq)
          case "!=" => Success(Ne)
          case ">"  => Success(Gt)
          case "<"  => Success(Lt)
          case ">=" => Success(Ge)
          case "<=" => Success(Le)
          case _    => Failure(new Exception(s"RelationalOp.unknown: $relOp"))
        }
      } yield Predicate(id, ro, v)

    def command(s: String): Try[Command] = s.split(' ').toList match {
      case iId :: opS :: opV :: _ :: pId :: relOp :: pV :: Nil =>
        for {
          i <- instruction(iId, opS, opV)
          p <- predicate(pId, relOp, pV)
        } yield Command(i, p)
      case _ =>
        Failure(new Exception(s"Command.unknown: $s"))
    }

    (split _).andThen(_.map(command)).andThen(transform).apply(input)
  }

}
