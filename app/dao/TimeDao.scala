package dao

import anorm.Macro
import anorm.SqlParser.scalar
import javax.inject._
import play.api.db.Database
import anorm._
import service.{TimedItem, Login}

@Singleton
class TimeDao @Inject()(timeDb: Database) {

    def getUserId(creds: Login): Option[Int] = {
        timeDb.withConnection( implicit con => 
            SQL"""
               select user_id from dsrleiwu.public.users where
               userName = ${creds.userName} and password = ${creds.password};
            """.as(scalar[Option[Int]].single)
        )
    }

    def fetchForUser(id: Long): List[TimedItem]= {
        val x = timeDb.withConnection( implicit con =>
            SQL"""
                select id, project, startTime, endTime, note from dsrleiwu.public.timed_items where user_id = $id;
            """.as(Macro.indexedParser[TimedItem].*)
        )
        println(s"result: $x")
        x
    }

    def insertItem(timedItem: TimedItem, userId: Int): Boolean = {
        timeDb.withConnection( implicit con => 
            SQL"""
                insert into dsrleiwu.public.timed_items (project, startTime, endTime, note, user_id) 
                values ('test_projec1', 1588004579, 1588004579, 'test_note1', $userId);
                """.execute()
        )
    }
}