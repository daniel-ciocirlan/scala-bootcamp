package part1essentials

object StringOperations {

  val aString = "Scala " + "is awesome" // "Scala is awesome"
  val stringWithNumber = "Scala" + 3 // "Scala3"

  // string functions
  // identifying characters
  val secondChar = aString.charAt(1) // strings are 0-indexed
  // string pieces
  val firstWord = aString.substring(0, 5) // "Scala"
  val everythingWithoutFirstWord = aString.substring(5) // " is awesome"
  // splitting into pieces delimited by a set of chars
  val words = aString.split(" ")
  // testing prefixes
  val startsWithScala = aString.startsWith("Scala") // returns a Boolean, true in this case
  // replacing tokens with other strings
  val allDashes = aString.replace(" ", "-----")
  // convert a string to lower/uppercase
  val allUppercase = aString.toUpperCase() // also toLowerCase
  // length
  val nChars = aString.length
  // reversing
  val reversed = aString.reverse
  // parse numerical values
  val numberAsString = "42"
  val meaningOfLife = numberAsString.toInt

  // interpolation
  // s interpolator
  val name = "Alice"
  val age = 12
  val greetingRegular = "Hi, I'm " + name + " and I'm " + age + " years old."
  val greetingInterpolator = s"Hi, I'm $name and I'm $age years old." // JS: `Hi, I'm ${name} and I'm ${age} years old.`
  val greetingOneYearLater = s"Hi, I'm $name and I'm now ${age + 1 /* any expression works! */}" +
    s" years old and I'm learning from \"Rock the JVM\""
  /*
    Escape sequences:
      \' - single quote
      \" - double quote "
      \n - new line
      \t - tab
      \\ - single backslash
   */

  // f interpolator
  val speed = 1.2f
  val myth = f"$name can eat $speed%2.4f burgers per minute."
  //                                  ^ exactly this many decimals (rounded or expanded with zeros)
  //                                ^ at least this many significant digits

  // raw interpolator prints things literally, ignores escape sequences
  // used rarely
  val aQuote = '\"'
  val withEscapes = "This is a \n newline"
  val noEscapes = raw"This is a \n $aQuote newline" // can inject variables holding special characters

  def main(args: Array[String]): Unit = {
    println(secondChar)
    println(firstWord)
    println(everythingWithoutFirstWord)
    println(words)
    println(startsWithScala)
    println(allDashes)
    println(allUppercase)
    println(nChars)
    println(myth)
    println(withEscapes)
    println(noEscapes)
  }
}
