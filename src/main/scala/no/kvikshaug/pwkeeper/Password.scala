package no.kvikshaug.pwkeeper

object Password {
  val tabsize = 8
  val columns = 4
}

case class Password(usage: String, values: List[String]) {
  override def toString = {
    val sb = new StringBuilder
    sb.append(usage).append(tabs(usage.length)).append(" '").append(values(0)).append("'")
    values.tail.foreach { value =>
      sb.append("\n").append(tabs(0)).append(" '").append(value).append("'")
    }
    sb.toString
  }

  def tabs(len: Int) = "\t" * (Password.columns - (len / Password.tabsize))
}
