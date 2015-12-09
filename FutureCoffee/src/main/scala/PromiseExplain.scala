
import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success}


/**
  * Created by dylan on 12/6/15.
  */
object PromiseExplain extends App{

  /**
    * Create a Future
    * 1. pass the value to the apply method of the Future companion object and given ExecutionContext was in the scope
    * 2. use Promise complete a Future by putting a value into it, only can be done once. Once completed, immutable
    * A Promise instance => exactly one instance of Future
    */

  /**
    * Promise a rosy future
    * Use case: suppose politicians that ghen got elected into office promised their voters a tax cut.
    * This prepresented as a Promise[TaxCut], by callying the apply method on the Promise companion object
    */
  case class TaxCut(reduction: Int)
  val taxCut = Promise[TaxCut]()
  val taxCut2: Promise[TaxCut] = Promise()

  /**
    * Once created a Promise, get the Future belonging to it by calling the future method on the Promise Instance
    */

  val taxCutF = taxCut.future
  /**
    * Note: The returned Future might not be the same object as the Promise, but calling the future method of a Promise
    * multiple times will definitely always return the same object to make sure the one-to-one relationship between
    * a Promise and its Future is preserved.
    */

  /**
    * Completing a Promise
    * Once create a Promise, you will deliver on it in the forseeable Future
    * in Scala, complete a Promise either with a success or a failure
    */

  /**
    * Delivering on your Promise
    * To complete a Promise with a success by calling its success method and passing it the value that the
    * Future associated with
    */

  taxCut.success(TaxCut(20))

  /**
    * Note: Once have done this, that Promise instance is no longer writable.
    */

  /**
    * Usually, the completion of the Promise and the procssing of the completed Future will not happen in the same thread
    * It's more likely that you create your promise, start computing its value in another thread and immediately
    * return the uncompleted Future to the caller
    */

  def redeemCampinPledge(): Future[TaxCut] = {
    val p = Promise[TaxCut]()
    Future {
      println("Starting the new legislative period..")
      Thread.sleep(2000)
      p.success(TaxCut(20))
      println("We reduced the taxes! You must reelect us!!!")
    }
    p.future
  }

  /**
    * In the code above, we use the apply method of the Future companion object. Beacuse it is so convenient for
    * executing a block of code async.
    * The point is not the Promise is not completed in the caller thread.
    */

  /**
    * Redeem our campain pledge then and add an onComplete callback to our Future:
    */

  val taxCutFComplete: Future[TaxCut] = redeemCampinPledge()
  println("Now that they're elected, let's see if they remember their promise")
  taxCutF.onComplete {
    case Success(TaxCut(reduction)) =>
      println(s"A miracle! They realy cut our taxes by $reduction percentage point!")
    case Failure(ex) =>
      println(s"They are liars, because of ${ex.getMessage}")
  }

  /**
    * Handle Failure of Promise
    * Politician are liars, if that happens, you can still complete your Promise instance gracefully,
    * by calling its failure method and passing it an exception
    */

  case class LameExcuse(msg: String) extends Exception(msg)
  def redeemCampainPledge(): Future[TaxCut] = {
    val p = Promise[TaxCut]()
    Future{ // calling apply method of Future's companion obejct, to handle async
      println("String the new legislative period.")
      Thread.sleep(20000)
      p.failure(LameExcuse("Global economy crisis"))
      println("we didin't fulfill our promises")
    }
    p.future
  }


  /**
    * Non-blocking IO
    * Blocking IO
    * Long-running computations
    *
    */


  /**
    * Non-blkciing IO
    * User Case: query a databse with a driver in order to respond to a HTTP Request,
    *
    */

  /**
    * Bloacking IO
    * Somethings there is no NIO-based library available.
    * Most database drivers in Java World are blocking IO
    * If you made a query to your db with such a driver in order to responsed a HTTP request,
    * that call would be made on a web server thread(not running in same thread). To avoid that,
    * place all the code talking to the db
    * inside a Future block
    */

  def queryDB(query:String) = {}
  val query:String = ""
  Future {
    queryDB(query)
  }

  /**
    * You can create a dedicated ExecutionContext that your will have in scope in your database layer
    */
  val executorService = Executors.newFixedThreadPool(4)
  val executionContext = ExecutionContext.fromExecutorService(executorService)

  /**
    * Long-running computations
    * Sometings you will occasionally have to call long-running tasks that don't involve any IO at all,
    * which they are CPU heavy tasks, should not be executed by a web server thread.
    * Hence, you should turn them into Futures
    */
  def longRunningComputation(data:String, moreData:String) ={}

  Future {
    longRunningComputation(data= "",moreData = "")
  }

  /**
    * Note: If you have long-running computations, having them run in a separate ExecutionContext for CPU-bound
    * tasks is a good idea
    */
}

