package no.kvikshaug.pwkeeper

import javax.crypto.Cipher
import javax.crypto.spec.{SecretKeySpec, IvParameterSpec}
import java.util.Scanner

object Crypt {

  // We'll use the AES (256-bit key) cipher with cipher block chaining
  // and a 16-bit initialization vector, prepended at the start of the file.
  val cipher = "AES/CBC/PKCS5PADDING"
  val algorithm = "AES"
  val ivLength = 16
  val keyLength = 256

  private def readKey(request: String) = {
    print(request); val key = new Scanner(System.in).nextLine.getBytes
    if(key.length != keyLength / 8) {
      throw new IllegalArgumentException("The key must be " + keyLength + " bit in size.")
    }
    new SecretKeySpec(key, Crypt.algorithm)
  }

  def decrypt(b: Array[Byte]): Array[Byte] = {
    val c = Cipher.getInstance(cipher)
    c.init(Cipher.DECRYPT_MODE, readKey("Please enter the decryption key: "), new IvParameterSpec(b.take(ivLength)))
    c.doFinal(b.drop(ivLength))
  }

  def encrypt(data: Array[Byte]) = {
    val c = Cipher.getInstance(cipher)
    c.init(Cipher.ENCRYPT_MODE, readKey("Please enter an encryption key: "));
    val encrypted = c.doFinal(data);
    c.getIV ++ encrypted
  }

}
