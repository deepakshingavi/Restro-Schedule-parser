package com.dp.training.formatter

import com.dp.training.model.Event
import spray.json.DefaultJsonProtocol._
import spray.json._

object BetterPersonFormat extends JsonFormat[Event] {
  val restroTypes = Set("open", "close")

  override def read(json: JsValue): Event = {
    val fields = json.asJsObject("Event object expected").fields

    if(!restroTypes.contains(fields("type").convertTo[String].toLowerCase)){
      throw new RuntimeException("Invalid event type:" + fields("type").convertTo[String])
    }

    Event(
      isOpenEvent = "open".equalsIgnoreCase(fields("type").convertTo[String]),
      value = fields("value").convertTo[Int]
    )
  }

  // serialization code
  override def write(event: Event): JsValue = JsObject(
    "type" ->  {
      val status : String = if (event.isOpenEvent) {
         "open"
      } else {
        "close"
      }
      status.toJson
    },
    "value" -> event.value.toJson
  )
}
