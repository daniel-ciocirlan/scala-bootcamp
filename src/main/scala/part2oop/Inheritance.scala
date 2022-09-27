package part2oop

object Inheritance {

  class Animal {
    val creatureType = "wild"
    def eat(): Unit = println("nomnomnom")
  }

  // can only inherit from ONE class
  class Cat extends Animal {// Cat "inherits from" Animal, Cat "is a subtype of" Animal, "IS AN" Animal
    // can define its own methods/fields besides what Animal has
    def crunch(): Unit = {
      eat()
      println("crunched my food!")
    }
  }

  val cat = new Cat

  // overriding
  class Dog extends Animal {
    // override fields or methods = give them other implementations
    override val creatureType = "domestic"
    override def eat(): Unit = println("mmm I like this bone!")

    // popular overridden method: toString (for nice printing of your own data types)
    override def toString = "a dog"

    // popular overridden method: equals
    override def equals(obj: Any) = obj.isInstanceOf[Dog] // boolean
  }

  class Dinosaur extends Animal {
    override val creatureType = "extinct"
  }

  // Any -> AnyRef -> Object is the parent of all "classes"
  val dog = new Dog
  // println(dog) // calls .toString internally

  // inheritance from a class with constructor args
  class Person(val name: String, val age: Int) {
    // protected = access for the class and just the sub-classes
    protected def sayHi(): String = s"Hi, my name is $name." // available for just the class Person and all subtypes
    // private = access JUST for the class, means it will not be inherited!
    // benefit: keep the relevant code for this class just inside this class
    private def watchYouTube(): String = "I'm binge watching on YouTube."

    /* cannot be overridden -> */ final def enjoyLife(): Int = 42
  }

  class Adult(name: String, age: Int, idCard: String) extends Person(name, age)
  // in order to build an Adult(name, age, idCard) I need to build a Person(name, age)

  // further inheritance down the line
  /* prevents inheritance -> */final class Elder(name: String, age: Int, idCard: String, isRetired: Boolean) extends Adult(name, age, idCard)

  val daniel = new Adult("Daniel", 99, "rockthejvm")
  /*
    new Person("Daniel", 99)                         ["Daniel", 99, ______ ]
    new Adult("Daniel", 99, "rockthejvm" -----------------------------^ goes here
   */

  //    preventing overriding
  // 1. use 'final' on fields/methods, prevents them being overridden
  // 2. use 'final' on a class, prevents inheritance
  // 3. 'sealed' allows a type hierarchy to be defined IN THIS FILE ONLY, inheritance is forbidden outside
  sealed class Guitar(nStrings: Int)
  class ElectricGuitar(nStrings: Int) extends Guitar(nStrings)
  class MetalGuitar extends ElectricGuitar(8)
  class AcousticGuitar extends Guitar(6)

  // "polymorphism"
  val lassie: Animal = new Dog // compiles because Dog is an Animal
  lassie.eat() // method from the class Dog will be called!
  // the most overridden methods will always be called

  /*
    Animal { eat() ... }
    Dog { override eat() ... }
    Puppy
    GoldenRetrieverPuppy

    val grp = new GoldenRetrieverPuppy
    grp.eat() // the method from Dog will be called
   */


  def main(args: Array[String]): Unit = {
    val anotherDog = new Dog
    println(dog == anotherDog) // dog.equals(anotherDog)

  }
}
