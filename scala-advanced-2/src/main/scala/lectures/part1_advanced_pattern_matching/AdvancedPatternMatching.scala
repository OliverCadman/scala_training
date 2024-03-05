package lectures.part1_advanced_pattern_matching

object AdvancedPatternMatching extends App {

  val someList = List(1, 2, 3, 4, 5)

  // Recursive pattern matching.
  def printListOfElements[A](list: List[A]): String  = list match {
    case head :: Nil => s"$head (last element)"
    case head :: tail => s"$head ${printListOfElements(tail)}"
    case _ => "Some other case."
  }

  println(printListOfElements(someList))

  val anotherList = List(1)

  val listMatcher = anotherList match {
    case head :: Nil => s"List has only one element: $head"
    case _ => "Some other case."
  }

  println(listMatcher)

  class Person(val name: String, val age: Int)

  val bob = Person("Bob", 24)

  object Person {
    def apply(name: String, age: Int): Person = {
      new Person(name, age)
    }

    def unapply(person: Person): Option[(String, Int)] = {
      if (person.age < 21) None else Some(person.name, person.age)
    }
  }
}
