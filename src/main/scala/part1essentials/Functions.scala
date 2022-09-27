package part1essentials

import scala.annotation.tailrec

object Functions {

  // function = reusable piece of code
  // function signature: function name + args + return type
  def aFunction(a: String, b: Int): String = {
    // A SINGLE EXPRESSION, may be a code block
    a + " " + b
  }

  // function calls are expressions
  val aFunctionInvocation = aFunction("Scala", 100)

  // no-argument functions
  def aNoArgFunction(): Int = 45
  def aParameterlessFunction: Int = {
    println("doing parameterless things")
    45
  }

  // no arg functions
  val noArgFunctionInvocation = aNoArgFunction()
  val parameterlessFunctionInvocation = aParameterlessFunction // the invocation of the function

  /*
    sc("Scala", 3) = "Scala" + sc("Scala", 2)
    sc("Scala", 2) = "Scala" + sc("Scala", 1)
    sc("Scala", 1) = "Scala"

    => sc("Scala", 2) = "Scala" + sc("Scala", 1) = "Scala" + "Scala" = "ScalaScala"
    => sc("Scala", 3) = "Scala" + sc("Scala", 2) = "Scala" + "ScalaScala" = "ScalaScalaScala"
   */
  // functions can be recursive
  def stringConcatenation(string: String, n: Int): String =
    if (n <= 0) ""
    else if (n == 1) string
    else string + stringConcatenation(string, n - 1)

  // RECURSION is used to implement "repetitiveness"

  // "void" functions
  def aVoidFunction(string: String): Unit =
    println("I am printing something: " + string)

  // can use side effects with returning meaningful values
  // discouraged
  def computeDoubleStringWithPrinting(string: String): String = {
    aVoidFunction(string) // side effect
    string + string // returning a value
  }

  // can define small (auxiliary) functions in big ones
  def aBigFunction(n: Int): Int = {
    // local values
    val aLocalValue = 40
    // local functions
    def aSmallFunction(a: Int, b: Int) = a + b

    // the last expression is the function implementation
    aSmallFunction(n, aLocalValue + 1)
  }

  /*
    Exercises
    1. A greeting function (name, company) => "Hi, my name is $name and I work for $company."
    2. Factorial function (n) => 1 * 2 * 3 * ... * n
    3. Fibonacci function
      fib(1) = 1
      fib(2) = 2
      fib(n) = fib(n-1) + fib(n-2)
      => 1 2 3 5 8 13 21 34 ...

    4. Test if a number is prime (n) => a boolean (true if the number is prime)
   */

  // good practice: have function return "pure" values, THEN do something with them (e.g. main
  // 1
  def greet(name: String, company: String): String = {
    "Hi, I am " + name + " and I work for " + company
  }

  /*
    same principle as stringConcatenation:
    factorial(3) = 3 * factorial(2) => 3 * 2 = 6
    factorial(2) = 2 * factorial(1) => 2 * 1 = 2
    factorial(1) = 1
   */
  // 2
  def factorial(n: Int): Int =
    if (n <= 1) 1
    else n * factorial(n - 1)

  /*
    fibo(5) = fibo(4) + fibo(3)
      fibo(4) = fibo(3) + fibo(2)
        fibo(3) = fibo(2) + fibo(1)
          fibo(2) = 2
          fibo(1) = 1
          => fibo(3) = 2 + 1 = 3
        fibo(2) = 2
        => fibo(4) = 3 + 2 = 5
      fibo(3) = fibo(2) + fibo(1)
        fibo(2) = 2
        fibo(1) = 1
        => fibo(3) = 2 + 1 = 3
      => fibo(5) = 5 + 3 = 8
   */
  // 3
  def fibonacci(n: Int): Int =
    if (n <= 0) -1
    else if (n <= 2) n
    else fibonacci(n - 1) + fibonacci(n - 2)

  // stack-based recursion
  // stack overflows if the recursion depth is too large (~10k stack frames)

  // 4
  // German
  // isPrime(11) = test(5) = test(4) = test(3) = test(2) = test(1) = true
  def isPrime(n: Int): Boolean = {
    def testAllPossibleDivisorsUpTo(d: Int): Boolean = {
      if (d == 1) true
      else if (n % d == 0) false
      else testAllPossibleDivisorsUpTo(d - 1)
    }

    if (n <= 1) false
    else testAllPossibleDivisorsUpTo(n / 2)
  }

  /*
    Tail recursion
   */
  /*
  stack:
  -----------------------------------
    sumUntil(10) = 10 + sumUntil(9) = 10 + 45 = 55
    sumUntil(9) = 9 + sumUntil(8)
    .
    .
    .
    sumUntil(1) = 1

   */
  def sumUntil(n: Int): Int =
    if (n <= 1) 1
    else n + sumUntil(n - 1) // last expression is the +, need to evaluate the operands first

  /*
    Desired recursion:
      place the recursive calls as the LAST EXPRESSION of their code path
    Outcome:
      recursive calls DO NOT use additional stack frames (bye StackOverflows!)
   */
  def sumUntil_v2(n: Int): Int = {
    @tailrec
    def sumTailRecursion(x: Int, accumulator: Int = 0): Int =
      if (x <= 1) accumulator
      else sumTailRecursion(x - 1, accumulator + x) // last expression is the recursive call, don't need to remember anything

    sumTailRecursion(n, 0)
  }

  // default arguments
  def functionWithDefaultArgs(name: String = "Daniel", company: String = "Rock the JVM"): String =
    "I'm " + name + " and work at " + company

  val riccardo = functionWithDefaultArgs("Riccardo") // functionWithDefaultArgs("Riccardo", "Rock the JVM")
  val daniel = functionWithDefaultArgs() // "Daniel", "Rock the JVM"

