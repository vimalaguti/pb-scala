name := "pb"
ThisBuild / version := "0.2.1"
ThisBuild / scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time" % "2.20.0",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "jline" % "jline" % "2.14.6"
)

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
homepage := Some(url("http://github.com/a8m/pb-scala"))

publishMavenStyle := true
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}
pomExtra := (
  <scm>
    <url>git@github.com:a8m/pb-scala.git</url>
    <connection>scm:git:git@github.com:a8m/pb-scala.git</connection>
  </scm>
  <developers>
    <developer>
      <id>a8m</id>
      <name>Ariel Mashraki</name>
      <url>http://github.com/a8m/</url>
    </developer>
  </developers>
)
