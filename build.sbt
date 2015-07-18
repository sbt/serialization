import Dependencies._
import com.typesafe.sbt.SbtGit._

lazy val commonSettings = Seq(
  git.baseVersion := "0.1.2",
  scalaVersion := scala210Version,
  crossScalaVersions := Seq(scala210Version, scala211Version),
  libraryDependencies ++= Seq(junitInterface % Test, scalaCheck % Test),
  bintrayOrganization := Some("sbt"),
  bintrayRepository := "maven-releases",
  scalacOptions <<= (scalaVersion) map { sv =>
    Seq("-unchecked", "-deprecation", "-Xmax-classfile-name", "72") ++
      { if (sv.startsWith("2.9")) Seq.empty else Seq("-feature") }
  },
  javacOptions in Compile := Seq("-target", "1.6", "-source", "1.6"),
  javacOptions in (Compile, doc) := Seq("-source", "1.6")
)

lazy val root = (project in file(".")).
  aggregate(serialization).
  settings(
    // inThisBuild(Seq(
    //   organization := "org.scala-sbt",
    //   homepage := Some(url("https://github.com/sbt/serialization")),
    //   description := "serialization facility for sbt",
    //   licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    //   scmInfo := Some(ScmInfo(url("https://github.com/sbt/serialization"), "git@github.com:sbt/serialization.git")),
    //   developers := List(
    //     Developer("havocp", "Havoc Pennington", "@havocp", url("https://github.com/havocp")),
    //     Developer("eed3si9n", "Eugene Yokota", "@eed3si9n", url("https://github.com/eed3si9n")),
    //     Developer("jsuereth", "Josh Suereth", "@jsuereth", url("https://github.com/jsuereth"))
    //   ),
    //   bintrayReleaseOnPublish := false
    // )),
    // Travis can't use 0.13.9 yet
    organization in ThisBuild := "org.scala-sbt",
    homepage in ThisBuild := Some(url("https://github.com/sbt/serialization")),
    description in ThisBuild := "serialization facility for sbt",
    licenses in ThisBuild  := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    scmInfo in ThisBuild := Some(ScmInfo(url("https://github.com/sbt/serialization"), "git@github.com:sbt/serialization.git")),
    developers in ThisBuild := List(
      Developer("havocp", "Havoc Pennington", "@havocp", url("https://github.com/havocp")),
      Developer("eed3si9n", "Eugene Yokota", "@eed3si9n", url("https://github.com/eed3si9n")),
      Developer("jsuereth", "Josh Suereth", "@jsuereth", url("https://github.com/jsuereth"))
    ),
    bintrayReleaseOnPublish in ThisBuild := false,
    commonSettings,
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
