name := "scalikejdbc-async-sample"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc-async" % "0.5.+",
  "com.github.mauricio" %% "postgresql-async" % "0.2.+",
  "com.github.mauricio" %% "mysql-async" % "0.2.+",
  "org.slf4j" % "slf4j-simple" % "1.7.+", // slf4j implementation
  "mysql" % "mysql-connector-java" % "5.1.35",
  "com.typesafe.akka" %% "akka-actor" % "2.3.6"
)
