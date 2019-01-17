package utils

import play.api.http.{ContentTypeOf, ContentTypes, Writeable}
import play.api.mvc.Codec
import io.circe.{Json, Printer}

class ApiException(val status: Int,
                   val message: String,
                   val info: Option[Json] = None,
                   val cause: Option[Throwable] = None)
  extends RuntimeException(message, cause.orNull)

object ApiException {

  private val defaultPrinter = Printer.noSpaces

  implicit val contentTypeOf_Json: ContentTypeOf[Json] = {
    ContentTypeOf(Some(ContentTypes.JSON))
  }
  implicit def writableOf_Json(implicit codec: Codec, printer: Printer = defaultPrinter): Writeable[Json] = {
    Writeable(a => codec.encode(a.pretty(printer)))
  }
}
