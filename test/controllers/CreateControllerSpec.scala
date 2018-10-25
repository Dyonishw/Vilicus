package controllers

import play.api.mvc._
import play.api.db.{Database, Databases}
import play.api.test._
import play.api.test.Helpers._
import play.api.test.CSRFTokenHelper._

import org.scalatest._
import org.scalatestplus.play._
import org.scalatestplus.play.guice._

import crud.CRUD
import scala.concurrent.Future


class CreateControllerSpec extends PlaySpec with Results with GuiceOneAppPerTest
  with Injecting {

  // This is a hack because it was added a few days ago and it is not released
  // https://github.com/playframework/playframework/pull/8711/commits/8ff8b411a747201e269def84b89bfc3b825eb716
  // TODO: remove this and bump version when it is released
  def stubMessagesControllerComponents(): MessagesControllerComponents = {
    val stub = stubControllerComponents()
    new DefaultMessagesControllerComponents(
      new DefaultMessagesActionBuilderImpl(stubBodyParser(AnyContentAsEmpty), stub.messagesApi)(stub.executionContext),
      DefaultActionBuilder(stub.actionBuilder.parser)(stub.executionContext), stub.parsers,
      stub.messagesApi, stub.langs, stub.fileMimeTypes, stub.executionContext
    )
  }

  // NOTE: Consider using an inMemory DB for tests
  def withMyDatabase[T](block: Database => T) = {
    Databases.withDatabase(
      driver = "org.postgres.Driver",
      url = "jdbc:postgres:world",
      name = "postgres",
      config = Map(
        "username" -> "postgres",
        "password" -> "1234"
      )
    )(block)
  }

  "indexTest Action " should {

      "should be valid" in {

      val CRUD: CRUD = app.injector.instanceOf[CRUD]
      implicit val assetsFinder: AssetsFinder = inject[AssetsFinder]

      val controller = new CreateController(stubMessagesControllerComponents(), CRUD)
      val result: Future[Result] = controller
        .indexTest()
        .apply(FakeRequest(GET, "/").withCSRFToken)
      val bodyText: String = contentAsString(result)
      bodyText mustBe "OK"

    }

    // Add more tests here

  }

}
