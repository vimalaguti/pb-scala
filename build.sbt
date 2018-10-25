ThisBuild / version := "0.2.2"
ThisBuild / scalaVersion := "2.11.12"
ThisBuild / description := "A progress bar for scala"
ThisBuild / licenses := List("MIT" -> new URL("http://opensource.org/licenses/MIT"))
ThisBuild / homepage := Some(url("https://github.com/vimalaguti/pb-scala"))

//lazy val pbar = (project in file("."))
//  .settings(
//    
//  )
name := "pb"

crossScalaVersions := Seq("2.11.12", "2.12.7")

libraryDependencies in Test += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time" % "2.20.0",
  "jline" % "jline" % "2.14.6"
)

ThisBuild / publishMavenStyle := true
ThisBuild / pomIncludeRepository := { _ => false }
publishArtifact in Test := false

ThisBuild / publishTo := {
  val office = "http://10.0.0.4:5000/maven/"
  if (isSnapshot.value)
    Some("snapshots" at office + "snapshots")
  else
    Some("releases"  at office + "releases")
}

ThisBuild / developers := List(
  Developer(
    id    = "a8m",
    name  = "Ariel Mashraki",
    email = "your@email",
    url   = url("http://github.com/a8m")
  ),
  Developer(
    id    = "vimalaguti",
    name  = "Vittorio Malaguti",
    email = "your@email",
    url   = url("http://github.com/vimalaguti")
  )
)

credentials += Credentials("GitBucket Maven Repository", "localhost", "username", "password")
//credentials += Credentials(Path.userHome / ".credentials")
