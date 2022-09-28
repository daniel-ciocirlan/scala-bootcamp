package part2oop

object AnonymousClasses {

  abstract class Animal {
    def eat(): Unit
  }

  // a special animal whose eat() prints "GROWL!!!"
  class SpecialAnimal extends Animal {
    override def eat(): Unit = println("GROWL!!!") // just for one animal
  }

  // only useful for a single animal
  // option 1: keep it a class, instantiate it
  val specialAnimal = new SpecialAnimal
  // option 2: make SpecialAnimal an object (single instance)

  // option 3: instantiate Animal on the spot, for THAT particular value
  val specialAnimal_v2 = new Animal { // anonymous class
    override def eat(): Unit = println("GROWL!!!")
  }
  /*
    Mechanism:
    1. compiler creates a synthetic class Animal$Anonymous$32764832 extends Animal {
        override def eat(): Unit = println("GROWL!!!")
      }
    2. instantiate that class
    3. assign that instance to specialAnimal_v2

    works for classes and traits
    useful for assigning custom class instances to A SINGLE value; it doesn't make sense to add a fresh new class
   */

  // works on non-abstract classes too
  class Person(name: String) {
    def sayHi(): String = s"I'm $name."
  }

  val daniel = new Person("Daniel") {
    override def sayHi() = "Rock the JVM!"
  }


  def main(args: Array[String]): Unit = {

  }
}
