package dao

import anorm.Macro
import anorm.SqlParser.scalar
import javax.inject._
import play.api.db.Database
import anorm._
import service.{TimedItem, Login}

@Singleton
class TimeDao @Inject()(timeDb: Database) {

    private val table: String = "dsrleiwu.public.timed_items"

    def fetchForUser(id: Long): List[TimedItem]= {
        val x = timeDb.withConnection( implicit con =>
            SQL"""
                select id, project, startTime, endTime, note from dsrleiwu.public.timed_items where user_id = $id;
            """.as(Macro.indexedParser[TimedItem].*)
        )
        x
    }

    def insertItem(userId: Long, timedItem: TimedItem) = {
        timeDb.withConnection( implicit con => 
            SQL"""
                insert into dsrleiwu.public.timed_items (id, project, startTime, endTime, note, user_id) 
                values (${timedItem.id}, ${timedItem.project}, ${timedItem.startTime}, ${timedItem.endTime}, ${timedItem.note}, $userId)
                returning id;
            """.execute
        )
    }

    def deleteItem(userId: Long, itemId: String) = {
        timeDb.withConnection( implicit con =>
            SQL"""
                delete from dsrleiwu.public.timed_items * where user_id = $userId and id = ${itemId}
            """.execute
        )
    }

    def updateItem(userId: Long, timedItem: TimedItem) = {
        timeDb.withConnection( implicit con =>
            SQL"""
                UPDATE dsrleiwu.public.timed_items
                SET project=${timedItem.project}, starttime=${timedItem.startTime}, endtime=${timedItem.endTime}, note=${timedItem.note}
                WHERE user_id = $userId and id = ${timedItem.id};
            """.execute
        )
    }
}