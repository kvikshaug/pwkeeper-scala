package no.kvikshaug.pwkeeper

import java.io.{IOException, ByteArrayOutputStream, InputStream}

object Console {

  var originalState = stty("-g").trim

  def setTerminal {
    // set the console to be character-buffered instead of line-buffered
    stty("-icanon min 1")

    // disable character echoing
    stty("-echo")
  }

  def restoreTerminal = stty(originalState)

  def stty(args: String) = exec(List[String]("sh", "-c", "stty " + args + " < /dev/tty").toArray)

  def exec(cmd: Array[String]) = {
    val out = new ByteArrayOutputStream
    val p = Runtime.getRuntime.exec(cmd)
    def read(in: InputStream): Unit = in.read match {
      case -1 => return
      case c  => out.write(c); read(in)
    }
    read(p.getInputStream)
    read(p.getErrorStream)
    p.waitFor
    new String(out.toByteArray)
  }
}
