package controllers

import crud.CRUD
import javax.inject._
import play.api.mvc._
import play.api.data._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class CreateController @Inject()(cc: MessagesControllerComponents, CRUD: CRUD)(implicit assetsFinder: AssetsFinder)
  extends MessagesAbstractController(cc) {

  import forms.CreateForm._

  // TODO: Refactor and hard-code some default Items
  private val items = scala.collection.mutable.ArrayBuffer(
    ListItemRead(1337,"Ulei", "Masline", "Costa D'oro","Litri",1 ),
    ListItemRead(1338,"Ulei", "Floarea Soarelui", "Solarie","Litri",1 ),
    ListItemRead(1339,"Ulei", "Portocale", "Portocalis","Litri",1 )
  )

  private val postUrl = routes.CreateController.createItem()

  // TODO: remove this
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def listItems: Action[AnyContent] = Action.async {implicit request: MessagesRequest[AnyContent] =>
    CRUD.readAll.map(x => Ok(views.html.create(x, form, postUrl)))
  }

  def createItem = Action.async { implicit request: MessagesRequest[AnyContent] =>

    val errorFunction = { formWithErrors: Form[ListItemWrite] =>

      // TODO: Remove these println
      println(formWithErrors.discardingErrors)
      println(formWithErrors.errors)

      // TODO: refactor this. The maps are not needed
      CRUD.readAll.map(_.toList).map(x =>
        BadRequest(views.html.create(x, formWithErrors, postUrl)))

    }

    val successFunction = { items: ListItemWrite =>

      val CreateItem = ListItemWrite(id = items.id, itemType = items.itemType, itemSubType = items.itemSubType,
        brand = items.brand, SKU = items.SKU, quantity = items.quantity)

      CRUD.createCustomItem(CreateItem)

      // TODO: Add the item to the flashing info
      Future.successful(Redirect(routes.CreateController.listItems()).flashing("info" -> "Item created"))
    }

    val formValidationResult = form.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

}
