package part5advanced

object CurryingPAF {

  // currying
  val superadder: Int => Int => Int = x => y => x + y
  val add3 = superadder(3)
  val eight = add3(5)
  val eight_v2 = superadder(3)(5)

  val superfunction: (Int, Int) => (Int, Int, Int) => Int = (x, y) => (a,b,c) => (x + y) * (a + b + c)
  val miniProduct = superfunction(3,4)
  val product = miniProduct(1,2,3)
  val product_v2 = superfunction(3,4)(1,2,3)

  // curried methods
  def curriedAdder(x: Int)(y: Int): Int = x + y
  val add3_v2 = curriedAdder(3) _ // <- underscore means that the expression returns another function (eta-expansion)
  val add3_v3: Int => Int = curriedAdder(3)
  //         ^^^^^^^^^^^^ because we specify the type, the compiler puts the '_' automatically

  // eta-expansion = transformation between METHODS and FUNCTION values
  def incrementMethod(x: Int) = x + 1
  val incrementFunction: Int => Int = incrementMethod // eta-expansion done automatically
  val incrementFunction_v2 = incrementMethod _ // same

  // pass "methods" as arguments
  val print10Numbers = (1 to 10).foreach(println)
  // actually syntax sugar for:
  val printlnFunction: Any => Unit = println
  val print10Numbers_v2 = (1 to 10).foreach(printlnFunction)
  // another example:
  def add(x: Int, y: Int) = x + y
  val sum = List(1,2,3).reduce(add) // automatic eta-expansion only possible if the signatures match

  // methods without parens cannot be expanded
  def currentTime(): Long = System.currentTimeMillis()
  val currentTimeFunction: () => Long = currentTime // eta-expansion

  //  def currentTime_v2: Long = System.currentTimeMillis()
  //  val currentTimeFunction_v2: () => Long = currentTime_v2 // doesn't work, the compiler thinks that you're invoking it

  def main(args: Array[String]): Unit = {

  }
}
