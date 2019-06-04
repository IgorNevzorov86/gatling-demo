
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

    val httpProtocol = http
      .baseUrl("https://www.google.com")
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
      .doNotTrackHeader("1")
      .acceptLanguageHeader("en-US,en;q=0.5")
      .acceptEncodingHeader("gzip, deflate")
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

    val feeder = csv("src/test/resources/searchTerms.csv").random

    object Search {
      val search = exec(http("Open Google search page")
        .get("/"))
        .pause(3)
        .feed(feeder)
        .exec(http("Enter search term")
          .get("/search?q=${searchTerm}")
          .check(status.is(200)))
        .pause(3)
    }

    val scn = scenario("Check Google Search").exec(Search.search)

    setUp(
      scn.inject(rampUsers(5) during (10 seconds))
    ).protocols(httpProtocol)
  }

