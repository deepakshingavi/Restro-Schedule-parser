# Restro-schedule-Parser
Accept JSON as input in form of file
Validates it
Gives out the Restaurant schedule in Human readable form 

### Pre-Requisites
* Scala 2.13
* JDK 1.8
* SBT 1.5.2

### Build and run
`sbt "runMain com.dp.training.JsonFileParser src/test/resources/big.json"` 

This program should 
1. Load the JSON file 
2. Validate it
   1. Empty JSON or JSON without any schedule
   2. JSON validity
   3. if status is open/close 
   4. value field ranges from {0 to 86400}
   5. if the number of open events are equal to number of closed events 
3. 


### Application Input 
I have added few smaple JSON at `src/test/resources/`

### Assumption:
1. Any open's close events will always arrive at maximum on consecutive day schedule.
2. All days scheduled need to be displayed regardless of the event for that day.

### Suggestions
1. Event schema evolution can be protected by using google-protobufs or Avro schema registry
2. It better to have schema as flat as possible 
      a. `{ "friday" : [{"type" : "open","value" : 64800},{"type" : "close","value" : 68400}] }`
      b. `[{"type" : "open","value" : 64800, "dayOfWeek" : 5 },{"type" : "close","value" : 68400, "dayOfWeek" : 5} ]`
      c. It's always easier to process, query and evolve flatten schema than a nested one. 




