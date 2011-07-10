package no.kvikshaug.pwkeeper

import java.io.{File, FileNotFoundException}
import java.util.Scanner
import com.codahale.jerkson.Json._

object Pwkeeper {

  val encryptedFile = new File("data")
  val tmpFile = new File("tmp")

  def main(args: Array[String]): Unit = {
    try {
      if(!tmpFile.exists && !encryptedFile.exists) {
        doFirstTimeStuff
        return
      }
      args.toList match {
        case "generate" :: Nil        => println(new String(Pwgen.generate()))
        case "generate" :: len :: Nil => println(new String(Pwgen.generate(len.toInt)))
        case "search" :: Nil          => Searcher.search(readPasswords)
        case "add" :: Nil             => add
        case "edit" :: Nil            => edit
        case "save" :: Nil            => save
        case Nil                      => Searcher.search(readPasswords)
        case _                        => println(help)
      }
    } catch {
      case e => println("Operation failed: " + e)
      System.exit(-1)
    }
  }

  def readPasswords = parse[List[Password]](Crypt.decrypt(IO.read(encryptedFile)))

  def add {
    val s = new Scanner(System.in)
    print("Usage for the new password: ")
    val usage = s.nextLine
    var pw = new String(Pwgen.generate())
    print("Password [" + new String(pw) + "]: ")
    val userPw = s.nextLine
    if(!userPw.isEmpty) {
      pw = userPw
    }
    val json = generate(Password(usage, List(pw)) :: readPasswords)
    val encData = Crypt.encrypt(json.getBytes)
    IO.write(encData, encryptedFile)
  }

  def edit {
    // decrypt the file and write it to a temporary file
    val decryptedData = Crypt.decrypt(IO.read(encryptedFile))
    IO.write(decryptedData, tmpFile)
    println("Plaintext written to: " + tmpFile.getAbsolutePath)
  }

  def save {
    // encrypt the temporary file and overwrite the previous encrypted file
    val data = IO.read(tmpFile)
    val encData = Crypt.encrypt(data)
    IO.write(encData, encryptedFile)
    tmpFile.delete
    println("Removed " + tmpFile.getAbsolutePath)
    println("Wrote encrypted data to " + encryptedFile.getAbsolutePath)
  }

  def doFirstTimeStuff {
    println()
    println("Welcome! Looks like your first time using Pwkeeper.")
    println("=========================================================")
    println("You'll need to give a 32-characted key used to encrypt your passwords.")
    println("Here's a randomly generated one: " + new String(Pwgen.generate(32)))
    println()
    println("Note: Every time you save to the encrypted file, you can specify a new key.")
    println("Warning: If you lose this key, you lose all the passwords saved in the encrypted file.")
    println()
    encryptedFile.createNewFile
    val encData = Crypt.encrypt(List[Byte]('[', ']').toArray)
    IO.write(encData, encryptedFile)
    println()
    println("Passwords are now stored in '" + encryptedFile.getAbsolutePath + "'.")
    println("Call 'pw add' to add a password to it.")
    println("Then call 'pw search' or 'pw edit' to find it again.")
    println()
    println("DON'T LOSE YOUR DECRYPTION KEY!")
  }

  val help = """Arguments:

generate     - generate a password with default length (currently 25)
generate <n> - generate a password with length n
search       - search for a password (default if no argument)
add          - add a new password to the file
edit         - decrypt and save contents to a temporary file for manual editing
save         - encrypt and save the contents of the temporary file"""

}
