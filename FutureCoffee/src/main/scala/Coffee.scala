package coffee

import scala.util.Try

/**
  * Prepare Coffee
  * 1. Grind the required coffee beans
  * 2. Heat some water
  * 3. Brew an espresso using the ground coffee and the heated water
  * 4. Forth some milk
  * 5. Combine the expreso and the frothed milk to a cappuccino
  *
  */


object Coffee {
  // define type aliases
  type CoffeeBeans = String
  type GroundCoffee = String
  case class Water(temperature: Int)
  type Milk = String
  type FrothedMilk = String
  type Espresso = String
  type Cappuccino = String

  // dummy implement of the individuals steps

  def grind(beans: CoffeeBeans): GroundCoffee = s"gound coffee of $beans"
  def heatWater(water: Water): Water = water.copy(temperature = 85)
  def frothMilk(milk: Milk): FrothedMilk = s"frothed $milk"
  def brew(coffee: GroundCoffee, heatedWater: Water): Espresso = s"$coffee + $heatedWater => espresso"
  def combine(espresso: Espresso, frothedMilk: FrothedMilk): Cappuccino = "cappuccino"

  // Some exception for things that might go wrong in the individual steps

  case class GrindingExcepption(msg: String) extends Exception(msg)
  case class FrothingException(msg: String) extends Exception(msg)
  case class WaterBoilingException(msg: String) extends Exception(msg)
  case class BrewingException(msg: String) extends Exception(msg)

  //prep cappuccino
  def prepCappuccino(): Try[Cappuccino] = for {
    ground <- Try(grind("arabica beans"))
    water <- Try(heatWater(Water(25)))
    expresso <- Try(brew(ground,water))
    foam <- Try(frothMilk("milk"))
  } yield combine(expresso,foam)

}
