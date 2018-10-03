package pb

import scala.util.Random

/**
  * Created by 
  *
  * @author Vittorio Malaguti 03/10/18
  *         @ meta-liquid
  */
object Sample {

  val total: Int = Random.nextInt(1000)

  def main(args: Array[String]): Unit = {

    val pb = new ProgressBar(total)
    for (_ <- 0 until total) {
      Thread.sleep(Random.nextInt(500))
      pb += 1
    }
  }

}
