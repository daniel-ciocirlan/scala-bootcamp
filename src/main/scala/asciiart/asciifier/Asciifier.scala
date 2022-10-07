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
  def computeColor(pixel: Color): Double = ??? // TODO 1

  /**
    * Returns the char representation of a colored pixel (taken as a single value).
    *
    * @param intensity the color of the pixel in the 0-255 interval
    * @return the corresponding character from Asciifier.asciiChars
    */
  def chooseChar(intensity: Double): Char = ??? // TODO 1

  /**
    * Turns a BufferedImage into a "matrix" of values obtained by applying a function to every pixel in the image.
    * The "matrix" has exactly H x W elements, in which W and H are the dimensions of the input image.
    *
    * @param image the input image
    * @param f a function to be applied to every pixel
    * @tparam A the type of the values obtained by the application of the function
    * @return the matrix of values as a sequence of sequences
    */
  def mapImage[A](image: BufferedImage)(f: Color => A): Seq[Seq[A]] = ??? // TODO 1
}

object Asciifier {
  /**
    * The characters we'll use in the string representation, in descending order of "darkness".
    */
  val asciiChars = List('#','A','@','%','$','+','=','*',':',',','.',' ')
}

