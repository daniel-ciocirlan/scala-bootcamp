package part3fp

import scala.util.Random

object Collections {

  case class Person(name: String, age: Int) // case class, so we have equals + hashCode

  def demoLists(): Unit = {
    // List: linked list
    val aList = List(1,2,3,4) // [1,2,3,4] \
    // fundamental methods: head, tail
    println(aList.head)
    println(aList.tail)
    // adding new elements to a list
    val largerList = aList.::(0) // :: is the 'add' method
    val largerList_v2 = 0 :: aList // same thing
    //                    ^^ infix notation, weird order*

    // concatenating
    val anotherList = List(5,6,7)
    val biggerList = aList ++ anotherList
    // map, flatMap, filter
    val anIncrementedList = aList.map(_ + 1) // [2,3,4,5]
    val flatMappedList = aList.flatMap(x => List(x, x + 1)) // [1,2,2,3,3,4,4,5]
    val filteredList = aList.filter(_ < 3) // [1,2]
    // for comprehensions
    val combinedList = for {
      x <- aList
      y <- anotherList
    } yield x + y // [6,7,8,7,8,9,8,9,10,9,10,11]
    // utility methods
    val emptyCheck = aList.isEmpty
    val reversedList = aList.reverse // [4,3,2,1]
    val sum = aList.reduce(_ + _) // 10
    aList.foreach(x => println(x))
    val thirdNumber = aList(2) // slow
  } // the most useful.

  def demoSeq(): Unit = {
    // Seq = trait for general type of sequential collections
    val aSeq = Seq(1,2,3,4) // Seq.apply(1,2,3,4)

    // main property: access an element at an index
    val thirdElement = aSeq(2) // sequences are 0-indexed
    // map, flatMap, filter, reduce, isEmpty, for comprehensions, reverse
  }

  def demoRanges(): Unit = {
    // fictitious "collection" that only counts without storing all the elements inside
    val aRange = 1 to 1000 // range(1,10)
    aRange.foreach(x => println(x))
  }

  // vectors = fast Seq implementations
  // perform well with LOTS of data
  def demoVectors(): Unit = {
    val aVector: Vector[Int] = Vector(1,4,6,23,6,7,7)
    // same Seq API: apply, reverse, reduce, map, flatMap, filter, foreach
  }

  // sets = collections with NO DUPLICATES
  def demoSets(): Unit = {
    val aSet = Set(1,2,3,4,2,3,1,4,5,6) // set does not allow duplicates, order is not well-defined
    val daniel = new Person("Daniel", 99)
    val daniel_2 = new Person("Daniel", 99)
    val people = Set(daniel, daniel_2)
    // lesson: store instances of case classes in sets
    // fundamental methods
    val is2intheset = aSet.contains(2) // true
    val is2intheset_v2 = aSet(2) // same as apply, which does the same thing as contains
    val biggerSet = aSet + 7 // adds 7 to the set, returning a NEW set
    val smallerSet = aSet - 2 // removes 2, returning a NEW set
    // "concatenation" = union
    val anotherSet = Set(4,5,6,7,8)
    val combinedSet = aSet ++ anotherSet // [1,2,3,4,5,6,7,8]
    val combinedSet_v2 = aSet.union(anotherSet) // [1,2,3,4,5,6,7,8]
    val combinedSet_v3 = aSet | anotherSet // [1,2,3,4,5,6,7,8]
    // difference
    val difference = aSet -- anotherSet // [1,2,3]
    val difference_v2 = aSet.diff(anotherSet) // [1,2,3]
    // intersection = find the common elements
    val intersection = aSet & anotherSet // [4,5,6]
    val intersection_v2 = aSet.intersect(anotherSet) // [4,5,6]
  }

  def main(args: Array[String]): Unit = {
    demoSets()
  }
}
