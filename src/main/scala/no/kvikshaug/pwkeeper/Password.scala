package no.kvikshaug.pwkeeper

object Password {
  val tabsize = 8
  val columns = 4
}

case class Password(usage: String, values: List[List[Byte]]) {
  override def toString = {
    val sb = new StringBuilder
    sb.append(usage).append(tabs(usage.length)).append(" '").append(new String(values(0).toArray)).append("'")
    values.tail.foreach { value =>
      sb.append("\n").append(tabs(0)).append(" '").append(new String(value.toArray)).append("'")
    }
    sb.toString
  }

  def tabs(len: Int) = "\t" * (Password.columns - (len / Password.tabsize))
}
