package part1essentials

object CBNvsCBV {

  // call by value (CBV) = arguments are evaluated BEFORE function invocation
  def aFunction(arg: Int) = arg + 1
  //                        ^ arg was evaluated BEFORE
  val aComputation = aFunction(10 + 34) // aFunction(44) = 45

  // call by name (CBN) = arguments are evaluated ONLY WHEN USED
  def aByNameFunction(arg: => Int): Int = arg + 1
  //                                      ^ arg is evaluated NOW
  val aComputation_v2 = aByNameFunction(10 + 34) // 10 + 34 + 1 = 45


  // another example where CBN makes a difference
  def printTwice(arg: Long): Unit = {
    // arg is evaluated before invocation, used everywhere
    println("Called by value: " + arg)
    println("Called by value: " + arg)
  }

  def printTwiceByName(arg: => Long): Unit = {
    // arg is evaluated ONLY WHEN used, EVERY TIME it's used
    println("Called by name: " + arg)
    println("Called by name: " + arg)
  }

  /*
    Reasons to use CBN:
    - to produce effects on every reference of the arg
    - to prevent the evaluation of the arg
   */

  def demoCBN(): Unit = {
    printTwiceByName(System.nanoTime())
  }

  // another example: preventing arg evaluation
  def functionWithDangerousArg(arg: Int): Unit = {
    println("Dangerous arg!")
  }

  def functionWithDangerousArgCBN(arg: => Int): Unit = {
    println("Dangerous arg!")
  }

  def main(args: Array[String]): Unit = {
    println(aFunction(1 + 2)) // argument is evaluated BEFORE the function invocation, so we get aFunction(3)
    println(aByNameFunction(1 + 2)) // arg is NOT evaluated UNTIL used in the function body
  }
}
