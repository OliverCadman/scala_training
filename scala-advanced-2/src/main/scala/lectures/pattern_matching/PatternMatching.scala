package lectures.pattern_matching

import scala.util.Random

object PatternMatching extends App {

  val random = new Random()
  val x = random.nextInt(4) // Any number from 0 - 10

  // Pattern matching tries to match a value against multiple patterns.
  val description = x match {
    case 1 => "Snake eyes"
    case 2 => "Doop de do"
    case 3 => "Third time's a charm."
    case _ => "Something else." // WILDCARD
  }

  println(x)
  println(description)

  /**
   * Pattern matching looks like a switch in Java, C, C++ etc. But PATTERN MATCHING IS MUCH MORE POWERFUL.
   *
   * The first interesting property of pattern matching is the ability to decompose values.
   * This is particularly the case with Case Classes.
   *
   * Case classes have the ability to be deconstructed/extracted with pattern matching.
   */

  case class Person(name: String, age: Int)

  val bob = Person("Bob", 20)

  val greeting = bob match {
    case Person(name, age) => s"Hi my name is $name and I am $age years old."
    case _ => "I don't know who I am!"
  }
  println(greeting) // Hi my name is Bob and I am 20 years old.

  /**
   * NOTE: CASES ARE MATCHED IN ORDER FROM TOP TO BOTTOM.
   */

  val greetingWithGuards = bob match {
   case Person(name, age) if age < 21 => s"My name is $name and I am a minor."
   case Person(name, age) if age > 21 => s"My name is $name. Let's go to the bar."
   case _ => "I don't know my name or age."
 }

  println(greetingWithGuards)

  /**
   * The type of a pattern matching expression is the UNIFIED TYPE OF ALL THE CASES.
   */

  /**
   * Pattern matching on sealed hierarches...
   */
  sealed class Animal
  case class Dog(breed: String) extends Animal
  case class Parrot(greeting: String) extends Animal

  val animal: Animal = new Dog("Terranova")
  animal match {
    case Dog(breed) => println(s"Matched a dog with breed $breed")

  }

  /**
   * Exercises
   *
   * Let's say we have a simple class hierarchy...
   */

  trait Expr
  case class Number(n: Int) extends Expr
  case class Sum(e1: Expr, e2: Expr) extends Expr
  case class Product(e1: Expr, e2: Expr) extends Expr

  /**
   * Write a simple function that uses pattern matching and takes an expression
   * as a parameter, and returns the human-readable form of it.
   *
   * Sum(Number(2), Number(3)) = 2 + 3
   * Sum(Number(2), Number(3)
   */

  def show(expr: Expr): String = expr match {
    case Number(n) => s"$n"
    case Sum(e1, e2) => s"${show(e1)} + ${show(e2)}"
    case Product(e1, e2) => {
      def maybeShowParens(exp: Expr): String = exp match {
        case Product(_, _) => show(exp)
        case Number(_) => show(exp)
        case _ => s"(${show(exp)})"
      }

      s"${maybeShowParens(e1)} * ${maybeShowParens(e2)}"
    }
  }

  val expr = Product(
    Product(Number(3), Number(6)),
    Sum(
      Product(
        Number(6),
        Sum(Number(10), Number(45))
      ),
      Number(10)
    )
  )

  println(show(expr))
}
