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

  // new NonEmpty(1, new NonEmpty(2, new Empty))
  // => "1, 2"
  // a toString without the []
  private def enumerateElements: String = {
    if (isEmpty) "" // empty list
    else if (tail.isEmpty) s"$head" // list with one element
    else s"$head, ${tail.enumerateElements}" // list with more than one element
  }

  // part of the Any type, overriding here
  override def toString = s"[$enumerateElements]"

  // applies the transformer's transform method on ALL elements of this list, returning the list of all the results
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
  def zipWith[B,C](anotherList: LList[B], combinator: (A,B) => C): LList[C]
}

class Empty[A] extends LList[A] {
  override def head = throw new NoSuchElementException
  override def tail = throw new NoSuchElementException
  override def isEmpty = true

  override def map[B](transformer: A => B) = new Empty[B]
  override def filter(predicate: A => Boolean) = new Empty[A]

  override def ++(anotherList: LList[A]): LList[A] = anotherList
  override def flatMap[B](transformer: A => LList[B]) = new Empty[B]

  override def foreach(transformer: A => Unit): Unit = () // the unit value
  override def reverse = this
  // TODO
  override def zipWith[B,C](anotherList: LList[B], combinator: (A,B) => C): LList[C] = ???
}

class NonEmpty[A](firstElement: A, rest: LList[A]) extends LList[A] {
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
    new NonEmpty[B](transformer.apply(head), tail.map(transformer))

  /*
    [1,2,3].filter(evenPredicate) = [2,3].filter(evenPredicate) = [2]
    [2,3].filter(evenPredicate) = [2, [3].filter(predicate)] = [2]
    [3].filter(predicate) = [].filter(predicate) = []
   */
  override def filter(predicate: A => Boolean) =
    if (predicate.apply(head)) new NonEmpty(head, tail.filter(predicate))
    else tail.filter(predicate)

  /*
     [1,2,3] ++ [4,5,6,7] = [1, [2,3] ++ [4,5,6,7]] = [1,2,3,4,5,6,7]
     [2,3] ++ [4,5,6,7] = [2, [3] ++ [4,5,6,7]] = [2,3,4,5,6,7]
     [3] ++ [4,5,6,7] = [3, [] ++ [4,5,6,7]] = [3,4,5,6,7]
     [] ++ [4,5,6,7] = [4,5,6,7]
   */
  override def ++(anotherList: LList[A]): LList[A] =
    new NonEmpty(head, tail ++ anotherList)

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

    reverseTailrec(this, new Empty)
  }

  // TODO
  override def zipWith[B,C](anotherList: LList[B], combinator: (A,B) => C): LList[C] = ???
}


object LListPlayground {

  def main(args: Array[String]): Unit = {
    val aList = new NonEmpty(1, new NonEmpty(2, new NonEmpty(3, new Empty)))
    val anotherList = new NonEmpty(4, new NonEmpty(5, new NonEmpty(6, new NonEmpty(7, new Empty))))
    println(aList.head) // 1
    println(aList.tail.head) // 2
    println(aList.isEmpty) // false
    println(aList.add(0).head) // 0

    println(aList) // "[1,2]"
    println(new Empty) // "[]"

    val evenPredicate: Int => Boolean = _ % 2 == 0
    val doubler: Int => Int = _ * 2
    val tenx: Int => Int = _ * 10
    val listTransformer: Int => LList[Int] = x => new NonEmpty(x, new NonEmpty(x + 1, new Empty))

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
