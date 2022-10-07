package practice

import scala.annotation.tailrec
import scala.util.Try

object FinalExercises {

  // TODO 1. Calculate the number of occurrences of 3 in a number
  def nDigits(n: Int): Int = {
    if (n == 0) 0
    else if (n % 10 == 3) 1 + nDigits(n / 10)
    else nDigits(n / 10)
  }

  def nDigits_v2(n: Int): Int = {
    @tailrec
    def aux(in: Int, acc: Int): Int = {
      if (in == 0) acc
      else if (in % 10 == 3) aux(in / 10, acc + 1)
      else aux(in / 10, acc)
    }
    aux(n, 0)
  }

  // TODO 2. Find how many words there are in a string (separated by space)
  // nWords("Scala") = 1
  // nWords("") = 0
  // nWords("Scala is awesome") = 3
  // nWords("526378cs ahfkj ;;.//./") = 3
  // nWords("      Scala       is        awesome      ") = 3
  def nWords(string: String): Int =
    if (string.isEmpty) 0
    else string.split(" ").count(_.nonEmpty)

  // TODO 3. Create a Person class with first name, surname, birth date (string), salary (int) and job title (String).
  //  Make a function to parse a CSV string of the form "firstname lastname, 1986-01-01, 30000, clerk" and return a Person.
  //  Advanced: write some code so that you can say val daniel = Person("...")
  case class Person(firstName: String, lastName: String, dob: String, salary: Int, jobTitle: String)

  object Person {
    def fromCSV(csv: String): Person = {
      val tokens = csv.split(",").map(_.trim) // [fn ln, dob, sal, jt]
      val name = tokens(0).split(" ").filter(_.nonEmpty)
      Person(name(0), Try(name(1)).getOrElse(""), tokens(1), Try(tokens(2).toInt).getOrElse(0), tokens(3))
    }
  }

  // TODO 4. Add methods to LList
  //  - mkString
  //  - sort

  // TODO 5. Make LList covariant, fix all problems

  // TODO 6. Ascii art mini-project

  def main(args: Array[String]): Unit = {
    println(nDigits_v2(333331212))
    println(nWords("Scala"))
    println(nWords(""))
    println(nWords("Scala is awesome"))
    println(nWords("526378cs ahfkj ;;.//./"))
    println(nWords("      Scala       is        awesome      "))
    println(Person.fromCSV("Daniel Ciocirlan, 1901-01-01, hsjdkf, Scala dev"))
  }
}
