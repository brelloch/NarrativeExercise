package ch.brello.utils

import java.util.UUID

import ch.brello.narrative.utils.{TimeUtils, UuidUtils}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpec}

class UuidUtilsSpec extends WordSpec with MustMatchers with MockitoSugar with ScalaFutures {
  
  "isUuid" must {

    "return true for valid uuid" in {
      UuidUtils.isUuid(UUID.randomUUID().toString) mustEqual true
    }

    "return false for invalid uuid" in {
      UuidUtils.isUuid("bad uuid") mustEqual false
    }

  }
}