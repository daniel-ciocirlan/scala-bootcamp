package part5advanced

object Implicits {

  // 1 - implicit values and arguments
  def methodWithImplicitArgument(x: Int, y: String)(implicit meaningOfLife: Int) =
    s"The meaning of life is $meaningOfLife, so we're computing ${x * meaningOfLife + y}"
  // methods with implicit arguments can be called like any other methods
  val normalCall = methodWithImplicitArgument(3, "Scala")(42)
  implicit val answerToLife: Int = 42
  val implicitCall = methodWithImplicitArgument(3, "Scala") // the arg (42) is passed "implicitly" by the compiler

  // can only have ONE implicit value for a particular type
  // implicit val anotherAnswerToLife: Int = 99 // ambiguous implicit values
  def anotherMethodWithImplicitArgs(x: Int)(implicit defaultValue: Int) = x + defaultValue
  val fifty = anotherMethodWithImplicitArgs(8) // same: arg 42 is passed implicitly

  // 2 - implicit classes and extension methods
  implicit class MyRichInt(n: Int) /* <-- can only have ONE constructor argument */ {
    // all these methods will be "extension methods" to the Int type
    def multiply(string: String): String = {
      def stringConcatenation(string: String, n: Int): String =
        if (n <= 0) ""
        else if (n == 1) string
        else string + stringConcatenation(string, n - 1)

      stringConcatenation(string, n)
    }
  }

  val scalax3 = new MyRichInt(3).multiply("Scala")
  val scalax3_v2 = 3.multiply("Scala") // compiler rewrites this as new MyRichInt(3).multiply("Scala")
  /*
    The compiler cannot find the multiply method on Int
    The compiler then searches for ALL implicit classes that might have the multiply method
      - if something is found, that class is instantiated automatically
      - otherwise, compiler error
   */

  // can have multiple implicit classes for the same type
  implicit class PrintableInt(n: Int) {
    def print(): Unit = println(n)
  }

  // can import implicit classes
  import scala.concurrent.duration._
  val aDuration = 3.seconds
  // another example:
  val aRange = 1 to 10 // the 'to' method is an extension method

  // exercise: enrich the String class such that math expressions work like in JS
  implicit class RichString(string: String) {
    def /(n: Int): Int =
      string.toInt / n

    // extension methods DO NOT override existing methods on that type
    def +(n: Int): Int =
      string.toInt + n
  }

  // 3 - implicit conversions (discouraged)
  case class Person(name: String) {
    def greet: String = s"Hi, I'm $name."
  }

  object Person {
    // best practice: if you have an implicit value that makes THE MOST SENSE for your type, place it in the companion object for the type
    implicit val personOrdering: Ordering[Person] = Ordering.fromLessThan((a,b) => a.name < b.name)
  }

  // implicit methods with ONE argument enable conversions
  implicit def string2Person(string: String): Person =
    Person(string)

  val danielsGreetingStandard = string2Person("Daniel").greet
  val danielsGreetingMagic = "Daniel".greet // compiler rewrites this to string2Person("Daniel").greet

  // can replace a required type with something that can be implicitly converted
  def makePersonTalk(person: Person): String = person.greet
  val danielTalks = makePersonTalk(Person("Daniel")) // normal way, passing a Person instance
  val danielTalksMagic = makePersonTalk("Daniel") // "Daniel" automatically converted to Person via string2Person("Daniel")

  /*
    Organizing implicits
   */

  // Places where the compiler looks for implicits if you call a method with implicit arguments:
  // 1 - local scope = the scope where the method is being invoked
  // implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan((a, b) => a > b)

  // 2 - imported scope = all objects and packages imported explicitly
  // import MyImplicits._ // <- implicit Ordering[Int] will be used from HERE

  // 3 - all companion objects of all types involved in the method call
  // for sorted: companion of List, companion object of Int, companion object of Ordering
  // it happens that there is an implicit Ordering[Int] in the companion object of Int

  val aList = List(1,4,2,5,3)
  val sortedList = aList.sorted // needs an implicit Ordering[Int]

  val people = List(Person("Zod"), Person("Spider-Man"), Person("Alice"))
  val alphabeticList = people.sorted // needs an implicit Ordering[Person]

  def main(args: Array[String]): Unit = {
    println(sortedList)
    println(alphabeticList)
  }
}

object MyImplicits {
  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan((a, b) => a > b)
}
