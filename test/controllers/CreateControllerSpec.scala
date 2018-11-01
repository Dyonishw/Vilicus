package controllers

import play.api.mvc._
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

  "Create Controller " should {

      "work with SEE_OTHER redirect" in {

      val CRUD: CRUD = app.injector.instanceOf[CRUD]
      implicit val assetsFinder: AssetsFinder = inject[AssetsFinder]

      val controller = new CreateController(stubMessagesControllerComponents(), CRUD)
      val result: Future[Result] = controller
        .createItem()
        .apply(FakeRequest(GET, "/")
          .withFormUrlEncodedBody(
            "id" -> "9000",
            "itemType" -> "Oil",
            "itemSubType" -> "FishOil",
            "brand" -> "FishOilBrand",
            "SKU" -> "Pack",
            "quantity" -> "0"
          )
          .withCSRFToken)

      val bodyText: String = contentAsString(result)

      status(result) mustBe SEE_OTHER

    }

    "should fail with BAD_REQUEST" in {

      val CRUD: CRUD = app.injector.instanceOf[CRUD]
      implicit val assetsFinder: AssetsFinder = inject[AssetsFinder]

      val controller = new CreateController(stubMessagesControllerComponents(), CRUD)
      val result: Future[Result] = controller
        .createItem()
        .apply(FakeRequest(GET, "/")
          .withFormUrlEncodedBody(
            "id" -> "0",
            "itemType" -> "Oil",
            "itemSubType" -> "FishOil",
            "brand" -> "FishOilBrand",
            "SKU" -> "Pack",
            "quantity" -> "-1"
          )
          .withCSRFToken)

      val bodyText: String = contentAsString(result)

      status(result) mustBe BAD_REQUEST
    }
  }
}
