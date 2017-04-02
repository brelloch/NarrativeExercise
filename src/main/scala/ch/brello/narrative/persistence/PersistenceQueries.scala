package ch.brello.narrative.persistence

import ch.brello.narrative.models.Data
import doobie.imports._
import doobie.h2.H2Types._

class PersistenceQueries {
  def getAnalytics(start: Long, stop: Long): ConnectionIO[List[Data]] = {
    sql"select userid, timestamp, event from analytics where timestamp > ${start} and timestamp < ${stop}".query[Data].list
  }
}
