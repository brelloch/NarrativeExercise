package ch.brello.narrative.persistence

import ch.brello.narrative.models.Data
import doobie.imports._
import doobie.h2.H2Types._

class PersistenceCommands {
  def saveAnalytics(value: Data): Update0 = {
    sql"insert into analytics (userid, timestamp, event) values (${value.userId}, ${value.timestamp}, ${value.event})".update
  }
}
