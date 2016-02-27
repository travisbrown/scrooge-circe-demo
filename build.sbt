scalaVersion := "2.11.7"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.apache.thrift" % "libthrift" % "0.8.0",
  "com.twitter" %% "finagle-thrift" % "6.33.0",
  "io.circe" %% "circe-core" % "0.4.0-SNAPSHOT",
  "io.circe" %% "circe-generic" % "0.4.0-SNAPSHOT",
  "io.circe" %% "circe-parser" % "0.4.0-SNAPSHOT",
  "com.twitter" %% "scrooge-core" % "4.5.0"
)

