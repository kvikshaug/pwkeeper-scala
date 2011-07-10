package no.kvikshaug.pwkeeper

import java.io.{BufferedOutputStream, FileOutputStream, BufferedInputStream, FileInputStream, File}

object IO {
  def read(f: File): Array[Byte] = {
    val in = new BufferedInputStream(new FileInputStream(f))
    val data = new Array[Byte](f.length.asInstanceOf[Int])
    in.read(data, 0, data.length)
    in.close
    data
  }

  def write(b: Array[Byte], f: File) = {
    val out = new BufferedOutputStream(new FileOutputStream(f))
    out.write(b, 0, b.size)
    out.close
  }
}