package forms

object UpdateForm {

  import play.api.data.Form
  import play.api.data.Forms._

  case class UpdateClass(id: Int, quantity: Int)

  val updateForm = Form(
    mapping(
      "updateID" -> number(min = 1),
      "quantity" -> number(min = 0)
    )(UpdateClass.apply)(UpdateClass.unapply)
  )

}