package asciiart.asciifier

import java.awt.Color
import java.awt.image.BufferedImage

/**
  * A trait that is capable of converting a BufferedImage into a String (the ASCII representation of the image).
  */
trait Asciifier {
  import Asciifier._

  /**
    * Converts an image into a String representation, where
    * 1) every pixel is converted to an ASCII-readable representation (a Char, a String, etc)
    * 2) the image is taken as-is, with no scaling or filters of any kind.
    *
    * The returned String will contain the appropriate endlines for the pixel rows.
    *
    * @param image the input image as a BufferedImage
    * @return the String representation
    */
  def asciify(image: BufferedImage): String

  /**
    * Returns the color of the pixel as a Double between 0-255.
    *
    * @param pixel the colored pixel containing red, green and blue channels
    * @return the final color of the pixel (grayscale)
    */
  def computeBrightness(pixel: Color): Double =
    (pixel.getRed + pixel.getGreen + pixel.getBlue) / 3 // average/grayscale

  /**
    * Returns the char representation of a colored pixel (taken as a single value).
    *
    * @param brightness the color of the pixel in the 0-255 interval
    * @return the corresponding character from Asciifier.asciiChars
    */
  // brightness / 255 * list.length => approximate index of the char
  def chooseChar(brightness: Double): Char =
    if (brightness == 0) ' '
    else {
      val index = (brightness  / 255 * (asciiChars.length - 1)).toInt // make sure I round this down
      asciiChars(index)
    }

  /**
    * Turns a BufferedImage into a "matrix" of values obtained by applying a function to every pixel in the image.
    * The "matrix" has exactly H x W elements, in which W and H are the dimensions of the input image.
    *
    * @param image the input image
    * @param f a function to be applied to every pixel
    * @tparam A the type of the values obtained by the application of the function
    * @return the matrix of values as a sequence of sequences
    */
    /*
      xxxxxx    Seq(Seq(6 values of type A), Seq(...), Seq(...))
      xxxxxx =>
      xxxxxx

      mapImage(image)(color => chooseChar(computeBrightness(color))) => Seq(Seq(chars))
      |-----------------------------x = column index
      |
      |
      |
      |
      y = row index
     */
  def mapImage[A](image: BufferedImage)(f: Color => A): Seq[Seq[A]] =
    (0 until image.getHeight).map { y =>
      // build an entire row of characters
      val colors: Seq[Color] = (0 until image.getWidth).map(x => new Color(image.getRGB(x, y)))
      colors.map(x => f(x)) // transforms the Seq[Color] into Seq[A]
      // .. FOR EVERY ROW!
    }
}

object Asciifier {
  /**
    * The characters we'll use in the string representation, in descending order of "darkness".
    */
  val asciiChars = List('#','A','@','%','$','+','=','*',':',',','.',' ')

  def main(args: Array[String]): Unit = {
    (1 to 10).foreach(println)
  }
}

