package lectures.functional_programming

object TuplesAndMaps extends App {

  // Tuples
  val aTuple = new Tuple2[Int, String](1, "Hello Scala")
  val aMoreConciseTuple = (1, "Hello Scala, but it is nicer")
  println(aTuple)
  println(aMoreConciseTuple)
  println(aTuple._1) // 1
  println(aTuple._2) // Hello Scala
  println(aTuple.copy(_2 = "Goodbye Java"))
  println(aTuple.swap) // ("Hello Scala", 2)

  // Maps - associate keys to values.
  val anEmptyMap: Map[String, Int] = Map()
  val phoneBook= Map(
    "Jim" -> 555, // Syntax sugar for Tuple2("Jim", 555)
    "Daniel" -> 789
  ).withDefaultValue(-1)

  println(phoneBook)

  // Map operations
  println(phoneBook.contains("Jim")) // true
  println(phoneBook("Jim")) // 555
  println(phoneBook("Mary")) // Throws NoSuchElementException. Initialize map withDefaultValue ^^^^


  val newPairing = "Mary" -> 678
  val newPhoneBook = phoneBook + newPairing
  println(newPhoneBook)

  // Functionals on Maps
  // Maps have .map(), .flatMap() and .filter()!

  println(phoneBook.map(pair => pair._1.toLowerCase())) // List(jim, daniel)

  // filterKeys: DEPRECATED. Use .filter instead, calling .toMap if required to return the result as a map.
  println(phoneBook.filter((pair) => pair._1.startsWith("J")))

  // mapValues: DEPRECATED. Use .map instead, calling .toMap if required to return result as map.
  // YOU HAVE TO RETURN THE ENTIRE TUPLE. pair._1 -> pair._2. Returning pair._1 isn't enough.
  println(phoneBook.map(pair => pair._1.toUpperCase() -> pair._2).toMap)

  println(phoneBook.map(pair => pair._1 -> ("0245 - " + pair._2)).toMap)

  // Conversions to other collections
  println(phoneBook.toList) // List((Jim, 555), (Daniel, 789))
  println(List(("Daniel", 789)).toMap) // Map(Daniel -> 789)

  val names = List("Bob", "James", "Angela", "Barry", "Daniel", "Jim")
  println(names.groupBy(name => name.charAt(0)))
  // HashMap(J -> List(James, Jim), A -> List(Angela), B -> List(Bob, Barry), D -> List(Daniel))

  /**
   * Exercise
   *
   * 1. What would happen if I had two original entries "Jim -> 555" "JIM -> 9000"
   *    and I mapped over the keys to make them uppercase?
   *
   * 2. Overly simplified social network based on maps
   *    - Person(name: String)
   *    - add a person to the network
   *    - remove person from the network
   *    - friend (mutual)
   *    - unfriend
   *
   *    - number of friends of a person
   *    - person with most friends
   *    - how many people have NO friends
   *    - if there is a social connection between two people (direct or not)
   */

  // 1
  val phonebookWithUppercaseJim = phoneBook + ("JIM" -> 9000)
  println(phonebookWithUppercaseJim)
  println(phonebookWithUppercaseJim.map(pair => pair._1.toUpperCase() -> pair._2)) // Map(JIM -> 9000, DANIEL -> 789) "Jim" is lost!!


  // 2
  case class Person(name: String)

  def addToNetwork(network: Map[Person, Set[Person]],
                   newPerson: (Person, Set[Person])): Map[Person, Set[Person]] = {
    if (network.contains(newPerson._1)) network
    else network + newPerson
  }

  def removeFromNetwork(network: Map[Person, Set[Person]], person: Person): Map[Person, Set[Person]] = {
    val newNetwork = network.map(user => user._1 -> user._2.filterNot(p => p == person))
    newNetwork - person
  }
  // Update function to remove person from friends that have the person in their friends list.

