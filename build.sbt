name := "importacao"
organization := "io.xmacedo"
version := "1.0"
scalaVersion := "2.11.8"

// disable using the Scala version in output paths and artifacts
crossPaths := false

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.2.0" % "provided",
  "org.apache.spark" %% "spark-sql" % "2.2.0" % "provided",
  "org.apache.spark" %% "spark-streaming" % "2.2.0" % "provided",
  "org.apache.spark" %% "spark-mllib" % "2.2.0" % "provided",
  "com.github.nscala-time" %% "nscala-time" % "1.8.0",
  "com.github.scopt" %% "scopt" % "3.5.0",
  "com.ecwid.consul" %  "consul-api" % "1.2.4"
)

unmanagedJars in Compile += file("lib/ojdbc6.jar")