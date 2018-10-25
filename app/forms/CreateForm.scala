package forms

object CreateForm {

  import play.api.data.Form
  import play.api.data.Forms._

  case class ListItemRead(id: Long, itemType: String, itemSubType: String, brand: String, SKU: String, quantity: Int)

  case class ListItemWrite(id: Int, itemType: String, itemSubType: String, brand: String, SKU: String, quantity: Int)

  val form = Form(
    mapping(
      "id" -> number(min = 0),
      "itemType" -> nonEmptyText,
      "itemSubType" -> nonEmptyText,
      "brand" -> nonEmptyText,
      "SKU" -> nonEmptyText,
      "quantity" -> number(min = 0)
    )(ListItemWrite.apply)(ListItemWrite.unapply)
  )

}

