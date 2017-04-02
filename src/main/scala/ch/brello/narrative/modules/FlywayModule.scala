package ch.brello.narrative.modules

import org.flywaydb.core.Flyway

trait FlywayModule {
  def migrate: Unit
}

trait FlywayModuleImpl extends PersistenceModule{
  this: Configuration =>

  def migrate(): Unit = {
    val flyWay = new Flyway()
    flyWay.setDataSource(config.getString("database.url"), config.getString("database.user"), config.getString("database.password"))
    flyWay.migrate()
  }
}
