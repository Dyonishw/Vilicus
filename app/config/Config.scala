package config

import pureconfig.generic.auto._
import config.Config._


class Config {

  val config: DbConfig = pureconfig.loadConfigOrThrow[DbConfig]

}

object Config {

  final case class DbConfig(database: CRUDConfig)

  final case class CRUDConfig(driver: String,
                             url: String,
                             username: String,
                             password: String)
}
