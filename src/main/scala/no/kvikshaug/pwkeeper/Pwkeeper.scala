package no.kvikshaug.pwkeeper

import java.io.{File, FileNotFoundException}
import java.util.Scanner
import com.codahale.jerkson.Json._

object Pwkeeper {

  val encryptedFile = new File("data")
  val tmpFile = new File("tmp")

  var passwords: List[Password] = Nil

  def main(args: Array[String]): Unit = {
    try {
      args.toList match {
        case "generate" :: Nil        => println(new String(Pwgen.generate()))
        case "generate" :: len :: Nil => println(new String(Pwgen.generate(len.toInt)))
        case "search" :: Nil          => search()
        case "add" :: Nil             => add
        case "edit" :: Nil            => edit
        case "save" :: Nil            => save
        case Nil                      => search()
        case _                        => println(help)
      }
    } catch {
      case e => println("Operation failed: " + e)
      System.exit(-1)
    }
  }

  def search() {}

  def add {
    val s = new Scanner(System.in)
    print("Usage for the new password: ")
    val usage = s.nextLine
    var pw = Pwgen.generate()
    print("Password [" + new String(pw) + "]: ")
    val userPw = s.nextLine.getBytes
    if(!userPw.isEmpty) {
      pw = userPw
    }
    val decryptedData = Crypt.decrypt(IO.read(encryptedFile))
    passwords = Password(usage, pw.toList) :: parse[List[Password]](decryptedData)
    val json = generate(passwords)
    val encData = Crypt.encrypt(json.getBytes)
    IO.write(encData, encryptedFile)
  }

  def edit {
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
    tmpFile.delete
  }

  val help = """Arguments:

generate     - generate a password with default length (currently 25)
generate <n> - generate a password with length n
search       - search for a password (default if no argument)
add          - add a new password to the file
edit         - decrypt and save contents to a temporary file for manual editing
save         - encrypt and save the contents of the temporary file"""

}
