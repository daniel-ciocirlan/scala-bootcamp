package asciiart.application

import asciiart.asciifier.{Asciifier, PlainTextAsciifier}
import asciiart.imageloader.{FileLoader, ImageLoader}


trait AsciiArtApplication {
  val asciifier: Asciifier
  val imageLoader: ImageLoader

  /**
    * Loads an image at a given path and returns its String representation.
    *
    * @param path the path of the image
    * @return a Some containing the String representation of the image, or None if the image could not be loaded
    */
  def run(path: String): Option[String] = ??? // TODO 3
  /*
    You only need to use the loader and asciifier that you are instantiated with.
    1) load the image
    2) turn the image into a string with the asciifier and return it
   */
}

object AsciiArt extends AsciiArtApplication {

  override val asciifier = new PlainTextAsciifier
  override val imageLoader = new FileLoader

  def main(args: Array[String]): Unit = {
    // can read the file path from the command line
    val path = "src/main/resources/fiery-lava.png"

    val asciiImage = run(path)
    asciiImage.foreach(println)
  }
}


