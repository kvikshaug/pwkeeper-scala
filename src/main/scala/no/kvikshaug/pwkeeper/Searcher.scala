package no.kvikshaug.pwkeeper

object Searcher {

  var buffer = List[Char]()
  var continue = true

  def search(passwords: List[Password]) {
    Console.setTerminal
    while(true) {
      clear
      printHits(passwords)
      println
      printPrompt
      System.in.read match {
        case 4   => println; Console.restoreTerminal; return // EOT
        case 10  => println; Console.restoreTerminal; return // LF
        case 27  => println; Console.restoreTerminal; return // ESC
        // emulate æøåÆØÅ
        case 195 => System.in.read match {
          case 166 => buffer = buffer :+ 'æ'
          case 184 => buffer = buffer :+ 'ø'
          case 165 => buffer = buffer :+ 'å'
          case 134 => buffer = buffer :+ 'Æ'
          case 152 => buffer = buffer :+ 'Ø'
          case 133 => buffer = buffer :+ 'Å'
        }
        // backspace
        case 127 => buffer = buffer.take(buffer.length - 1)
        case c => buffer = buffer :+ c.toChar
      }
    }
  }

  def printPrompt = print("Search > " + new String(buffer.toArray))
  def printHits(passwords: List[Password]) = passwords foreach { x =>
    if(x.usage.contains(new String(buffer.toArray))) {
      println(x.usage + tabs(x.usage.length) + " '" + new String(x.value.toArray) + "'")
    }
  }
  // these are the characters outputted by /usr/bin/clear to clear the screen
  def clear = print(new String(List[Byte](27, 91, 72, 27, 91, 50, 74).toArray))

  val tabsize = 8
  val columns = 4
  def tabs(len: Int) = "\t" * (columns - (len / tabsize))
}
