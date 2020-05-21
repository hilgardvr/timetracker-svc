package dao

import anorm.SqlParser.scalar
import javax.inject._
import play.api.db.Database
import anorm._
import service.Login

@Singleton
class UserDao @Inject()(timeDb: Database) {

    def getUserId(creds: Login): Option[Int] = {
        timeDb.withConnection( implicit con => 
            SQL"""
               select user_id from dsrleiwu.public.users where
                email = ${creds.email}
               and
                password = ${creds.password};
            """.as(scalar[Int].singleOpt)
        )
    }

    def createUser(creds: Login): Int = {
        timeDb.withConnection( implicit con => 
            SQL"""
                insert into dsrleiwu.public.users (email, password)
                values (${creds.email}, ${creds.password})
                returning user_id;
            """.as(scalar[Int].single)
        )
    }

}