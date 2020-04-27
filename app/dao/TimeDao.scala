package dao

import anorm.Macro
import javax.inject._
import play.api.db.Database
import anorm._
import service.TimedItem

@Singleton
class TimeDao @Inject()(timeDb: Database) {
    def fetchForUser(id: Long) = {
        val x = timeDb.withConnection( implicit con =>
            SQL"""
                select * from dsrleiwu.public.timed_items;
            """.as(Macro.indexedParser[TimedItem].single)
        )
        println(s"result: $x")
        x
    }
}