package part3fp


import scala.util.{Failure, Random, Success, Try}
//                ^^^^^^^^^^^^^^^^^^^^^^^ all these types are imported from scala.util
// Python equivalent: from scala.util import Try, Success, Failure

object HandlingFailure {

  // Try = a potentially failed computation
  val anOption = Option(42)
  val aTry: Try[Int] = Try(42) // Try.apply, takes an argument BY NAME

  // empty options
  val anEmptyOption: Option[String] = Option.empty // has no value
  // "empty" try = Failures, store an exception
  def throwSomething(): Int = throw new RuntimeException("BAAAD!")
  val aFailedTry: Try[Int] = Try(throwSomething())
  //                             ^^^^^^^^^^^^^^^^ expression passed BY NAME, so the evaluation is DELAYED until used inside Try.apply

  /*
    Two cases for Try:
    - container of ONE value (Success)
    - container of an EXCEPTION (Failure)
   */

  // Try mechanics/API
  val checkSuccess = aFailedTry.isSuccess // false
  val checkFailure = aFailedTry.isFailure // true
  // get the value with a backup if the Try was a Failure
  val getWithAlternative: Int = aFailedTry.getOrElse(90) // same as an Option
  //                                                 ^^ good practice: avoid expressions which might throw
  // chain Try instances
  val chainedTry: Try[Int] = aFailedTry.orElse(aTry) // same as with Options: returns the first Try if it's successful, or the second otherwise

  // map, filter, flatMap
  val multipliedTry = aTry.map(x => x * 10) // Success(420)
  val multipliedFailure = aFailedTry.map(_ * 10) // Failure(RuntimeException...)
  // map returns a new Try containing f(value) if it's successful, or a failed Try with the original exception otherwise
  val filteredTry = aTry.filter(_ > 10) // Success(42)
  val filteredFailure = aFailedTry.filter(_ > 10) // Failure(RuntimeException...)
  val filteredTry2 = aTry.filter(_ > 100) // Failure(NoSuchElementException)
  // filter returns the original Try if it's successful AND satisfies the predicate, Failure otherwise
  val flatMappedList = List(1,2,3).flatMap(x => List(x, x * 10))
  val flatMappedOption = anOption.flatMap(x => Option(x * 100))
  val aFlatMappedTry = aTry.flatMap(x => Try(x / 0)) // Failure(ArithmeticException)
  val aFlatMappedTry2 = aFailedTry.flatMap(x => Try(x / 0)) // Failure(RuntimeException), the exception from the original Try
  // flatMap returns the Try obtained by running the function on the value (if the original Try is successful), Failure otherwise

  // for comprehensions!
  val combinedTry = for { // Failure(RuntimeException)
    a <- aTry
    b <- aFailedTry
  } yield a + b
  // aTry.flatMap(a => aFailedTry.map(b => a + b))

  // pattern matching to deconstruct a Try, get its value, or process the exception
  val messageToUser = aTry match {
    case Success(value) => s"hey user, the meaning of your life is $value"
    case Failure(ex) => s"ERROR! $ex"
  }

  ////////////////////////////////////////////////////////////////////////////////////////////
  // The why
  ////////////////////////////////////////////////////////////////////////////////////////////

  def unsafeMethod(x: Int): String =
    if (x < 0) throw new RuntimeException("NUMBER CANNOT BE SMALL!")
    else "Scala"

  // defensive style
  val defensiveString = try {
    // any sort of code in here
    unsafeMethod(-45).length
  } catch {
    // treat all exception
    case e: RuntimeException =>
      try {
        unsafeMethod(0).length
      } catch {
        case _: RuntimeException => "something else"
      }
  }

  // problem 1: YOU need to protect yourself
  // problem 2: nested try-catches are THE WORST, hard to read, understand, debug, modify

  // solution: use Try
  val stringLength = Try(unsafeMethod(-45)).map(_.length)

  // best practice #1: if you think an expression can throw, wrap it in a Try
  // best practice #2: if you design functions that can throw, return Try[...] instead
  def safeMethod(x: Int): Try[String] =
    if (x < 0) Failure(new RuntimeException("Number cannot be negative"))
    else Success("Scala")

  val stringLength_v2 = safeMethod(-45).map(_.length)
  val finalMessageToUser = stringLength_v2 match {
    case Success(value) => s"the length of your name is $value"
    case Failure(ex) => s"Error: $ex"
  }

  /*
    Exercise: simulation of real life
   */
  class ConnectionError(message: String) extends RuntimeException(message)
  class ServiceError(message: String) extends RuntimeException(message)

  val host = "124.45.23.78"
  val port = "7777"
  val pageUrl = "rockthejvm.com/handling-failure.html"

  class Connection(h: String, p: String) {
    def fetchPage(url: String): String = {
      if (Random.nextBoolean()) s"<html src='$url' host='$h' port='$p'>Success! ... blog post</html>"
      else throw new ConnectionError("Cannot fetch page right now")
    }
  }

  object HttpService {
    def getConnection(h: String, p: String): Connection = {
      if (Random.nextBoolean()) new Connection(h, p)
      else throw new ServiceError(s"Cannot open connection at $h:$p")
    }
  }

  // TODO: try obtaining a Connection instance from host and port, then call the fetchPage method and print the HTML.
  // Any errors should be displayed to the console.
  def maybePage() = for {
    conn <- Try(HttpService.getConnection(host, port))
    html <- Try(conn.fetchPage(pageUrl))
  } yield html

  def maybePage_v2() = Try(HttpService.getConnection(host, port)).flatMap(conn => Try(conn.fetchPage(pageUrl)).map(html => html))

  def main(args: Array[String]): Unit = {
    println(aTry)
    println(aFailedTry)
    println(combinedTry)
    println(messageToUser)
    println(stringLength)
    println(stringLength_v2)
    println(finalMessageToUser)
    (1 to 1000).foreach { i =>
      println(maybePage_v2())
    }

  }
}
