package service

import service.TimedItem
import java.time.Instant
import scala.collection.mutable.ListBuffer

class HistoryService {
    val now: Long = Instant.now().getEpochSecond()

    val history: ListBuffer[TimedItem] = {
        ListBuffer(
            TimedItem(1, "Project name1", now, now + 1, "note1"), 
            TimedItem(2, "Project name2", now, now + 2, "note2")
        )
    }

    def fetchUserHistory(id: Long) = {
        history
    }

    def addUserHitoryItem(id: Long, timedItem: TimedItem) = {
      history += timedItem
    }
}