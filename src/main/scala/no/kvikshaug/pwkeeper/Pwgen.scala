object Pwgen {
  val r = new java.util.Random
  val defaultPwLength = 10

  def main(args: Array[String]): Unit = {
    val pwLength = if(args.length > 0) args(0).toInt
                   else defaultPwLength
    for(i <- 1 to pwLength) {
      // we'll make it 1/3rd for lower, upper or number char
      val c = r.nextInt(3)
      if(c == 0) {
        print(randomLowerChar)
      } else if(c == 1) {
        print(randomUpperChar)
      } else if(c == 2) {
        print(randomNumber)
      }
    }
    println()
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
