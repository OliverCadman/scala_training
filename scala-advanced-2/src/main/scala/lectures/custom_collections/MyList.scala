package lectures.custom_collections
import scala.annotation._

abstract class MyList[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyList[A]
  def prepend[B >: A](elem: B): MyList[B]
  def append[B >: A](elem: B): MyList[B]
  def printElements: String
  override def toString: String = s"[$printElements]"
  def ++[B >: A](anotherList: MyList[B]): MyList[B]
  def map[B](transformer: A => B): MyList[B]
  def filter(predicate: A => Boolean): MyList[A]
  def flatMap[B](transformer: A => MyList[B]): MyList[B]
  def foreach(f: A => Unit): Unit
  def sort(compare: (A, A) => Int): MyList[A]
  def sortStackRecursive(compare: (A, A) => Int): MyList[A]
  def zipWith[B, C](f: (A, B) => C, anotherList: MyList[B]): MyList[C]
  def fold[B](start: B)(f: (B, A) => B): B

}

object Empty extends MyList[Nothing] {
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException
  def tail: MyList[Nothing] = throw new NoSuchElementException
  def prepend[B >: Nothing](elem: B): MyList[B] = new Cons(elem, Empty)
  def append[B >: Nothing](elem: B): MyList[B] = new Cons(elem, Empty)
  def printElements: String = ""
  def ++[B >: Nothing](anotherList: MyList[B]): MyList[B] = anotherList
  def map[B](transformer: Nothing => B): MyList[B] = Empty
  def filter(predicate: Nothing => Boolean): MyList[Nothing] = Empty
  def flatMap[B](transformer: Nothing => MyList[B]): MyList[B] = Empty
  def foreach(f: Nothing => Unit): Unit = ()
  def sort(compare: (Nothing, Nothing) => Int): MyList[Nothing] = Empty
  def sortStackRecursive(compare: (Nothing, Nothing) => Int): MyList[Nothing] = Empty
  def zipWith[B, C](f: (Nothing, B) => C, anotherList: MyList[B]): MyList[C] = {
    if (!anotherList.isEmpty) {
      println(anotherList)
      throw new RuntimeException("Lists are not the same length.")
    }
    else Empty
  }
  def fold[B](start: B)(f: (B, Nothing) => B): B = start
}

case class Cons[+A](h: A, t: MyList[A]) extends MyList[A] {
  def isEmpty: Boolean = false

  def head: A = h

  def tail: MyList[A] = t

  def prepend[B >: A](elem: B): MyList[B] = new Cons(elem, this)
  def append[B >: A](elem: B): MyList[B] = new Cons(h, t.append(elem))

  def printElements: String =
    if (t.isEmpty) s"$h"
    else {
      s"$h, ${t.printElements}"
    }

  def ++[B >: A](anotherList: MyList[B]): MyList[B] = new Cons(h, t ++ anotherList)
  def map[B](transformer: A => B): MyList[B] =
    new Cons(transformer(h), t.map(transformer))

  def filter(predicate: A => Boolean): MyList[A] =
    if (predicate(h)) new Cons(h, t.filter(predicate))
    else t.filter(predicate)

  def flatMap[B](transformer: A => MyList[B]): MyList[B] =
    transformer(h) ++ t.flatMap(transformer)

  def foreach(f: A => Unit): Unit = {
    f(h)
    t.foreach(f)
  }

  def sort(compare: (A, A) => Int): MyList[A] = {
    @scala.annotation.tailrec
    def insert(sortedTail: MyList[A], currentList: MyList[A]): MyList[A] = {
      if (sortedTail.isEmpty) currentList ++ new Cons(h, Empty)
      else if (compare(h, sortedTail.head) >= 0) {
        insert(sortedTail.tail, currentList ++ new Cons(sortedTail.head, Empty))
      } else {
        currentList ++ new Cons(h, Empty) ++ sortedTail
      }
    }

    val sorted = t.sort(compare)
    insert(sorted, Empty)
  }

  def sortStackRecursive(compare: (A, A) => Int): MyList[A] = {
    def insert(x: A, sortedList: MyList[A]): MyList[A] = {

      if (sortedList.isEmpty) new Cons(h, Empty)
      else if (compare(x, sortedList.head) <= 0){
//        println("sorted list head is larger than x")
//        println(s"X: $x. Sorted List Head: ${sortedList.head}")
        new Cons(x, sortedList)
      }
      else {
//        println("x is larger than sorted list head")
//        println(s"X: $x. Sorted List Head: ${sortedList.head}")
        new Cons(sortedList.head, insert(x, sortedList.tail))
      }
    }

    val sorted = t.sortStackRecursive(compare)
//    println("****")
//    println(this)
//    println(sorted)
//    println("****")
    insert(h, sorted)
  }

  def zipWith[B, C](f: (A, B) => C, anotherList: MyList[B]): MyList[C] =
    if (anotherList.isEmpty) throw new RuntimeException("Lists are not the same length!")
    else new Cons(f(h, anotherList.head), t.zipWith(f, anotherList.tail))

  def fold[B](start: B)(f: (B, A) => B): B =
    t.fold(f(start, h))(f)

}

object MyList {
  def apply[A](values: A*): MyList[A] = {
    @tailrec
    def buildList(valSeq: Seq[A], acc: MyList[A]): MyList[A] = {
      if (valSeq.isEmpty) acc
      else buildList(valSeq.tail, acc.append(valSeq.head))
    }

    buildList(values.toSeq, Empty)
  }

}


object playground extends App {
  val myList = MyList(1, 2, 3)
  println(myList)

  val newList = MyList(4, 5, 6)

  val combinedList = myList ++ newList

  println(
    combinedList.map(_ * 2)
  )

  println(
    combinedList.filter(_ % 2 == 0)
  )

  println(
    combinedList.flatMap(
      (elem: Int) => MyList(elem, elem * 10, elem * 20)
    )
  )

  combinedList.foreach(println)

  println(combinedList.sort((x, y) => y - x))

  println(combinedList.zipWith[Int, Int]((x, y) => x * y, MyList(2, 2, 2, 2, 2, 2)))


  val something =  combinedList.zipWith[Int, Int]((x, y) => x * y, MyList(2, 2, 2, 2, 2, 2))
      .fold(10)(_ + _)



  val anotherCombinedList = combinedList ++ MyList(7, 8, 9)
  val zipped = anotherCombinedList
    .zipWith[Int, Int]((x, y) => x * y, MyList(2, 2, 2, 2, 2, 2, 2, 2, 2))

  println(zipped)
  println(zipped.sortStackRecursive((x, y) => {
    println(s"$x - $y is ${x - y}")
    x - y
  }))

  val numbers = MyList(1, 2, 3, 4)
  val characters = MyList("a", "b", "c", "d")
  val colors = MyList("black", "white")

  val combinations = for {
    n <- numbers
    c <- characters
    color <- colors
  } yield {
    s"$n$c-$color"
  }

  println(combinations)
}