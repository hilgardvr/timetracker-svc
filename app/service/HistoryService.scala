package service

import com.google.inject.Inject
import dao.TimeDao
import dao.UserDao
import org.mindrot.jbcrypt.BCrypt
import play.api.mvc.Request
import play.api.mvc.AnyContent
import play.api.libs.json.JsValue
import scala.util.Try
import scala.util.Success
import scala.util.Failure

class HistoryService @Inject()(
  timeDao: TimeDao,
  userDao: UserDao
) {

    def hashPassword(password: String): String = {
      BCrypt.hashpw(password, BCrypt.gensalt())
    }

     def checkPassword(creds: Login): Boolean = {
       val pwHash = userDao.getPasswordHash(creds)
       pwHash match {
         case Some(hash) => BCrypt.checkpw(creds.password, hash)
         case None => false
       }
     }

    def fetchUserHistory(userHash: String): List[TimedItem] = {
      timeDao.fetchForUser(userHash)
    }

    def fetchUserHistory(creds: Login): List[TimedItem] = {

      val userHash: Option[String] = userDao.getUserHash(creds)

      userHash match {
        case Some(hash) => {
          timeDao.fetchForUser(hash)
        }
        case None => List[TimedItem]()
      }

    }

    def addUserHistoryItem(userHash: String, timedItem: TimedItem): Boolean = {
      val checkedItem =
        if (timedItem.project.length() > 128) {
          timedItem.copy(project = timedItem.project.substring(64))
        } else {
          timedItem
        }
      timeDao.insertItem(userHash, checkedItem)
    }

    def login(creds: Login): Option[String] = {
      if (checkPassword(creds)) userDao.getUserHash(creds)
      else None
    }

    def createAccount(creds: Login): Either[String, String] = {
      try {
        val hashedCreds = Login(creds.email, hashPassword(creds.password))
        Right(userDao.createUser(hashedCreds))
      } catch {
        case e: Exception => Left(e.toString())
      }
    }

    def deleteItem(userHash: String, itemId: String) = {
      timeDao.deleteItem(userHash, itemId)
    }

    def updateItem(userHash: String, timedItem: TimedItem) = {
      timeDao.updateItem(userHash, timedItem)
    }

   def extractCreds(request: Request[AnyContent]): Option[Login] = {
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

}
