organization := "de"

name := "scala-rest-client-evaluation"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

// Libs for Dispatch 
libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.3",
  "net.liftweb" %% "lift-json" % "2.6.2",
  "net.liftweb" %% "lift-json-ext" % "2.6.2"
  
)

// Libs for ScalaWS
libraryDependencies ++= Seq(
  "com.typesafe.play" % "play-ws_2.11" % "2.4.3"
)

// Libs for Spring
libraryDependencies ++= Seq(
  "org.springframework" % "spring-context" % "4.2.0.RELEASE",
  "org.springframework" % "spring-web" % "4.2.0.RELEASE",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.6.1"
)

// Libs for test
libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"  
)
 


// libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.6.1"
// libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.1"
// libraryDependencies += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.6.1"

