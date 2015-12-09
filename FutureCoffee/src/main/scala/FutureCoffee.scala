import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import scala.util.{Failure, Random, Success}

/**
  * Future: Future[T] <- scala.concurrent package
  * Future is a write-once container aka after completed, immutable, only read value
  * Promise: writing the computed value
  *
  *
  */

//object Future {
//  def apply[T](body: => T)(implicit execctx: ExecutionContext): Future[T]
//}

object FutureCoffee {
  // define type aliases
  type CoffeeBeans = String
  type GroundCoffee = String

  case class Water(temperature: Int)

  type Milk = String
  type FrothedMilk = String
  type Espresso = String
  type Cappuccino = String

  // dummy implement of the individuals steps

  def grind(beans: CoffeeBeans): Future[GroundCoffee] = Future {
    println("string grinding...")
    Thread.sleep(Random.nextInt(2000))
    if (beans == "baked beans") throw GrindingExcepption("are yu joking?")
    println("finished grinding...")
    s"gound coffee of $beans"
  }

  def heatWater(water: Water): Future[Water] = Future {
    println("heating the water now...")
    Thread.sleep(Random.nextInt(2000))
    println("hot, it's hot!")
    water.copy(temperature = 85)
  }

  def frothMilk(milk: Milk): Future[FrothedMilk] = Future {
    println("milk frothing system engaged!")
    Thread.sleep(Random.nextInt(2000))
    println("shutting down milk frothing system")
    s"frothed $milk"
  }

  def brew(coffee: GroundCoffee, heatedWater: Water): Future[Espresso] = Future {
    println("brewing coffee now...")
    Thread.sleep(Random.nextInt(2000))
    println("It's brewed!")
    s"$coffee + $heatedWater => espresso"
  }

  def combine(espresso: Espresso, frothedMilk: FrothedMilk): Cappuccino = "cappuccino"

  // Some exception for things that might go wrong in the individual steps
  case class GrindingExcepption(msg: String) extends Exception(msg)

  case class FrothingException(msg: String) extends Exception(msg)

  case class WaterBoilingException(msg: String) extends Exception(msg)

  case class BrewingException(msg: String) extends Exception(msg)

  /**
    * apply Callbacks
    * Callbacks for Futures are partial functions
    * call onSuccess => ge the computed value
    */
  grind("arabica beans").onSuccess { case ground =>
    println("okay, got my ground coffee")
  }

  /**
    * Register onComplete callback to handle both case
    */
  grind("baked beans").onComplete {
    case Success(ground) => println(s"got my $ground")
    case Failure(ex) => println("This grinder needs a replacement!")
  }

  /**
    * Composing Future
    * 1. mapping the future
    * 2. keeping the future flat
    * 3. for comprehensions
    */

  /**
    * Suppose that once your water has heated and you want to check if its temperature is ok
    * mapping your Future[Water] => Future[Boolean]
    * Future[Boolean] will be ssigned to temperatureOkay
    * when the Future function pass to map, you're in future or possible future. That mapping function gets
    * executed as soon as your Future[Water] instance has completed successfully. However, the timeline that
    * happens might not be the one you live in.
    * If instance of Future[Water] fails, what's taking place in the function you passed to map will never happen.
    * Instead the result of calling map will be a Future[Boolean] containing a Failure
    */
  val temperatureOkay: Future[Boolean] = heatWater(Water(25)).map { water =>
    println("checking water temperature is ok?") // will never be printed to the console
    ( 80 to 85 ).contains(water.temperature)
  }

  /**
    * Keeping the future flat
    * If the computation of one Future depneds on the result of another,
    * try to use flatMap to avoid a deeply nested structure of futures
    */
  def temperatureOkay(water: Water): Future[Boolean] = heatWater(Water(25)).map { water =>
    ( 80 to 85 ).contains(water.temperature)
  }

  /**
    * The mapping function is only executed after the Future[Water] instance has been completed
    * successfully
    */
  def flatFuture: Future[Boolean] = heatWater(Water(25)).flatMap {
    water => temperatureOkay(water)
  }

  /**
    * For comprehensions
    * same as flatMap
    * code easier to read
    */
  def acceptable: Future[Boolean] = for {
    heatedWater <- heatWater(Water(25))
    okay <- temperatureOkay(heatedWater)
  } yield okay

  /**
    * computed swquentially
    */

  //prep cappuccino
  def prepCappuccinoSequentially(): Future[Cappuccino] = for {
    ground <- grind("arabica beans")
    water <- heatWater(Water(25))
    expresso <- brew(ground, water)
    foam <- frothMilk("milk")
  } yield combine(expresso, foam)


  def prepareCappuccinoConcurrent(): Future[Cappuccino] = {
    val groundCoffee = grind("beans")
    val heatedWater = ( Water(23) )
    val frothedMilk = frothMilk("milk")

    for {
      ground <- groundCoffee
      water <- heatedWater
      foam <- frothedMilk
      expresso <- brew(ground, water)
    } yield combine(expresso, foam)

  }

  /**
    * Failure projections
    * Future[T] => map, flatMap, filter
    *
    * failed => Future[Throwable]
    *
    */






}


