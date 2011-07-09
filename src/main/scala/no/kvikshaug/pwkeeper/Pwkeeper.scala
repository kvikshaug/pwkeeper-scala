package no.kvikshaug.pwkeeper

import java.io.{File, FileNotFoundException}
import java.util.Scanner

object Pwkeeper {

  val encryptedFile = new File("data")
  val tmpFile = new File("tmp")

  def main(args: Array[String]): Unit = {
    try {
      args.toList match {
        case "generate" :: Nil        => println(Pwgen.generate())
        case "generate" :: len :: Nil => println(Pwgen.generate(len.toInt))
        case "read" :: Nil            => read
        case "save" :: Nil            => save
        case "search" :: Nil          => search()
        case Nil                      => search()
        case _ => println("Wrong usage.")
      }
    } catch {
      case e => println("Operation failed: " + e)
      System.exit(-1)
    }
  }

  def search() {}

  def read {
    // decrypt the file and write it to a temporary file
    val decryptedData = Crypt.decrypt(IO.read(encryptedFile))
    IO.write(decryptedData, tmpFile)
    println()
  }

  def save {
    // encrypt the temporary file and overwrite the previous encrypted file
    val data = IO.read(tmpFile)
    val encData = Crypt.encrypt(data)
    IO.write(encData, encryptedFile)
  }
}
