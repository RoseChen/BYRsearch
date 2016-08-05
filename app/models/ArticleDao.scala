package models

/**
  * Created by Rose on 2016/8/2.
  */
import javax.inject.{Inject, Singleton}

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
@Singleton
class ArticleDao @Inject()(
                            protected val dbConfigProvider: DatabaseConfigProvider
                          ) extends HasDatabaseConfigProvider[JdbcProfile]{
  import SlickTables._
  import slick.driver.MySQLDriver.api._
  val articleT = tArticle
/*
  def insert(url: String, id: String, firstId: String, author: String, boardName: String, title: String, content: String, postTime: String, fromIp: String) ={
    db.run(
      articleT.map(c =>(c.url,c.id,c.firstid,c.author,c.boardname,c.title,c.content,c.posttime,c.fromip)).returning(articleT.map(_.id)) += (url,id,firstId,author,boardName,title,content,postTime,fromIp)
    ).mapTo[Int]
  }
*/
  def getAll() ={
    db.run(
      articleT.result
    )
  }

}