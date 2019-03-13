package controllers

import crud.CRUD
import javax.inject._
import play.api.mvc._
import play.api.mvc.{PlayBodyParsers => _, _}
import play.api.http.{ContentTypeOf, ContentTypes,Writeable}
import play.filters.csrf._
import play.filters.csrf.CSRF.Token
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser.{parse => circeParse, decode => circeDecode}

import utils.NotFoundException
import scala.concurrent.ExecutionContext.Implicits.global
import scribe._


@Singleton
class CreateController @Inject()(cc: MessagesControllerComponents,
                                 CRUD: CRUD,
                                 addToken: CSRFAddToken,
                                 checkToken: CSRFCheck)
                                (implicit assetsFinder: AssetsFinder)
  extends MessagesAbstractController(cc) {

  import forms.CreateForm._

  // TODO: This is not used anymore
//  private val postUrl = routes.CreateController.createItem()

  private val defaultPrinter = Printer.noSpaces

  implicit val contentTypeOf_Json: ContentTypeOf[Json] = {
    ContentTypeOf(Some(ContentTypes.JSON))
  }
  implicit def writableOf_Json(implicit codec: Codec, printer: Printer = defaultPrinter): Writeable[Json] = {
    Writeable(a => codec.encode(a.pretty(printer)))
  }

  def listItems: Action[AnyContent] = addToken {
    Action.async {implicit request: MessagesRequest[AnyContent] =>

      scribe.info("It has reached listItems: " + request)
      scribe.info(
        s"""id  = ${request.id}
           | method = ${request.method}
           | uri = ${request.uri}
           | remote-address = ${request.remoteAddress}
           | body = ${request.body.asJson}
           | rawQueryString = ${request.rawQueryString}
           | headers = ${request.headers}
           | CSRFToken = ${request.session.data}
         """.stripMargin)

      for {
        x <- CRUD.readAll.map(x => circeParse(x.asJson.noSpaces).getOrElse(throw NotFoundException()))
        _ = println(Ok(x.noSpaces).toString() + "this is the response body")
      } yield Ok(x)

    }
  }

  def createItem = checkToken {
      Action.async { implicit request: MessagesRequest[AnyContent] =>

      val rawRequest = request.body.asJson.get
      scribe.info("rawRequest in createItem: " + request)

      for {
        _ <- CRUD.createCustomItem(circeDecode[ListItemWrite](rawRequest.toString).getOrElse(throw NotFoundException()))
        x <- CRUD.readAll.map(x => circeParse(x.asJson.noSpaces).getOrElse(throw NotFoundException()))
      } yield Ok(x)

    }
  }

}
