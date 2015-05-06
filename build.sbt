name := "solver-server"

organization := "uk.co.codesynergy"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.apache.camel" % "camel-scala" % "2.15.1",
  "org.apache.activemq" % "activemq-core" % "5.7.0",
  "org.apache.camel" % "camel-jms" % "2.15.1",
  "ch.qos.logback" % "logback-core" % "1.1.3",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "javax.mail" % "mail" % "1.4.7",
  "com.icegreen" % "greenmail" % "1.4.0",
  "org.apache.camel" % "camel-mail" % "2.15.1",
  "org.apache.camel" % "camel-velocity" % "2.15.1",
  "com.typesafe.akka" % "akka-camel_2.11" % "2.3.10"
)
    