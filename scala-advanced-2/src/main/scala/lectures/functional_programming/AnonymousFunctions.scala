package lectures.functional_programming

object AnonymousFunctions extends App {

  // Old OO method.
  val doubler: Int => Int = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 * 2
  }

  // Scala-specific syntactic sugar (SSSS :P)
  val doublerAnon: Int => Int = (x: Int) => x * 2

  val multiParam: (Int, Int) => Int = (x: Int, y: Int) => x + y

  val noParam: () => Int = () => 42

  // Unlike calling functions, calling a lambda without parentheses will not work.
  // A call without parentheses references the instance of the lambda. It doesn't actually call it.
  // Careful with this.
  println(noParam)
  println(noParam())

  // Curly braces
  val stringToInt = { (x: String) =>
    x.toInt
  }

  // MOAR syntactic sugar
  val niceIncrementer: Int => Int = _ + 1  // equivalent to (x) => x + 1

  val niceAdder: (Int, Int) => Int = _ + _  // equivalent to (x, y) => x + y
    // Each underscore stands for a different parameter. Extremely useful
    // when you want to chain multiple HOF calls.

    // THAT VAL NEEDS A TYPE FOR THIS SYNTACTIC SUGAR TO WORK!

  /**
   * Exercises:
   *
   * 1. Go to MyList, and replace all FunctionX calls with lambdas.
   * 2. Define/rewrite the specialAdder (curried) function as an anonymous function.
   */
}
