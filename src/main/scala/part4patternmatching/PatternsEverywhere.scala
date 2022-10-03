package part4patternmatching

object PatternsEverywhere {

  // PM expression

  // 1 - try-catches are actually MATCHES
  val potentialFail = try {
    // code
  } catch {
    // catches are based on pattern matching
    case _: NullPointerException => 4
  }

  /*
    try {
      // some code
    } catch (e) {
      e match {
        case _: NullPointerException =>
        ...
      }
    }
   */

  // 2 - for comprehensions are based on PM
  val aList = List(1,2,3,4)
  val evenNumbers = for {
    n <- aList if n % 2 == 0
  } yield n * 10 // not so similar

  val tuples = List((1,23),(2,574))
  val filterTuples = for {
    (a, b) <- tuples if a < 5
    // every tuple is destructed into (a,b) and a must be < 5
  } yield b * 200

  case class Guitar(typ: String, model: String)
  val fender = Guitar("acoustic", "Fender CD235y")
  val gibson = Guitar("electric", "Gibson Les Paul")
  val guitarz = List(fender, gibson)
  val namesAllCaps = for {
    Guitar(tp, model) <- guitarz if tp == "acoustic"
  } yield model.toUpperCase

  // 3 - can destruct an instance right at declaration
  val typ = fender.typ
  val model = fender.model
  // in a single declaration:
  val Guitar(typ_v2, model_v2) = fender

  val (id, name, yearOfRelease) = ("536728fas3573628df", "The Black Album", 1991)
  // syntax sugar
  /*
    Python equivalent:
    def func(...):
      return a,b,c,d

    (a,b,c,d) = func(...)
   */

  def main(args: Array[String]): Unit = {

  }
}
