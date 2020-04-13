package service

import service.TimedItem
import java.time.Instant

class HistoryService {
    val now: Long = Instant.now().getEpochSecond()

    val history: List[TimedItem] = {
        List(
            TimedItem(1, "Project name", now, now + 1, "note"), 
            TimedItem(2, "Project name", now, now + 1, "note")
        )
    }

    def fetchUserHistory(id: Long) = {
        history
    }

    //def addUserHitoryItem(id: Long
}