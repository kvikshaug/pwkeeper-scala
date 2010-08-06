package no.kvikshaug.pwkeeper

import java.security.{KeyPairGenerator, NoSuchAlgorithmException, KeyPair}
import javax.crypto.{Cipher}
import javax.crypto.spec.{SecretKeySpec, IvParameterSpec}
import java.io.{BufferedInputStream, FileInputStream, File}

object Pwgetter {

  def main(args: Array[String]) = {
    val c = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    val key = "1234567890123456".getBytes

    val file = new File("test")
    val in = new BufferedInputStream(new FileInputStream(file))
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

  def makeKey(alg: String) = {
    val keyPair = KeyPairGenerator.getInstance(alg).generateKeyPair
    println(keyPair.getPublic)
    println(keyPair.getPrivate)
   }
}
