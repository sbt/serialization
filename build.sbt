import Dependencies._
import com.typesafe.sbt.SbtGit._

lazy val commonSettings: Seq[Setting[_]] = Util.settings ++ Seq(
  organization := "org.scala-sbt",
  git.baseVersion := "0.1.1",
  scalaVersion := scala211Version,
  crossScalaVersions := Seq(scala210Version, scala211Version),
  libraryDependencies ++= Seq(junitInterface % Test, scalaCheck % Test)
)
lazy val noPublish: Seq[Setting[_]] = Seq(
  publishArtifact := false,
  publish := {},
  publishLocal := {}
)

lazy val root = (project in file(".")).
  aggregate(serialization).
  settings(commonSettings: _*).
  settings(noPublish:_*)

lazy val serialization = (project in file("serialization")).
  settings(commonSettings: _*).
  settings(
    parallelExecution in Test := false,
    libraryDependencies ++= Seq(
      pickling,
      junitInterface % Test,
      insane % Test
    ) ++ jsonDependencies
  )

lazy val benchmarks = (project in file("benchmark")).
  settings(commonSettings:_*).
  settings(noPublish:_*).
  dependsOn(serialization)
