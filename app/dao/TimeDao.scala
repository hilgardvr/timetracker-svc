package dao

import anorm.Macro
import anorm.SqlParser.scalar
import javax.inject._
import play.api.db.Database
import anorm._
import service.{TimedItem, Login}

@Singleton
class TimeDao @Inject()(timeDb: Database) {

    def fetchForUser(id: Long): List[TimedItem]= {
        val x = timeDb.withConnection( implicit con =>
            SQL"""
                select id, project, startTime, endTime, note from dsrleiwu.public.timed_items where user_id = $id;
            """.as(Macro.indexedParser[TimedItem].*)
        )
        println(s"result: $x")
        x
    }

    def insertItem(timedItem: TimedItem, userId: Int): Option[Int] = {
        timeDb.withConnection( implicit con => 
            SQL"""
                insert into dsrleiwu.public.timed_items (project, startTime, endTime, note, user_id) 
                values (${timedItem.project}, ${timedItem.startTime}, ${timedItem.endTime}, ${timedItem.note}, $userId)
                returning user_id;
            """.as(scalar[Int].singleOpt)
        )
    }
}