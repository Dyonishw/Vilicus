package controllers

import crud.CRUD
import javax.inject._
import play.api.mvc._
import play.api.data._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class UpdateController @Inject()(cc: MessagesControllerComponents, CRUD: CRUD)(implicit assetsFinder: AssetsFinder)
  extends MessagesAbstractController(cc) {

  import forms.UpdateForm._

  private val UpdateUrl = routes.UpdateController.updateItem()

  def showForm = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.update(updateForm, UpdateUrl))
  }

  def updateItem = Action.async { implicit updateRequest: MessagesRequest[AnyContent] =>

    def errorFunction = { formWithErrors: Form[UpdateClass] =>

      // TODO: Replace these println with logging
      println("Update Request has reached errorFunction with error: ")
      println(formWithErrors.errors)

      Future.successful(BadRequest(views.html.update(formWithErrors, UpdateUrl)))
    }

    def successFunction = { updateFormSuccessful: UpdateClass =>

      val CRUDVals = UpdateClass(id = updateFormSuccessful.id, quantity = updateFormSuccessful.quantity)
      CRUD.update(CRUDVals.id, CRUDVals.quantity)
      Future.successful(Redirect(routes.CreateController.listItems()).flashing("info" -> "items updated"))
    }

    val formValidationUpdate = updateForm.bindFromRequest
    formValidationUpdate.fold(errorFunction, successFunction)
  }

}
