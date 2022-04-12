import com.dp.training.model.Event
import com.dp.training.parser.JsonParser
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

class JsonParserTest extends AnyFunSuite with BeforeAndAfter {

  test("simple json parsing") {
    val input =
      """
        |{
        |  "monday" : [
        |    {
        |      "type" : "open",
        |      "value" : 32400
        |    },
        |    {
        |      "type" : "close",
        |      "value" : 72000
        |    }
        |  ]
        |}
      """.stripMargin

    val restroSchedules = JsonParser.parseJsonSchedule(input)

    assertResult(1)(restroSchedules.keys.size)
    assertResult(1)(restroSchedules.values.size)
    assertResult(true)(restroSchedules.contains("monday"))
    assertResult(2)(restroSchedules("monday").size)
    assertResult(Set("monday"))(restroSchedules.keys)
    assertResult(List(Event(true, 32400), Event(false, 72000)))(restroSchedules("monday"))
  }

  test("date parsing") {

    Array(
      (36000, false, "10 AM"),
      (64800, true, "6 PM"),
      (37800, false, "10:30 AM"),
      (64800, true, "6 PM"),
      (36000, false, "10 AM"),
      (3600, true, "1 AM"),
      (36000, false, "10 AM"),
      (3600, true, "1 AM"),
      (43200, false, "12 PM"),
      (75600, true, "9 PM"),
    ).foreach(event => {
      assertResult(event._3)(JsonParser.getHour(event._1))
    })
  }
}
