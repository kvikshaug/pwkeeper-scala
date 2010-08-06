package no.kvikshaug.pwkeeper

import java.security.{KeyPairGenerator, NoSuchAlgorithmException, KeyPair}
import javax.crypto.{Cipher}
import javax.crypto.spec.SecretKeySpec
import java.io.{BufferedOutputStream, FileOutputStream, File}

object Pwkeeper {

  def main(args: Array[String]) = {
    val c = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    val key = "1234567890123456".getBytes
    val data = scala.io.Source.fromFile("orig").mkString.getBytes
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

  def makeKey(alg: String) = {
    val keyPair = KeyPairGenerator.getInstance(alg).generateKeyPair
    println(keyPair.getPublic)
    println(keyPair.getPrivate)
   }
}
