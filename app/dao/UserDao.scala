package dao

import anorm.Macro
import anorm.SqlParser.scalar
import javax.inject._
import play.api.db.Database
import anorm._
import service.{TimedItem, Login}

@Singleton
class UserDao @Inject()(timeDb: Database) {

    def readUserId(creds: Login): Option[Int] = {
        timeDb.withConnection( implicit con => 
            SQL"""
               select user_id from dsrleiwu.public.users where
               email = ${creds.email};
            """.as(scalar[Int].singleOpt)
        )
    }

    def createUser(creds: Login) = {
        timeDb.withConnection( implicit con => 
            SQL"""
                insert into dsrleiwu.public.users (email, password)
                values (${creds.email}, ${creds.password});
            """.execute()
        )
    }

}