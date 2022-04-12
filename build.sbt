
organization     := "com.dp.training"
organizationName := "restro-schedule-parser"
version          := "1"

scalaVersion     := "2.13.6"

val sparkVersion = "3.2.0"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

scalacOptions += "-target:jvm-1.8"

libraryDependencies ++= Seq(

  //Akka
//  "com.typesafe.akka" %% "akka-stream"          % "2.6.17",
//  "com.typesafe.akka" %% "akka-actor"          % "2.6.17" ,
  "io.spray" %%  "spray-json" % "1.3.6",

  //Test
  "org.scalatest"     %% "scalatest"            % "3.2.9"         % Test exclude("org.slf4j","slf4j-api"),
)











