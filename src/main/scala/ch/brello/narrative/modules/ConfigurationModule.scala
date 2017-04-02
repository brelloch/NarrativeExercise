package ch.brello.narrative.modules

import com.typesafe.config.{Config, ConfigFactory}

trait Configuration {
  def config: Config
}

trait ConfigurationModuleImpl extends Configuration {
  private val internalConfig: Config = {
    ConfigFactory.load()
  }

  def config: Config = internalConfig
}