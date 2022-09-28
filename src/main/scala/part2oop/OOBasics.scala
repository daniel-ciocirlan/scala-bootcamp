package part2oop

object OOBasics {

  /*
    Python:
      class Person(name, age):
        body of the class

    Java:
      class Person {
        public Person(String name, int age) {}
      }
   */
  // classes
  class Person(name: String, val /* <-- this makes the constructor argument a FIELD */ age: Int) { // <- constructor of the class
    // body of the class: fields, functions, other classes etc
    val nameInAllCaps = name.toUpperCase() // field

    // functions aka METHODS
    def greet(): String =
      s"Hi everyone, I am $name!"

    // OVERLOADING = multiple methods with the same name but different argument lists
    def greet(someone: String): String =
      s"Hi $someone, I'm $name, how are you?"

    def greet(person1: String, person2: String) =
      s"Hi $person1 and $person2, I'm $name, how are you?"
  }

  // instance of a class = value (with real fields stored in memory)
  val daniel = new Person("Daniel", 99) // instantiating a class
  // [ ____ ____ ____ ] <- person
  //  ^^ fields of the class Person
  val danielAllCaps = daniel.nameInAllCaps // use the dot notation instance.field to access a field
  val danielsGreeting = daniel.greet()
  val danielGreetsGary = daniel.greet("Gary")
  val danielGreets2People = daniel.greet("Gary", "Germans")
  // cannot access constructor arguments outside the class... UNLESS we make them fields (by adding a 'val' in front of them)
  val danielsAge = daniel.age

  /*
    Exercises
    1. Create a Novel class and a Author class
      - Author:
        fields: first name, last name, year of birth
        methods: full name
      - Novel: name, year of release, author
        methods: authorAge, isWrittenBy(potentialAuthor), copy(newYearOfRelease) - builds a new instance of Novel with a new year of release
   */
  class Author(val firstName: String, val lastName: String, val yearOfBirth: Int) {
    def fullName(): String =
      s"$firstName $lastName"

    // (from the overriding methods section)
    override def equals(obj: Any) = // different functionality than its parent class
      if (!obj.isInstanceOf[Author]) false
      else {
        val other: Author = obj.asInstanceOf[Author]
        this.firstName == other.firstName &&
          this.lastName == other.lastName &&
          this.yearOfBirth == other.yearOfBirth
      }
  }

  class Novel(val name: String, val author: Author, val yearOfRelease: Int = 2022) {
    def authorAge(): Int =
      yearOfRelease - author.yearOfBirth

    def copy(newYearOfRelease: Int): Novel = {
      val newEdition = new Novel(name, author, newYearOfRelease)
      newEdition
    }

    def isWrittenBy(potentialAuthor: Author): Boolean = {
      potentialAuthor == author // internally calls potentialAuthor.equals(author)

      // data equality done manually
      //      if (potentialAuthor.firstName == author.firstName &&
      //        potentialAuthor.lastName == author.lastName &&
      //        potentialAuthor.yearOfBirth == author.yearOfBirth) true else false
    }
  }

  // multiple constructors
  class MultiNamedPerson(val fn: String, val mn: String, val ln: String) { // <- primary class constructor
    // secondary constructor
    def this(fn: String, ln: String) =
      this(fn, "", ln)
  }

  /*
    Exercise: create a counter class
    - hold a "count" value
    - is instantiated with an initial count value

    methods:
      - increment(), returns a new counter with an incremented count
      - decrement() ... decremented count
      - increment(n), returns a new counter with a count + n
      - decrement(n)
   */
  class Counter(val count: Int) {
    def increment(): Counter = new Counter (count + 1)
    def decrement(): Counter = new Counter(count - 1)
    def increment(n: Int): Counter = new Counter(count + n)
    def decrement(n: Int): Counter = new Counter(count - n)
  }


  def main(args: Array[String]): Unit = {
    val jkr = new Author("JK", "Rowling", 1900)
    val novel = new Novel("Harry Potter", jkr, 1999) // name = "Harry Potter", author = jkr
    val latestHP = new Novel("Harry Potter", jkr)
    // test Novel methods
    val futureNovel = novel.copy(2022)
    val jkr_v2 = new Author("JK", "Rowling", 1900) // different instance than jkr, even though it has the same data
    println(novel.isWrittenBy(jkr))
    println(novel.isWrittenBy(jkr_v2)) //  jkr_v2 is a different instance i.e. jkr_v2 has another reference/pointer


    // test secondary constructors
    val mnp = new MultiNamedPerson("Daniel", "C", "Ciocirlan")
    val mnp_v2 = new MultiNamedPerson("Daniel", "Ciocirlan")

    // test counter
    val counter = new Counter(1)
    println(counter.count)
    val newCounter = counter.increment(10) // returns a NEW instance!
    println(newCounter.count)

    val greatExpectations = new Novel("Great Expectations", new Author("Charles", "Dickens", 1865), 2022)
    //                                                      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    //                                                           an instance of Author
  }
}

