package controllers

import crud.CRUD
import javax.inject._
import play.api.mvc._
import play.api.http.{ContentTypeOf, ContentTypes, Writeable}
import play.filters.csrf.{CSRF, CSRFAddToken, CSRFCheck}
import play.filters.csrf.CSRF.Token

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser.{decode => circeDecode, parse => circeParse}

import utils.NotFoundException
import scala.concurrent.ExecutionContext.Implicits.global
import scribe._

@Singleton
class UpdateController @Inject()(cc: MessagesControllerComponents,
                                 CRUD: CRUD,
                                 addToken: CSRFAddToken,
                                 checkToken: CSRFCheck)
                                (implicit assetsFinder: AssetsFinder)
  extends MessagesAbstractController(cc) {

  import forms.UpdateForm._

  private val defaultPrinter = Printer.noSpaces

  implicit val contentTypeOf_Json: ContentTypeOf[Json] = {
    ContentTypeOf(Some(ContentTypes.JSON))
  }
  implicit def writableOf_Json(implicit codec: Codec, printer: Printer = defaultPrinter): Writeable[Json] = {
    Writeable(a => codec.encode(a.pretty(printer)))
  }

  // TODO: This is not used anymore
//  private val UpdateUrl = routes.UpdateController.updateItem()

  def listItems: Action[AnyContent] = Action.async {implicit request: MessagesRequest[AnyContent] =>

    scribe.info("It has reached listItems: " + request)

    for {
      x <- CRUD.read.map(x => x.asJson.noSpaces)
    } yield Ok(x)

  }

  def flushItems = addToken(Action.async { implicit request: MessagesRequest[AnyContent] =>

    scribe.info("It has reached flushItems: " + request.body)

    val Token(name, value) = CSRF.getToken.get

    for {
      _ <- CRUD.flushItems()
      x <- CRUD.read.map(x => x.asJson.noSpaces)
    } yield Ok(x)

  })

  def updateItem = addToken(Action.async { implicit  updateRequest: MessagesRequest[AnyContent] =>

    val Token(name, value) = CSRF.getToken.get

    val rawRequest = updateRequest.body.asJson.get

    scribe.info("rawRequest in updateItem: " + rawRequest)

    // FIXME: This uses asJson from Play instead of Circe
    for {
      _ <- CRUD.update(circeDecode[UpdateClass](rawRequest.toString).getOrElse(throw NotFoundException()))
      x <- CRUD.readAll.map(x => circeParse(x.asJson.noSpaces).getOrElse(throw NotFoundException()))
    } yield Ok(x)

  })

}
