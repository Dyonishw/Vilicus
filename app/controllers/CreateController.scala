package controllers

import crud.CRUD
import javax.inject._
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

  def listItems: Action[AnyContent] = Action.async {implicit request: MessagesRequest[AnyContent] =>

    scribe.info("It has reached listItems: " + request)

    for {
      x <- CRUD.readAll.map(x => circeParse(x.asJson.noSpaces).getOrElse(throw NotFoundException()))
    } yield Ok(x)

  }

  // TODO: https://stackoverflow.com/questions/50713068/how-to-generate-csrf-token-in-reactjs-and-send-to-play-framework
  def createItem = addToken(Action.async { implicit request: MessagesRequest[AnyContent] =>

    val Token(name, value) = CSRF.getToken.get

    val rawRequest = request.body.asJson.get

    scribe.info("rawRequest in createItem: " + request)

    for {
      _ <- CRUD.createCustomItem(circeDecode[ListItemWrite](rawRequest.toString).getOrElse(throw NotFoundException()))
      x <- CRUD.readAll.map(x => circeParse(x.asJson.noSpaces).getOrElse(throw NotFoundException()))
    } yield Ok(x)

  })

}