  def addFriend(network: Map[Person, Set[Person]], personOne: Person, personTwo: Person): Map[Person, Set[Person]] = {

    if (!network.contains(personOne) || !network.contains(personTwo)) network

    else {
      val befrienderList = network(personOne)
      val befriendedList = network(personTwo)

      network + (personOne -> (befrienderList + personTwo)) + (personTwo -> (befriendedList + personOne))
    }
  }

  def removeFriend(network: Map[Person, Set[Person]], personOne: Person, personTwo: Person): Map[Person, Set[Person]] = {

    val unfrienderList = network(personOne)
    val unfriendedList = network(personTwo)

    network + (personOne -> unfrienderList.filterNot(person => person == personTwo)) +
      (personTwo -> unfriendedList.filterNot(person => person == personOne))
  }

  def numberOfFriends(network: Map[Person, Set[Person]], person: Person): String = {
    val numFriends = network(person).size
    if (numFriends == 1) s"${person.name} has $numFriends friend."
    else s"${person.name} has $numFriends friends."
  }

  def mostFriends(network: Map[Person, Set[Person]]): String = {
    val mostFriends = network.maxBy(person => person._2.size)._1
    s"${mostFriends.name} has the most friends."
  }

  def noFriends(network: Map[Person, Set[Person]]): String = {
    val noFriendsCount = network.count(person => person._2.isEmpty)
    if (noFriendsCount == 1) s"$noFriendsCount person has no friends."
    else s"$noFriendsCount people have no friends."
  }

  def socialConnection(network: Map[Person, Set[Person]], personA: Person, personB: Person): String = {
    @scala.annotation.tailrec
    def go(consideredPeople: Set[Person], discoveredPeople: Set[Person], target: Person): Option[(Person, Person)] = {
      if (discoveredPeople.isEmpty) None
      else {
        val person = discoveredPeople.head
        if (person == target) Some((personA, personB))
        else if (consideredPeople.contains(person)) go(consideredPeople, discoveredPeople.tail, target)
        else go(consideredPeople + person, discoveredPeople.tail ++ network(person), target)
      }
    }
    val connection = go(Set.empty[Person], network(personA) + personA, personB)
    if (connection.isDefined) s"${personA.name} has a connection with ${personB.name}"
    else s"${personA.name} does not share a connection with ${personB.name}"

  }

  val network: Map[Person, Set[Person]] = Map()
  val mary = Person("Mary")
  val bob = Person("Bob")


  val newNetwork = addToNetwork(network, (mary, Set.empty[Person]))
  println(newNetwork)

  val newNetwork2 = addToNetwork(newNetwork, (bob, Set.empty[Person]))
  println(newNetwork2)



  val mike = Person("Mike")
  val newNetwork3 = addToNetwork(newNetwork2, (mike, Set.empty[Person]))
  val friendedNetwork2 = addFriend(newNetwork3, mary, mike)
  println(friendedNetwork2)

  val unfriendedNetwork = removeFriend(newNetwork2, mary, bob)
  println(unfriendedNetwork)

  println(numberOfFriends(friendedNetwork2, mary))
  println(numberOfFriends(friendedNetwork2, mike))



  val oliver = Person("Oliver")
  val newNetwork4 = addToNetwork(newNetwork3, (oliver -> Set.empty[Person]))
  println(noFriends(newNetwork4))

  val friendedNetwork5 = addFriend(newNetwork4, oliver, mike)
  println(friendedNetwork5)
  val friendedNetwork6 = addFriend(friendedNetwork5, mike, mary)
  println(socialConnection(friendedNetwork6, oliver, mary))

  val dani = Person("Dani")
  val newNetwork5 = addToNetwork(friendedNetwork6, (dani, Set.empty[Person]))

  val friendedNetwork7 = addFriend(newNetwork5, dani, bob)
  println(friendedNetwork7)
  println(socialConnection(friendedNetwork7, oliver, dani))
}
