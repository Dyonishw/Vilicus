package dbconnection

import doobie.Transactor
import monix.eval.Task
import javax.inject.{Inject, Singleton}


@Singleton
class DbConnection @Inject() {

    val transactor = Transactor.fromDriverManager[Task](

      "org.postgresql.Driver",
      "jdbc:postgresql:grocerylist",
      "postgres",
      "1234"
      )

}
