package pb

import com.github.nscala_time.time.Imports._
import jline.TerminalFactory
import pb.Units.{Bytes, Default, Units}

/** pb.Output type format, indicate which format wil be used in
  * the speed box.
  */
object Units extends Enumeration {
  type Units = Value
  val Default, Bytes = Value
}

/** We're using pb.Output as a trait of pb.ProgressBar, so be able
  * to mock the tty in the tests(i.e: override `print(...)`)
  */
trait Output {
  def print(s: String): Unit = Console.print(s)
}

object ProgressBar {
  private val Format = "[=>-]"

  val LOG_INFO_MARGIN: Int = 7
  val LOG_DEBUG_MARGIN: Int = 8

  def kbFmt(n: Double): String = {
    val kb = 1024
    n match {
      case x if x >= Math.pow(kb, 4) => f"${x / Math.pow(kb, 4)}%.2f TB"
      case x if x >= Math.pow(kb, 3) => f"${x / Math.pow(kb, 3)}%.2f GB"
      case x if x >= Math.pow(kb, 2) => f"${x / Math.pow(kb, 2)}%.2f MB"
      case x if x >= kb => f"${x / kb}%.2f KB"
      case _ => f"$n%.0f B"
    }
  }
}

/** By calling new pb.ProgressBar with Int as a total, you'll
  * create a new pb.ProgressBar with default configuration.
  */
class ProgressBar(val total: Int, margin: Int = 0) extends Output {
  private[pb] var current = 0
  private val startTime = DateTime.now
  private var units = Units.Default
  private var barStart, barCurrent, barCurrentN, barRemain, barEnd = ""
  private[pb] var isFinish = false

  var showBar, showSpeed, showPercent, showCounter, showTimeLeft = true

  format(ProgressBar.Format)

  /** Add to current value
    *
    * @param          i the number to add to current value
    * @return current value
    */
  def add(i: Int): Unit = {
    current += i
    if (current <= total) draw()
  }

  /** Add value using += operator
    */
  def +=(i: Int): Unit = add(i)

  /** Set pb.Units size
    * the default is simple numbers, but you can use Bytes type instead.
    */
  def setUnits(u: Units): Unit = units = u

  /** Set custom format to the drawing bar, default is `[=>-]`
    */
  def format(fmt: String): Unit = {
    if (fmt.length >= 5) {
      val v = fmt.split("").toList
      barStart = v(0)
      barCurrent = v(1)
      barCurrentN = v(2)
      barRemain = v(3)
      barEnd = v(4)
    }
  }

  private def draw(): Unit = {
    val width = TerminalFactory.get().getWidth - margin
    var prefix, base, suffix = ""
    // percent box
    if (showPercent) {
      val percent = current.toFloat / (total.toFloat / 100)
      suffix += f" $percent%.2f %% "
    }
    // speed box
    if (showSpeed) {
      val fromStart = (startTime to DateTime.now).millis.toFloat
      val speed = current / (fromStart / 1.seconds.millis)
      suffix += (units match {
        case Default => f"$speed%.0f/s "
        case Bytes => s"${ProgressBar.kbFmt(speed)}/s "
      })
    }
    // time left box
    if (showTimeLeft) {
      val fromStart = (startTime to DateTime.now).millis.toFloat
      val left = (fromStart / current) * (total - current)
      val dur = Duration.millis(Math.ceil(left).toLong)
      if (dur.seconds > 0) {
        if (dur.seconds < 1.minutes.seconds) suffix += s"eta ${dur.seconds}s"
        else suffix += s"eta ${dur.minutes}m"
      }
    }
    // counter box
    if (showCounter) {
      prefix += (units match {
        case Default => s"$current / $total "
        case Bytes => s"${ProgressBar.kbFmt(current)} / ${ProgressBar.kbFmt(total)} "
      })
    }
    // bar box
    if (showBar) {
      val size = width - (prefix + suffix).length - 3
      if (size > 0) {
        val curCount = Math.ceil((current.toFloat / total) * size).toInt
        val remCount = size - curCount
        base = barStart
        if (remCount > 0) {
          base += barCurrent * (curCount - 1) + barCurrentN
        } else {
          base += barCurrent * curCount
        }
        base += barRemain * remCount + barEnd
      }
    }
    // out
    val out = prefix + base + suffix
    val spaces: String = String.copyValueOf(Array.fill(width - out.length)(' '))
    val newLine: Char = if (current >= total) '\n' else ' '

    // print
    print("\r" + out + spaces + newLine)
  }

  /** Calling finish manually will set current to total and draw
    * the last time
    */
  def finish(): Unit = {
    if (current < total) add(total - current)
    println()
    isFinish = true
  }
}
