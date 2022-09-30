package practice

import scala.annotation.tailrec

/*
  Write a linked list data structure.
 */
abstract class LList[A] {
  /*
    [].head = throw new NoSuchElementException
    [3,2,5,4,1].head = 3
   */
  def head: A
  /*
    [].tail = throw new NoSuchElementException
    [3,2,5,4,1].tail = [2,5,4,1]
   */
  def tail: LList[A]
  /*
    [].isEmpty = true
    [3,2,5,4,1].isEmpty = false
   */
  def isEmpty: Boolean
  /*
    [].add(100) = [100]
    [3,2,5,4,1].add(100) = [100,3,2,5,4,1]
   */
  def add(element: A): LList[A] = new NonEmpty(element, this)


  // part of the Any type, overriding here
  override def toString = {
    /*
      [1,2,3,...20000].toString = s"[ ${enum([1,2,3...20000], "")} ]"
      enum([1,2,3...20000], "") =
      enum([2,...20k], ", 1") =
      enum([3,...20k], ", 1, 2") =
      enum([4,...20k], ", 1, 2, 3") =
      ...
      enum([], ", 1, 2, ...., 20k") =
      ", 1, 2, ...., 20k"
     */
    @tailrec
    def enumerateElements(list: LList[A], acc: String): String = {
      if (list.isEmpty) acc // empty list
      else if (acc.isEmpty) enumerateElements(list.tail, s"${list.head}") // placing my first number
      else enumerateElements(list.tail, s"$acc, ${list.head}") // list with more than one element
    }

    s"[${enumerateElements(this, "")}]"
  }

  // applies the transformer on ALL elements of this list, returning the list of all the results
  // [1,2,3].map(doubler) => [2,4,6]
  // [].map(doubler) => []
  // [1,2,3,4,5].map(transformer which does x => x.toString) => ["1", "2", "3", "4", "5"]
  def map[B](transformer: A => B): LList[B]

  // return a new list containing just the elements that "pass" the filter, i.e. elements for which predicate.test(e) == true
  // [1,2,3,4,5].filter(evenPredicate) => [2,4]
  // [].filter(_) => []
  // [1,3,5].filter(evenPredicate) => []
  def filter(predicate: A => Boolean): LList[A]

  // concatenates this list to another list
  // [1,2,3].concatenate([4,5,6,7]) = [1,2,3,4,5,6,7]
  def ++(anotherList: LList[A]): LList[A]

  // runs the transformer on every element of the original list (resulting in mini-lists) then concatenates ALL mini-lists
  // [1,2,3].flatMap(listTransformer) = [1,2,2,3,3,4]
  // [].flatMap(_) = []
  def flatMap[B](transformer: A => LList[B]): LList[B]

  // execute the transformer for every element
  def foreach(transformer: A => Unit): Unit
  // returns a new list with the elements in reverse order
  def reverse: LList[A]

  // functions practice
  /*
    combine two lists of the SAME LENGTH by combining corresponding elements in each
    example:
      [1,2,3].zipWith(["Scala", "is", "easy"], (number,string) => s"$number-$string")) =
      ["1-Scala","2-is","3-easy"]

    if the lists don't have the same length, throw new RuntimeException
   */
  def zipWith[B,C](anotherList: LList[B], combinator: (A,B) => C): LList[C] = {
    /*
    original expr
    [1,2,3].zipWith(["Scala", "is", "easy"], (number,string) => s"$number-$string")) =
      zipAux([1,2,3], ["Scala", "is", "easy"], []) =
      zipAux([2,3], ["is", "easy"], ["1-Scala"])) =
      zipAux([3], ["easy"], ["2-is","1-Scala"]) =
      zipAux([],[],["3-easy", "2-is", "1-Scala"]) =
      ["3-easy", "2-is", "1-Scala"]

    => ["1-Scala", "2-is", "3-easy"]
     */
    @tailrec
    def zipAux(list: LList[A], anotherList: LList[B], acc: LList[C]): LList[C] = {
      if (list.isEmpty && !anotherList.isEmpty) throw new RuntimeException()
      else if (anotherList.isEmpty && !list.isEmpty) throw new RuntimeException()
      else if (list.isEmpty) acc // this means anotherList is empty too
      else zipAux(list.tail, anotherList.tail, new NonEmpty[C](combinator(list.head, anotherList.head), acc))
    }

    zipAux(this, anotherList, new Empty[C]).reverse
  }

  // shorter stack-recursive solution
  def zipWith_v2[B,C](anotherList: LList[B], combinator: (A,B) => C): LList[C]

  // combine all elements of a list
  def reduce(combiner: (A,A) => A, seed: A): A = {
    @tailrec
    def reduceTailrec(list: LList[A], acc: A): A =
      if (list.isEmpty) acc
      else reduceTailrec(list.tail, combiner(acc, list.head))

    reduceTailrec(this, seed)
  }

