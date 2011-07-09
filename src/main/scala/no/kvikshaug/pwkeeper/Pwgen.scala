package no.kvikshaug.pwkeeper

object Pwgen {
  val r = new java.util.Random

  def generate(len: Int = 10) = {
    val sb = new StringBuilder
    for(i <- 1 to len) {
      // we'll make it 1/3rd for lower, upper or number char
      r.nextInt(3) match {
        case 0 => sb.append(randomLowerChar)
        case 1 => sb.append(randomUpperChar)
        case 2 => sb.append(randomNumber)
      }
    }
    sb.toString
  }

  def randomLowerChar: Char = {
    return (r.nextInt(25) + 97).toChar
  }

  def randomUpperChar: Char = {
    return (r.nextInt(25) + 65).toChar
  }

  def randomNumber: Char = {
    return (r.nextInt(10) + 48).toChar
  }
}
