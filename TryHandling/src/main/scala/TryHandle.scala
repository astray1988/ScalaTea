import java.io.InputStream

import scala.util.Try

/**
  * Created by dylan on 12/6/15.
  */
object TryHandle {

  /**
    * Try[A] => Success[A]
    *        => Failure[A] -> Throwable
    *  when we use Try?
    *  If we know that a computation may result in an error, we can simply use Try[A] as the return
    *  type of our function
    */
  case class Connection() {
    def getInputStream: InputStream= {
      new InputStream {
        override def read(): Int = ???
      }
    }
  }
  case class URL(url: String) {
    def openConnection(): Connection

    def getprotocol(): String = {
      "http"
    }
  }
  case class MalformedURLException(msg: String) extends Exception(msg)

  def parseURL(url: String): Try[URL] = Try(new URL(url))


  /**
    * Chaining operations with Try
    * Mapping and flat mapping
    * filter and foreach
    * for comprehensions
    */

  /**
    * Mapping and flat mapping
    * if chain multipe map operations will => nested Try[Try[Object]] X => flatMap
    *
    */

  def inputStreamForURL(url: String): Try[InputStream] = parseURL(url).flatMap { url =>
    Try {
      url.openConnection().getInputStream
    }
  }

  /**
    * Filter and foreach
    * filter: return s a Failure if the Try on which it is called is already a Failure or
    * if the predicate passed to it returns a false
    * If the Try on which it is called is a Success and the predicate returns true, that Sucess instance is
    * returned unchanged
    */

  def parseHttpURL(url: String) = parseURL(url).filter(_.getprotocol() == "http")

  parseHttpURL("http://www.dfsf.com") // results in a Success[URL]
  parseHttpURL("ftp://www.dfsf.com") // results in a Failure[URL]

  /**
    * For comprehensions
    * Chain operations on Try instance
    * Easy readable code
    */

  /**
    * Pattern Matching
    * Try => case Success(s)
    *     => case Failure(ex)
    */

  /**
    * Recovering from a Failure
    * Use case: Want to establish some kind of behaviour in the case of a Failure,
    * don't have to use getOrElse.
    * Use recover, which expects a partial function and returns another Try
    */


}
