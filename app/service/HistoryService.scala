package service

import com.google.inject.Inject
import dao.TimeDao
import dao.UserDao
import org.mindrot.jbcrypt.BCrypt

class HistoryService @Inject()(
  timeDao: TimeDao,
  userDao: UserDao
) {

    def hashPassword(password: String): String = {
      BCrypt.hashpw(password, BCrypt.gensalt())
    }

     def checkPassword(creds: Login): Boolean = {
       val pwHash = userDao.getPasswordHash(creds)
       BCrypt.checkpw(creds.password, pwHash)
     }

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

    def addUserHistoryItem(userId: Long, timedItem: TimedItem): Boolean = {
      timeDao.insertItem(userId, timedItem)
    }

    def login(creds: Login): Option[Int] = {
      if (checkPassword(creds)) userDao.getUserId(creds)
      else None
    }

    def createAccount(creds: Login): Either[String, Int] = {
      try {
        val hashedCreds = Login(creds.email, hashPassword(creds.password))
        Right(userDao.createUser(hashedCreds))
      } catch {
        case e: Exception => Left(e.toString())
      }
    }

    def deleteItem(userId: Long, itemId: String) = {
      timeDao.deleteItem(userId, itemId)
    }

    def updateItem(userId: Long, timedItem: TimedItem) = {
      timeDao.updateItem(userId, timedItem)
    }
}
