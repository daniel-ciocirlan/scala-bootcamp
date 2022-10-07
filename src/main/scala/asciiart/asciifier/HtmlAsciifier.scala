package asciiart.asciifier

import java.awt.image.BufferedImage


class HtmlAsciifier extends Asciifier {

  def asciify(image: BufferedImage): String = {
    val lines: Seq[Seq[(Int, Int, Int, Char)]] = mapImage(image)(color => {
      (color.getRed, color.getGreen, color.getBlue, chooseChar(computeBrightness(color)))
    })
    val rows: Seq[String] = lines.map(tupleSeq => tupleSeq.map(tuple => char2Span(tuple._1, tuple._2, tuple._3, tuple._4)).reduce(_ + _))
    val finalString: String = rows.mkString("\n")

    s"""
       |
       |      <html>
       |        <body style="padding: 20px;">
       |          <p style="
       |            font-family:Courier,monospace;
       |            font-size:5pt;
       |            letter-spacing:1px;
       |            line-height:4pt;
       |            font-weight:bold">
       |
       |            $finalString
       |
       |          </p>
       |        </body>
       |      </html>
       |""".stripMargin
  }


  def char2Span(red: Int, green: Int, blue: Int, char: Char): String = {
    s"<span style=\"display:inline; color:rgb($red, $green, $blue)\">$char</span>"
  }

  /**
    * Converts an image into an HTML block, where every character becomes a <span>.
    * The span is able to keep the color information of the original pixel, through CSS.
    *
    * @param image the input image as a BufferedImage
    * @return the String representation
    */

  /*
    Hints:
    1) Use the charToSpan and toHtmlString methods to convert a pixel to a span so that you don't curse HTML.
    2) When testing, use dimensions < 100 for width, as you'll generate >10k HTML tags each with its own CSS, which even powerful browsers have a hard time managing.
    3) Use the following HTML as an example, so you don't need to write the HTML by hand:

      <html>
        <body style="padding: 20px;">
          <p style="
            font-family:Courier,monospace;
            font-size:5pt;
            letter-spacing:1px;
            line-height:4pt;
            font-weight:bold">

            INSERT YOUR SPANS HERE

          </p>
        </body>
      </html>

   */

  /**
    * Converts a pixel to a <span> tag containing the character in the original color of the pixel.
    * @param red
    * @param green
    * @param blue
    * @param char
    * @return
    */
  def charToSpan(red: Int, green: Int, blue: Int, char: Char): String = {
    val string = toHtmlString(char)
    "<span style=\"" + String.format("display:inline; color: rgb(%s, %s, %s);", red.toString, green.toString, blue.toString) + "\">" + string + "</span>"
  }

  def toHtmlString(char: Char) = char match {
    case ' ' => "&nbsp;"
    case c => c.toString
  }
}