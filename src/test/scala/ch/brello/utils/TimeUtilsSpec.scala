package ch.brello.utils

import ch.brello.narrative.utils.TimeUtils
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpec}

class TimeUtilsSpec extends WordSpec with MustMatchers with MockitoSugar with ScalaFutures {
  
  "getRange" must {

    "return the start and stop of the hour" in {
      val submitTime: Long = 1491136179446L
      val result: (Long, Long) = TimeUtils.getRange(submitTime)

      result._1 mustEqual 1491134400446L
      result._2 mustEqual 1491138000446L
    }
  }
}