package no.kvikshaug.pwkeeper

import javax.crypto.Cipher
import javax.crypto.spec.{SecretKeySpec, IvParameterSpec}
import java.io.{BufferedOutputStream, FileOutputStream, BufferedInputStream, FileInputStream, File}

object Pwgetter {

  val clearTextFile = new File("orig")
  val encryptedFile = new File("data")

  val cipher = "AES/CBC/PKCS5PADDING"
  val algorithm = "AES"

  def main(args: Array[String]) = {
    val key = "1234567890123456".getBytes
    val data = scala.io.Source.fromFile(clearTextFile).mkString.getBytes
    val k = new SecretKeySpec(key, "AES");
    c.init(Cipher.ENCRYPT_MODE, k);
    val encData = c.doFinal(data);
    val iv = c.getIV
    val b = new BufferedOutputStream(new FileOutputStream(new File("test")))
    println(iv.size)
    b.write(iv, 0, iv.size)
    b.write(encData, 0, encData.size)
    b.flush
  }

  def decrypt = {
    val c = Cipher.getInstance(cipher)
    val key = "1234567890123456".getBytes
    val in = new BufferedInputStream(new FileInputStream(encryptedFile))
    val iv = new Array[Byte](16)
    val data = new Array[Byte]((file.length - 16).asInstanceOf[Int])
    in.read(iv, 0, iv.size)
    in.read(data, 0, data.length)
    in.close
    val k = new SecretKeySpec(key, "AES");
    c.init(Cipher.DECRYPT_MODE, k, new IvParameterSpec(iv));
    val encData = c.doFinal(data);

    println(new String(encData))
  }

}
