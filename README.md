# Terminal progress bar for Scala

Console progress bar for Scala Inspired from [pb](http://github.com/cheggaaa/pb).

![Screenshot](https://github.com/a8m/pb-scala/blob/master/gif/pb_rec.gif)

## Sbt

Add to the build.sbt:
`lazy val scalaProgressBar = ProjectRef(uri("git://github.com/vimalaguti/pb-scala#v0.2.1"), "pb-scala")`

`pb-scala#v0.2.1` can also be `pb-scala#master` or other tag depending on the verson you want to use

and set to your project `.dependsOn(scalaProgressBar)`


## Examples
1. simple example
```scala
object Main {
  def main(args: Array[String]) {
    var count = 1000
    var pb = new ProgressBar(count)
    pb.showSpeed = false
    for (_ <- 1 to count) {
      pb += 1
      Thread.sleep(10)
    }
    println("done")
  }
}
```
