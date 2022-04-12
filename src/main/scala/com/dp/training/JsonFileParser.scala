package com.dp.training

import scala.io.{BufferedSource, Source}

object JsonFileParser  {

  def main(args: Array[String]): Unit = {
    val file : BufferedSource = Source.fromFile(args(0))
    val json = file.getLines().mkString
    file.close()
    EventController.computeRestaurantHours(json)
  }

}