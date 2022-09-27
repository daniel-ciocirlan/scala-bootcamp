package part2oop

object MethodNotations {

  class Person(val name: String, val age: Int, val favoriteMovie: String) {
    def likes(movie: String): Boolean =
      favoriteMovie == movie

    // very permissive method naming!
    // not allowed: ':' (anywhere), '_' (allowed inside the name), (), {}, [], "=>", '`'
    def 😘(): String = s"$name says: xoxo"

    def +(person: Person): String =
      s"$name is hanging out with ${person.name}"

    // overloading
    def +(title: String): Person =
      new Person(s"$title $name", age, favoriteMovie)

    // prefix notation (for + - ! ~)
    def unary_- : Person =
      new Person(name + " Zombie", -age, favoriteMovie)
    def unary_~ : Person =
      new Person(name + " with curly hair", age, favoriteMovie)

    // apply has special meaning, makes an instance "invokable"
    def apply(): String =
      s"Hi, my name is $name and I really enjoy $favoriteMovie"
    def apply(n: Int): String =
      s"I've watched $favoriteMovie $n times"
  }

  val mary = new Person("Mary", 34, "Inception")
  val john = new Person("John", 36, "Fight Club")
  val movie = "Interstellar"

  val maryAndInterstellar = mary.likes(movie)
  val maryAndInterstellar_v2 = mary likes movie // same thing!
  //                           ^^^^^^^^^^^^^^^^ infix notation (available for methods with ONE argument)
  // syntactic sugar: nicer syntax that does the same thing as another syntax

  val maryAndJohn = mary.+(john)
  val maryAndJohn_v2 = mary + john
  val drMary = mary + "Dr."

  // all operators in Scala are actually methods!
  val three = 1 + 2
  //            ^ method in infix notation!
  val three_v2 = 1.+(2) // same thing

  // unary operators (only for -, !, +, ~)
  val maryZombie = -mary // mary.unary_- (translated automatically by the compiler)
  val maryWithCurlyHair = ~mary // mary.unary_~

  // testing apply
  val maryGreeting = mary.apply()
  val maryGreeting_v2 = mary() // same as mary.apply()
  val maryWatchTime = mary.apply(10)
  val maryWatchTime_v2 = mary(10) // same as mary.apply(10)

  def main(args: Array[String]): Unit = {
    println(maryWithCurlyHair.😘)
  }
}
