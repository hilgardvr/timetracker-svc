name := """timetracker-svc"""
organization := "com.gmail.hilgardvr"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "org.playframework.anorm" %% "anorm" % "2.6.2"
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.1"
// https://mvnrepository.com/artifact/com.typesafe.play/play
libraryDependencies += "com.typesafe.play" %% "play" % "2.7.0"
// https://mvnrepository.com/artifact/org.mindrot.bcrypt/bcrypt
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"

