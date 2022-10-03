package part4patternmatching

import scala.util.Random

object PatternMatching {

  val aValue = Random.nextInt(100) // generates a random value between 0 and 100 (exclusive)

  // equivalent of switch(aValue)
  val description = aValue match { // Pattern Matching
    case 1 => "the first" // expression
    case 2 => "the second"
    case 3 => "the third"
    case _ => s"something else: $aValue"
  }

  /*
    if (aValue == 1) "the first"
    else if (aValue == 2) "..."
    else ...
    else s"something else: $aValue"
   */
  // patterns are tested in sequence
  // if no pattern matches, the PM expression will throw a MatchError

  // deconstruct a value into constituent parts
  case class Person(name: String, age: Int)

  val bob = Person("Bob", 24)

  val greeting = bob match {
    case Person(n, a) => s"Hello there, I'm $n and asking for my age is inappropriate."
    //   ^^^^^^^^^^^^ PATTERN
    // "if bob matches the _pattern_ described by Person, let n and a be its fields."
    case _ => "I'm nobody."
  }

  // most common use case: deconstruct a (sealed) hierarchy of classes
  sealed class Animal

  case class Dog(breed: String) extends Animal

  case class Cat(meowStyle: String) extends Animal

  def fetchAnimal(): Animal = // someone else wrote this method and you can only call it
    Dog("Terra Nova")

  val animal: Animal = fetchAnimal()
  val animalPM = animal match {
    case Dog(someBreed) => s"I've detected a dog of breed $someBreed"
    case Cat(meow) => s"Cat detected, brace for meow $meow"
  }

  /*
    Exercise.
   */
  sealed trait Expr
  case class Num(n: Int) extends Expr
  case class Sum(e1: Expr, e2: Expr) extends Expr
  case class Prod(e1: Expr, e2: Expr) extends Expr
  def show(expression: Expr): String = expression match {
    case Num(n) => s"$n"
    case Sum(e1, e2) => s"${show(e1)} + ${show(e2)}"
    case Prod(e1, e2) =>
      def maybeWithParentheses(exp: Expr): String = exp match {
        case Num(n) => s"$n"
        case Sum(s1, s2) => s"(${show(s1)} + ${show(s2)})" // important: wrap sums in () when embedded in Prod
        case _ => show(exp)
      }
      s"${maybeWithParentheses(e1)} * ${maybeWithParentheses(e2)}"
  }

  // DRY: don't repeat yourself

  def main(args: Array[String]): Unit = {
    val five = Sum(Num(1), Num(4))
    val eight = Sum(five, Num(3))
    val twelve = Prod(Num(3), Num(4))
    val sixteen = Prod(Prod(Num(2), Num(2)), Num(4))
    val ten = Sum(Prod(Num(2), Num(3)), Num(4))
    val twenty = Prod(Sum(Num(2), Num(3)), Num(4))

    println(show(five)) // "1 + 4"
    println(show(eight)) // "1 + 4 + 3"
    println(show(twelve)) // "3 * 4"
    println(show(sixteen)) // 2 * 2 * 4
    println(show(ten)) // 2 * 3 + 4
    println(show(twenty)) // (2 + 3) * 4
  }
}
