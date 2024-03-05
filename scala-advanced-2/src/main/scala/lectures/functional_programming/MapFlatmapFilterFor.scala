package lectures.functional_programming

object MapFlatmapFilterFor extends App {

  val list = List(1, 2, 3)
  println(list)

  // Map
  println(list.map(_ + 1))
  println(list.map(_ + " is a number."))

  // Filter
  println(list.filter(_ % 2 == 0))

  // FlatMap
  val twoPair = (x: Int) => List(x, x + 1)
  println(list.flatMap(twoPair))

  // Print out all combinations between two lists

  val numbers = List(1, 2, 3, 4)
  val characters = List("a", "b", "c", "d")
  val colors = List("black", "white")

  val allCombinations = numbers.flatMap(x => characters.map(y => x + y))
  println(allCombinations)

  // "iterating"
  val colorCombinations = numbers.flatMap(x => characters.flatMap(y => colors.map(c => s"$x$y-$c")))
  println(colorCombinations)

  // foreach
  numbers.foreach(println)

  // For-comprehension
  val forCombinations = for {
    n <- numbers
    c <- characters
    color <- colors
  } yield s"$n$c-$color"

  println(forCombinations)

  // Syntax overload
  list.map { x => x * 2}

  /**
   * 1. See if MyList supports for comprehensions
   * 2. Implement a small collection of AT MOST one element
   *  - Maybe (covariant)
   *  - Only possible subtypes are Empty collection or a Construct with ONE element of type T
   *  - Implement map, flatMap and filter
   */

}
