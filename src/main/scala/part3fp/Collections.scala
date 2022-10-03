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

  def demoConversions(): Unit = {
    val aList = List(1,2,3,4,1,2,3)
    val set = aList.toSet // removes duplicates
    val uniqueElementsAsList = set.toList

    // .toList, .toSet, .toVector, .toSeq between any two collections
  }

  def demoTuples(): Unit = {
    val aTuple: (Int, String) = (2, "rock the jvm")
    //          ^^^^^^^^^^^^^ same as Tuple2[Int, String]
    val firstField = aTuple._1
    val copiedTuple = aTuple.copy(_1 = 54) // all other fields are the same, the first is 54

    // syntax sugar for tuples of TWO elements
    val aTuple_v2 = 2 -> "rock the jvm" // useful for "associations"
    println(aTuple_v2)
  }

  def demoMaps(): Unit = {
    // "objects" in JS: key-value associations
    // "dicts" in Python
    val aMap: Map[String, Int] = Map() // Map.apply()
    val phonebook: Map[String, Int] = Map(
      "Jim" -> 123,
      "Alice" -> 456,
      "Harry Potter" -> 999
    )

    // core APIs
    // test whether a key is in the map
    val phonebookHasHarryPotter = phonebook.contains("Harry Potter") // true or false
    val hpNumber = phonebook("Harry Potter") // same as .apply... returns the value associated to this key
    // beware .apply() crashes if the key is not in the map, make sure to check if it's contained

    // add new association
    val phonebookWithDaniel = phonebook + ("Daniel" -> 900) // returns a new map, will overwrite if the key is in the map
    // remove a key
    val phonebookWithMuggles = phonebook - "Harry Potter" // removes the association of that key, returns a new map

    // conversions
    val listOfTuples = phonebook.toList // returns a list of tuples
    // can convert a list of TWO-element tuples to a map
    val linearPhonebook = List(
      "Jim" -> 123,
      "Alice" -> 456,
      "Harry Potter" -> 999
    )
    val phonebook_v2 = linearPhonebook.toMap // works for TWO element tuples only.

    // getting keys
    val names = phonebook.keySet // returns a Set
    // mapping values
    val newPhonebook = phonebook.view.mapValues(_ * 10).toMap // clunky
    // filtering keys
    val phonebookWithLongNames = phonebook.view.filterKeys(_.length > 5).toMap
    // filter
    val phonebookInArea = phonebook.filter(tuple => tuple._2 % 10 == 9)

    // group elements from a list by a function
    val namesList = List("Bob", "Angela", "James", "Mary", "David", "Alice")
    val nameGroupings = namesList.groupBy(_.charAt(0))
    /*
      B -> ["Bob"]
      A -> ["Angela", "Alice"]
      J -> ["James"]
      M -> ["Mary"]
      D -> ["David"]
     */

    println(namesList.groupBy(_.length))
  }

  /*
    Social network = Map[String, Set[String]]
    MUTUAL relationships

    Alice -> Set[Bob, Mary, Jane]
    Bob -> Set[Alice, Mary]
    Jane -> Set[Alice, David]
    David -> Set[Jane]
    Mary -> Set[Alice, Bob]
    Superman -> []
   */
  def demoSocialNetwork(): Unit = {
    val initialNetwork: Map[String, Set[String]] = Map(
      "Alice" -> Set("Bob", "Mary", "Jane"),
      "Bob" -> Set("Alice", "Mary"),
      "Jane" -> Set("Alice", "David"),
      "David" -> Set("Jane"),
      "Mary" -> Set("Alice", "Bob"),
      "Superman" -> Set(),
    )

    // add a person to the network with no initial friends
    def addPerson(network: Map[String, Set[String]], newPerson: String): Map[String, Set[String]] =
      network + (newPerson -> Set())

    // remove a person from the network
    def removePerson(network: Map[String, Set[String]], person: String): Map[String, Set[String]] = {
      (network - person) // keeps the same network if person is not in the map
        .map(tuple => {
          val name = tuple._1
          val friends = tuple._2
          val newFriends = friends - person
          // return new tuple
          name -> newFriends
        })
    }

    // add a new friend relationship
    def makeNewFriend(network: Map[String, Set[String]], a: String, b: String): Map[String, Set[String]] =
      if (!network.contains(a) || !network.contains(b)) throw new RuntimeException("keys not in the map")
      else {
        val newFriendsOfA = network(a) + b // Set[String]
        val newFriendsOfB = network(b) + a
        network + (a -> newFriendsOfA) + (b -> newFriendsOfB)
      }

    // remove friend relationship
    def unfriend(network: Map[String, Set[String]], a: String, b: String): Map[String, Set[String]] =
      if (!network.contains(a) || !network.contains(b)) throw new RuntimeException("keys not in the map")
      else {
        val newFriendsOfA = network(a) - b // Set[String]
        val newFriendsOfB = network(b) - a
        network + (a -> newFriendsOfA) + (b -> newFriendsOfB)
      }

    // find the number of friends of a person
    def nFriends(network: Map[String, Set[String]], person: String): Int =
      if (! network.contains(person)) -1
      else network(person).size

    // find the person with the MOST friends
    def mostConnected(network: Map[String, Set[String]]): String = {
      val peopleWithNFriends = network // { "Alice" -> Set[...], "Bob" -> Set[...], ... }
        .toList // [         ("Alice", Set[...]),   ("Bob", Set[...])    ]
        .map(tuple => {// [  ("Alice", 3)           ("Bob", 2)   .... ]
          val name = tuple._1
          val friends = tuple._2
          name -> friends.size
        })

      def findMax(people: List[(String, Int)], mostConn: (String, Int)): String =
        if (people.isEmpty) mostConn._1
        else {
          val person = people.head
          val nf = person._2

          if (nf > mostConn._2)
            findMax(people.tail, person)
          else
            findMax(people.tail, mostConn)
        }

      findMax(peopleWithNFriends, ("", Int.MinValue))
    }

    println(removePerson(initialNetwork, "Alice"))
  }

  def main(args: Array[String]): Unit = {
    demoSocialNetwork()
  }
}
