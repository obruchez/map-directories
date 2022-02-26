name := "map-directories"

version := "0.1"

scalaVersion := "2.13.8"

libraryDependencies ++= Seq("com.google.guava" % "guava" % "31.0.1-jre",
                            "commons-io" % "commons-io" % "2.11.0")

mainClass in assembly := Some("org.bruchez.olivier.mapdirectories.MapDirectories")

assemblyJarName in assembly := "map-directories.jar"

scalafmtOnCompile in ThisBuild := true
