package lectures.functional_programming

import scala.util.{Failure, Random, Success, Try}

object TryObject extends App {

  val aSuccess = Success(2)
  val aFailure = Failure(new RuntimeException("EPIC FAIL."))

  def unsafeMethod: String = throw new RuntimeException("No string for you mate.")

  // Create Try objects via the apply method
  val potentialFailure = Try(unsafeMethod)
  println(potentialFailure)

  // Syntax Sugar
  val potentialFailureSugared = Try {
    unsafeMethod
  }
  println(potentialFailureSugared)

  // Utilities
  println(potentialFailure.isSuccess)
  println(potentialFailure.isFailure)

  // orElse
  def backupMethod: String = "A valid result."
  val fallbackTry = Try(unsafeMethod).orElse(Try(backupMethod))
  println(fallbackTry)

  // If you design the API...
  def betterUnsafeMethod: Try[String] = Try(throw new RuntimeException("No string for you mate, but better!"))
  def betterFallbackMethod: Try[String] = Try("A valid result")

  val betterFallbackTry = betterUnsafeMethod orElse betterFallbackMethod
  println(betterFallbackTry)

  // Map, FlatMap, Filter

  println(aSuccess.map(_ * 2)) // Success(4)
  println(aSuccess.flatMap(x => Success(x * 10))) // Success(20)
  println(aSuccess.filter(_ % 2 != 0)) // Failure(java.util.NoSuchElementException: Predicate does not hold for 2)
  // Can also use for-comprehensions!

  val hostname = "host"
  val port = "8080"

  def renderHTML(page: String): Unit = println(page)

  class Connection {
    def get(url: String): String = {
      val random = new Random(System.nanoTime())
      if (random.nextBoolean()) "<html>...</html>"
      else throw new RuntimeException("Connection interrupted.")
    }

    def getSafe(url: String): Try[String] = Try(get(url))
  }

  object HttpService {
    val random = new Random(System.nanoTime())

    def getConnection(host: String, port: String): Connection = {
      if (random.nextBoolean()) new Connection
      else throw new RuntimeException("Someone else took the port.")
    }

    def getSafeConnection(host: String, port: String): Try[Connection] = Try(getConnection(host, port))
  }

  // If you get the HTML page from Connection, print it to the console.
//  val connection = HttpService.getSafeConnection(hostname, port)
//  connection.flatMap(conn => conn.getSafe("/home")).foreach(renderHTML)

  for {
    conn <- HttpService.getSafeConnection(hostname, port)
    page <- conn.getSafe("/home")
  } yield renderHTML(page)
}
