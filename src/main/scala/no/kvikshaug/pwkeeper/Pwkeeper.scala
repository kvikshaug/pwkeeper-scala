package no.kvikshaug.pwkeeper

import javax.crypto.Cipher
import javax.crypto.spec.{SecretKeySpec, IvParameterSpec}
import java.io.{BufferedOutputStream, FileOutputStream, BufferedInputStream, FileInputStream, File}
import java.io.ByteArrayOutputStream
import java.util.Scanner

object Pwkeeper {

  val encryptedFile = new File("data")

  def main(args: Array[String]): Unit = {
    args.toList match {
      case "generate" :: Nil        => println(Pwgen.generate())
      case "generate" :: len :: Nil => println(Pwgen.generate(len.toInt))
      case "read" :: Nil     => read
      case "save" :: Nil     => save
      case "search" :: Nil          => search()
      case Nil                      => search()
      case _ => println("Wrong usage.")
    }
  }

  def read {
    if(!encryptedFile.exists) {
      println("The expected encrypted file doesn't exist at '" + encryptedFile.getAbsolutePath + "'.")
      return
    }

    try {
      // decrypt the file and write it to a temporary file
      val decryptedData = Crypt.readAndDecrypt(encryptedFile)
      writeFile(decryptedData, "tmp")
    } catch {
      case e => println("Decryption failed: " + e)
    }
  }

  def save {
    // encrypt the temporary file and overwrite the previous encrypted file
    val data = readFile(temporaryFile)
    val encData = encrypt(data, key)
    writeFile(encData, encryptedFile)
  }

  def writeFile(array: Array[Byte], f: File) = {
    if(!f.exists) {
      f.createNewFile
    }
    val b = new BufferedOutputStream(new FileOutputStream(f))
    b.write(array, 0, array.size)
    b.close
  }
}
