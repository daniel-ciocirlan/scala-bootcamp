package part3fp


object WhatsAFunction {

  // regular values (Int, Char, ...), reference types (all classes we define)
  // FP is about using functions as "first-class" elements of the language = values

  trait MyFunction[A, B] {
    // one method
    def apply(value: A): B

    // some other code
    def printHello() = println("Hello, function")
  }

  val doubler = new MyFunction[Int, Int] {
    override def apply(value: Int) = value * 2

    // that other code will be inherited here
  }

  // my own "function" type
  def useAFunctionOnAValue(theFunction: MyFunction[Int, Int], value: Int): Int =
    theFunction(value)
  val aThing: MyFunction[Int, Int] = doubler

  // instance of something with an apply method == a "function"
  val doubler_v2 = new Function1[Int, Int] { // Function1 is a built-in trait in the Scala standard library, similar to MyFunction
    override def apply(value: Int) = value * 2
  }

  val adder = new Function2[Int, Int, Int] {
    override def apply(a: Int, b: Int) = a + b
  }

  val fourArgFunction = new Function4[Int, String, Double, Boolean, String]  {
    override def apply(v1: Int, v2: String, v3: Double, v4: Boolean) = "4"
  }

  /*
    Exercises
    1. Create a function which takes 2 strings and concatenates them.
    2. What similarity can you find between Predicates, Transformers and Function1,2,3,...?
        THEY ARE FUNCTIONS!
        Get rid of them.
    3. Create a function which takes an int as an argument (x) and
        returns ANOTHER FUNCTION, which takes an Int and returns x + that int.
   */
  val concatenator = new Function2[String, String, String] {
    override def apply(a: String, b: String) = a + b
  }

  // 3
  val superAdder = new Function1[Int, Function1[Int, Int]] {
    override def apply(x: Int): Function1[Int, Int] = {
      val add = new Function1[Int, Int] {
        override def apply(y: Int): Int = x + y
      }
      add
    }
  }

  val add5: Function1[Int, Int] = superAdder(5)
  val seven = add5(2) // 7
  val seven_v2 = superAdder(5)(2) // 7
  //             ^^^^^^^^^^^^^
  //             Function1    ^^^ => Int
  //                          Int
  val seven_v3 = superAdder.apply(5).apply(2) // with apply

  // function types
  // Function1[A, B] == A => B (syntax sugar)
  val incrementer: Int => Int = new Function1[Int, Int] { override def apply(x: Int) = x + 1 }
  //               ^^^^^^^^^^
  //                 TYPE
  //            same as Function1[Int, Int]

  val adder_v2: (Int, Int) => Int = new Function2[Int,Int,Int] { override def apply(x: Int, y: Int) = x + y }
  //            ^^^^^^^^^^^^^^^^^


  def main(args: Array[String]): Unit = {
    println(adder.apply(5, 7)) // 12

  }

}
