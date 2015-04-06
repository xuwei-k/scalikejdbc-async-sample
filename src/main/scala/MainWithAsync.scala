import java.util.concurrent.TimeUnit

import akka.actor.{Props, ActorSystem, Actor}
import akka.pattern._
import akka.util.Timeout
import org.joda.time.DateTime
import programmerlist.Company
import scalikejdbc.async.ShortenedNames._
import scalikejdbc.async._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

object MainWithAsync extends App {

  // set up connection pool (that's all you need to do)
  AsyncConnectionPool.singleton("jdbc:mysql://localhost:3306/foo", "root", "")

  // normalCreate
  akkaCreate

  private def normalCreate = {
    // create a new record within a transaction
    val created: Future[Company] = AsyncDB.localTx { implicit tx =>
      for {
        company <- Company.create("ScalikeJDBC, Inc." + DateTime.now.getMillis, Some("http://scalikejdbc.org/"))
        company <- Company.create("ScalikeJDBC, Inc." + DateTime.now.getMillis, Some("http://scalikejdbc.org/"))
        company <- Company.create("ScalikeJDBC, Inc." + DateTime.now.getMillis, Some("http://scalikejdbc.org/"))
        company <- Company.create("ScalikeJDBC, Inc." + DateTime.now.getMillis, Some("http://scalikejdbc.org/"))
        company <- Company.create("ScalikeJDBC, Inc." + DateTime.now.getMillis, Some("http://scalikejdbc.org/"))
      } yield company
    }

    Await.result(created, 5.seconds)
  }

  private def akkaCreate = {
    implicit val timeout = Timeout(60, TimeUnit.SECONDS)

    val system = ActorSystem("mySystem")
    val cActor = system.actorOf(Props[CompanyActor], "cActor")

    val tx = AsyncDB.localTx { implicit tx =>
      /*
      val c1 = cActor.ask("ScalikeJDBC, Inc." + DateTime.now.getMillis)
      val c2 = cActor.ask("ScalikeJDBC, Inc." + DateTime.now.getMillis)
      val c3 = cActor.ask("ScalikeJDBC, Inc." + DateTime.now.getMillis)
      val c4 = cActor.ask("ScalikeJDBC, Inc." + DateTime.now.getMillis)
      val c5 = cActor.ask("ScalikeJDBC, Inc." + DateTime.now.getMillis)
      */
      for {
        c <- cActor.ask(("ScalikeJDBC, Inc." + DateTime.now.getMillis, tx))
        c <- cActor.ask(("ScalikeJDBC, Inc." + DateTime.now.getMillis, tx))
        c <- cActor.ask(("ScalikeJDBC, Inc." + DateTime.now.getMillis, tx))
        c <- cActor.ask(("ScalikeJDBC, Inc." + DateTime.now.getMillis, tx))
        c <- cActor.ask(("ScalikeJDBC, Inc." + DateTime.now.getMillis, tx))
      } yield()
    }

    try Await.result(tx, 5.seconds)
    catch { case e:Exception => println(e.getMessage)}

    system.shutdown()
  }
}

class CompanyActor extends Actor {
  implicit val session = AsyncDB.sharedSession

  def receive = {
    case x:(String, EC) =>
      implicit val ec = x._2
      println(session.toString)
      Company.create(x._1, Some("http://scalikejdbc.org/")).pipeTo(sender())
  }
}