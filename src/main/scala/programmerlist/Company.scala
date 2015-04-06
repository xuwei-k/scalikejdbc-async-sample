package programmerlist

import org.joda.time.DateTime
import scalikejdbc._
import scalikejdbc.async.FutureImplicits._
import scalikejdbc.async._

import scala.concurrent._

case class Company(
  id: Long,
  name: String,
  url: Option[String] = None,
  createdAt: DateTime,
  deletedAt: Option[DateTime] = None) extends ShortenedNames {
  def save()(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Company] = Company.save(this)(session, cxt)

  def destroy()(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = Company.destroy(id)(session, cxt)
}

object Company extends SQLSyntaxSupport[Company] with ShortenedNames {
  override val columnNames = Seq("id", "name", "url", "created_at", "deleted_at")

  def apply(c: SyntaxProvider[Company])(rs: WrappedResultSet): Company = apply(c.resultName)(rs)

  def apply(c: ResultName[Company])(rs: WrappedResultSet): Company = new Company(
    id = rs.long(c.id),
    name = rs.string(c.name),
    url = rs.stringOpt(c.url),
    createdAt = rs.jodaDateTime(c.createdAt),
    deletedAt = rs.jodaDateTimeOpt(c.deletedAt)
  )

  lazy val c = Company.syntax("c")

  def create(name: String, url: Option[String] = None, createdAt: DateTime = DateTime.now)(
    implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Company] = {
    if(DateTime.now.getMillis % 10 == 0){
      //throw new IllegalArgumentException("oops.")
      Future.failed(new IllegalArgumentException("oops."))
    } else{
      for {
        id <- withSQL {
          insert.into(Company).namedValues(
            column.name -> name,
            column.url -> url,
            column.createdAt -> createdAt)
        }.updateAndReturnGeneratedKey()
      } yield Company(id = id, name = name, url = url, createdAt = createdAt)
    }
  }
}
