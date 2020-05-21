package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import service.HistoryService
import service.TimedItem
import service.Login
import scala.util.{Try, Success, Failure}

@Singleton
class TimedItemController @Inject()(val controllerComponents: ControllerComponents, historyService: HistoryService) extends BaseController {

  private def extractCreds(request: Request[AnyContent]): Option[Login] = {
    val jsonBody: Option[JsValue] = request.body.asJson

    jsonBody match {
      case Some(jsonItem) => {

        val creds = Try(jsonItem.as[Login])
      
        creds match {
          case Success(creds) => Some(creds)
          case Failure(_) => None
        }
      }
      case None => None
    }
  }

  def health() = Action { implicit request: Request[AnyContent] => 
    Ok("")
  }

  def getHistory(userId: Long) = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(historyService.fetchUserHistory(userId)))
  }

  def createItemList(userId: Long) = Action { implicit request: Request[AnyContent] =>
    val jsonBody: Option[JsValue] = request.body.asJson

    jsonBody match {
      case Some(jsonItems) => {
        val items = Try(jsonItems.as[List[TimedItem]])
        items match {
          case Success(timedItems) => {
            timedItems.map(item => historyService.addUserHistoryItem(userId, item))
            Created
          }
          case Failure(_) => BadRequest(request.body.toString)
        }
      }
      case None => {
        BadRequest(request.body.toString)
      }
    }
  }

  def createItem(userId: Long) = Action { implicit request: Request[AnyContent] =>

    val jsonBody: Option[JsValue] = request.body.asJson

    jsonBody match {
      case Some(jsonItem) => {
        val item = Try(jsonItem.as[TimedItem])
        item match {
          case Success(timedItem) => {
            historyService.addUserHistoryItem(userId, timedItem)
            Created
          }
          case Failure(_) => BadRequest(request.body.toString)
        }
      }
      case None => BadRequest(request.body.toString)
    }
  }

  def deleteItem(userId: Long, itemId: String) = Action { implicit request: Request[AnyContent] =>
    historyService.deleteItem(userId, itemId)
    Ok("Deleted")
  }

  def updateItem(userId: Long) = Action { implicit request: Request[AnyContent] =>
    
    val jsonBody: Option[JsValue] = request.body.asJson

    jsonBody match {
      case Some(jsonItem) => {
        val item = Try(jsonItem.as[TimedItem])
        item match {
          case Success(timedItem) => {
            historyService.updateItem(userId, timedItem)
            Created
          }
          case Failure(_) => BadRequest(request.body.toString)
        }
      }
      case None => BadRequest(request.body.toString)
    }
  }

  def login() = Action { implicit request: Request[AnyContent] =>
    
    val login = extractCreds(request)
            
    login match {
      case Some(creds) => {
        historyService.login(creds) match {
          case Some(userId) => Ok(JsNumber(userId))
          case None         => Unauthorized(request.body.toString)
        }
      }
      case None => BadRequest(request.body.toString)
    }
  }


  def createAccount() = Action { implicit request: Request[AnyContent] =>

    val login: Option[Login] = extractCreds(request)
            
    login match {
      case Some(creds) => {
        historyService.createAccount(creds) match {
          case Left(err)     => ServiceUnavailable(err)
          case Right(result) => Created(JsNumber(result))
        }
      }
      case None => BadRequest(request.body.toString)
    }
  }

}
