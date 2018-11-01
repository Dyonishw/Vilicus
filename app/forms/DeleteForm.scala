package forms

object DeleteForm {

  import play.api.data.Form
  import play.api.data.Forms._

  case class DeleteIdClass(id: Int)

  // This could be a single form
  val deleteForm = Form(
    mapping(
      "deleteID" -> number(min = 1)
    )(DeleteIdClass.apply)(DeleteIdClass.unapply)
  )

}
