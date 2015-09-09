organization := "de"

name := "scala-rest-client-evaluation"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "net.liftweb" %% "lift-json" % "2.6.2",
  "net.liftweb" %% "lift-json-ext" % "2.6.2",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.3"
)
 
 
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

libraryDependencies += "org.springframework" % "spring-context" % "4.2.0.RELEASE"
libraryDependencies += "org.springframework" % "spring-web" % "4.2.0.RELEASE"

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.6.1"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.1"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.6.1"
