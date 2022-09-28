package part2oop

object AbstractDataTypes {

  // abstract classes = classes where you can have fields or methods WITHOUT an implementation
  abstract class Animal { // "blueprint" of how animals should behave, every animal behaves in its own specific way
    //  "interface"
    val creatureType: String // no implementation/value = abstract
    def eat(): Unit // abstract method = just the signature
    //       ^^^^^^ specifying the return type is mandatory

    // non-abstract fields/methods
    def preferredMeal: String = "anything"
  }

  // abstract classes CANNOT be instantiated
  // val anAnimal = new Animal // not allowed

  // define a type + signatures of functions/methods, the implementation is unknown
  class Dog extends Animal {
    // have to define the abstract fields/methods
    override val creatureType = "domestic"
    override def eat(): Unit = println("chewing a bone")
    // plus your own methods
    def chaseBurglar(): Unit = println("Bark!")

    // plus you can override other methods (non-abstract)
    // can override a parameterless method with a FIELD
    override val preferredMeal: String = "bones" // benefit: the field is not reevaluated every time it's referred
    //                        ^^^^^^^^ can return a different type, AS LONG AS it's a subtype of the original method's return type
  }

  val anAnimal = new Dog
  val preferredThingToEat = anAnimal.preferredMeal

  // traits similar to abstract classes
  trait Carnivore { // supposed to have only abstract fields/methods
    def eat(animal: Animal): Unit
  }

  // traits can be extended
  class TRex extends Carnivore {
    override def eat(animal: Animal): Unit = println("I'm a T-Rex, I eat animals!")
  }

  // can extend MULTIPLE traits
  trait ColdBlooded
  class Crocodile extends Animal with Carnivore with ColdBlooded { // can extend A SINGLE class but multiple traits
    override val creatureType = "croc"
    override def eat(): Unit = println("I'm a croc, I just crunch stuff")
    override def eat(animal: Animal): Unit = println("croc eating animal")
  }

  def main(args: Array[String]): Unit = {

  }
}
