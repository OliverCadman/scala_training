package lectures.functional_programming

import scala.util.Random

object Sequences extends App {
  val aSequence = Seq(1, 2, 3, 4)

  println(aSequence)
  println(aSequence.reverse)
  println(aSequence(2))
  println(aSequence ++ Seq(5, 6, 7))
  println(Seq(1,6,3,7,10).sorted)


  val aRange = 1 until 10
  println(aRange)

  (1 to 10).foreach(println)

  val aList = List(1, 2, 3)

  val prepended = 42 :: aList
  println(prepended)
  println(42 +: aList :+ 56)

  val apples5 = List.fill(5)("apples")
  println(apples5)
  println(apples5.mkString("-"))

  /**
   * Arrays
   *
   * Can be manually constructed with predefined lengths
   * Can be MUTATED (updated in place)
   * Are interoperable with Java's T[] Arrays
   * Indexing is fast.
   */

  // Arrays
  val numbers = Array(1, 2, 3, 4)
  val fourDefaultInts = Array.ofDim[Int](4) // Populates with default values. If data type is Int, the value is 0.
  val fourDefaultStrings = Array.ofDim[String](4)
  val fourDefaultBools = Array.ofDim[Boolean](4)
  val fourDefaultFloats = Array.ofDim[Float](4)

  fourDefaultInts.foreach(println) // Prints 0
  fourDefaultBools.foreach(println) // Prints false
  fourDefaultStrings.foreach(println) // Prints null
  fourDefaultFloats.foreach(println) // Prints 0.0

  // Mutating Arrays
  numbers(2) = 0  // Syntax sugar = numbers.update(2, 0)
  println(numbers.mkString(" "))

  // Arrays vs. Sequences
  val numbersSeq: Seq[Int] = numbers // Implicit conversion
  println(numbersSeq) // ArraySeq(1, 2, 0, 4)

  // Vectors
  // Vectors are in their own world.
  // They are the the default implementation for mutable collections because they're very efficient.
  // Constant time for read and write: O(log32(n))
  // Fast element addition: append/prepend
  // Implemented as a fixed-branch tree (branch factor 32)
  // Good performance for large sizes

  val noElements = Vector.empty
  val vecNumbers = noElements :+ 1 :+ 2 :+ 3
  val modified = vecNumbers updated (0, 7)

  println(noElements)
  println(vecNumbers)
  println(modified)

  val intVector: Vector[Int] = Vector(1, 2, 3)

  // Vectors vs Lists (write time)
  val maxRuns = 1000
  val maxCapacity = 1000000

  def getWriteTime(collection: Seq[Int]): Double = {
    val random = new Random
    val writeTime = for {
      1 <- 1 to maxRuns
    } yield {
      val currentTime = System.nanoTime()
      collection.updated(random.nextInt(maxCapacity), random.nextInt())
      System.nanoTime() - currentTime
    }
    writeTime.sum * 1.0 / maxRuns
  }

  val listToWrite = (1 to maxCapacity).toList
  var vecToWrite = (1 to maxCapacity).toVector

  println(getWriteTime(listToWrite))
  println(getWriteTime(vecToWrite))

  val optionalInt: Option[Int] = None
  val something = optionalInt match {
    case Some(int) => "int exists"
    case None => "int does not exist"
    case _ => "something else"
  }
  println(something)
}
