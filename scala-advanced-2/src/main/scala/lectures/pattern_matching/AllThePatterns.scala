package lectures.pattern_matching

import lectures.custom_collections.{Cons, MyList, Empty}

object AllThePatterns extends App {

  // 1. Constants
  val x: Any = "Scala"
  val constants = x match {
    case 1 => "A number"
    case "Scala" => "The best language..."
    case true => "the truth"
    case AllThePatterns => "A singleton."
  }

  // 2.1 Match anything
  val matchAnything = x match {
    case _ =>
  }

  // 2.2 Match a variable
  val matchAVariable = x match {
    case something => s"Here we have $something"
  }

  // 3. Tuples
  val aTuple = (1, 2)
  val matchTuple = aTuple match {
    case (1, 1) => "Contains two values: 1 and 1"
    case (something, 2) => s"$something" // PM can be nested with tuples. We can use this 'something' variable.
  }

  println(matchTuple) // 1

  val nestedTuple = (1, (2, 3))
  val matchNestedTuple = nestedTuple match {
    case (_, (v, 3)) => s"Found the value $v"
    case _ => "Found something else"
  }
  println(matchNestedTuple) // Found the value 2

//  // 4. Case classes - Constructor pattern
//  // PMs can be nested with Case Classes aswell
//  val aList: MyList[Int] = new Cons(1, new Cons(2, Empty))
//  val matchList = aList match {
//    case Empty =>
//      // Bind the values that are extracted to 'head', 'subhead' and 'tail'.
//    case Cons(head, Cons(subhead, subtail)) => s"$head, $subhead, $subtail"
//  }
//
//  println(matchList)
//
//  // 5. List patterns
//  val standardList: List[Int] = List(1, 2, 3, 42)
//  val standardListMatch = standardList match {
//    // Match standard list against a list of four things, where 1 is the first element.
//    case List(1, _, _, _) => "Found standard list" // Extractor. List is not a case class. List constructor extractor exists in Scala standard library.
//    case List(1, _*) => "List of arbitrary length, where 1 is the first element."
//    case 1 :: List(_) => "Infix pattern"
//    case List(1, 2, 3) :+ 42 => "Another infix pattern"
//  }
//
//  // 6. Type specifiers
//  val unknown: Any = 2
//  val unknownMatch = unknown match {
//    // Type specifier.
//    case list: List[Int] => "Found a list type."
//    case _ =>
//  }
//
//  println(standardListMatch)
//
//  // 7. Name binding
//  val nameBindingMatch = aList match {
//    case nonEmptyList @ Cons(_, _) => nonEmptyList // You can name entire patterns, not just invididual values.
//    case Cons(1, rest @ Cons(2, _)) => // Name binding inside nested patterns.
//  }
//
//  // 8. Multi-patterns
//  val multiPatternList = aList match {
//    case Empty | Cons(0, _) => // Compound/multi pattern
//  }
//
//  // 9. If-guards
//  val secondElementSpecial = aList match {
//    case Cons(_, Cons(specialElement, _)) if specialElement % 2 == 0 =>
//  }

  /**
   * Question.
   */

  val numbers = List(1, 2, 3)
  val numbersMatch = numbers match {
    case listOfStrings: List[String] => "A list of strings"
    case listOfNumbers: List[Int] => "A list of numbers"
  }

  println(numbersMatch) // A list of strings.

  /**
   * Why does this happen? This is a JVM issue. The JVM was designed for the Java language,
   * but Java was designed with backwards-compability in mind. Java 1 did not have generic parameters.
   * Generics were introduced in Java 5. To make the JVM compatible with Java 1, the compiler erased
   * all generic types after type-checking, which makes the JVM oblivious to generic types.
   *
   */
}
