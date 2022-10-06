package part5advanced

object Variance {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  // variance question for the List type: if Dog extends Animal, should List[Dog] "extend" List[Animal]?
  // YES => we call the List type COVARIANT
  val hachi = new Dog
  val lassie = new Dog
  val someDogs: List[Animal] = List(lassie, hachi)

  // define covariant types
  class MyList[+A] // MyList is COVARIANT in A
  val myAnimalList: MyList[Animal] = new MyList[Dog] // ok

  // answer NO => we call Combiner INVARIANT
  trait Combiner[A] {
    def combine(x: A, y: A): A
  }

  // HELL NO => we call Vet CONTRAVARIANT
  // if Dog "extends" Animal, Vet[Animal] "extends" Vet[Dog]
  class Vet[-A] {
    def heal(animal: A): Boolean = true
  }
  val aVet: Vet[Dog] = new Vet[Animal]
  // intuition: if this vet can treat any Animal, she/he can treat my dog too
  val healedHachi = aVet.heal(hachi)

  /*
    Rules of thumb:
    - if your type PRODUCES or RETRIEVES values, make it COVARIANT (e.g. a list)
    - if your type CONSUMES or ACTS ON values, make it CONTRAVARIANT (e.g. a vet)
    - otherwise, INVARIANT
   */

  class RandomGenerator[+A] // covariant: produces elements
  class JSONSerializer[-A] // contravariant: consumes values to turn them into Strings
  class MyOption[+A] // covariant: retrieves value
  class Transformer[-A, +B]

  abstract class NewList[+A] {
    def head: A
    def tail: NewList[A]
  }

  case object Empty extends NewList[Nothing] { // <-- this thing is big
    override def head = throw new NoSuchElementException()
    override def tail = throw new NoSuchElementException()
  }

  case class NonEmpty[+A](h: A, t: NewList[A]) extends NewList[A] {
    override def head = h
    override def tail = t
  }

  val emptyList: NewList[Int] = Empty

  /*
    Type bounds
   */
  class Car
  class Supercar extends Car
  class Minivan extends Car

  class Garage[A <: Car] // <: means "must be a subtype of"
  val aGarage = new Garage[Minivan] // ok, Minivan extends Car
  val aGarage_2 = new Garage[Car] // ok
  //  val illegalGarage = new Garage[Dog] // not ok, because Dog is not a subtype of Car

  class Container[A >: Minivan] // >: means "must be a supertype of"
  val container = new Container[Minivan]
  val container2 = new Container[Car] // ok because Car >: Minivan
  val container3 = new Container[Any] // ok, because Any >: Minivan
  // val illegalContainer = new Container[Supercar] // not ok, because Supercar is not related to Minivan

  /*
    Variance positions
   */
  // class Vet2[-A](val favoriteAnimal: A) // <-- fields are in COVARIANT POSITION
  /*
    val garfield = new Cat
    val vet: Vet2[Animal] = new Vet[Animal](garfield) // I can build a Vet[Animal] with an Animal as a constructor arg (garfield)
    val dogVet: Vet2[Dog] = vet // I can assign vet as a Vet2[Dog], because this vet can treat my dog
    val aDog = dogVet.favoriteAnimal // should be a Dog, BUT is actually a Cat (PROBLEM!)
   */

  // method arg types are in CONTRAVARIANT POSITION
  //  class MyList2[+A] {
  //    def add(element: A): MyList2[A] = new MyList2[A]
  //  }
  /*
    val animals: MyList2[Animal] = new MyList2[Cat] // possible because MyList2 is covariant
    val biggerListOfAnimals = animals.add(hachi) // I'm adding an Animal (because animals: MyList2[Animal]) BUT I'm adding a dog to a list of cats (PROBLEM!)
   */

  // how to overcome the "method arg type is in contravariant position for a covariant class"
  class MyList2[+A] {
    def add[B >: A](element: B): MyList2[B] = new MyList2[B] // widening the type
    // let the compiler infer what the type B should be, which correctly describes the argument AND the existing type A
    // "adding a dog to a list of cats => list of animals"
  }

  // method return types are in COVARIANT POSITION
  /*
    abstract class Vet3[-A] {
      def rescueAnimal(): A
    }

    val vet: Vet3[Animal] = new Vet3[Animal] {
      def rescueAnimal(): Animal = new Cat // can return a Cat because the method says I can return an Animal
    }
    val hachisVet: Vet3[Dog] = vet // can do that because Vet3 is contravariant
    val hachiRescued = hachisVet.rescueAnimal // should return a Dog (by the type signature) BUT it returns a Cat (PROBLEM!)
   */
  class Vet3[-A] {
    def rescueAnimal[B <: A](animal: B): B = animal // narrowing the type
  }

  val garfield = new Cat
  val hachisVet: Vet3[Dog] = new Vet3[Animal] // contravariant
  // val rescuedAnimal = hachisVet.rescueAnimal(garfield) // can't call this, as Cat is not a subtype of Dog, only Dogs allowed

  def main(args: Array[String]): Unit = {

  }
}
