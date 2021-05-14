package simulations




import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.jsonwebtoken.{Jwts, SignatureAlgorithm}

import java.time.Instant
import java.util
import java.util.{Base64, Date}
//import io.gatling.http.protocol.HttpProtocolBuilder  //
//import io.gatling.core.structure.ChainBuilder  //
import io.gatling.http.Predef._   //// required for Gatling HTTP DSL
import scala.concurrent.duration._  // used for specifying duration unit, eg "5 second"
//import io.gatling.app.





class test1 extends  Simulation {

 // object jwtfunction {

    val Secret: String = "jwtkey"

    val secretkeyBytes: Array[Byte] = Secret.getBytes()
    val memberID: String = "1211228" //"doterraId")
    val password: String = "12341234" //pwd
    val secretKey: String = Base64.getEncoder().encodeToString(secretkeyBytes)
    val nowMillis:Long = System.currentTimeMillis()
    val now:Date = new Date(nowMillis)

    System.out.println("*******111 " + now)
    var jwtToken: String = Jwts.builder()
      .setSubject("1211228")
      .setIssuedAt(Date.from(Instant.now()))

      .claim("password", "12341234")
      //.setId(UUID.randomUUID.toString)

      .signWith(SignatureAlgorithm.HS256, Secret)
      .compact()



    var token2: String = jwtToken
    var token3:String = "Bearer " + token2
    println(Secret)
    println(now)

    println(secretkeyBytes)
    println(memberID)
    println(token2)
    println(password)
    println(token3)

//
//    val jwtLogin = exec(http(requestName = "login")
//
//      .get("/US/en/login/hybris-authenticate")
//      .headers(login_header)
//      //.headers(sessionHeaders)
//      .header("Authorization", "Bearer {token2}")
//      .check(status.is(200)))

    println(" AFTER JWT build request call : " + token2)

 // }






  val httpConf = http.baseUrl(url = "https://prf.doterra.com/")
    .inferHtmlResources(BlackList(""".*\.js""",
      """.*\.css""",
      """.*\.gif""",
      """.*\.jpeg""",
      """.*\.jpg""",
      """.*\.ico"""), WhiteList()
    )
    .header(name = "User_Agent", value = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36  Chrome/88.0.4324.190 Safari/537.36")
    .header(name = "content-type", value = "application/json; text/plain; charset=UTF-8")
    .header(name = "Accept", value = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
    .header(name = "Accept-Language", value = "en-US,en;q=0.9")
    .header(name = "Keep-Alive", value = "115")
    .header(name = "Accept-Encoding", value = "gzip, deflate, br")
    .header(name = "Accept-Charset", value = "utf-8;q=0.7,*;q=0.7")



    .silentResources
  //.silentUri()


  //===================================================================

  //Header Definitions:

  val home_page_header = Map(
    "User_Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36  Chrome/88.0.4324.190 Safari/537.36",
    "content-type" -> "application/json; text/plain; charset=UTF-8",
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
    "Accept-Language" -> "en-US,en;q=0.9",
    "Keep-Alive" -> "115",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Accept-Charset" -> "utf-8;q=0.7,*;q=0.7"
  )


  val csvUserFeeder = csv(fileName ="data/user_data.csv").queue //default all the  data's are store in  memory


  val login_header = Map("Accept" -> "application/json, text/plain, */*")


  val sessionHeaders = Map("Authorization" -> s"Bearer ${token2}" )

  //==================================================================

  val scn = scenario(scenarioName = "oneTimeOrder")
    //clear cache & cookies
    .exec(flushHttpCache) //   will clear cache
    .exec(flushSessionCookies)
    .exec(flushCookieJar)

    // Home Page 1st http call
    // .repeat(times = 2)

    .exec(http(requestName = "Home Page")
      .get("/US/en")
      //.silent
      //.notSilent
      .headers(home_page_header)
      .check(regex("""<input type="hidden" name="CSRFToken" value="(.+?)" />""").saveAs("csrftoken"))
      .check(status.in(expected = 200, 201, 202, 304)))

    .exec({
      session =>
        println("++++++++++++++++++++++++++++++++++++++++++++++++++")
        println(session("csrftoken").as[String])
          println(token3)
        println("++++++++++++++++++++++++++++++++++++++++++++++++++")
        session
    })
    //Step2
    .exec(http(requestName = "login")
          .get("/US/en/login/hybris-authenticate")
          .headers(login_header)


      //.signWithOAuth1("Authorization" -> "Bearer $token2")

          .headers(sessionHeaders)

          //.header("Authorization",  "${token3}" )
          //.check(currentLocationRegex("Location").ofType[(String)])
          .check(status.is(200)))
    .pause(duration = 60)





  // .exec(jwtfunction.jwtLogin)


















  //
  //  val token3 = "test *******"
  //  val b = "Bearer "
  //  val token1 = b + "${jwtToken}"
  //  val test1 = Map("Authorization" -> "${token1}" )

  // scenario("Login")


  //    val test1= http(requestName = "Login").get("/US/en/login/hybris-authenticate")
  //      .headers(login_header)
  //      //.headers(sessionHeaders)
  //      .header("Authorization", "Bearer ${token2}")
  //
  //
  //
  ////      .formParamSeq(
  ////      Seq(
  ////        ("username", memberID), ("password", password)
  ////      ))
  //      .check(status.is(200))
  //
  //      exec(test1)







  //========================================================================================

  //-------------------------SETUP ----------------------------------------------------------------------------------------------------------------
  //-------------------------SETUP ----------------------------------------------------------------------------------------------------------------
  // Simulation Definition :


  setUp(
    scn.inject(atOnceUsers(1))
       ).protocols(httpConf)

}






