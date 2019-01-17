package utils

import play.api.http.{ContentTypeOf, ContentTypes, Writeable}
import play.api.mvc.Codec
import io.circe.{Json, Printer}

case class ErrorResponse(errorId: String,
                         message: String,
                         info: Option[Json])

object ErrorResponse {

  private val defaultPrinter = Printer.noSpaces

  implicit val contentTypeOf_Json: ContentTypeOf[Json] = {
    ContentTypeOf(Some(ContentTypes.JSON))
  }
  implicit def writableOf_Json(implicit codec: Codec, printer: Printer = defaultPrinter): Writeable[Json] = {
    Writeable(a => codec.encode(a.pretty(printer)))
  }
}
