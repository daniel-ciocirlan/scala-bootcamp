package part3fp

import scala.util.Random

object Options {

  // option = a "list" with at most one element
  val aList: List[Int] = List(42)
  val anEmptyList: List[Int] = List.empty
  val anOption: Option[Int] = Option(42) // has one element, 42
  val anEmptyOption: Option[Int] = Option.empty // has no elements

  // Options API
  val isEmpty = anOption.isEmpty // false
  // options only allow GETTING the element IF the option is not empty
  val innerValue = anOption.getOrElse(90) // returns the value inside the option IF it's not empty, OR 90 if it is empty
  val altValue = anEmptyOption.getOrElse(90) // 90
  // chain options - for the case where .getOrElse doesn't work (you don't know which value to use, in advance)
  val chainedOption = anEmptyOption.orElse(Option(99)) // the old option if it's NOT empty, OR the alternative (arg) otherwise
  val chainedValue = chainedOption.getOrElse(0) // 99

  // example for .orElse
  // option 1: read from the standard config file
  val option1 = Option.empty[String] // you don't know if it's empty
  // option 2: read config from the backup store
  val option2 = Option("com.rockthejvm.port=99") // you don't know if it's empty
  val combinedOption = option1.orElse(option2)

  // map: returns another option with the value transformed (if it's not empty), or empty
  val aStringList = aList.map(x => x.toString) // ["42"]
  val aStringOption = anOption.map(x => x.toString) // ("42")
  val incrementedOption = anOption.map(_ + 1) // (43)
  val anEmptyIncrementedOption = anEmptyOption.map(_ + 1) // empty option
  // filter: preserves the value if it matches the predicate, returns empty option otherwise
  val filteredList = aList.filter(_ > 100) // []
  val filteredOption = anOption.filter(_ > 100) // []
  val emptyFilteredOption = anEmptyOption.filter(_ > 100) // []
  // flatMap
  val flatMappedList = aList.flatMap(x => List(x, x + 1, x * 10)) // [42, 43, 420]
  val flatMappedOption = anOption.flatMap(x => Option(x * 10)) // [420]
  //                                      ^^^^^^^^^^^^^^^^^^^ Int => Option[Int]

  // for thingies!
  val forOption = for {
    a <- anOption
    b <- flatMappedOption
  } yield a + b
  // anOption.flatMap(a => flatMappedOption.map(b => a + b))

  // pattern matching! the preferred way to "get" the value of the option
  val deconstructOption = anOption match {
    case Some(value) => s"a non-empty option containing $value"
    case None => "an empty option"
    //   ^^^^ THE empty option, similar to Nil for lists
  }

  ////////////////////////////////////////////////////////////////////////////
  // The why: avoid using nulls
  ////////////////////////////////////////////////////////////////////////////

  // options describe a POSSIBLY absent value
  def unsafeMethod(): String = {
    // some code
    null
  }

  // defensive style of checking for nulls - not recommended
  val stringLength = {
    val potentialString = unsafeMethod()
    if (potentialString == null) -1
    else potentialString.length
  }

  // options: no null checks
  val stringOption = Option(unsafeMethod()) // [], Option.apply takes care of the null checks
  val stringLengthOption = stringOption.map(_.length) // [], mapping an empty option

  // best practice #1: if you ever doubt a value might not exist, wrap it in an Option
  // best practice #2: if you design an API that might return "absent" values, make that API return an Option instead of nulls.
  def safeMethod(): Option[String] = {
    // some code
    Option.empty // instead of null
  }

  // example of safe API: Map.get
  val phonebook: Map[String, Int] = Map(
    "Jim" -> 123,
    "Alice" -> 456,
    "Harry Potter" -> 999
  )
  val maybeDanielsNumber = phonebook.get("Daniel")
  // returns a non-empty option with the value associated to "Daniel" if it's there, or an empty option otherwise
  // phonebook("Daniel") crashes!

  // example 2: List.headOption
  // val emptyHead = anEmptyList.head // this crashes! unsafe method
  val emptyHeadOption = anEmptyList.headOption
  // returns an option with the head of the list if it's there, empty option otherwise

  // exercise: add a .headOption method on our own LList!

  /*
    Exercise
   */
  // a simulated "configuration"
  class Connection private {
    def connect(): String = "Connection successful"
  }

  object Connection {
    def apply(host: String, port: String): Option[Connection] = {
      if (Random.nextBoolean()) Some(new Connection)
      else None
    }
  }

  val configMap = Map(
    "host" -> "172.123.14.1",
    "port" -> "8080"
  )

  // TODO 1: get the values for host and port out of the configMap
  val hostOption: Option[String] = configMap.get("host")
  val portOption: Option[String] = configMap.get("port")
  // TODO 2: use the options returned from the map to pass arguments to Connection.apply
  // TODO 3: call the apply method and obtain a possible Connection instance
  // solution 1: pattern matching
  val connectionOption_v1: Option[Connection] = hostOption match {
    case Some(host) =>
      portOption match {
        case Some(port) => Connection(host, port) // happy path, both host and port are available, can call the apply method
        case None => Option.empty // absent connection, no port
      }
    case None => Option.empty // absent connection, no host
  }

  // solution 2: flatMaps
  val connectionOption_v2 = hostOption.flatMap(host => portOption.flatMap(port => Connection(host, port)))
  val connectionDetails_v2 = hostOption.flatMap(host => portOption.flatMap(port => Connection(host, port).map(conn => conn.connect())))

  // solution 3: for comprehensions
  val connectionDetails_v3: Option[String] = for {
    host <- hostOption
    port <- portOption
    conn <- Connection(host, port)
  } yield conn.connect()

  // TODO 4: call the connect method on the Connection inside the returned Option, print the result
  val connectionDetails: String = connectionOption_v2 match {
    case Some(conn) => conn.connect()
    case None => "No connection"
  }

  // Morning workout
  // 1
  val morningOption: Option[List[Option[Int]]] = Option(List(Option(1), Option(2)))
  //               ^ what type is this?
  val morningOption2: List[Option[List[Int]]] = List(Option(List(1,2,3)))
  val morningOption3: List[Option[List[Any]]] = List(Option(List("scala", 2)), Option.empty)
  val morningOption4: List[Option[List[(String, Int)]]] = List(Option(List(("scala", 2))))

  // 2
  val maybeString: Option[String] = Some("Scala") // language
  val maybeInt: Option[Int] = Some(42) // meaning of life
  val maybeTime: Option[Int] = Some(15) // time
  // return "The meaning of life is $meaningOfLife, learning $language at $time"

  val statement = for {
    lang <- maybeString
    mol <- maybeInt
    time <- maybeTime
  } yield s"The meaning of life is $mol, learning $lang at $time"


  def main(args: Array[String]): Unit = {
    println(statement.getOrElse("Unknown statement"))
  }
}
