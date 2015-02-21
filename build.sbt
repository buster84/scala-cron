import SonatypeKeys._

sonatypeSettings

name := """scala-cron"""

organization := "com.github.buster84"

version := "1.0.0"

scalaVersion := "2.11.5"

// Change this to another test framework if you prefer
libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.6" % "test"

libraryDependencies <++= scalaVersion {sv =>
  if ( sv.startsWith( "2.11" ) ){
    Seq("org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3")
  } else {
    Seq()
  }
}

// joda time
libraryDependencies += "joda-time" % "joda-time" % "2.3"

libraryDependencies += "org.joda" % "joda-convert" % "1.2"

// Publishing setting
publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>http://github.com/buster84/scala-cron</url>
  <licenses>
    <license>
      <name>Apache License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:buster84/scala-cron.git</url>
    <connection>scm:git:git@github.com:buster84/scala-cron.git</connection>
  </scm>
  <developers>
    <developer>
      <id>buster84</id>
      <name>Yasuki Okumura</name>
      <url>http://buster84.github.com</url>
    </developer>
  </developers>)
