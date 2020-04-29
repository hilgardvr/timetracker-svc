package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import service.HistoryService
import service.TimedItem
import service.Login
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

            historyService.addUserHistoryItem(id, timedItem)

            Ok("created")
          }
          case Failure(_) => BadRequest(request.body.toString)
        }


      }
      case None => BadRequest(request.body.toString)
    }

  }

  private def extractCreds(request: Request[AnyContent]): Option[Login] = {
    val jsonBody: Option[JsValue] = request.body.asJson

    jsonBody match {
      case Some(jsonItem) => {
        println(jsonItem)
        val creds = Try(jsonItem.as[Login])
      
        println(creds)

        creds match {
          case Success(creds) => Some(creds)
          case Failure(_) => None
        }
      }
      case None => None
    }
  }

  def login() = Action { implicit request: Request[AnyContent] =>
    val login = extractCreds(request)
            
    login match {
      case Some(creds) => {
        println(s"item: $creds")
        Ok("1")
      }
      case None => BadRequest(request.body.toString)
    }
  }


  def createAccount() = Action { implicit request: Request[AnyContent] =>

    val login = extractCreds(request)
            
    login match {
      case Some(creds) => {
        println(s"item: $creds")
        Ok("1")
      }
      case None => BadRequest(request.body.toString)
    }
  }
}
