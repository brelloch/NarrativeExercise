package ch.brello.rest

import akka.http.scaladsl.model.StatusCodes
import ch.brello.narrative.models.Result
import ch.brello.narrative.rest.AnalyticsRoutes
import ch.brello.narrative.utils.TimeUtils
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpec}
import org.mockito.Mockito.{times, verify, when}

class AnalyticsRoutesSpec extends WordSpec with MustMatchers with MockitoSugar with ScalaFutures with AbstractRestTest {

  val modules = new Modules {}
  val analytics = new AnalyticsRoutes(modules)

  "AnalyticsRoutes" must {
    "return result for succesful get" in {
      val timestamp: Long = 1491136179446L
      when(modules.service.getAnalytics(timestamp)) thenReturn(Result(1,0,1))

      Get(s"/analytics?timestamp=${timestamp.toString}") ~> analytics.routes ~> check {
        handled mustEqual true
        status mustEqual StatusCodes.OK
        responseAs[String] mustEqual "{\"uniqueUsers\":1,\"clicks\":0,\"impressions\":1}"
      }
    }


    "return 204 for successful post" in {
      val timestamp: String = "1491136179446"
      val user: String = "6fdadee0-8b50-4f74-b4b4-25b59c979637"
      val event: String = "impression"

      Post(s"/analytics?timestamp=${timestamp}&user=${user}&event=${event}") ~> analytics.routes ~> check {
        handled mustEqual true
        status mustEqual StatusCodes.NoContent
      }
    }

    "return 400 for post with bad user id" in {
      val timestamp: String = "1491136179446"
      val user: String = "BadUserId"
      val event: String = "impression"

      Post(s"/analytics?timestamp=${timestamp}&user=${user}&event=${event}") ~> analytics.routes ~> check {
        handled mustEqual true
        status mustEqual StatusCodes.BadRequest
        responseAs[String] mustEqual "\"User must be a valid UUID\""
      }
    }

    "return 400 for post with bad event" in {
      val timestamp: String = "1491136179446"
      val user: String = "6fdadee0-8b50-4f74-b4b4-25b59c979637"
      val event: String = "BadEvent"

      Post(s"/analytics?timestamp=${timestamp}&user=${user}&event=${event}") ~> analytics.routes ~> check {
        handled mustEqual true
        status mustEqual StatusCodes.BadRequest
        responseAs[String] mustEqual "\"Event must either be 'click' or 'impression'\""
      }
    }

  }
}