package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import service.HistoryService

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HistoryController @Inject()(val controllerComponents: ControllerComponents, historyService: HistoryService) extends BaseController {

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

  // def createItem()
}
