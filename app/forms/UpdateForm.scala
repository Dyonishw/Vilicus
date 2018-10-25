package forms

object UpdateForm {

  import play.api.data.Form
  import play.api.data.Forms._

  // could I use ListItemWrite with default values here ?
  case class UpdateClass(id: Int, quantity: Int)

  val updateForm = Form(
    mapping(
      "updateID" -> number(min = 0),
      "quantity" -> number
    )(UpdateClass.apply)(UpdateClass.unapply)
  )

}