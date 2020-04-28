package service

import service.TimedItem
import java.time.Instant
import scala.collection.mutable.ListBuffer
import com.google.inject.Inject
import dao.TimeDao

class HistoryService @Inject()(timeDao: TimeDao) {

    // val now: Long = Instant.now().getEpochSecond()

    // val history: ListBuffer[TimedItem] = {
    //     ListBuffer(
    //         TimedItem("1", "Project name1", now, now + 1, "note1"), 
    //         TimedItem("2", "Project name2", now, now + 2, "note2")
    //     )
    // }

    val emptyHistory: ListBuffer[TimedItem] = new ListBuffer[TimedItem]()

    def fetchUserHistory(id: Long) = {

        timeDao.fetchForUser(id)

    }

    def addUserHistoryItem(id: Long, timedItem: TimedItem) = {
      // emptyHistory += timedItem
    }

    def login(creds: Login) = {
      timeDao.getUserId(creds)
    }
}
