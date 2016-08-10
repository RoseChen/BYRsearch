package actor

/**
  * Created by Rose on 2016/8/1.
  */

import java.nio.file.Paths
import javax.inject.{Singleton, Inject}
import scala.util.Failure
import models.{ArticleDao, Article}
import scala.concurrent.ExecutionContext.Implicits.global
import actor.IndexProtocol.{AddSignal, InitSignal}
import akka.actor._
import org.apache.lucene.index.{IndexWriter, IndexWriterConfig}
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer
import org.apache.lucene.document.{TextField, StringField}
import org.apache.lucene.document.Field.Store
import org.apache.lucene.document.Document
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.store.Directory
import org.apache.lucene.util.Version

@Singleton
class IndexActor @Inject()(val articleD : ArticleDao) extends Actor with ActorLogging{
  def receive = {
    case InitSignal => {
      createIndex
      log.info("Create Index")
    }
    case AddSignal => {
      log.info("Add Index")
    }

  }
  override def preStart(): Unit = {
    // 初始化children
    self ! InitSignal
  }
  // 重写postRestart防止preStart每次重启都被调用
  override def postRestart(reason: Throwable): Unit = ()
  override def preRestart(reason: Throwable, message: Option[Any]): Unit =
  {
    // 仍然要清理自己，但是不Stop children
    postStop()
  }
  def createIndex = {
    try {
      val path = "C:/Users/hp-pc/IdeaProjects/web3/app/index" //索引文件夹
      //创建中文分析器
      val analyzer = new SmartChineseAnalyzer()
      val directory = FSDirectory.open(Paths.get(path))
      val indexWriterConfig = new IndexWriterConfig(analyzer)
      val indexWriter = new IndexWriter(directory,indexWriterConfig)
      articleD.getAll().map {
        result =>
          val re = result.map {
            each => {
              Article(each.url, each.id, each.firstid, each.author, each.boardname, each.title, each.content, each.posttime, each.fromip)
            }
          }.toList
          re.foreach(
            e => {
              val doc = new Document()
              doc.add(new StringField("url", e.url, Store.YES))
              doc.add(new StringField("id", e.id, Store.YES))
              doc.add(new StringField("firstId", e.firstId, Store.YES))
              doc.add(new StringField("author", e.author, Store.YES))
              doc.add(new StringField("boardName", e.boardName, Store.YES))
              doc.add(new TextField("title", e.title, Store.YES))
              doc.add(new TextField("content", e.content, Store.YES))
              doc.add(new StringField("postTime", e.postTime, Store.YES))
              doc.add(new StringField("fromIp", e.fromIp, Store.YES))
              println(doc)
              indexWriter.addDocument(doc)
              indexWriter.commit()
            }
          )
      }.andThen {
        case Failure(e) => e.printStackTrace()
      }

      println("Create: "+indexWriter.numDocs()+" index")
    }
    catch {
      case ex: Exception => {
        println(ex.printStackTrace())
      }
    }
    finally {
      println("Done")
    }
  }
}

object IndexActor {
  implicit val system = ActorSystem("IndexSystem")
}