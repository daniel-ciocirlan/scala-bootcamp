package asciiart.asciifier

import java.awt.Color
import java.awt.image.BufferedImage

class PlainTextAsciifier extends Asciifier  {

  /**
    * Converts an image into a String containing H x W characters, one for each pixel in the image.
    *
    * @param image the input image as a BufferedImage
    * @return the String representation
    */
  def asciify(image: BufferedImage): String = {
    val lines: Seq[Seq[Char]] = mapImage(image)(color => {
      chooseChar(computeBrightness(color))
    })
    val rows: Seq[String] = lines.map(charSeq => charSeq.map(_.toString).reduce(_ + _))
    val finalString: String = rows.mkString("\n")
    finalString
  }

}

object TestCharSeq {
  def main(args: Array[String]): Unit = {
    val stringSeq: Seq[String] = Seq("abc", "def", "ghi")
    println(stringSeq.mkString("\n"))
  }
}