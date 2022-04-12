import com.dp.training.EventController
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

class EventControllerTest extends AnyFunSuite with BeforeAndAfter {

  test("empty input test") {
    val input =
      """
        |{}
      """.stripMargin


    val thrown = intercept[Exception] {
      EventController.computeRestaurantHours(input)
    }

    assertResult("Empty restaurant schedule!!!")(thrown.getMessage)
  }

  test("display json parsing") {
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

    val result = EventController.computeRestaurantHours(input)
    assertResult(Array(("monday", "9 AM - 8 PM"), ("tuesday", "Closed"), ("wednesday", "Closed"), ("thursday", "Closed"), ("friday", "Closed"), ("saturday", "Closed"), ("sunday", "Closed")))(result)
  }

  test("whole week test") {
    val input =
      """
        {
        "monday" : [],
        "tuesday" : [
        {
        "type" : "open",
        "value" : 36000
        },
        {
        "type" : "close",
        "value" : 64800
        }
        ],
        "wednesday" : [],
        "thursday" : [
        {
        "type" : "open",
        "value" : 37800
        },
        {
        "type" : "close",
        "value" : 64800
        }
        ],
        "friday" : [
        {
        "type" : "open",
        "value" : 36000
        }
        ],
        "saturday" : [
        {
        "type" : "close",
        "value" : 3600
        },
        {
        "type" : "open",
        "value" : 36000
        }
        ],
        "sunday" : [
        {
        "type" : "close",
        "value" : 3600
        },
        {
        "type" : "open",
        "value" : 43200
        },
        {
        "type" : "close",
        "value" : 75600
        }
        ]
        }
      """.stripMargin

    val result = EventController.computeRestaurantHours(input)
    assertResult(Array(("monday", "Closed"), ("tuesday", "10 AM - 6 PM"), ("wednesday", "Closed"), ("thursday", "10:30 AM - 6 PM"), ("friday", "10 AM - 1 AM"), ("saturday", "10 AM - 1 AM"),
      ("sunday", "12 PM - 9 PM")))(result)
  }

  test("corner case") {
    val input =
      """
        {
        "monday" : [{"type" : "close","value" : 75600}],
        "sunday" : [{"type" : "open","value" : 0}]
        }
      """.stripMargin

    val result = EventController.computeRestaurantHours(input)
    assertResult(Array(("monday", "Closed"), ("tuesday", "Closed"), ("wednesday", "Closed"), ("thursday", "Closed"), ("friday", "Closed"), ("saturday", "Closed"), ("sunday", "12 AM - 9 PM")))(result)
  }

  test("invalid type") {
    val input =
      """
        {
        "monday" : [{"type" : "closed","value" : 75600}],
        "sunday" : [{"type" : "open","value" : 0}]
        }
      """.stripMargin

    val thrown = intercept[Exception] {
      EventController.computeRestaurantHours(input)
    }
    assertResult("Invalid event type:closed")(thrown.getMessage)
  }

  test("invalid day") {
    val input =
      """
        {
        "holiday" : [{"type" : "close","value" : 75600},{"type" : "close","value" : 75800}],
        "sunday" : [{"type" : "open","value" : 0}]
        }
      """.stripMargin

    val thrown = intercept[Exception] {
      EventController.computeRestaurantHours(input)
    }
    assertResult("Invalid day(s) : Set(holiday)")(thrown.getMessage)
  }

  test("mis matched event types") {
    val input =
      """
        {
        "monday" : [{"type" : "close","value" : 75600},{"type" : "close","value" : 75800}],
        "sunday" : [{"type" : "open","value" : 0}]
        }
      """.stripMargin

    val thrown = intercept[Exception] {
      EventController.computeRestaurantHours(input)
    }
    assertResult("Un balanced event count.\nopen=1\nclose=2!!!")(thrown.getMessage)
  }

  test("alt day test") {
    val input =
      """
        |{
        |  "monday": [{
        |      "type": "open",
        |      "value": 36000
        |    }],
        |  "tuesday": [ {
        |      "type": "close",
        |      "value": 21600
        |    }
        |  ],
        |  "wednesday": [{
        |      "type": "open",
        |      "value": 86400
        |    }],
        |  "thursday": [
        |    {
        |      "type": "close",
        |      "value": 21600
        |    }
        |  ],
        |  "friday": [
        |    {
        |      "type": "open",
        |      "value": 36000
        |    }
        |  ],
        |  "saturday": [
        |    {
        |      "type": "close",
        |      "value": 0
        |    }
        |  ],
        |  "sunday": [
        |  ]
        |}
      """.stripMargin

    val result = EventController.computeRestaurantHours(input)
    assertResult(Array(("monday", "10 AM - 6 AM"), ("tuesday", "Closed"), ("wednesday", "12 AM - 6 AM"), ("thursday", "Closed"), ("friday", "10 AM - 12 AM"), ("saturday", "Closed"), ("sunday", "Closed")))(result)
  }

}
