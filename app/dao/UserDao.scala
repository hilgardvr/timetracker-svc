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
            """.as(scalar[Int].singleOpt)
        )
    }

    def getUserHash(creds: Login): Option[String] = {
        timeDb.withConnection(implicit con => 
            SQL"""
               select user_hash from dsrleiwu.public.users where
                email = ${creds.email}
            """.as(scalar[String].singleOpt)
        )
    }

    def createUser(creds: Login): String = {
        timeDb.withConnection( implicit con => 
            SQL"""
                insert into dsrleiwu.public.users (email, password, user_hash)
                values (${creds.email}, ${creds.password}, concat(md5(random()::text), ${creds.email}))
                returning user_hash;
            """.as(scalar[String].single)
        )
    }

    def getPasswordHash(creds: Login): Option[String] = {
      timeDb.withConnection( implicit con =>
        SQL"""
             select password
             from users
             where email = ${creds.email}
        """.as(scalar[String].singleOpt)
      )
    }

}