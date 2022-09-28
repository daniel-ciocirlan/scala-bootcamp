package part2oop

object Generics {

  // reuse code on different types
  abstract class IntList {
    def head: Int
    def tail: IntList
  }
  class EmptyIntList extends IntList {
    override def head = throw new NoSuchElementException
    override def tail = throw new NoSuchElementException
  }
  class NonEmptyIntList(override val head: Int, override val tail: IntList) extends IntList

  // option 1: copy it to offer the same functionality for strings
  abstract class StringList {
    def head: String
    def tail: StringList
  }
  class EmptyStringList extends StringList {
    override def head = throw new NoSuchElementException
    override def tail = throw new NoSuchElementException
  }
  class NonEmptyStringList(override val head: String, override val tail: StringList) extends StringList

  // 1. tedious
  // 2. copy EVERY time you need to support a new type
  // 3. new code => new copies
  // BOOM!!!!

  // option 2: change the data type
  abstract class GeneralList {
    def head: Any
    def tail: GeneralList
  }
  class EmptyGeneralList extends GeneralList {
    override def head = throw new NoSuchElementException
    override def tail = throw new NoSuchElementException
  }
  class NonEmptyGeneralList(override val head: Any, override val tail: GeneralList) extends GeneralList
  // 1. you lost TYPE SAFETY - make no assumptions about the elements inside
  // 2. lists can now be heterogeneous - can store ints, strings, crocodiles and network packets in the same list

  // option 3: declare a GENERIC type
  abstract class MyList[A] {
    def head: A
    def tail: MyList[A]
  }

  class MyEmpty[A] extends MyList[A] {
    override def head = throw new NoSuchElementException
    override def tail = throw new NoSuchElementException
  }

  class MyNonEmpty[A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  val numbers = new MyNonEmpty[Int](/* can only work with integers! */1, new MyNonEmpty(2, new MyEmpty[Int]))
  val firstNumber: Int = numbers.head
  val strings = new MyNonEmpty[String]("Scala", new MyNonEmpty("is", new MyNonEmpty("awesome", new MyEmpty)))
  val firstString: String = strings.head

  class MyDictionary[K, V]
  val dictionary = new MyDictionary[String, Int]

  // can add generic type args to traits
  trait Predicate[A] {
    def test(value: A): Boolean
  }

  // generic methods
  def buildListFrom2Elements[A](first: A, second: A): MyList[A] =
    new MyNonEmpty[A](first, new MyNonEmpty(second, new MyEmpty))

  val twoNumbers = buildListFrom2Elements(1,2)
  val twoStrings = buildListFrom2Elements("Scala", "generics")

  class Animal
  class Dog extends Animal
  class Cat extends Animal
  val twoAnimal = buildListFrom2Elements(new Dog, new Cat) // compiler finds the lowest common ancestor of the types of the args
  val twoThings = buildListFrom2Elements(2, "things") // no common ancestor => A == Any
  // val twoNumbers_v2 = buildListFrom2Elements[Int](100, "200")
  //                                           ^^^^^ specifying the type argument makes the compiler check the arguments against their appropriate types

}
