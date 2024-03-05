package lectures.oop

object Generics extends App {

  //  class MyList[+A] {
  //    def add[B >: A](elem: B): MyList[B] = ???
  //  }
  //
  //  class MyMap[Key, Value]
  //
  //  val listOfIntegers = new MyList[Int]
  //  val listOfStrings = new MyList[String]
  //
  //  object MyList {
  //    def empty[A]: MyList[A] = ???
  //  }
  //
  //  val emptyListOfIntegers = MyList.empty[Int]


  /**
   * There are some hard questions we need to understand.
   *
   * The VARIANCE problem...
   */

  class Animal

  class Cat extends Animal

  class Dog extends Animal

  /**
   * If Cat extends Animal, does a list of Cats extend Animal?
   *
   * 1. Yes - List of Cat extends list of Animal (co-variance)
   */

  class CovariantList[+A] // Co-variant list

  val animal: Animal = new Cat
  val animalList: CovariantList[Animal] = new CovariantList[Cat]

  /**
   * Once I declare an animal list to be a covariant list of Animal,
   * can I add any animal to it?
   *
   * e.g. AnimalList.add(new Dog)
   */

  /**
   * 2. No. List of Cats and list of Animals are two seperate things = INVARIANCE
   */

  class InvariantList[A] // No signs before or after. Invariant classes are each in it's own world, and you can't
  // substitute one for another.

  val invariantAnimalList: InvariantList[Animal] = new InvariantList[Animal]


  /**
   * 3. Hell no = CONTRAVARIANCE
   *
   * Contravariant classes are defined with a type parameter -A.
   */

  class ContravariantList[-A]

  val contravariantList: ContravariantList[Cat] = new ContravariantList[Animal]

  // How can you replace a list of Cats with a list of Animals?!
  class Trainer[-A]

  val trainer: Trainer[Cat] = new Trainer[Animal]

  /**
   * BOUNDED TYPES:
   *
   * Bounded types allow you to use your generic classes only for certain types that are either
   * a sub-class of a different type, or a super-class of a different type.
   */

  // Upper-bounded type.
  class Cage[A <: Animal](animal: A) // This class only accepts type parameters which are SUBTYPES of Animal.

  val cage: Cage[Dog] = new Cage(new Dog)


}
