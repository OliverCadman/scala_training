package lectures.functional_programming

import scala.util.Random

object Options extends App {

  /**
   * Options are a special type of 'collection' in Scala.
   * Options solve the 'billion-dollar mistake' i.e. the NullPointerException.
   *
   * Null references are the bain of our existence. They cause crashes, called NullPointerExceptions.
   *
   * To guard against these null pointer exceptions we often resort to all sorts of checks, which leads
   * to all sorts of spaghetti code and things which are hard to understand.
   *
   * So we would need some kind of data type that could encapsulate the possible absence of a value.
   *
   * These are Options. Options are a wrapper for a value that might be absent.
   *
   * Options are present in many places throughout the Scala standard library.
   * For example, in Maps, when you want to get a value associated with a key, instead of calling the .apply()
   * method, you call the .get() method, which will return the value if it exists, or None otherwise.
   */

  val myFirstOption: Option[Int] = Some(2)
  val noOption: Option[Int] = None

  println(myFirstOption)
  println(noOption)

  // Options were invented to deal with UNSAFE APIs.

  def superUnsafeMethod: String = null

  val result = Some(superUnsafeMethod) // WRONG
  println(superUnsafeMethod)

  val resultSafe: Option[String] = Option(superUnsafeMethod) // Some or None
  println(resultSafe)

  // The point of Options is that we should never do null checks ourselves. The Option will do this for us.

  // Chained methods
  def backupMethod(): String = "A Valid Result."

  val chainedResult = Option(superUnsafeMethod).orElse(Option(backupMethod()))
  println(chainedResult)

  // If you want to design unsafe APIs...
  // Make your methods return Option() of something, instead of returning nulls.

  def aBetterUnsafeMethod(): Option[String] = None
  def aBetterBackupMethod(): Option[String] = Some("A valid result.")

  val betterChainedResult = aBetterUnsafeMethod().orElse(aBetterBackupMethod())
  println(betterChainedResult)

  // Functions on Options
  println(myFirstOption.isEmpty) // false
  println(myFirstOption.get) // Retrieves a value from an option. This is UNSAFE. DO NOT USE THIS. BREAKS THE WHOLE OPTION IDEA.

  println(myFirstOption.map(_ * 2))
  println(myFirstOption.filter(_ % 2 != 0))
  println(myFirstOption.flatMap(x => Option(x * 10)))

  // for comprehensions

  /**
   * Assuming you're given an API from some other programmers,
   * you're given a val called config which is a network configuration: Map[String, String]
   *
   * It has the following associations:
   *
   *  host - 176.45.36.1
   *  post - 8080
   *
   */

  val config: Map[String, String] = Map(
    "host" -> "176.45.36.1",
    "port" -> "8080"
  )
  /**
   * You're given a class called 'Connection'. You're purpose is to establish one such connection.
   * This Connection class has a 'connect' method, which just returns a string 'connected'.
   * (In reality, this would connect to some server)
   *
   * The companion object for this Connection would have an apply method which receives a host: String, and a port: String,
   * and this returns an Option[Connection].
   *
   * Try to establish a connection - if so, print the connect method
   */

  class Connection {
    def connect: String = "Connection established."
  }

  object Connection {
    val random = new Random(System.nanoTime())
    def apply(host: String, port: String): Option[Connection] = {
      if (random.nextBoolean()) Some(new Connection)
      else None
    }
  }

  val host = config.get("host")
  val port = config.get("port")

  /**
   * if (h != null) {
   *  if (p != null) {
   *    return Connection.apply(h, p)
   *  }
   *
   *  return null
   * }
   */

  val connection = host.flatMap(h => port.flatMap(p => Connection(h, p).map(c => c.connect)))
  connection.foreach(println)


  /**
   * if (c != null) {
   *  return c.connect
   * } else {
   *  return null
   * }
   */


  class Action {
    def act: String = "Let's do something!"
  }

  object Action {
    def apply(): Option[Action] = {
      val random = new Random()
      if (random.nextBoolean()) Some(new Action)
      else None

    }
  }

  def mayOrMayNotReturn(): Option[Boolean] = {
    val random = new Random()
    if (random.nextBoolean()) Some(true)
    else None
  }

  val action = Action()

  val mayOrMayNotExist = action.map(f => f.act)
  mayOrMayNotExist.foreach(println)

  val connectionForComp = for {
    host <- config.get("host")
    port <- config.get("port")
    connection <- Connection(host, port)
  } yield {
    connection.connect
  }

  connectionForComp.foreach(println)
}
