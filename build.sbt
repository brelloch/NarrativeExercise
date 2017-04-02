name := """NarrativeExercise"""
organization := "ch.brello"
version := "0.0.1"
scalaVersion := "2.12.1"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

resolvers += Resolver.jcenterRepo

libraryDependencies ++= {
  val catsV          = "0.9.0"
  val akkaHttpV      = "10.0.3"
  val circeV         = "0.7.0"
  val scalaMockV     = "3.4.2"
  val catsScalatestV = "2.2.0"
  val doobieV        = "0.4.0"

  Seq(
    "org.typelevel"     %% "cats-core"                   % catsV,
    "com.typesafe.akka" %% "akka-http"                   % akkaHttpV,
    "de.heikoseeberger" %% "akka-http-circe"             % "1.12.0",
    "io.circe"          %% "circe-core"                  % circeV,
    "io.circe"          %% "circe-generic"               % circeV,
    "io.circe"          %% "circe-parser"                % circeV,
    "org.tpolecat"      %% "doobie-core-cats"            % doobieV,
    "org.tpolecat"      %% "doobie-h2-cats"              % doobieV,
    "org.slf4j"         %  "slf4j-log4j12"               % "1.7.25",
    "org.flywaydb"      %  "flyway-core"                 % "4.0.3",
    "org.tpolecat"      %% "doobie-scalatest-cats"       % doobieV        % "it,test",
    "org.mockito"       %  "mockito-core"                % "2.7.20"       % "it,test",
    "com.ironcorelabs"  %% "cats-scalatest"              % catsScalatestV % "it,test",
    "com.typesafe.akka" %% "akka-http-testkit"           % akkaHttpV      % "it,test"
  )
}

lazy val root = project.in(file(".")).configs(IntegrationTest)
Defaults.itSettings
Revolver.settings
enablePlugins(JavaAppPackaging)
