package ch.brello.persistence

import java.util.{Date, UUID}

import ch.brello.narrative.models.Data
import ch.brello.narrative.persistence.{PersistenceCommands, PersistenceQueries, PersistenceService}
import ch.brello.narrative.utils.TimeUtils
import doobie.imports.{IOLite, Transactor}
import org.mockito.Mock
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpec}

class PersistenceServiceSpec extends WordSpec with MustMatchers with MockitoSugar with ScalaFutures {
  val commands = mock[PersistenceCommands]
  val queries = mock[PersistenceQueries]
  val xa = mock[Transactor[IOLite]]
  val service = new PersistenceService(commands, queries, xa)

  "getAnalytics" must {

    "grab from cache" in {
      val timestamp = (new Date).getTime
      val testData1 = Data(UUID.randomUUID(), timestamp, "click")
      val testData2 = Data(UUID.randomUUID(), timestamp, "impression")
      service.naiveCache += testData1
      service.naiveCache += testData1
      service.naiveCache += testData1
      service.naiveCache += testData2

      val result = service.getAnalytics(timestamp)

      result.uniqueUsers mustEqual 2
      result.impressions mustEqual 1
      result.clicks mustEqual 3
    }
  }
}