  // name your arguments to disambiguate
  // suggestion: name your boolean arguments
  val coderProdigy = functionWithDefaultArgs(company = "Coder Prodigy")

  /*
    Exercises:
   */
  // 1. Build a stringConcatenation(string, n) which is TAIL recursive
  def stringConcatenation_v2(string: String, n: Int): String = {
    @tailrec
    def concat(acc: String, i: Int): String = {
      if (i == 0) acc
      else concat(acc + string, i - 1)
    }
    concat("", n)
  }

  val quotient = 1 / 10 // = 1
  val remainder = 1 % 10 // = 1
  quotient * 10 + remainder == 1

  /*
    a, b
    quotient = a / b
    remainder = a % b

    property:
    a == quotient * b + remainder
   */

  /*
    countDigits(12345) =
    countAux(12345, 0) =
    countAux(1234, 1) =
    countAux(123, 2) =
    countAux(12, 3) =
    countAux(1, 4) =
    countAux(0, 5) =
    5
   */
  // 2. Count how many digits a number has (stack + tail recursive)
  // countDigits(13) = 2
  // countDigits(1234567) = 7
  def countDigits(n: Int): Int = {
    @tailrec
    def countAux(n: Int, acc: Int): Int =
      if (n == 0) acc
      else countAux(n / 10, acc + 1)

    countAux(n, 0)
  }

  def countDigitsStack(n: Int): Int =
    if (n == 0) 0
    else 1 + countDigitsStack(n / 10)

  // isUgly(2 * 7 * 11) = isUgly(7 * 11)

  // 3. Find if a number is "ugly"
  // ugly number has ONLY the "proper" factors 2,3 or 5, can occur multiple times
  // 10 is ugly = 2 * 5
  // 15 is ugly = 3 * 5
  // 125 is ugly = 5 * 5 * 5
  // 49 is not ugly = 7 * 7
  def isUgly(n: Int): Boolean = {
    if (n <= 0) false
    else if (n == 1) true
    else if (n % 2 == 0) isUgly(n / 2) // n contains the factor 2 once, need to test the rest of the factors
    else if (n % 3 == 0) isUgly(n / 3)
    else if (n % 5 == 0) isUgly(n / 5)
    else false
  }

  /*
    1. Count to 1000. For every number 1-1000, print "Counting at $number".
      for (let i=0; i<1000; i++)
        console.log("Counting at", number)

    2. Reverse a string, without using the .reverse function.
        Use
          - charAt to get characters out of strings
          - substring to cut the string
          - length to get the string length

        reverse("scala") => "alacs"

    3. Reverse an integer. Return a new integer with the digits reversed.
      (don't use the string reverse)
   */
  // 1
  def countToNumber(limit: Int): Unit = {
    @tailrec
    def countAux(i: Int): Unit = {
      if (i <= limit) {
        println(s"Counting at $i")
        countAux(i + 1)
      }
    }

    countAux(1)
  }

  // 2
  def reverseString(aString: String): String = {
    // take the char at i and put it at length - i
    @tailrec
    def tailRecursion(word: String, charPos: Int, acc: String): String = {
      if (charPos < 0) acc
      else {
        tailRecursion(word, charPos - 1, acc + word.charAt(charPos))
      }
    }

    tailRecursion(aString, aString.length - 1, "")
  }

  // 3
  // 1234 -> 4321
  // 1230 -> 123
  // 2 -> 2
  // 0 -> 0
  // -123 -> -321 watch for negatives
  // 2147483647 -> 7463847412 too big!! return 0
  def reverseIntegerCheat(int: Int): Int =
    reverseString(int.toString).toInt

  val aNumber = 1
  val biggerThanMax = aNumber + Int.MaxValue // overflow of the Int representation
  /*
    Ints
    ________ ________ ________ ________
    ^ sign (0=positive, 1=negative)
     _______ ________ ________ ________ 31 bits are the "value"

     -2 =
    1_______ ________ ________ ______10

    1 = 2^0
    00000000 00000000 00000000 00000001

    2 = 1 + 1
    00000000 00000000 00000000 00000001 +
    00000000 00000000 00000000 00000001
    =
    00000000 00000000 00000000 00000010

    3 = 2 + 1
    00000000 00000000 00000000 00000010 +
    00000000 00000000 00000000 00000001
    =
    00000000 00000000 00000000 00000011

    Max number
    01111111 11111111 11111111 11111111

    max number + 1 = Int.MinValue
    10000000 00000000 00000000 00000000

    -Int.MaxValue = all the bits from Int.MaxValue + the sign bit is 1
    11111111 11111111 11111111 11111111
   */

  def reverseNumber(n: Int): Int = {
    @tailrec
    def reverseTailrec(x: Int, acc: Int=0): Int = {
      if (x > 0) {
        val resultAttempt = acc * 10 + (x % 10)
        reverseTailrec(x / 10, resultAttempt)
      }
      else acc
    }
    if (n >= 0) reverseTailrec(n)
    else -reverseTailrec(-n)
  }


  def main(args: Array[String]): Unit = {
    println(isUgly(0)) // false
    println(isUgly(1)) // true
    println(isUgly(2)) // true
    println(isUgly(3)) // true
    println(isUgly(5)) // true
    println(isUgly(1200)) // true
    println(isUgly(625)) // true
    println(isUgly(625 * 2003)) // false
    countToNumber(10000)
    println(reverseString("scala"))
    println(reverseIntegerCheat(123456))
    // test reverse number
    println(reverseNumber(1234)) // 4321
    println(reverseNumber(2)) // 2
    println(reverseNumber(0)) // 0
    println(reverseNumber(-1234))
    println(reverseNumber(1000000009))

  }
}