  def reduceStackRec(combiner: (A,A) => A, seed: A): A =
    if (this.isEmpty) seed // empty case
    else tail.reduceStackRec(combiner, combiner(seed, head)) // non-empty case
}

case class Empty[A]() extends LList[A] {
  override def head = throw new NoSuchElementException
  override def tail = throw new NoSuchElementException
  override def isEmpty = true

  override def map[B](transformer: A => B) = Empty[B]()
  override def filter(predicate: A => Boolean) = Empty[A]()

  override def ++(anotherList: LList[A]): LList[A] = anotherList
  override def flatMap[B](transformer: A => LList[B]) = Empty[B]()

  override def foreach(transformer: A => Unit): Unit = () // the unit value
  override def reverse = this

  def zipWith_v2[B,C](anotherList: LList[B], combinator: (A,B) => C): LList[C] =
    if (!anotherList.isEmpty) throw new RuntimeException
    else Empty[C]()
}

case class NonEmpty[A](firstElement: A, rest: LList[A]) extends LList[A] {
  override val head = firstElement
  override val tail = rest
  override def isEmpty = false

  /*
    [1,2,3].map(doubler) => [2, [2,3].map(doubler)] = [2,4,6]
    [2,3].map(doubler) => [4,[3].map(doubler)] = [4,6]
    [3].map(doubler) => [6, [].map(doubler)] = [6]
    [].map(doubler) => []
   */
  override def map[B](transformer: A => B): LList[B] =
    NonEmpty[B](transformer.apply(head), tail.map(transformer))

  /*
    [1,2,3].filter(evenPredicate) = [2,3].filter(evenPredicate) = [2]
    [2,3].filter(evenPredicate) = [2, [3].filter(predicate)] = [2]
    [3].filter(predicate) = [].filter(predicate) = []
   */
  override def filter(predicate: A => Boolean) =
    if (predicate.apply(head)) NonEmpty(head, tail.filter(predicate))
    else tail.filter(predicate)

  /*
     [1,2,3] ++ [4,5,6,7] = [1, [2,3] ++ [4,5,6,7]] = [1,2,3,4,5,6,7]
     [2,3] ++ [4,5,6,7] = [2, [3] ++ [4,5,6,7]] = [2,3,4,5,6,7]
     [3] ++ [4,5,6,7] = [3, [] ++ [4,5,6,7]] = [3,4,5,6,7]
     [] ++ [4,5,6,7] = [4,5,6,7]
   */
  override def ++(anotherList: LList[A]): LList[A] =
    NonEmpty(head, tail ++ anotherList)

  override def flatMap[B](transformer: A => LList[B]) = {
    val firstPart: LList[B] = transformer.apply(head) // first mini-list
    val secondPart: LList[B] = tail.flatMap(transformer) // everything else

    firstPart ++ secondPart
  }

  override def foreach(transformer: A => Unit): Unit = {
    transformer.apply(head) // process the head
    tail.foreach(transformer) // process the rest of the stuff
  }

  override def reverse: LList[A] = {
    // rt([1,2,3], []) = rt([2,3], [1]) = rt([3], [2,1]) = rt([], [3,2,1]) = [3,2,1]
    @tailrec
    def reverseTailrec(list: LList[A], acc: LList[A]): LList[A] =
      if (list.isEmpty) acc
      else reverseTailrec(list.tail, acc.add(list.head))

    reverseTailrec(this, Empty())
  }

  def zipWith_v2[B,C](anotherList: LList[B], combinator: (A,B) => C): LList[C] =
    if (anotherList.isEmpty) throw new RuntimeException
    else NonEmpty[C](combinator(head, anotherList.head), tail.zipWith(anotherList.tail, combinator))
}

// LList.make(1, 10) = [1,2,3,4,5,6,7,8,9,10]
object LList { // companion object
  def make(low: Int, hi: Int): LList[Int] = {
    @tailrec
    def countOut(number: Int, acc: LList[Int]): LList[Int] = {
      if(number > hi) acc
      else countOut(number + 1, NonEmpty[Int](number, acc))
    }
    countOut(low, Empty[Int]).reverse
  }
}


object LListPlayground {

