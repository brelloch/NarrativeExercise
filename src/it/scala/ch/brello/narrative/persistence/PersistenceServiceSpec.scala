package ch.brello.narrative.persistence

import java.util.UUID

import ch.brello.narrative.models.Data
import doobie.imports._
import doobie.h2.H2Types._
import ch.brello.narrative.utils.TimeUtils
import org.flywaydb.core.Flyway
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpec}

class PersistenceServiceSpec extends WordSpec with MustMatchers with MockitoSugar with ScalaFutures {
  private val xa: Transactor[IOLite] = DriverManagerTransactor[IOLite](
    "org.h2.Driver", "jdbc:h2:mem:todo;DB_CLOSE_DELAY=-1", "", ""
  )

  val flyWay = new Flyway()
  flyWay.setDataSource("jdbc:h2:mem:todo;DB_CLOSE_DELAY=-1", "", "")
  flyWay.migrate()


  "getAnalytics" must {

    "return analytics matching the requested hour from db" in {
      val timestamp1: Long = 1491136179446L
      val timestamp2: Long = 1491106179446L
      val userId1: String = "6fdadee0-8b50-4f74-b4b4-25b59c979637"
      val userId2: String = "6fdadee0-8b50-4f74-b4b4-25b59c979638"
      val event1: String = "impression"
      val event2: String = "click"

      sql"insert into analytics (userid, timestamp, event) values (${userId1}, ${timestamp1.toString}, ${event1})".update.run.transact(xa).unsafePerformIO
      sql"insert into analytics (userid, timestamp, event) values (${userId1}, ${timestamp2.toString}, ${event1})".update.run.transact(xa).unsafePerformIO
      sql"insert into analytics (userid, timestamp, event) values (${userId1}, ${timestamp1.toString}, ${event1})".update.run.transact(xa).unsafePerformIO
      sql"insert into analytics (userid, timestamp, event) values (${userId2}, ${timestamp1.toString}, ${event2})".update.run.transact(xa).unsafePerformIO

      val service = new PersistenceService(new PersistenceCommands, new PersistenceQueries, xa)

      val result = service.getAnalytics(timestamp1)

      result.uniqueUsers mustEqual 2
      result.clicks mustEqual 1
      result.impressions mustEqual 2
    }
  }

  "saveAnalytics" must {

    "successfully save to db" in {
      val userId = UUID.randomUUID()
      val timestamp: Long = 1491136179446L
      val event = "click"

      val data = Data(userId, timestamp, event)
      val service = new PersistenceService(new PersistenceCommands, new PersistenceQueries, xa)

      service.saveAnalytics(data)

      val result: List[Data] = sql"select userid, timestamp, event from analytics where userid = ${userId} and timestamp = ${timestamp} and event = ${event}".query[Data].list.transact(xa).unsafePerformIO

      result.size mustEqual 1
      result.head.event mustEqual event
      result.head.timestamp mustEqual timestamp
      result.head.userId mustEqual userId
    }
  }
}