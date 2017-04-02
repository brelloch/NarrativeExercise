package ch.brello.narrative.utils

import java.util.Date

object TimeUtils {
  def getRange(timestamp: Long): (Long, Long) = {
    val date = new Date(timestamp)
    date.setMinutes(0)
    date.setSeconds(0)

    val start = date.getTime

    date.setHours(date.getHours + 1)

    val stop = date.getTime

    (start, stop)
  }
}
