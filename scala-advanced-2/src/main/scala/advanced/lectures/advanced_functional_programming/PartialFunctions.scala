package advanced.lectures.advanced_functional_programming

object PartialFunctions extends App {

  /**
   * A partial function is a function applicable to a subset of data that it has been defined for.
   *
   * For example, we can define a function on the Int domain that operates only on odd numbers...
   *
   * A partial function:
   *  - Is a 'unary' operation, which means it only takes one parameter.
   *  - Is applicable to a subdomain of values
   *  - Can explicitly include an 'isDefinedAt' method, to define its domain, and an 'apply' method.
   */

  val aTotalFunction: Int => Int = (x: Int) => x + 1

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 59
    else if (x == 3) 100
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 59
    case 3 => 100
    case _ => "Not found."
  }

  println(aNicerFussyFunction(10))

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 59
    case 3 => 100
  }

  println(aPartialFunction(2))

  // PF Utilities
  println(aPartialFunction.isDefinedAt(1)) // true
  println(aPartialFunction.isDefinedAt(100)) // false

  // lift
  val lifted = aPartialFunction.lift // Int => Int to Int => Option[Int]
  println(lifted(100)) // None

  // Chains
  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 42 => 999
  }

  println(pfChain.lift(44))

  // PFs extend normal/total functions
  val aTotalFunctionTwo: Int => Int = {
    case 1 => 42
    case _ => -1
  }

  // HOFs can also be partial functions
  val aMappedList = List(1, 2, 3).map {
    case 1 => 42
    case 2 => 59
    case 4 => 100
    case _ => -1
  }

  println(aMappedList)

  /**
   * Create a PF instance yourself.
   */

  val aPartialFunctionInstance: PartialFunction[Int, Int] = new PartialFunction[Int, Int] {
    override def apply(v1: Int): Int = v1 match {
      case 1 => 42
      case 2 => 59
      case 3 => 100
    }

    override def isDefinedAt(x: Int): Boolean = x == 1 || x == 2 || x == 3
  }
  println(aPartialFunctionInstance.isDefinedAt(3))
  println(aPartialFunctionInstance(3))

  val chatbot: PartialFunction[String, String] = {
    case "Hello" => "Hi, how are you doing?"
    case "I'm fine thank you, and you?" => "I'm doing fine thanks."
    case "Goodbye" => "See you later!"
    case _ => "I don't have a response for this."
  }

  def runner: Unit = {
    scala.io.Source.stdin.getLines().map(chatbot).foreach(line => {
      if (line == "See you later!") {
        println(line)
        System.exit(0)
      } else println(line)
    })
  }

  runner
}
