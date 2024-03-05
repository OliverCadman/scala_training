package advanced.lectures.taste_of_advanced_scala

import scala.annotation.tailrec

object AdvPatternMatching extends App {
  val singleElementList: List[Int] = List(1)

  val oneElementListMatch = singleElementList match {
    case head :: Nil => s"The only element is $head."
    case _ => "Something else."
  }

  println(oneElementListMatch)

  @tailrec
  def extractListElements(list: List[Int], acc: String): String = list match {
    case head :: Nil => acc + s"\n The last element is $head"
    case head :: tail =>
      val msg = s"The current element is $head. The next element is ${tail.head}"
      extractListElements(list.tail, acc + msg + "\n")

  }

  val someListOfInts = List(142, 64, 85, 1774, 630, 251, 7434, 1, 16)
  println(extractListElements(someListOfInts, ""))


  // Unapply
  class Person(val name: String, val age: Int)

  object Person {
    def apply(name: String, age: Int) = new Person(name, age)

    def unapply(age: Int): Option[String] =
      Some(if (age > 21) "major" else "minor")
  }

  val bob = Person("Bob", 22)

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }

  println(legalStatus)

  /**
   * Exercise
   */
  val value: Int = -5

 val valueProperty = value match {
   case x if x < 10 && x >= 0 || x > -10 && x <= 0 => s"$x is a single digit."
   case x if x % 2 == 0 => s"$x is an even number."
   case _ => "Something else."
 }

  println(valueProperty)

  object Even {
    def unapply(x: Int): Option[Int] = {
      if (x % 2 == 0) Some(x)
      else None
    }
  }

  object SingleDigit {
    def unapply(x: Int): Option[Int] = {
      if (x < 10 && x >= 0 || x > -10 && x <= 0) {
        Some(x)
      } else None
    }
  }

  val valuePropertyBetter = value match {
    case Even(x) => s"$x is an even number."
    case SingleDigit(x) => s"$x is a single digit."
    case _ => "Something else."
  }

  println(valuePropertyBetter)

  /**
   * LECTURE 2
   */

  // Infix patterns
  case class Or[A, B](a: A, b: B)
  val either = Or(2, "two")

  val humanDescription = either match {
    case number Or string  => s"$number is written as $string"
    case _ => "Something else"
  }

  println(humanDescription)

  // Decomposing sequences
  val varArg = someListOfInts match {
    case List(42, _*) => "Starts with 42"
    case _ => "Something else."
  }

  /**
   * ^^^^^^^^^^
   *
   * Pattern matching against the whole list as sequence.
   * It may have 1, 2, 3 or multiple values to decompose. The standard technique cannot work here.
   * Let's say we have our own collection.
   */


  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]

  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] = {
      if (list == Empty) Some(Seq.empty[A])
      else unapplySeq(list.tail).map(list.head +: _)
    }
  }

  val myList = Cons(1, Cons(2, Cons(3, Cons(4, Cons(5, Empty)))))

  val decomposedPattern = myList match {
    case MyList(1, 2, 3, _*) => "Starts with 1, 2 and 3."
    case _ => "Something else..."
  }

  println(decomposedPattern)


  /**
   * Say we want to pattern match on our own list with a var arg pattern?
   * We would need to create an unapplySeq method on MyList object
   */




  // Custom return types

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      override def isEmpty: Boolean = false
      override def get: String = person.name
    }
  }

  println(
    bob match {
      case PersonWrapper(n) => s"This person's name is $n"
      case _ => "Something else"
    }
  )

  /**
   * WE CAN DEFINE OUR OWN PATTERNS.
   * TO DO THAT, WE DEFINE A SINGLETON OBJECT WITH THE NAME OF THE PATTERN THAT WE WANT TO USE.
   * WE PROVIDE A METHOD CALLED unapply. THIS METHOD TAKES AN ARGUMENT OF THE TYPE THAT YOU WANT TO
   * PATTERN MATCH AGAINST, AND THE RETURN TYPE IS AN OPTION/WRAPPER, CONTAINING THE THINGS THAT YOU
   * LATER WANT TO USE IN THE PATTERN MATCH EXPRESSION.
   *
   * HAVING DEFINED THAT, WHEN WE PATTERN MATCH 'SOMETHING', THE COMPILER LOOKS AT THE TYPE OF THING
   * BEING PATTERN-MATCHED AND ALSO AT THE TYPES AND NUMBER OF THINGS THAT WE WANT TO USE IN THE PATTERN
   * MATCH EXPRESSION, AND LOOKS FOR A PROPER UNAPPLY METHOD DEFINED WITHIN THE SINGLETON OBJECT WITH THE SAME
   * NAME AS THE PATTERN.
   */

}
