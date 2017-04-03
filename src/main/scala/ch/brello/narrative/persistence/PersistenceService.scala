package ch.brello.narrative.persistence

import java.util.Date

import ch.brello.narrative.models.{Data, Result}
import ch.brello.narrative.utils.TimeUtils
import doobie.imports._
import doobie.h2.H2Types._

import scala.collection.mutable.ArrayBuffer

class PersistenceService(commands: PersistenceCommands, queries: PersistenceQueries, xa: Transactor[IOLite]) {

  // Issues with this cache:
  //  - It does not scale because the cache is not distributed - redis would probably be a good option.
  //  - It is mutable and begging for issues if deployed in a high throughput environment when multiple threads hit it
  var naiveCache: ArrayBuffer[Data] = ArrayBuffer[Data]()

  def getAnalytics(timestamp: Long): Result = {
    val range = TimeUtils.getRange(timestamp)
    val currentEpoch = (new Date).getTime

    if (currentEpoch > range._1 && currentEpoch < range._2) { // within the current hour so go to cache for results
      val value: List[Data] = naiveCache.filter(x => x.timestamp > range._1).toList
      Result(value.map(_.userId).distinct.size, value.count(x => x.event == "click"), value.count(x => x.event == "impression"))
    } else { // outside of current cache so go to db
      val value: List[Data] = queries.getAnalytics(range._1, range._2).transact(xa).unsafePerformIO
      Result(value.map(_.userId).distinct.size, value.count(x => x.event == "click"), value.count(x => x.event == "impression"))
    }
  }

  def saveAnalytics(value: Data): Unit = {
    val range = TimeUtils.getRange(value.timestamp)
    val currentEpoch = (new Date).getTime

    // if in the current hour throw in cache
    if (currentEpoch > range._1 && currentEpoch < range._2) {
      naiveCache += value
      naiveCache = naiveCache.filter(x => x.timestamp > range._1)
    }
    commands.saveAnalytics(value).run.transact(xa).unsafePerformIO
  }
}
