package CRUD

import org.scalatestplus.play._
import play.api.db.{Database, Databases}

class CRUDSpec extends PlaySpec {

  // TODO: Add DB evolutions
  // NOTE: Consider using an inMemory DB for tests
  def withMyDatabase[T](block: Database => T) = {
    Databases.withDatabase(
      driver = "org.postgresql.Driver",
      url = "jdbc:postgresql:world",
      name = "postgres",
      config = Map(
        "username" -> "postgres",
        "password" -> "1234"
      )
    )(block)
  }

  "Test Database connection " should {

    "should be properly configured" in {

    withMyDatabase { database =>

      val connName = database.name
      val connURL = database.url

      connName mustBe "postgres"
      connURL mustBe "jdbc:postgresql:world"

    }

    }
  }
}
