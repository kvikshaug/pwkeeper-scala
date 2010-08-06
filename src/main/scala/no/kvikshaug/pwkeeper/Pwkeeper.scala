package no.kvikshaug.pwkeeper

import javax.crypto.Cipher
import javax.crypto.spec.{SecretKeySpec, IvParameterSpec}
import java.io.{BufferedOutputStream, FileOutputStream, BufferedInputStream, FileInputStream, File}
import java.io.ByteArrayOutputStream

object Pwkeeper {

  val clearTextFile = new File("orig")
  val encryptedFile = new File("data")
  val temporaryFile = new File("tmp")

  val cipher = "AES/CBC/PKCS5PADDING"
  val algorithm = "AES"

  def main(args: Array[String]): Unit = {
    if(!encryptedFile.exists) {
      println("There is no encrypted file.")
      return
    }

    if(args.size != 1) {
      println("One argument, please.")
      return
    }

    print("Enter key: ")
    val userPassword = System.console.readPassword
    var userKey: Array[Char] = new Array[Char](16)
    if(userPassword.size == 8) {
      userKey = (new String(userPassword) + new String(userPassword)).toCharArray
    } else {
      // if not 16, then just try, and probably throw exception
      userKey = userPassword
    }
    val key = new SecretKeySpec(new String(userKey).getBytes, algorithm)

    if(args(0).equals("decrypt")) {
      // decrypt encrypted file
      val (data, iv) = readEncryptedFile(encryptedFile)
      val decryptedData = decrypt(data, iv, key)

      // write it to a temporary file
      writeFile(decryptedData, temporaryFile)
      println("Tempfile is now ready for modifications...")
    } else if(args(0).equals("encrypt")) {
      // encrypt the temporary file and overwrite the previous encrypted file
      val data = readFile(temporaryFile)
      val encData = encrypt(data, key)
      writeFile(encData, encryptedFile)
      temporaryFile.delete
      println("Saved encrypted file.")
    } else {
      println("Wrong argument.")
    }
  }

  def readFile(f: File): Array[Byte] = {
    val in = new BufferedInputStream(new FileInputStream(f))
    val data = new Array[Byte]((f.length).asInstanceOf[Int])
    in.read(data, 0, data.length)
    in.close
    data
  }

  def readEncryptedFile(f: File):(Array[Byte], Array[Byte]) = {
    val in = new BufferedInputStream(new FileInputStream(f))
    val iv = new Array[Byte](16)
    in.read(iv, 0, iv.size)
    val data = new Array[Byte]((f.length - 16).asInstanceOf[Int])
    in.read(data, 0, data.length)
    in.close
    (data, iv)
  }

  def writeFile(array: Array[Byte], f: File) = {
    if(!f.exists) {
      f.createNewFile
    }
    val b = new BufferedOutputStream(new FileOutputStream(f))
    b.write(array, 0, array.size)
    b.close
  }

  def encrypt(data: Array[Byte], key: SecretKeySpec) = {
    val c = Cipher.getInstance(cipher)
    c.init(Cipher.ENCRYPT_MODE, key);
    val encData = c.doFinal(data);
    val iv = c.getIV
    val out = new ByteArrayOutputStream
    out.write(iv, 0, iv.size)
    out.write(encData, 0, encData.size)
    out.toByteArray
  }

  def decrypt(data: Array[Byte], iv: Array[Byte], key: SecretKeySpec) = {
    val c = Cipher.getInstance(cipher)
    c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv))
    c.doFinal(data)
  }

}
