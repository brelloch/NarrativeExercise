package ch.brello.narrative.modules

import java.util.Date

import ch.brello.narrative.models.Data
import ch.brello.narrative.persistence.{PersistenceCommands, PersistenceQueries, PersistenceService}
import doobie.imports._
import doobie.h2.H2Types._

trait PersistenceModule {
  val service: PersistenceService
}

trait PersistenceModuleImpl extends PersistenceModule{
  this: Configuration =>

  private val xa: Transactor[IOLite] = DriverManagerTransactor[IOLite](
    config.getString("database.driver"), config.getString("database.url"), config.getString("database.user"), config.getString("database.password")
  )

  private val internalService = new PersistenceService(new PersistenceCommands, new PersistenceQueries, xa)

  val service: PersistenceService = internalService
}