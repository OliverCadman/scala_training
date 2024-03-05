package lectures.functions
import scala.annotation._

object Functions extends App {

  /**
   *    factorial
        fibonacci
        isPrime
   */

  def factorial(n: Int) = {
    @tailrec
    def factHelper(x: Int, acc: BigInt): BigInt = {
      if (x <= 1) acc
      else factHelper(x - 1, acc * x)
    }

    factHelper(n, 1)
  }

//  println(factorial(1000))

  def fibonacci(n: Int): BigInt = {
    @tailrec
    def fibHelper(x: Int, acc1: BigInt, acc2: BigInt): BigInt = {
      if (x >= n) acc1
      else fibHelper(x + 1, acc1 + acc2, acc1)
    }

    if (n <= 2) 1
    else fibHelper(2, 1, 1)
  }

  println(fibonacci(10))

  def isPrime(n: Int): Boolean = {
    @tailrec
    def isPrimeUntil(x: Int, isStillPrime: Boolean): Boolean = {
      if (!isStillPrime) false
      else if (x <= 1) true
      else isPrimeUntil(x - 1, n % x != 0 && isStillPrime)
    }

    isPrimeUntil(n / 2, isStillPrime = true)
  }

  println(isPrime(11))
}
