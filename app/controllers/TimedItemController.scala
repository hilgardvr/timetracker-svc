package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import service.HistoryService
import service.TimedItem
import scala.util.{Try, Success, Failure}

@Singleton
class TimedItemController @Inject()(val controllerComponents: ControllerComponents, historyService: HistoryService) extends BaseController {

  def getHistory(id: Long) = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(historyService.fetchUserHistory(id)))
  }

  def createItem(id: Long) = Action { implicit request: Request[AnyContent] =>
    val jsonBody: Option[JsValue] = request.body.asJson

    jsonBody match {
      case Some(jsonItem) => {
        val item = Try(jsonItem.as[TimedItem])

        item match {
          case Success(timedItem) => {

            println(s"item: $timedItem")

            historyService.addUserHitoryItem(id, timedItem)

            Ok("created")
          }
          case Failure(_) => BadRequest(request.body.toString)
        }


      }
      case None => BadRequest(request.body.toString)
    }

  }
}
