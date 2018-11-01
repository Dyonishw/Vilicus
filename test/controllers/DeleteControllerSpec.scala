package controllers

import org.scalatestplus.play.PlaySpec
import crud.CRUD
import org.scalatestplus.play.guice.{GuiceOneAppPerSuite, GuiceOneAppPerTest}
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Injecting}
import play.api.test.CSRFTokenHelper._

import scala.concurrent.Future

class DeleteControllerSpec extends PlaySpec with Results with GuiceOneAppPerTest
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

  "Delete Controller " should {

    "should work with SEE_OTHER redirect" in {

      val CRUD: CRUD = inject[CRUD]
      implicit val assetsFinder = inject[AssetsFinder]

      val controller = new DeleteController(stubMessagesControllerComponents(), CRUD)
      val result: Future[Result] = controller
        .deleteItem()
        .apply(FakeRequest(GET, "/")
          .withFormUrlEncodedBody("deleteID" -> "1")
          .withCSRFToken)
      println(result + " This is result in DeleteController")

      val content: String = contentAsString(result)
      println(content)

      // TODO: Refactor to use JSON
      val bodyText: String = contentAsString(result)

      status(result) mustBe SEE_OTHER

    }

    "should fail with BAD_REQUEST" in {


      val CRUD: CRUD = inject[CRUD]
      implicit val assetsFinder = inject[AssetsFinder]

      val controller = new DeleteController(stubMessagesControllerComponents(), CRUD)
      val result: Future[Result] = controller
        .deleteItem()
        .apply(FakeRequest(GET, "/")
          .withFormUrlEncodedBody("deleteID" -> "0")
          .withCSRFToken)

      status(result) mustBe BAD_REQUEST

    }
  }
}
