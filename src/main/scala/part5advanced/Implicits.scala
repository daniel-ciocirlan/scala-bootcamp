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

  def main(args: Array[String]): Unit = {
    (1.to(10)).foreach(println)
  }
}
