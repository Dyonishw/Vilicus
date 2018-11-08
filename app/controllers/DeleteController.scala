package controllers

import crud.CRUD
import forms.CreateForm.ListItemWrite
import javax.inject._
import play.api.mvc._
import play.api.data._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class DeleteController @Inject()(cc: MessagesControllerComponents, CRUD: CRUD)(implicit assetsFinder: AssetsFinder)
  extends MessagesAbstractController(cc) {

  private val deleteUrl = routes.DeleteController.deleteItem()

  private val restoreUrl = routes.DeleteController.restore()

  import forms.DeleteForm._

  def showForm = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.delete(deleteForm, deleteUrl))
  }

  private val defaultItems = List(
    ListItemWrite(1,"Oil", "OliveOil", "OliveOilBrand", "Litre" ,0),
    ListItemWrite(2,"Oil", "SunflowerOil", "SunflowerOilBrand", "Litre" ,0),
    ListItemWrite(3,"Sugar", "BrownSugar", "BrownSugarBrand", "Gram" ,0),
    ListItemWrite(4,"Spice", "Cinnamon", "CinnamonBrand", "Gram" ,0),
    ListItemWrite(5,"Tea", "GreenTea", "GreenTeaBrand", "Gram" ,0)
  )

  def restore = Action { implicit request: MessagesRequest[AnyContent] =>

    println("it has reached restore " + request)
    CRUD.restore(defaultItems)
    Redirect(routes.CreateController.listItems()).flashing("info" -> "List reset to default")
  }

  def deleteItem = Action.async { implicit requestDelete: MessagesRequest[AnyContent] =>

    val errorFunction = { formWithErrors: Form[DeleteIdClass] =>

      // TODO: Replace these println with logging
      println(formWithErrors.errors)

      Future.successful(BadRequest(views.html.delete(formWithErrors, deleteUrl)))
    }

    val successFunction = { deleteIDValue: DeleteIdClass =>

      val DeleteItem = DeleteIdClass(id = deleteIDValue.id)

      // TODO: Replace these println with logging
      println("Id for delete is: " + DeleteItem.id)

      CRUD.delete(DeleteItem.id)

      Future.successful(Redirect(routes.CreateController.listItems()).flashing("info" -> "Item deleted"))
    }

    val formValidationResultDelete = deleteForm.bindFromRequest
    formValidationResultDelete.fold(errorFunction, successFunction)
  }
}
