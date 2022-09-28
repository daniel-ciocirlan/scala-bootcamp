package part2oop

object ObjectsAndCompanions {

  object Singleton { // type + the only possible instance of this type
    // fields + methods
    val aField = 45
    def aMethod(x: Int) = x + 1
  }

  // companions = class + object with the same name in the same file
  class Person(name: String) {
    // fields or methods related to a particular INSTANCE of a Person
    def sayHi(): String = s"Hi, my name is $name"
  }

  object Person { // companion object
    // place fields and methods that DO NOT DEPEND ON A PARTICULAR INSTANCE of Person
    val N_EYES = 2
    def canFly: Boolean = false
  }

  val mary = new Person("Mary")


  def main(args: Array[String]): Unit = {
    println(mary.sayHi())
    println(Person.canFly) // false for all Person instances
  }
}
