package ch.brello.utils

import java.util.UUID

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpec}
import ch.brello.narrative.utils.StringUtils.StringImprovements

import scala.language.implicitConversions

class StringUtilsSpec extends WordSpec with MustMatchers with MockitoSugar with ScalaFutures {
  
  "isEvent" must {
    "return true for click" in {
      "click".isEvent mustEqual true
    }

    "return true for impression" in {
      "impression".isEvent mustEqual true
    }

    "return false for anything else" in {
      "bad".isEvent mustEqual false
    }
  }

  "isUuid" must {
    "return true valid uuid" in {
      UUID.randomUUID().toString.isUuid mustEqual true
    }

    "return false bad uuid" in {
      "bad".isUuid mustEqual false
    }
  }
}