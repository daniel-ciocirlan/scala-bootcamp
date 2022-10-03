package part4patternmatching

import practice._

object AllThePatterns {

  // 1 - constants
  val aNumber = 45
  val testingAgainstNumberConstants = aNumber match {
    case 1 => "first"
    case 2 => "second"
    case _ => "everyone else"
  }
  // can also test against chars, booleans, ints, doubles, any numerical value

  // Strings are also constants
  val someString = "Scala"
  val testingAgainstStringConstants = someString match {
    case "scala" => "lower scala"
    case "SCALA" => "upper scala"
    case _ => "can't understant"
  }

  // objects are constants
  object MySingleton

  // 2 - match anything
  val matchAnythingWithName = aNumber match {
    case 1 => "one"
    case 2 => "two"
    case nameThisAnything => s"a number: $nameThisAnything"
    //   ^ "match anything, and name that n"
  }

  val matchAnything = aNumber match {
    case _ => "anything"  // _ = wildcard
  }

  // 3 - case classes
  case class Phone(make: String, model: String)
  val aPhone = Phone("Apple", "iPhone 15")
  val phoneMatch = aPhone match {
    case Phone(m, mo) => s"got a phone: $m $mo"
  }

  // tuples are case classes
  val aTuple = (1,4)
  val matchTuple = aTuple match {
    // put the stricter pattern first
    case (1, _) => "a tuple with 1 as its first field"
    //       ^ matches anything
    case (a, b) => s"aTuple with two members: $a $b"
    //    ^  ^ patterns that match anything
  }

  // our own linked list is eligible for PM
  val myList: LList[Int] = NonEmpty(1, NonEmpty(2, NonEmpty(3, Empty())))
  val listPM = myList match {
    case Empty() => "empty list"
    case NonEmpty(1, t) => s"list starting with 1, ending with $t"
  }

  // 4 - regular lists
  val aRegularList = List(1,2,3,42)
  val regularListPM = aRegularList match {
    case List(1,_,_,42) => "list starting with 1, ending with 42, with EXACTLY 4 elements"
    case List(1,_*) => "list starting with 1, arbitrary length"
    // ^^^ matches List(1), List(1,2), List(1,4,5,7,2,3,6,7,3,64,643,6)
    case h :: t => s"list deconstructed into $h and $t"
  }

  // 5 - type specifiers
  val unknown: Any = 54
  val matchAny = unknown match {
    case anInt: Int => anInt + 4
    case aString: String => aString.length // can call this variable's methods/fields, the compiler knows its type
    case _: Double => 0
  }

  // 6 - binding names to patterns
  val listPMBind = myList match {
    case NonEmpty(1, t @ NonEmpty(2, NonEmpty(_, _))) => s"list starting with 1,2, tail sum is ${t.reduce(_ + _, 0)}"
    //               ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ this piece is named `t`
  }

  // 7 - if guards
  val listPMCondition = myList match {
    case NonEmpty(h, _) if h < 5 => "list starting with something small"
  }

  // 8 - chained patterns
  val multiMatch = myList match {
    case Empty() | NonEmpty(1, _) => "an empty list to my eyes"
    // useful when you return the same expression for multiple cases
  }

  // advice: don't use PM for boolean checks
  val simpleInt = 45
  val evenCheck = simpleInt match {
    case n if n % 2 == 0 => true
    case _ => false
  }

  val rewritten = simpleInt % 2 == 0

  // beware: type guards don't work with generics!
  // type erasure (JVM limitation)
  // PM is done at RUNTIME, where NO GENERICS ARE KNOWN!
  val numbers: List[Int] = List(1,2,3,4)
  val numbersMatch = numbers match {
    case _: List[String] => "list of strings"
    case _: List[Int] => "list of ints"
    case _ => "something else"
  }

  def main(args: Array[String]): Unit = {
    println(numbersMatch)
  }
}
