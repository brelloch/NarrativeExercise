package ch.brello.narrative.models

import java.util.UUID
import doobie.h2.h2types

case class Data(userId: UUID, timestamp: Long, event: String) {
  require(event == "click" || event == "impression", "Event must be \"click\" or \"impression\"")
}