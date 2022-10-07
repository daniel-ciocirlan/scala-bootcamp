package asciiart.imageloader

import javax.imageio.ImageIO
import scala.util.Try

class FileLoader extends ImageLoader {
  
  import java.io.File

  def loadImage(url: String) =
    Try {
      ImageIO.read(new File(url))
    }.toOption
}