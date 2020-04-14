package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import service.HistoryService
import service.TimedItem

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class TimedItemController @Inject()(val controllerComponents: ControllerComponents, historyService: HistoryService) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok("")
  }

  def getHistory(id: Long) = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(historyService.fetchUserHistory(id)))
  }

  def createItem(id: Long) = Action { implicit request: Request[AnyContent] =>
    val jsonItem = Json.parse(request.body)

    println(jsonItem.toString)

    val item = Json.fromJson[TimedItem](jsonItem)

    item match {
      case JsSuccess(item: Item, path: JsPath) =>
        Ok(item.project)
      
      case e @ JsError(_) =>
        Ok(e.toString)

    }
    // Ok(historyService.addUserHitoryItem(id))
    //Ok(s"${request.headers.toString}\n${request.body.toString}")
  }
}
