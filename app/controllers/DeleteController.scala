package controllers

import crud.CRUD
import javax.inject._
import play.api.mvc._
import play.api.data._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class DeleteController @Inject()(cc: MessagesControllerComponents, CRUD: CRUD)(implicit assetsFinder: AssetsFinder)
  extends MessagesAbstractController(cc) {

  private val deleteUrl = routes.DeleteController.deleteItem()

  import forms.DeleteForm._

  def showForm = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.delete(deleteForm, deleteUrl))
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
      println("It has reached successFunction")
      println("Id for delete is: " + DeleteItem.id)

      CRUD.delete(DeleteItem.id)

      Future.successful(Redirect(routes.CreateController.listItems()).flashing("info" -> "Item deleted"))
    }

    val formValidationResultDelete = deleteForm.bindFromRequest
    formValidationResultDelete.fold(errorFunction, successFunction)
  }
}
