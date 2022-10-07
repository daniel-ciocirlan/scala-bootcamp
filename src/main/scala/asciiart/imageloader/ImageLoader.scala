package asciiart.imageloader

import java.awt.image.BufferedImage

trait ImageLoader {
  def loadImage(url: String): Option[BufferedImage]
}