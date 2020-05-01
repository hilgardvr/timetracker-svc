package service

import java.time.Instant
import scala.collection.mutable.ListBuffer
import com.google.inject.Inject
import dao.TimeDao
import play.api.http._
import play.api.mvc._
import dao.UserDao

class HistoryService @Inject()(
  timeDao: TimeDao,
  userDao: UserDao
) {

    def fetchUserHistory(userId: Long): List[TimedItem] = {
      timeDao.fetchForUser(userId)
    }

    def fetchUserHistory(creds: Login): List[TimedItem] = {

      val user_id: Option[Int] = userDao.getUserId(creds)

      user_id match {
        case Some(user_id) => {
          timeDao.fetchForUser(user_id)
        }
        case None => List[TimedItem]()
      }

    }

    def addUserHistoryItem(id: Long, timedItem: TimedItem) = {
      // emptyHistory += timedItem
    }

    def login(creds: Login) = {
      userDao.getUserId(creds)
    }

    def createAccount(creds: Login): Either[String, Int] = {
      try {
        Right(userDao.createUser(creds))
      } catch {
        case e: Exception => Left(e.toString())
      }
    }
}
