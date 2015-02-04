import sbt._
import Keys._
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

object Dependencies {
  // Here are the versions used for the core project
  val scala210Version = "2.10.4"
  val scala211Version = "2.11.5"

  val picklingVersion = "0.10.0-M3"
  val pickling210 = "org.scala-lang.modules" % "scala-pickling_2.10" % picklingVersion
  val pickling211 = "org.scala-lang.modules" % "scala-pickling_2.11" % picklingVersion
  val pickling = "org.scala-lang.modules" %% "scala-pickling" % picklingVersion

  private val jsonTuples = Seq(
    ("org.json4s", "json4s-core", "3.2.10"),
    ("org.spire-math", "jawn-parser", "0.6.0"),
    ("org.spire-math", "json4s-support", "0.6.0")
  )

  val jsonDependencies = jsonTuples map {
    case (group, mod, version) => (group %% mod % version).exclude("org.scala-lang", "scalap")
  }
  val jsonDependencies210 = jsonTuples map {
    case (group, mod, version) => group % s"${mod}_2.10" % version
  }
  val jsonDependencies211 = jsonTuples map {
    case (group, mod, version) => group % s"${mod}_2.11" % version
  }

  val mimeUtil             = "eu.medsea.mimeutil" % "mime-util" % "2.1.1"
  // need to manually set this to override an incompatible old version
  val slf4jLog4j           = "org.slf4j" % "slf4j-log4j12" % "1.6.6"

  val scalaCheckVersion = "1.11.5"
  val junitInterface       = "com.novocode" % "junit-interface" % "0.11"
  val scalaCheck           = "org.scalacheck" %% "scalacheck" % scalaCheckVersion
  val specs2               = "org.specs2" %% "specs2" % "2.3.11"
}
