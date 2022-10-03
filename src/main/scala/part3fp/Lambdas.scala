package part3fp

object Lambdas {

  val doubler: Int => Int = new Function1[Int, Int] {
    override def apply(x: Int) = x * 2
  }

  // lambdas aka anonymous functions (same as "arrow functions" in JS)
  // JS: x => x * 2
  // Python: lambda x: x * 2
  // Java: (x) => x * 2
  val doubler_v2: Int => Int = (x: Int) => x * 2 // <- overriding the apply method (the only one abstract in Function1)
  //                           ^^^^^^^^^^^^^^^^^ syntax sugar for new Function1[...] { override ... }

  // JS: (x, y) => x + y
  // Python: lambda x,y: x + y
  // Java: (x,y) => x + y
  val adder: (Int, Int) => Int = (x: Int, y: Int) => x + y

  // can use type inference IF the compiler knows the type of the function
  val adder_v2: (Int, Int) => Int = (x, y) => x + y
  //                                 ^^^^ types of x and y are inferred
  val adder_v3 = (x: Int, y: Int) => x + y
  //  ^^^^^^^^ type of the function is inferred

  // zero arg functions
  val justDoSomething = () => 45

  // short lambdas
  val adder_v4: (Int, Int) => Int = _ + _ // same as (x, y) => x + y
  //                                ^   ^
  //                                x   y
  val doubler_v3: Int => Int = _ * 2
  //                           ^ used ONCE in the function's "body"

  // 1. Use arrow functions (lambdas) everywhere in the LList implementation.
  // 2. use lambdas to write this superAdder in a shorter way
  val superAdder: Int => Int => Int = x => y => x + y
  //                                  ^    ^    ^^^^^
  //                                 Int  Int   MUST BE an Int
  // CURRIED function

  /*
    functions which
    - take other functions as args
    AND/OR
    - return other functions as results

    are called Higher-Order Functions (HOFs)
   */

  def main(args: Array[String]): Unit = {
    println(superAdder(5)(2))
  }
}
