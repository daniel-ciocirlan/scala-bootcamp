package asciiart.application

import asciiart.asciifier.{Asciifier, HtmlAsciifier, PlainTextAsciifier}
import asciiart.imageloader.{FileLoader, ImageLoader}

import java.awt.Color
import java.awt.image.BufferedImage


trait AsciiArtApplication {
  val asciifier: Asciifier
  val imageLoader: ImageLoader

  /**
    * Loads an image at a given path and returns its String representation.
    *
    * @param path the path of the image
    * @return a Some containing the String representation of the image, or None if the image could not be loaded
    */
  def run(path: String): Option[String] = {
    val image: Option[BufferedImage] = imageLoader.loadImage(path)
    image.map(img => asciifier.asciify(img))
  }
  /*
    You only need to use the loader and asciifier that you are instantiated with.
    1) load the image
    2) turn the image into a string with the asciifier and return it
   */
}

object AsciiArt extends AsciiArtApplication {

  override val asciifier = new HtmlAsciifier
  override val imageLoader = new FileLoader

  def main(args: Array[String]): Unit = {
    // can read the file path from the command line
    val path = "src/main/resources/img.png"

    /*
      BufferedImage:
        - getWidth, getHeight for dimensions
        - getRGB to get the color of a pixel as an Int
        - new Color(int) to get a Color data structure
        - getRed, getGreen, getBlue => color values in the range 0-255 (0=absent, 255=full)
          (r, g, b) => character
          example:
              (0, 3, 10) is a dark pixel => pick '#' to represent it
              (240, 238, 255) is bright => pick ' ' to represent it


     */
    val bufferedImageOption = imageLoader.loadImage(path)
    println(bufferedImageOption.map(image => new Color(image.getRGB(75, 56)).getBlue))

    // run the ascii application
    val maybeAscii: Option[String] = run(path)
    println(maybeAscii.getOrElse(""))
  }
}


