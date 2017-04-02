package ch.brello.narrative.rest

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import ch.brello.narrative.models.Data
import ch.brello.narrative.modules.{Configuration, PersistenceModule}
import de.heikoseeberger.akkahttpcirce.CirceSupport
import io.circe.generic.auto._
import ch.brello.narrative.utils.StringUtils.StringImprovements
import scala.language.implicitConversions

//scalastyle:off public.methods.have.type
class AnalyticsRoutes(modules: Configuration with PersistenceModule) extends Directives with CirceSupport {
  def analytics = path("analytics") {
    get {
      parameters('timestamp.as[Long]) { timestamp =>
        complete(
          modules
            .service
            .getAnalytics(timestamp)
        )
      }
    } ~
      post {
        parameters('timestamp.as[Long], 'user.as[String], 'event.as[String]) { (timestamp, userId, event) =>
          if (!userId.isUuid){
            complete(StatusCodes.BadRequest, "User must be a valid UUID")
          } else if (!event.isEvent) {
            complete(StatusCodes.BadRequest, "Event must either be 'click' or 'impression'")
          } else {
            modules
              .service
              .saveAnalytics(Data(UUID.fromString(userId), timestamp, event))

            complete(StatusCodes.NoContent)
          }
        }
      }
  }

  val routes: Route = analytics
}
//scalastyle:on public.methods.have.type
