import sbt._
import Keys._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import com.typesafe.sbt.SbtGit

object Util {

  def baseVersions: Seq[Setting[_]] = SbtGit.versionWithGit

  def formatPrefs = {
    import scalariform.formatter.preferences._
    FormattingPreferences()
      .setPreference(IndentSpaces, 2)
  }
  // val typesafeMvnReleases = "typesafe-mvn-private-releases" at "http://private-repo.typesafe.com/typesafe/maven-releases/"
  val typesafeIvyReleases = Resolver.url("typesafe-ivy-private-releases", new URL("http://private-repo.typesafe.com/typesafe/ivy-releases/"))(Resolver.ivyStylePatterns)

  def settings: Seq[Setting[_]] =
    SbtScalariform.scalariformSettings ++
    Seq(
      publishTo := Some(typesafeIvyReleases),
      publishMavenStyle := false,
      scalacOptions <<= (scalaVersion) map { sv =>
        Seq("-unchecked", "-deprecation", "-Xmax-classfile-name", "72") ++
          { if (sv.startsWith("2.9")) Seq.empty else Seq("-feature") }
      },
      javacOptions in Compile := Seq("-target", "1.6", "-source", "1.6"),
      javacOptions in (Compile, doc) := Seq("-source", "1.6"),
      // Scaladoc is slow as molasses.
      Keys.publishArtifact in (Compile, packageDoc) := false,
      // scalaBinaryVersion <<= scalaVersion apply { sv =>
      //   CrossVersion.binaryScalaVersion(sv)
      // },
      ScalariformKeys.preferences in Compile := formatPrefs,
      ScalariformKeys.preferences in Test    := formatPrefs
    )
}
