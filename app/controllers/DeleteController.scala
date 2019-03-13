package controllers

import crud.CRUD
import javax.inject._
import play.api.mvc._
import play.api.http.{ContentTypeOf, ContentTypes, Writeable}
import play.filters.csrf._

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser.{decode => circeDecode, parse => circeParse}

import utils.NotFoundException
import scala.concurrent.ExecutionContext.Implicits.global
import scribe._


@Singleton
class DeleteController @Inject()(cc: MessagesControllerComponents,
                                 CRUD: CRUD,
                                 addToken: CSRFAddToken,
                                 checkToken: CSRFCheck)
                                (implicit assetsFinder: AssetsFinder)
  extends MessagesAbstractController(cc) {

  // TODO: These 2 are not used anymore
//  private val restoreUrl = routes.DeleteController.restore()
  //  private val deleteUrl = routes.DeleteController.deleteItem()

  import forms.DeleteForm._
  import forms.CreateForm.ListItemWrite

  private val defaultPrinter = Printer.noSpaces

  implicit val contentTypeOf_Json: ContentTypeOf[Json] = {
    ContentTypeOf(Some(ContentTypes.JSON))
  }
  implicit def writableOf_Json(implicit codec: Codec, printer: Printer = defaultPrinter): Writeable[Json] = {
    Writeable(a => codec.encode(a.pretty(printer)))
  }

  private val defaultItems = List(
    ListItemWrite(1,"Oil", "OliveOil", "OliveOilBrand", "Litre" ,0),
    ListItemWrite(2,"Oil", "SunflowerOil", "SunflowerOilBrand", "Litre" ,0),
    ListItemWrite(3,"Sugar", "BrownSugar", "BrownSugarBrand", "Gram" ,0),
    ListItemWrite(4,"Spice", "Cinnamon", "CinnamonBrand", "Gram" ,0),
    ListItemWrite(5,"Tea", "GreenTea", "GreenTeaBrand", "Gram" ,0)
  )

  def restore = checkToken{
    Action.async { implicit request: MessagesRequest[AnyContent] =>

      scribe.info("It has reached restore: " + request)

      for {
        _ <- CRUD.restore(defaultItems)
        x <- CRUD.readAll.map(x => circeParse(x.asJson.noSpaces).getOrElse(throw NotFoundException()))
      } yield Ok(x)

    }
  }

  def deleteItem = checkToken {
    Action.async { implicit request: MessagesRequest[AnyContent] =>

      val rawRequest = request.body.asJson.get

      scribe.info("rawRequest in deleteItem: " + rawRequest)

      for {
        _ <- CRUD.delete(circeDecode[DeleteIdClass](rawRequest.toString).getOrElse(throw NotFoundException()))
        x <- CRUD.readAll.map(x => circeParse(x.asJson.noSpaces).getOrElse(throw NotFoundException()))
      } yield Ok(x)

    }
  }

}
