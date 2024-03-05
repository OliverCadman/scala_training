package lectures.functional_programming

object HOFsAndCurries extends App {

  // Execute a function n times.

  // nTimes(f, 3, x) = f(f(f(x)))
  def nTimes(f: Int => Int, n: Int, x: Int): Int = {
    if (n <= 0) x
    else nTimes(f, n - 1, f(x))
  }

  def plusOne(x: Int): Int = x + 1

  val plus10 = nTimes(plusOne, 10, 0)
  println(plus10)

  def nTimesBetter(f: Int => Int, n: Int): Int => Int = {
    if (n <= 0) (x: Int) => x
    else (x: Int) => nTimesBetter(f, n - 1)(f(x))
  }

  val plus10Better = nTimesBetter(plusOne, 10)
  val tenPlusTen = plus10Better(10)
  println(tenPlusTen)

  val superAdder: (Int => (Int => Int)) = (x: Int) => (y: Int) => x + y
  val add3 = superAdder(3)
  println(add3(10))
  println(superAdder(10)(3))

  // Multiple parameter lists
  def curriedFormatter(c: String)(x: Double): String = c.format(x)

  val standardFormat: Double => String = curriedFormatter("%1.2f")
  val preciseFormat: Double => String = curriedFormatter("%1.6f")

  println(standardFormat(Math.PI))
  println(preciseFormat(Math.PI))

  /**
   * Exercises
   *
   * Expand MyList:
   *  - foreach: A => Unit - Apply function for every element
   *    - [1,2,3].foreach(x => println(x))
   *
   *  - sort: (A, A) => Int => MyList
   *    - [1, 2, 3].sort((x, y) => y - x) (x is 'less' than y. Sort descendingly.)
   *
   *  - zipWith: anotherList: MyList[A], f: (A, A) => B => MyList[B]
   *    - [1, 2, 3].zipWith([4, 5, 6], (x, y) => x * y) = [1 * 4, 2 * 5, 3 * 6]
   *
   *  - fold: (startValue)(function) => value
   *    - [1, 2, 3].fold(0)(0 + 1)
   *    = [2, 3].fold(1)(1 + 2)
   *    = [3].fold(3)(3 + 3)
   *    = [].fold(6)   = 6
   *
   *
   *    toCurry(f: (Int, Int) => Int): Int => Int => Int
   *
   *    fromCurry(f: Int => Int => Int): (Int => Int) => Int
   *
   *    compose(f, g) => x => f(g(x))
   *
   *    andThen(f, g) => x => g(f(x))
   **/

  def toCurry(f: (Int, Int) => Int): Int => Int => Int = {
    (x: Int) => (y: Int) => f(x, y)
  }

  val somethingToCurry = toCurry((x, y) => x + y)

  println(somethingToCurry(10)(5))

  def fromCurry(f: Int => Int => Int): (Int, Int) => Int = {
    (x: Int, y: Int) => f(x)(y)
  }

  val somethingFromCurry = fromCurry((x) => (y) => x + y)
  println(somethingFromCurry(10, 5))


  def compose[A, B, T](f: A => B, g: T => A): T => B =
    (x: T) => f(g(x))


  def andThen[A, B, T](f: A => B, g: B => T): A => T =
    (x) => g(f(x))

  def superAdder2: (Int => Int => Int) = toCurry(_ + _)
  val add4 = superAdder2(4)
  println(add4(6))

  def simpleAdder: (Int, Int) => Int = fromCurry(superAdder2)
  println(simpleAdder(6, 4))

  val add2 = (x: Int) => x + 2
  val times3 = (x: Int) => x * 3

  val composed = compose(add2, times3)
  val ordered = andThen(add2, times3)
  println(composed(4))
  println(ordered(4))
}
