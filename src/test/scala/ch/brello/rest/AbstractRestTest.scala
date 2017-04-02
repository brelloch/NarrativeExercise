package ch.brello.rest

import akka.http.scaladsl.testkit.ScalatestRouteTest
import ch.brello.narrative.modules.{ActorModule, ConfigurationModuleImpl, PersistenceModule}
import ch.brello.narrative.persistence.PersistenceService
import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.WordSpec

trait AbstractRestTest extends WordSpec with ScalatestRouteTest with MockitoSugar {

  trait Modules extends ConfigurationModuleImpl with ActorModule with PersistenceModule {
    val system = AbstractRestTest.this.system

    override val service = mock[PersistenceService]

    override def config = getConfig.withFallback(super.config)
  }

  def getConfig: Config = ConfigFactory.empty();

}
