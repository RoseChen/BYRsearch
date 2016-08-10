package models

/**
  * Created by Rose on 2016/8/2.
  */
case class Article(url: String, id: String, firstId: String, author: String, boardName: String, title: String, content: String, postTime: String, fromIp: String)

object Article {

  def all(): List[Article] = Nil

}
