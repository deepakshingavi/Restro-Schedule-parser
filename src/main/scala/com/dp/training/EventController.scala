package com.dp.training

import com.dp.training.model.Event
import com.dp.training.parser.JsonParser.{MAX_HOUR, computeWeekHours, parseJsonSchedule, validaDays}



object EventController {

  val MIN_HOUR = 0

  def computeRestaurantHours(json: String): Array[(String, String)] = {
    val restroScheduleMap = parseJsonSchedule(json)
    validate(restroScheduleMap)
    val weekHours = computeWeekHours(restroScheduleMap)
    displayRestroSchedule(weekHours)
    weekHours
  }

  def validate(parsedInput: Map[String, List[Event]]): Unit = {
    validateDays(parsedInput.keySet)

    val uniqRestroHours = parsedInput.values.flatMap(events => events.map(e => e.value)).toSet
    validateHours(uniqRestroHours)

    validateBalancedOpenClosed(parsedInput)
  }

  def validateDays(keys: Set[String]): Unit = {
    val invalidDays = keys.removedAll(validaDays)
    if (invalidDays.nonEmpty) {
      throw new IllegalArgumentException("Invalid day(s) : " + invalidDays)
    }
  }

  def validateHours(hours: Set[Int]): Unit = {
    val invalidHours = hours.filter(hour => MAX_HOUR < hour || hour < MIN_HOUR)
    if (invalidHours.nonEmpty) {
      throw new IllegalArgumentException("Invalid restaurant hour(s) : " + invalidHours)
    }
  }


  def validateBalancedOpenClosed(parsedInput: Map[String, List[Event]]): Unit = {
    val matchedStatuses = parsedInput
      .flatMap(day => day._2.map(event => event.isOpenEvent))
      .toSeq
      .groupMapReduce[Boolean, Int](e => e)(_ => 1)(_ + _)

    val isOpenCountSome = matchedStatuses.get(true)
    val isCloseCountSome = matchedStatuses.get(false)

    (isOpenCountSome, isCloseCountSome) match {
      case (None, None) => throw new RuntimeException("Empty restaurant schedule!!!")
      case (_, None) | (None, _) => throw new RuntimeException("Mis-matched Restaurant status!!!")
      case it if it._1.get != it._2.get => throw new RuntimeException(s"Un balanced event count.\nopen=${it._1.get}\nclose=${it._2.get}!!!")
      case _ =>
    }
  }

  def displayRestroSchedule(weekHours: Array[(String, String)]): Unit = {
    val toPrint = weekHours.map(daySchedule => {
      daySchedule._1.capitalize + ": " + daySchedule._2
    }).mkString("\n")
    println("<---------------------Restaurant Schedule-------------------->")
    println(toPrint)
  }

}
