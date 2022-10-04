package part5advanced

object PartialFunctions {

  // a lambda
  val aLambda = (x: Int) => x + 1

  /*
    a lambda that takes an int and returns:
    - 10 if the argument is 0
    - 25 if the argument is 1
    - 35 if the argument is 2
   */
  val newLambda: Int => Int = (x: Int) => x match {
    case 0 => 10
    case 1 => 25
    case 2 => 35
  } // partial function

  val newLambda_v2: PartialFunction[Int, Int] = { // same as above
  //                                ^^^  ^^^ return type
  //                                input
    case 0 => 10
    case 1 => 25
    case 2 => 35
  }

  // benefit #1: utilities
  // check if a PF can be called on an argument
  val canICallLambdaOn56 = newLambda_v2.isDefinedAt(56) // false, does NOT invoke the function
  // chain PFs
  val addedCase: PartialFunction[Int, Int] = {
    case 3 => 45
  }
  val combinedPF = newLambda_v2.orElse(addedCase) // PF with all 4 cases

  // benefit #2: nicer syntax
  val aList = List(0,1,2,3)
  val taxBrackets = aList.map(combinedPF) // possible because PartialFunction[Int, Int] extends Function1[Int, Int]
  // pass PF literally
  val taxBrackets_v2 = aList.map(x => x match { // "regular" function pass with pattern matching
    case 0 => 10
    case 1 => 25
    case 2 => 35
    case 3 => 45
  })
  // alt: use PF directly
  val taxBrackets_v3 = aList.map({
    case 0 => 10
    case 1 => 25
    case 2 => 35
    case 3 => 45
  })
  // with syntax sugars!
  val taxBrackets_v4 = aList.map { // same as v3, eliminate the ()
    case 0 => 10
    case 1 => 25
    case 2 => 35
    case 3 => 45
  }

  def main(args: Array[String]): Unit = {
    println(newLambda_v2(0)) // 10
    println(combinedPF(2)) // 35, returned by the first PF
    println(combinedPF(3)) // 45, returned by the second PF
    // println(combinedPF(56)) // crash, as neither PF supports it
    println(taxBrackets)
    println(taxBrackets_v2)
    println(taxBrackets_v3)
    println(taxBrackets_v4)
  }
}
