package dao

import anorm.Macro
import anorm.SqlParser.scalar
import javax.inject._
import play.api.db.Database
import anorm._
import service.{TimedItem, Login}

@Singleton
class TimeDao @Inject()(timeDb: Database) {

    def fetchForUser(userHash: String): List[TimedItem]= {
        timeDb.withConnection( implicit con =>
            SQL"""
                select id, project, startTime, endTime, note 
                from dsrleiwu.public.timed_items 
                where user_hash = $userHash 
                order by endtime desc;
            """.as(Macro.indexedParser[TimedItem].*)
        )
    }

    def insertItem(userHash: String, timedItem: TimedItem) = {
        timeDb.withConnection( implicit con => 
            SQL"""
                insert into dsrleiwu.public.timed_items (id, project, startTime, endTime, note, user_hash) 
                values (${timedItem.id}, ${timedItem.project}, ${timedItem.startTime}, ${timedItem.endTime}, ${timedItem.note}, $userHash)
                returning id;
            """.execute
        )
    }

    def deleteItem(userHash: String, itemId: String): Long = {
        timeDb.withConnection( implicit con =>
            SQL"""
                delete from dsrleiwu.public.timed_items * 
                where user_hash = $userHash and id = ${itemId}
            """.executeUpdate
        )
    }

    def updateItem(userHash: String, timedItem: TimedItem) = {
        timeDb.withConnection( implicit con =>
            SQL"""
                UPDATE dsrleiwu.public.timed_items
                SET project=${timedItem.project}, starttime=${timedItem.startTime}, endtime=${timedItem.endTime}, note=${timedItem.note}
                WHERE user_hash = $userHash and id = ${timedItem.id};
            """.execute
        )
    }
}