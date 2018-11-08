package controllers

import crud.CRUD
import forms.CreateForm.form
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

  def listItems: Action[AnyContent] = Action.async {implicit request: MessagesRequest[AnyContent] =>
    CRUD.read.map(x => Ok(views.html.update(x, updateForm, UpdateUrl)))
  }

  def flushItems = Action { implicit request: MessagesRequest[AnyContent] =>

    CRUD.flushItems()
    Redirect(routes.UpdateController.listItems()).flashing("info" -> "List reset to default")

  }

  def updateItem = Action.async { implicit updateRequest: MessagesRequest[AnyContent] =>

    def errorFunction = { formWithErrors: Form[UpdateClass] =>

      // TODO: Replace these println with logging
      println("Update Request has reached errorFunction with error: ")
      println(formWithErrors.errors)

      CRUD.read.map(x => BadRequest(views.html.update(x, formWithErrors, UpdateUrl)))

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
