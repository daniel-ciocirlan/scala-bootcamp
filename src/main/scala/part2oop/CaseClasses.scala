package part2oop

object CaseClasses {

  case class Person(name: String, age: Int) {
    // fields, methods, code...
  }

  /*
    automatically created:

    object Person {
      def apply(name: String, age: Int) = new Person(name, age)
    }
   */

  // 1 - class constructor args are automatically promoted to FIELDS
  val daniel = new Person("Daniel", 104)
  val danielsAge = daniel.age

  // 2 - toString, equals (data equality), hashCode automatically implemented
  val danielString = daniel.toString
  val daniel_2 = new Person("Daniel", 104)
  val sameDaniel = daniel == daniel_2

  // 3 - utility methods
  val danielYounger = daniel.copy(age=24) // new instance of Person with the same fields EXCEPT age=24

  // 4 - companion objects automatically created
  val daniel_3 = Person("Daniel", 104) // without the 'new', same as Person.apply(...)

  // 5 - serializable = can be turned into bytes to be read by other JVMs

  // 6 - can be deconstructed easily with pattern matching***

  /*
   restrictions:
   - cannot inherit between case classes
   - cannot make a case class abstract
   */

  case object UnitedKingdom { // less useful
    // add any other fields/methods
    def name: String = "The UK of GB and NI"
  }

  def main(args: Array[String]): Unit = {
  }
}
