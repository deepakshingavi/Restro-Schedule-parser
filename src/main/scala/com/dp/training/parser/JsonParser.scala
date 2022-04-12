package com.dp.training.parser

import com.dp.training.formatter.BetterPersonFormat
import com.dp.training.model.Event
import spray.json.DefaultJsonProtocol._
import spray.json._

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId, ZonedDateTime}
import scala.collection.mutable
import scala.util.Try


object JsonParser {

  implicit val eventFormatter = BetterPersonFormat

  implicit val movieFormat = mapFormat[String, List[Event]]

  val MAX_HOUR: Int = 3600 * 24
  val validaDays = List("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")

  def getHour(value: Int): String = {
    val formattedTimeStr = DateTimeFormatter.ofPattern("h:mm a")
      .format(
        LocalDateTime.ofEpochSecond(value, 0,
          ZonedDateTime.now(ZoneId.of("UTC")).getOffset))
      .replace(":00", "")
    formattedTimeStr

  }

  def computeWeekHours(parsedObj: Map[String, List[Event]]): Array[(String, String)] = {
    val weekChorus = new Array[mutable.Seq[Event]](7)

    validaDays.foreach(weekday => {
      val weekIndex = validaDays.indexOf(weekday)
      weekChorus(weekIndex) = mutable.Seq.empty

      if (parsedObj.contains(weekday) && parsedObj(weekday).nonEmpty) {
        val firstStatus = parsedObj(weekday).head
        if (!firstStatus.isOpenEvent) {
          weekChorus(weekIndex) = weekChorus(weekIndex) ++ parsedObj(weekday).drop(1)
          if (weekIndex != 0) {
            weekChorus(weekIndex - 1) = weekChorus(weekIndex - 1) :+ firstStatus
          } else {
            weekChorus(6) = mutable.Seq(firstStatus)
          }
        } else {
          weekChorus(weekIndex) = weekChorus(weekIndex) ++ parsedObj(weekday)
        }
      }
    })

    val isMondayFirstActionCLose = Try(!parsedObj("monday").head.isOpenEvent).getOrElse(false)
    if (isMondayFirstActionCLose) {
      weekChorus(6) = weekChorus(6) :+ parsedObj("monday").head
    }

    val weekHours: Array[(String, String)] = weekChorus.zipWithIndex.map(element => {
      val timeSlots = element._1
        .toSeq
        .map(event =>
          if (event.isOpenEvent) {
            getHour(event.value)
          } else {
            " - " + getHour(event.value) + ","
          }
        )
        .mkString("")
        .dropRight(1)

      (validaDays(element._2),
        if (timeSlots.isEmpty) {
          "Closed"
        } else {
          timeSlots
        })
    })

    weekHours
  }

  def parseJsonSchedule(json: String): Map[String, List[Event]] = {
    val result: Map[String, List[Event]] = json
      .stripMargin
      .parseJson
      .convertTo[Map[String, List[Event]]]
      .map { case (key: String, value: List[Event]) => key.toLowerCase -> value }

    result
  }



}



