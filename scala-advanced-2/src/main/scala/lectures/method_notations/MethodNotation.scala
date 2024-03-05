package lectures.method_notations

import scala.language.postfixOps

object MethodNotation extends App {

  class Person(val name: String, val age: Int, favouriteMovie: String) {
    /**
     * Operator notation
     * Postfix notation(anObject 'doesFunction')
     * Infix notation
     * Prefix notation
     */

    def isAlive: Boolean = true
    def likes(person: Person): String = s"$name likes ${person.name}"
    def learns(subject: String): String = s"$name is learning $subject."
    def learnsScala: String = learns("Scala")
    def +(person: Person): String = s"$name is hanging out with ${person.name}"
    def +(nickname: String): Person = new Person(s"$name $nickname", age, favouriteMovie)
    def unary_! : String = "Shut ya mout"
    def unary_+ : Person = new Person(name, age + 1, favouriteMovie)
    def apply(): String = s"$name's favourite movie is $favouriteMovie"
    def apply(n: Int): String = s"$name has watched $favouriteMovie $n times."

    override def toString: String = name

  }

  val oliver = new Person("Oliver", 33, "Old Boy")
  val dani = new Person("Dani", 29, "Parent Trap")

  println(oliver likes dani)
  println(oliver learns "Scala")
  println(oliver learnsScala)
  println(oliver())
  println(oliver(999))
  println(oliver + dani)
  println((+oliver).age)
  println(oliver isAlive)
  println(!oliver)
  println(oliver + "the new torchbearer of the forgiven wasteland.")
}
