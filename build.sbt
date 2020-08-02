ThisBuild / useSuperShell := false

addCommandAlias("validate", "scalafmtSbtCheck;scalafmtCheckAll;scalafixAll --check;test")
addCommandAlias("fmt", "scalafmtSbt;scalafmtAll;scalafixAll")

lazy val root = (project in file("."))
  .settings(
    name := "puzzle-15",
    organization := "com.kambius",
    maintainer := "mrkambius@gmail.com",
    version := "1.0.0",
    scalaVersion := "2.13.3",
    libraryDependencies ++= Seq(
      "org.typelevel"         %% "cats-core"   % "2.1.1",
      "org.typelevel"         %% "cats-effect" % "2.1.4",
      "com.github.pureconfig" %% "pureconfig"  % "0.13.0",
      "org.scalatest"         %% "scalatest"   % "3.2.0" % Test
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    addCompilerPlugin(scalafixSemanticdb)
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:existentials",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings",
  "-Ywarn-dead-code",
  "-Ywarn-extra-implicit",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Ywarn-unused:_",
  "-Xlint:-byname-implicit,_"
)

enablePlugins(JavaAppPackaging)

ThisBuild / scalafixDependencies += "com.nequissimus" %% "sort-imports" % "0.5.4"
