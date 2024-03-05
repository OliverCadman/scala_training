package lectures.oop

object CaseClasses extends App {

  /**
   * Case classes offer very handy boilerplate out of the box:
   *  - equals
   *  - hashCode
   *  - toString
   *
   *  These out-of-the-box implementations make Case Classes a great choice to use when
   *  working with lightweight data structures.
   *
   *  Constructor parameters are also automatically promoted to fields (without the need for 'val').
   */

  case class Person(name: String, age: Int)
  case class Animal(breed: String)

  // 1. Parameters promoted to fields
  val jim = Person("Jim", 33)
  println(jim.name)

  // 2. Equals & hashcode implemented out of the box
  val jim2 = Person("Jim", 33)
  println(jim == jim2)

  // 3. Sensible toString
  println(jim)

  // 4. Copy method
  val jimToOliver = jim.copy(name="Oliver")
  println(jimToOliver)

  // 5. CCs have companion objects
  val aPerson = Person
  val mary = aPerson("Mary", 24)

  println(mary)

  // 6. CCs are serialiable
  //  Used in Akka

  // 7. CCs have extractor patterns that can be used in pattern matching

  val isAPerson = mary match {
    case Person(name, _) => s"$name's age is some age"
    case _ => "This is not a person"
  }
//
//  def patternRecursor[A](caseClasses: Seq[A]): String = caseClasses match {
//    case Person(name, age) => s"This is a person. Their name is $name and their age is $age."
//    case Animal(breed) => s"This is an animal. Their breed is $breed."
//    case _ => "Does not match"
//  }

  val john = Person("John", 43)
  val dani = Person("Dani", 33)
  val oli = Person("Oli", 24)
  val dog = Animal("dog")
  val cat = Animal("cat")
  val monkey = Animal("monkey")

  def seqCreator[A](classes: A*): List[A] = {
    def go(valSeq: List[A], acc: List[A]): List[A] = {
      if (valSeq.isEmpty) acc
      else go(valSeq.tail, acc.appended(valSeq.head))
    }

    go(classes.toList, List.empty[A])
  }

  val classList = seqCreator(john, dani, oli, dog, cat, monkey)
  println(classList)

}
