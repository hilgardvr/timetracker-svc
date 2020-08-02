package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import play.api.Logging
import service.HistoryService
import service.TimedItem
import service.Login
import scala.util.{Try, Success, Failure}

@Singleton
class TimedItemController @Inject()(val controllerComponents: ControllerComponents, historyService: HistoryService) extends BaseController with Logging {

  def health() = Action { implicit request: Request[AnyContent] => 
    Ok("")
  }

  def getHistory(userHash: String) = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(historyService.fetchUserHistory(userHash)))
  }

  def createItemList(userHash: String) = Action { implicit request: Request[AnyContent] =>
    val jsonBody: Option[JsValue] = request.body.asJson

    jsonBody match {
      case Some(jsonItems) => {
        val items = Try(jsonItems.as[List[TimedItem]])
        items match {
          case Success(timedItems) => {
            timedItems.map(item => historyService.addUserHistoryItem(userHash, item))
            Created(Json.toJson(timedItems))
          }
          case Failure(_) => BadRequest(request.body.toString)
        }
      }
      case None => {
        BadRequest(request.body.toString)
      }
    }
  }

  def createItem(userHash: String) = Action { implicit request: Request[AnyContent] =>

    val jsonBody: Option[JsValue] = request.body.asJson

    jsonBody match {
      case Some(jsonItem) => {
        val item = Try(jsonItem.as[TimedItem])
        item match {
          case Success(timedItem) => {
            historyService.addUserHistoryItem(userHash, timedItem)
            Created(Json.toJson(timedItem))
          }
          case Failure(_) => BadRequest(request.body.toString)
        }
      }
      case None => BadRequest(request.body.toString)
    }
  }

  def deleteItem(userHash: String, itemId: String) = Action { implicit request: Request[AnyContent] =>
    val result = historyService.deleteItem(userHash, itemId)
    if (result == 1) Ok(JsString(itemId))
    else if (result > 1) {
      logger.warn("Multiple items deleted when only one was expected")
      Ok (JsString(itemId))
    }
    else NotFound
  }

  def updateItem(userHash: String) = Action { implicit request: Request[AnyContent] =>
    
    val jsonBody: Option[JsValue] = request.body.asJson

    jsonBody match {
      case Some(jsonItem) => {
        val item = Try(jsonItem.as[TimedItem])
        item match {
          case Success(timedItem) => {
            historyService.updateItem(userHash, timedItem)
            Created(jsonItem)
          }
          case Failure(_) => BadRequest(request.body.toString)
        }
      }
      case None => BadRequest(request.body.toString)
    }
  }

  def login() = Action { implicit request: Request[AnyContent] =>
    
    val login: Option[Login] = historyService.extractCreds(request)
            
    login match {
      case Some(creds) => {
        historyService.login(creds) match {
          case Some(userId) => Ok(JsString(userId))
          case None         => Unauthorized(request.body.toString)
        }
      }
      case None => BadRequest(request.body.toString)
    }
  }


  def createAccount() = Action { implicit request: Request[AnyContent] =>

    val login: Option[Login] = historyService.extractCreds(request)
            
    login match {
      case Some(creds) => {
        historyService.createAccount(creds) match {
          case Left(err)     => Conflict(err)
          case Right(result) => Created(JsString(result))
        }
      }
      case None => BadRequest(request.body.toString)
    }
  }

}
