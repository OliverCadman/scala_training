package advanced.lectures.taste_of_advanced_scala

import scala.util.Try

object DarkSugars extends App {

  // Syntax Sugar #1
  // Methods with single param

  def singleArgMethod(arg: Int): String = s"$arg little ducks."
  val description = singleArgMethod {
    // Perform logic to determine input within curly brackets!

    val aNestedTuple = (1, (45, 24), (752, 64, 8))
    aNestedTuple match {
      case (_, (_, _), (_, _, 24)) => 42
      case (_, (_, 24), (_, _, _)) => 100
      case (_, (45, _), (_, 64, 8)) => 1245
      case _ => 1000
     }
  }
  println(description)

  /**
   * This syntactic sugar allows us to write Scala code with curly braces,
   * in a syntax which is very similar to Java or C++
   */

  val aTryInstance = Try { // Java's try
    throw new RuntimeException()
  }

  List(1, 2, 3).map { x =>
    // Whatever complex lambda implementation!
     x + 1
  }

  // Syntax Sugar #2: Instances of traits with a single method can be reduced to lambdas
  // Single-abstract-method pattern

  /**
   * Say we define a trait Action, with a single method 'act'
   */
  trait Action {
    def act(n: Int): Int
  }

  /**
   * A normal way of instantiating this trait would be to either extend it with a non-abstract class,
   * or implement an anonymous class...
   */
  val anInstance: Action = new Action {
    override def act(n: Int): Int = n + 1
  }

  /**
   * We can convert this to a single abstract method.
   * We can reduce the instance of the Action trait into a lambda expression.
   */
  val aFunkyInstance: Action = (n: Int) => n + 1 // Compiler does a lot of magic to convert this into a lambda.

  /**
   * An example of instantiating traits with Runnables.
   * Runnables are instances of a trait/Java interface that can be passed onto threads.
   */
  // Example: Runnables
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Hello")
  })

  val aSweeterThread = new Thread(() => println("Sweet Scala!"))


  /**
   * This pattern also works for classes which have some methods implemented, but at most one method unimplemented.
   */

  abstract class AnAbstractType {
    def implemented: Int = 24
    def uninplemented(n: Int): Int
  }

  val anAbstractInstance: AnAbstractType = (n: Int) => n + 10

  // Syntax Sugar #3
  val prependedList = 2 :: List(3, 4)

  /**
   * How does this actually compile?
   *
   * You would imagine that 2 :: List(3, 4) compiles to 2.::List(3, 4)
   * But this doesn't make sense, because an Int type doesn't have a :: method.
   *
   * What actually happens is this:
   *
   * List(3, 4).::(2)
   *
   * How?!
   *
   * The answer lies in the Scala specification.
   * 'The associativity of a method is determined by the operator's LAST character.'
   *
   * If it ends in a colon, it means it's right associative. If not, it's left associative.
   * This allows the compiler to be able to write such operators in reverse order.
   *
   */

  println(1 :: 2 :: 3 :: List(4, 5)) // List(4, 5).::(3).::(2).::(1) == List(1, 2, 3, 4, 5)

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this
  }

  def myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]

  // The :: and #:: (prepend streams) are special right-associative syntax sugars for prepending.

  // Syntax Sugar #4: Multi-word method naming

  class TeenGirl(name: String) {
    def `and then said`(gossip: String): Unit = println(s"$name said $gossip")
  }

  val girl = new TeenGirl("Mary")
  girl `and then said` "Carly hates Jemima..."

  // Syntax Sugar #5: Infix types
  class Composite[A, B]

  val composite: Int Composite String = ???

  class -->[A, B]
  val something: Int --> String = ??? // Will see this more later in the course

  // Syntax Sugar #6: Setters for mutable containers
  class MutableContainer {
    private var internalValue: Int = 0
    def member: Int = internalValue
    def member_=(newVal: Int): Unit =
      internalValue = newVal
  }

  val mutable = new MutableContainer
  mutable.member = 42 // Rewritten as mutable.member_=(42)
}
