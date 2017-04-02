package ch.brello.narrative

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteConcatenation
import akka.stream.ActorMaterializer
import ch.brello.narrative.modules._
import ch.brello.narrative.rest.AnalyticsRoutes
import org.slf4j.{Logger, LoggerFactory}


object Main extends App with RouteConcatenation {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  val modules = new ConfigurationModuleImpl with ActorModuleImpl with PersistenceModuleImpl with FlywayModuleImpl

  implicit val system = modules.system
  implicit val materializer = ActorMaterializer()
  implicit val ec = modules.system.dispatcher

  // Run flyway against the database
  modules.migrate()


  val bindingFuture = Http().bindAndHandle(
    new AnalyticsRoutes(modules).routes, modules.config.getString("http.interface"), modules.config.getInt("http.port")
  )

  logger.info("Server started")

}

