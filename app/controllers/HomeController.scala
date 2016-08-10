package controllers

import java.text.SimpleDateFormat
import java.util.Date
import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.{Article,ArticleDao}
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor._
import actor._

import java.nio.file.Paths

import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.store.Directory
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.ScoreDoc
import org.apache.lucene.search.TopDocs
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser
import org.apache.lucene.queryparser.classic.ParseException
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.util.Version

import scala.concurrent.Future

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(val articleD : ArticleDao
                               //val system: ActorSystem
                              ) extends Controller {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  //val indexRef = system.actorOf(Props[IndexActor](new IndexActor(articleD)),"IndexActor")

  val searchForm = Form(
    "keyword" -> nonEmptyText
  )

  val filterForm = Form(
    tuple(
      "author" -> nonEmptyText,
      "boardName" -> nonEmptyText,
      "keyword" -> nonEmptyText
    )
  )

  val articleForm = Form(
    tuple(
      "url" -> text,
      "id" -> text,
      "firstId" -> text,
      "author" -> text,
      "boardName" -> text,
      "title" -> text,
      "content" -> text,
      "postTime" -> text,
      "fromIp" -> text
    )
  )

  def index = Action {
    Ok(views.html.index("",searchForm))
  }

  def search = Action.async {
    implicit request =>
      searchForm.bindFromRequest.value match{
        case Some(keyword) => {
          val re = searchK(keyword)
          Future.successful(Ok(views.html.solution(keyword,"","ALL", re, searchForm)))
        }
        case _ =>
          Future.successful(Ok("提交不能为空"))
      }
  }

  def searchK(queryStr: String) : List[Article] = {
    try {
      val path = "C:/Users/hp-pc/IdeaProjects/web3/app/index" //索引文件夹
      val analyzer = new SmartChineseAnalyzer()
      val directory = FSDirectory.open(Paths.get(path))
      val directoryReader = DirectoryReader.open(directory)
      val searcher = new IndexSearcher(directoryReader)
      val field = Array("id","firstId","author","boardName","title","content","postTime")
      val queryParser = new MultiFieldQueryParser(field,analyzer)
      val query = queryParser.parse(queryStr)
      val topDocs = searcher.search(query,10000)
      println("Total: "+topDocs.totalHits+" records")
      val scoreDoc = topDocs.scoreDocs
      var list: List[Article] = List()
      scoreDoc.foreach(
        s => {
          val doc = s.doc
          val a = searcher.doc(doc)
          list = list ::: List(Article(a.get("url"),a.get("id"),a.get("firstId"),a.get("author"),a.get("boardName"),a.get("title"),a.get("content"),a.get("postTime"),a.get("fromIp")))
        }
      )
      list
    }
    catch{
      case ex: Exception => {
        println(ex.printStackTrace())
        val list: List[Article] = List()
        list
      }
    }
  }

  def filter = Action.async {
    implicit request =>
      filterForm.bindFromRequest.value match{
        case Some((author,boardName,keyword)) => {
          println(author,boardName,keyword)
          if(boardName == "none") {
            if(author == "ALL"){
              Future.successful(Ok(views.html.solution(keyword,boardName,author, searchK(keyword), searchForm)))
            }
            else {
              var list: List[Article] = List()
              searchK(keyword).foreach(
                r => {
                  if(r.author == author)
                    list = list ::: List(r)
                }
              )
              Future.successful(Ok(views.html.solution(keyword, boardName,author, list, searchForm)))
            }
          }
          else {
            if(author == "ALL") {
              var list: List[Article] = List()
              searchK(keyword).foreach(
                r => {
                  if(r.boardName == boardName)
                    list = list ::: List(r)
                }
              )
              Future.successful(Ok(views.html.solution(keyword, boardName,author, list, searchForm)))
            }
            else {
              var list: List[Article] = List()
              searchK(keyword).foreach(
                r => {
                  if(r.boardName == boardName && r.author == author)
                    list = list ::: List(r)
                }
              )
              Future.successful(Ok(views.html.solution(keyword, boardName,author, list, searchForm)))
            }
          }
        }
      }
  }
}

