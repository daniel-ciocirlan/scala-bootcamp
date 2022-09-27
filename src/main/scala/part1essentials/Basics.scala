package part1essentials

object Basics {

  /*
    Scala
    - strongly typed language, compiler will check type "rules"
    - compiled language -> JVM bytecode (.class files, JARs)
      JVM bytecode is run by the Java Virtual Machine (JVM)
   */

  val meaningOfLife: Int = 42 // no semicolons
  // const meaningOfLife = 42 (JS)
  // const int meaningOfLife = 42; (Java)
  // value has a type: Int
  // values cannot be reassigned
  //  meaningOfLife = 43 // compiler error

  // type inference
  val anInteger = 67 // 67 is an Int, therefore anInteger is an Int

  // common types
  val aBoolean: Boolean = false // or true
  val aChar: Char = 's' // single quotes for single characters!
  val anInt: Int = Int.MaxValue // 4 bytes x 8 bits each 1111111111111111111111111111111111111111 = 2^31 -1
  val aShort: Short = 6373 // 2 bytes, rarely used
  val aLong: Long = 5623785632785L // 8 bytes, used for IDs and numbers > 2 billion
  val aFloat: Float = 2.3f // 4 bytes
  val aDouble: Double = 2.3 // 8 bytes, default decimal format
  val aString: String = "Scala is awesome" // watch the ""

  // instructions ARE EXECUTED
  // expressions ARE EVALUATED to a value
  val meaningOfLife_v2 = 40 + 2 // 42
  // think in terms of expressions

  // math expressions: + - * /, bitwise | & >> << >>>
  // the '/' operator returns the QUOTIENT of integer division
  // the % operator returns the REMAINDER of integer division

  // comparison expressions: < <= == != >= >
  val equalityTest = 1 == 2
  // boolean expressions: ! (negate), || (or), && (and)
  val nonEqualityTest = !equalityTest

  // if expressions
  /*
    in other languages:

      if (1 > 2) {
        System.out.println(...)
      } else {
        x = 45;
      }
   */
  val aCondition = 1 > 2
  val anIfExpression = if (aCondition) 45 else 99 // if structures are EXPRESSIONS

  // code blocks
  val aCodeBlock = {
    // add code
    // can define local values (invisible to other blocks)
    val localValue = 10
    val anotherLocalValue = localValue + 13
    // expressions

    // last expression decides the block
    anotherLocalValue + meaningOfLife * 10
    //                      ^^ can use outside values as well*
  }

  // Unit = nothing meaningful returned
  val printing: Unit = println("I am printing something") // side effect = an effect in the world (e.g. printing, writing files...), without producing any value
  val theUnit: Unit = () // only one possible value for Unit, and it's useless.

  /*
    Exercise:
    Without running the code, what will these expressions print?
   */
  // 1
  val ex1 = {
    2 < 3
  } // true

  // 2
  val ex2 = {
    if (ex1) 239 else 986
    42 // is its own expression
  } // 42

  // 3
  val ex3 = println("this is the life!") // this is the life

  /*
    Scala application
    - evaluate the app object with all its fields
    - run the main method of the object
   */
  def main(args: Array[String]): Unit = {
    println(ex1)
    println(ex2)
    println(ex3)
  }

}
