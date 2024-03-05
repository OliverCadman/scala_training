package advanced.lectures.taste_of_advanced_scala

object AdvPM extends App {

  val singleDigitList: List[Int] = List(1)
  val singleDigitListMatcher = singleDigitList match {
    case head :: Nil => s"List contains one element: $head"
    case _ => "List not found."
  }

  println(singleDigitListMatcher)

  class Person(val name: String, val age: Int)
  object Person {
    def unapply(person: Person): Option[(String, Int)] = Some((person.name, person.age))
  }

  object LegalAge {
    def unapply(person: Person): Option[(String, String)] = {
      if (person.age > 21) Some((person.name, "major"))
      else Some((person.name, "minor"))
    }
  }

  val bob = new Person("Bob", 23)
  val personMatch = bob match {
    case Person(name, age) => s"Found $name. Their age is $age"
    case _ => "No person found."
  }
  println(personMatch)

  val legalStatus = bob match {
    case LegalAge(name, status) => s"$name's legal status is '$status'."
    case _ => "No person found to extract a legal status from."
  }

  println(legalStatus)

  object Even {
    def unapply(x: Int): Boolean = x % 2 == 0
  }

  object Odd {
    def unapply(x: Int): Boolean = x % 2 != 0
  }

  object SingleDigit {
    def unapply(x: Int): Boolean = x < 10 && x >= 0 || x > -10 && x <= 0
  }

  val value: Int = 13

  val valProperty = value match {
    case Even() => "The value is even."
    case Odd() => "The value is odd."
    case SingleDigit() => "The value is a single digit."
    case _ => "Couldn't extract a property."
  }

  println(valProperty)


  // Infix patterns
  case class Or[A, B](a: A, b: B)
  val either = Or(2, "Two")

  val eitherOr = either match {
    case number Or string => s"$number is represented as '$string'."
    case _ => "Couldn't find this or that."
  }

  // Decomposing sequences
  val aList = List(1, 2, 3, 4, 5, 6, 7)
  val listMatch = aList match {
    case List(1, 2, 3, _*) => "List starts with 1, 2 and 3."
    case _ => "Couldn't find a list."
  }

  // Creating our own unapplySeq method

  // 1. Create mini collection library for testing...

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]

  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]


  object MyList {

    // CHALLENGE: MAKE THIS TAIL RECURSIVE? IS THIS POSSIBLE? I GUESS IT SHOULD BE...
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] = {
      if (list.tail == Empty) Some(Seq.empty[A])
      else unapplySeq(list.tail).map(list.head +: _)
    }
  }

  val testList = Cons(1, Cons(3, Cons(3, Cons(34, Cons(100, Empty)))))

  val varArgMatcher = testList match {
    case MyList(1, 2, _*) => "This list starts with 1 and 2."
    case _ => "Cannot find a matching pattern."
  }

  println(varArgMatcher)

  // Custom return type.
  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object LegalAgeWrapper {
    def unapply(person: Person): Wrapper[(String, String)] = new Wrapper[(String, String)] {
      override def isEmpty: Boolean = false
      override def get: (String, String) = {
        if (person.age > 21) (person.name, "major")
        else (person.name, "minor")
      }
    }
  }

  val legalStatusWrapped = bob match {
    case LegalAgeWrapper(name, status) => s"$name's legal status is '$status'."
    case _ => "No person found to extract a legal status from."
  }

  println(legalStatusWrapped)
}
