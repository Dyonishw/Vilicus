package dbconnection

import config.Config
import doobie.Transactor
import doobie.free.driver
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import javax.inject.{Inject, Singleton}


//@Singleton
//class DbConnection @Inject() {
//
//    val transactor = Transactor.fromDriverManager[Task](
//
//      "org.postgresql.Driver",
//      "jdbc:postgresql:grocerylist",
//      "postgres",
//      "1234"
//      )
//
//}

@Singleton
class DbConnection @Inject()(config: Config) {

val transactor = Transactor.fromDriverManager[Task](

  driver = config.config.database.driver,
  url = config.config.database.url,
  user = config.config.database.username,
  pass = config.config.database.password
)


//val transactor: HikariTransactor[Task] = HikariTransactor.newHikariTransactor[Task](
//driverClassName = "org.postgresql.Driver",
//url = s"jdbc:postgresql:${cfg.cfg.database.database}?currentSchema=${cfg.cfg.database.schema}",
//user = config.config.database.user,
//pass = config.config.database.password
//).runSyncUnsafe(10.seconds)

}