  def main(args: Array[String]): Unit = {
    val aList = NonEmpty(1, NonEmpty(2, NonEmpty(3, Empty())))
    val strings = NonEmpty("Scala", NonEmpty("fries", NonEmpty("brainz", Empty())))
    val anotherList = NonEmpty(4, NonEmpty(5, NonEmpty(6, NonEmpty(7, Empty()))))
    val tens = LList.make(1,4).map(_ * 10) // [10, 20, 30, 40]
    val letters = NonEmpty("a", NonEmpty("b", NonEmpty("c", Empty())))
    println(aList.head) // 1
    println(aList.tail.head) // 2
    println(aList.isEmpty) // false
    println(aList.add(0).head) // 0

    println(aList) // "[1,2]"
    println(Empty()) // "[]"

    val evenPredicate: Int => Boolean = _ % 2 == 0
    val doubler: Int => Int = _ * 2
    val tenx: Int => Int = _ * 10
    val listTransformer: Int => LList[Int] = x => NonEmpty(x, NonEmpty(x + 1, Empty()))

    println(aList.map(doubler)) // [2,4,6]
    println(aList.map(tenx)) // [10,20,30]
    println(aList.filter(evenPredicate)) // [2]
    // test for map/flatMap
    println(listTransformer.apply(3)) // [3,4]
    // [1,2,3].map(listTransformer) = [[1,2],[2,3],[3,4]]]
    println(aList.map(listTransformer)) // [[1,2],[2,3],[3,4]]
    println(aList.flatMap(listTransformer)) // [1,2,2,3,3,4]
    println(aList ++ anotherList) // [1,2,3,4,5,6,7]

    // will print all elements (1,2,3) on separate lines
    aList.foreach(new Function1[Int, Unit] {
      override def apply(value: Int): Unit = println(value)
    })
    println(aList.reverse) // [3,2,1]

    // zipWith
    val zipped = aList.zipWith[String, String](strings, (num, string) => s"$num-$string")
    println(zipped) // ["1-Scala", "2-fries", "3-brainz"]
    println(aList.zipWith_v2[String, String](strings, (num, string) => s"$num-$string"))
    val empties = Empty[Int]().zipWith[String,String](Empty[String](), (num, string) => s"$num-$string")
    println(empties) // []

    // making numbers
    println(LList.make(1,10))
    println(LList.make(3,21))
    println(LList.make(-1,10))
    println(LList.make(4,2))
    val twentyK = LList.make(1, 20000)
    val twentyKString = twentyK.toString // toString causes the crash if toString calls a stack-recursive function!

    // reduce
    println(LList.make(1, 10).reduce(_ + _, 0)) // 55
    println(LList.make(1, 10000).reduce(_ + _, 0)) // 50005000

    // combine [1,2,3] x ["Scala", "fries", "brainz"] = ["1-Scala", "1-fries", "1-brainz", "2-Scala", "2-fries", "2-brainz", "3-Scala", "3-fries", "3-brainz"]
    // hint: use flatMap + map
    // [1,2,3].flatMap(x => [s"$x-Scala", s"$x-fries", s"$x-brainz"]) = [1-s,1-f,1-b,2-s,2-f,2-b,3-s,3-f,3-b]
    val allCombinations = aList.flatMap(x => strings.map(string => s"$x-$string"))
    println(allCombinations)

    // for comprehension
    val allCombinations_v2 = for {
      x <- aList // aList.flatMap(x => strings.map(string => s"$x-$string"))
      string <- strings
    } yield s"$x-$string" // SAME as flatMap + map
    // EXPRESSION, NOT A LOOP!
    // in order for a for comprehension to work, YOU NEED a flatMap and a map WITH a particular signature

    /*
      Exercises:
      1. for every
        x <- [1,2,3]
        y <- [10, 20, 30, 40],
        s <- ["a", "b", "c"]
        build "${x * y}--$s"

        [10--a, 10--b, 10--c, 20--a, 20--b, 20--c, 30--a, 30--b, 30--c, 40--a, 40--b, 40--c, 20--a, 20--b, 20--c,
        (36 items)

        a) use a for comprehension
        b) use a flatMap + map chain
     */
    val combinations_ex2 = for {
      x <- aList // generator
      y <- tens
      s <- letters
    } yield s"${x * y}--$s"
    val combinations_ex2_v2 =
      aList.flatMap(x => // all other generators are flatMaps
        tens.flatMap(y =>
          letters.map(s => // last generator is always a map
            s"${x * y}--$s"
          )
        )
      )

    println(combinations_ex2)
    println(combinations_ex2_v2)

    aList add 0
  }
}


// old code, refactored with function types

// predicates and transformers
//trait Predicate[A] { // same as Function1[A,Boolean]
//  def test(value: A): Boolean
//}

//trait Transformer[A, B] { // similar to Function1[A,B]
//  def transform(value: A): B
//}

// predicates and transformers
//    val evenPredicate = new Function1[Int, Boolean] {
//      def apply(value: Int) = value % 2 == 0
//    }
//
//    val doubler = new Function1[Int, Int] {
//      override def apply(value: Int) = value * 2
//    }
//
//    val tenx = new Function1[Int, Int] {
//      override def apply(value: Int) = value * 10
//    }
//
//    val listTransformer = new Function1[Int, LList[Int]] {
//      override def apply(value: Int) =
//        new NonEmpty(value, new NonEmpty(value + 1, new Empty))
//    }
