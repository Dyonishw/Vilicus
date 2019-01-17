
import javax.inject.Singleton
import play.api.mvc._
import play.api.mvc.Results._
import play.api.http.{ContentTypeOf, ContentTypes, Writeable}
import play.api.http.HttpErrorHandler

import io.circe.{Json, Printer}
import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._

import utils.{ApiException => DanApiExcetion}
import utils.ErrorResponse
import scala.concurrent.Future
import scala.util.Random
import scribe._

@Singleton
class ErrorHandler extends HttpErrorHandler {

  private val defaultPrinter = Printer.noSpaces

  implicit val contentTypeOf_Json: ContentTypeOf[Json] = {
    ContentTypeOf(Some(ContentTypes.JSON))
  }
  implicit def writableOf_Json(implicit codec: Codec, printer: Printer = defaultPrinter): Writeable[Json] = {
    Writeable(a => codec.encode(a.pretty(printer)))
  }

  // TODO: This is usefull for semiauto derivation => Can and should be done
  // via automatic derivation
  implicit val encoderErrorResponse: Encoder[ErrorResponse] = deriveEncoder

  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    val errorId = generateErrorId
    scribe.error(s"Error: $errorId: $message. Request: $request")
    Future.successful(
      Status(statusCode)(
        ErrorResponse(
          errorId,
          message = message,
          info = None
        ).asJson
      )
    )
  }

  def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    val errorId = generateErrorId
    scribe.error(s"Error $errorId: ${exception.getMessage}. Request: $request", exception)
    Future.successful(
      exception match {
        case e: DanApiExcetion => Status(e.status)(
          ErrorResponse(
            errorId,
            message = e.message,
            info = e.info
          ).asJson
        )
        case _ => InternalServerError(
          ErrorResponse(
            errorId,
            message = "A server error occurred: " + exception.getMessage,
            info = None
          ).asJson
        )
      })
  }

  private def generateErrorId: String = Random.alphanumeric.take(5).mkString.toLowerCase

}
