package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object SlickTables extends {
  val profile = slick.driver.MySQLDriver
} with SlickTables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait SlickTables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = tArticle.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table tArticle
   *  @param url Database column url SqlType(VARCHAR), Length(255,true)
   *  @param id Database column id SqlType(VARCHAR), PrimaryKey, Length(255,true)
   *  @param firstid Database column firstId SqlType(VARCHAR), Length(255,true)
   *  @param author Database column author SqlType(VARCHAR), Length(255,true)
   *  @param boardname Database column boardName SqlType(VARCHAR), Length(255,true)
   *  @param title Database column title SqlType(VARCHAR), Length(255,true)
   *  @param content Database column content SqlType(VARCHAR), Length(255,true)
   *  @param posttime Database column postTime SqlType(VARCHAR), Length(255,true)
   *  @param fromip Database column fromIp SqlType(VARCHAR), Length(255,true) */
  case class rArticle(url: String, id: String, firstid: String, author: String, boardname: String, title: String, content: String, posttime: String, fromip: String)
  /** GetResult implicit for fetching rArticle objects using plain SQL queries */
  implicit def GetResultrArticle(implicit e0: GR[String]): GR[rArticle] = GR{
    prs => import prs._
    rArticle.tupled((<<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String]))
  }
  /** Table description of table article. Objects of this class serve as prototypes for rows in queries. */
  class tArticle(_tableTag: Tag) extends Table[rArticle](_tableTag, "article") {
    def * = (url, id, firstid, author, boardname, title, content, posttime, fromip) <> (rArticle.tupled, rArticle.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(url), Rep.Some(id), Rep.Some(firstid), Rep.Some(author), Rep.Some(boardname), Rep.Some(title), Rep.Some(content), Rep.Some(posttime), Rep.Some(fromip)).shaped.<>({r=>import r._; _1.map(_=> rArticle.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column url SqlType(VARCHAR), Length(255,true) */
    val url: Rep[String] = column[String]("url", O.Length(255,varying=true))
    /** Database column id SqlType(VARCHAR), PrimaryKey, Length(255,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(255,varying=true))
    /** Database column firstId SqlType(VARCHAR), Length(255,true) */
    val firstid: Rep[String] = column[String]("firstId", O.Length(255,varying=true))
    /** Database column author SqlType(VARCHAR), Length(255,true) */
    val author: Rep[String] = column[String]("author", O.Length(255,varying=true))
    /** Database column boardName SqlType(VARCHAR), Length(255,true) */
    val boardname: Rep[String] = column[String]("boardName", O.Length(255,varying=true))
    /** Database column title SqlType(VARCHAR), Length(255,true) */
    val title: Rep[String] = column[String]("title", O.Length(255,varying=true))
    /** Database column content SqlType(VARCHAR), Length(255,true) */
    val content: Rep[String] = column[String]("content", O.Length(255,varying=true))
    /** Database column postTime SqlType(VARCHAR), Length(255,true) */
    val posttime: Rep[String] = column[String]("postTime", O.Length(255,varying=true))
    /** Database column fromIp SqlType(VARCHAR), Length(255,true) */
    val fromip: Rep[String] = column[String]("fromIp", O.Length(255,varying=true))
  }
  /** Collection-like TableQuery object for table tArticle */
  lazy val tArticle = new TableQuery(tag => new tArticle(tag))
}
