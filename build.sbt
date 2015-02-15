name := """scala-cron"""

version := "1.0"

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

releaseSettings

// joda time
libraryDependencies += "joda-time" % "joda-time" % "2.3"

libraryDependencies += "org.joda" % "joda-convert" % "1.2"
