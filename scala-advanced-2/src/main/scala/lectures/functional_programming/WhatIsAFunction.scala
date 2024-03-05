package lectures.functional_programming

object WhatIsAFunction extends App {

  // Functions = First class elements
  // Functions can be treated as values

  val doubler = new MyFunction[Int, Int] {
    override def apply(elem: Int):Int  = elem * 2
  }

  val stringToInt = new MyFunction[String, Int] {
    override def apply(elem: String): Int = elem.toInt
  }
  println(doubler(2))
  println(stringToInt("2") * 2)

  trait MyFunction[A, B] {
    def apply(elem: A): B
  }

  val stringToInt2 = new Function1[String, Int] {
    override def apply(v1: String): Int = v1.toInt
  }

  println(stringToInt2("2") * 2)

  val adder: (Int, Int) => Int = new Function2[Int, Int, Int] {
    override def apply(v1: Int, v2: Int): Int = v1 + v2
  }

  println(adder(2, 3))

  def stringConcat: (String, String) => String = new Function2[String, String, String] {
    override def apply(v1: String, v2: String): String = s"$v1 $v2"
  }

  println(stringConcat("This is", "a string."))
  /*
    1.  a function which takes 2 strings and concatenates them
    2.  transform the MyPredicate and MyTransformer into function types
    3.  define a function which takes an int and returns another function which takes an int and returns an int
        - what's the type of this function
        - how to do it
   */

  def curriedMultiplier: Function1[Int, Function1[Int, Int]] = new Function1[Int, Function1[Int, Int]] {
    override def apply(v1: Int): Int => Int = new Function1[Int, Int] {
      override def apply(v2: Int): Int = v1 * v2
    }
  }

  def curriedMultiplierAnon: Int => (Int => Int) = (x: Int) => (y: Int) => x * y

  val times3 = curriedMultiplierAnon(3)

  println(times3(3))
}
