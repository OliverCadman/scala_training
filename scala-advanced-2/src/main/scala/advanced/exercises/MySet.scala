package advanced.exercises

import advanced.lectures.advanced_functional_programming.PartialFunctions.FunctionNotApplicableException

trait MySet[A] extends (A => Boolean) {
  def contains(x: A): Boolean
  override def apply(x: A): Boolean = contains(x)
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]
  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(f: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit
  def printElements: String
  def isEmpty: Boolean
  override def toString(): String = s"[$printElements]"
  def -(elem: A): MySet[A]
  def &(anotherSet: MySet[A]): MySet[A]
  def --(anotherSet: MySet[A]): MySet[A]
  def unary_! : MySet[A]

}



class EmptySet[A] extends MySet[A] {
  def contains(x: A): Boolean = false
  def +(x: A): MySet[A] = new NonEmptySet[A](x, new EmptySet[A])
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet
  def map[B](f: A => B): MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def filter(f: A => Boolean): MySet[A] = this
  def foreach(f: A => Unit): Unit = ()
  def printElements: String = ""
  def isEmpty: Boolean = true
  def -(elem: A): MySet[A] = this
  def &(anotherSet: MySet[A]): MySet[A] = this
  def --(anotherSet: MySet[A]): MySet[A] = this
  def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)
}

class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {
  def contains(x: A): Boolean = property(x)
  def +(elem: A): MySet[A] = new PropertyBasedSet[A](x => elem == x || property(x))
  // { x in A | property(x) } + elem = { x in A | property(x) || x == element
  def ++(anotherSet: MySet[A]): MySet[A] = new PropertyBasedSet[A](x => anotherSet(x) || property(x))

  def map[B](f: A => B): MySet[B] = throw new FunctionNotApplicableException
  def flatMap[B](f: A => MySet[B]): MySet[B] = throw new FunctionNotApplicableException
  def filter(f: A => Boolean): MySet[A] = new PropertyBasedSet[A](x => property(x) && f(x))
  def foreach(f: A => Unit): Unit = throw new FunctionNotApplicableException
  def printElements: String = throw new FunctionNotApplicableException
  def isEmpty: Boolean = throw new FunctionNotApplicableException
  def -(elem: A): MySet[A] = filter(x => x != elem)
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)
  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !property(x))
}

class NonEmptySet[A](h: A, t: MySet[A]) extends MySet[A] {
  def contains(elem: A): Boolean = h == elem || t.contains(elem)
  def +(elem: A): MySet[A] =
    if (this.contains(elem)) this
    else new NonEmptySet[A](elem, this)
  def ++(anotherSet: MySet[A]): MySet[A] = t ++ anotherSet + h
  def map[B](f: A => B): MySet[B] = (t map f) + f(h)
  def flatMap[B](f: A => MySet[B]): MySet[B] = (t flatMap f) ++ f(h)
  def filter(f: A => Boolean): MySet[A] = {
    val filteredTail = t filter f
    if (f(h)) filteredTail + h
    else filteredTail
  }
  def foreach(f: A => Unit): Unit = {
    f(h)
    t foreach f
  }

  def printElements: String =
    if (t.isEmpty) s"$h"
    else s"${t.printElements}, $h"


  def isEmpty: Boolean = false
  def -(elem: A): MySet[A] =
    if (h == elem) t
    else t - elem + h

  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

  def --(anotherSet: MySet[A]): MySet[A] = filter(x => !anotherSet.contains(x))

  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))
}

object MySet {
  def apply[A](values: A*): MySet[A] = {
    def go(valSeq: Seq[A], acc: MySet[A]): MySet[A] = {
      if (valSeq.isEmpty) acc
      else go(valSeq.tail, acc + valSeq.head)
    }

    go(values.toSeq, new EmptySet[A])
  }
}

object SetPlayground extends App {
  val mySet = MySet(1, 2, 3, 4, 5)

  println(mySet)

  println(mySet.filter(_ % 2 == 0))
  println(mySet.map(_ * 4))
  println(mySet.flatMap(x => MySet(x, x + 1)))

  println(mySet + 6)
  println(mySet ++ MySet(6, 7, 8, 8))

  mySet.foreach(println)
  println(mySet - 1)

  val anotherSet = MySet(3, 4, 5, 6, 7)
  println(anotherSet & mySet)
  println(anotherSet -- mySet)
}


