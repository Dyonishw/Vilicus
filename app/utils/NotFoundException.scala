package utils

import utils.{ApiException => DanApiException}
import play.api.http.Status

case class NotFoundException() extends DanApiException(Status.NOT_FOUND, "Not found")
