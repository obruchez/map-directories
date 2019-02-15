name := "map-directories"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq("com.google.guava" % "guava" % "27.0.1-jre",
                            "commons-io" % "commons-io" % "2.6")

mainClass in assembly := Some("org.bruchez.olivier.mapdirectories.MapDirectories")

assemblyJarName in assembly := "map-directories.jar"

scalafmtOnCompile in ThisBuild := true
