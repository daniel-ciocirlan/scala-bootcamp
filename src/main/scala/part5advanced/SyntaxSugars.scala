package part5advanced

object SyntaxSugars {

  // 1 - single abstract method pattern
  abstract class Action[A] { // can use generics too
    // has only ONE abstract method
    def act(x: A, y: A): String
    def increment(x: Int) = x + 1 // may have non-abstract methods
  }

  val anAction: Action[Int] = new Action[Int] {
    override def act(x: Int, y: Int) = "Scala" * x
  }

  val anAction_v2: Action[Int] = (x: Int, y: Int) => "Scala" * (x + y)
  // compiler REWRITES the lambda into a new Action { override def act(x: Int) = "Scala" * x }
  val scalax7 = anAction_v2.act(3, 4) // need to call the method from the abstract class
  // works with abstract classes AND traits with ONE abstract method

  // example: Runnable (the Java trait that defines computations on another thread)
  val aRunnable = new Runnable {
    override def run(): Unit = println("hello from another thread")
  }
  val aRunnable_v2: Runnable = () => println("hello from another thread")
  val aThread = new Thread(aRunnable_v2)

  // 2 - syntax for calling methods with ONE argument
  def singleArgMethod(x: Int): Int = x * 100

  // if we want to call such a method with a big expression, we would use a code block
  val bigResult = singleArgMethod({
    // LOTS of code
    42
  })

  val bigResult_v2 = singleArgMethod { // parens are implied, same as above
    // lots of code
    42
  } // the entire code block is the argument

  // example: .map, .filter, .flatMap, etc.
  List(1,2,3).map { x =>
    // a whole block of code
    x + 1
  }

  // 3 - methods ending in a ':' are RIGHT-associative, i.e. right-most operations evaluate first
  val aList = List(4,5,6)
  val biggerList = 1 :: 2 :: 3 :: aList // [1,2,3,4,5,6]
  val biggerList_op = 1 :: (2 :: (3 :: aList)) // [1,2,3,4,5,6]
  val biggerList_v2 = aList.::(3).::(2).::(1) // same = what the compiler rewrites biggerList to

  val sum = ((1 + 2) + 3) + 4
  //         ^ "natural" order = LEFT-associativity, i.e. left-most operations evaluate first

  class MyStream[A] {
    def -->:(value: A): MyStream[A] = this // implementation not important
    //     ^ ends in :, then this method is RIGHT-associative
  }

  val emptyStream = new MyStream[Int]
  val myStream = 1 -->: 2 -->: 3 -->: emptyStream // same as emptyStream.-->:(3).-->:(2).-->:(1)

  // 4 - multi-word identifiers, can use special chars (e.g. ' ', '/'...) inside backticks ``
  def `do this and tell me what you got`(x: Int) = x + 1

  // example: HTTP libraries, e.g. Akka HTTP, Http4s, etc.
  object `Content-Type` {
    val `application/json` = "application/json"
  }

  val header = "Header: ...."
  val description = header match {
    case `Content-Type`.`application/json` => "got a json"
    case _ => "got something else"
  }

  // 5 - infix types = types with TWO generic arguments
  class Or[A, B]
  val either: Or[Int, String] = new Or[Int, String] // "regular" syntax
  val either_v2: Int Or String = new (Int Or String)
  //             ^^^^^^^^^^^^^ same as Or[Int, String]

  // can combine infix types with permissive names!
  class -------->[A, B]
  val arrow: Int --------> String = new (Int --------> String)

  // 6 - variable arguments, aka varargs
  def methodWithVarArgs(firstArg: Int, otherArgs: String*) = firstArg + otherArgs.length
  //                                                    ^ means 0 or more Strings

  // call methods with varargs:
  val oneArg = methodWithVarArgs(1)
  val threeArgs = methodWithVarArgs(1, "Scala", "rocks")
  val fiveArgs = methodWithVarArgs(5, "Scala", "rocks", "big", "time")

  // can expand a collection to varargs
  val listOfStrings = List("Scala", "rocks", "big", "time")
  val fiveArgsWithList = methodWithVarArgs(5, listOfStrings: _*)
  //                                                       ^^^^ expands the list into all the arguments one by one

  def main(args: Array[String]): Unit = {
    println(fiveArgsWithList)
  }
}
