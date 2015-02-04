import Dependencies._
import com.typesafe.sbt.SbtGit._

lazy val commonSettings = Util.settings ++ versionWithGit ++ Seq(
  organization := "org.scala-sbt",
  git.baseVersion := "0.1.0",
  isSnapshot := true,
  version := {
    val old = version.value
    if (isSnapshot.value) old
    else git.baseVersion.value
  },
  scalaVersion := scala210Version,
  crossScalaVersions := Seq(scala210Version, scala211Version),
  libraryDependencies ++= Seq(junitInterface % Test, scalaCheck % Test)
)

lazy val root = (project in file(".")).
  aggregate(serialization).
  settings(commonSettings: _*).
  settings(
    publishArtifact := false,
    publish := {},
    publishLocal := {}
  )

lazy val serialization = (project in file("serialization")).
  settings(commonSettings: _*).
  settings(
    parallelExecution in Test := false,
    libraryDependencies ++= Seq(
      pickling,
      junitInterface % Test
    ) ++ jsonDependencies
  )
