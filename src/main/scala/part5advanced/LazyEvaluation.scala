package part5advanced

object LazyEvaluation {

  // lazy DELAYS the evaluation of a value until used for the first time
  lazy val x: Int = {
    println("This is an int")
    42
  }
  // once the value is evaluated, it will not be evaluated again

  // for comprehensions with if guards
  def lessThan30(n: Int): Boolean = {
    println(s"is ${n} less than 30?")
    n < 30
  }

  def greaterThan20(n: Int): Boolean = {
    println(s"is ${n} bigger than 20?")
    n > 20
  }

  val numbers = List(1, 25, 40, 5, 23)

  /*
    is 1 less than 30?
    is 25 less than 30?
    is 40 less than 30?
    is 5 less than 30?
    is 23 less than 30?
    is 1 bigger than 20?
    is 25 bigger than 20?
    is 5 bigger than 20?
    is 23 bigger than 20?
    [25,23]
   */
  def demoFilters(): Unit = {
    val lt30 = numbers.filter(lessThan30)
    val gt20 = lt30.filter(greaterThan20)
    println(gt20)
  }

  /*
    is 1 less than 30?
    is 1 bigger than 20?
    is 25 less than 30?
    is 25 bigger than 20? --- true
    is 40 less than 30?
    is 5 less than 30?
    is 5 bigger than 20?
    is 23 less than 30?
    is 23 bigger than 20? -- true
   */
  def demoWithFilters(): Unit = {
    // withFilter = filter with lazy evaluation
    val lt30 = numbers.withFilter(lessThan30)
    val gt20 = lt30.withFilter(greaterThan20)
    println(gt20.map(x => x))
    // withFilter is more efficient (single pass over the data), but should not be used manually
  }

  val nicelyProcessedList = for {
    x <- numbers if lessThan30(x) && greaterThan20(x)
    //           ^^ if guards invoke withFilter
  } yield x

  def main(args: Array[String]): Unit = {
    demoWithFilters()
  }
}
