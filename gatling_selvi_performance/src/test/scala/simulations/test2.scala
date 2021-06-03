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
import java.io._

import io.gatling.http.HeaderNames
import io.gatling.http.HeaderValues

class test2 extends  Simulation {

  // object jwtfunction {
   //default all the  data's are store in  memory
  val productFeeder = csv("./src/test/resources/data/test_configuration/product_data.csv").circular


  //  val UserFeeder1 = csv(fileName ="data/user_data.csv").batch(bufferSize = 200)
  val jsessionB2B: String =" "
  val Secret: String = "jwtkey"

  val secretkeyBytes: Array[Byte] = Secret.getBytes()
  val memberID: String = "1211228" //"doterraId")
  val password: String = "12341234" //pwd
  val secretKey: String = Base64.getEncoder().encodeToString(secretkeyBytes)
  val nowMillis:Long = System.currentTimeMillis()
  val now:Date = new Date(nowMillis)

  System.out.println("..............................")
  System.out.println(secretKey.toString)
  System.out.println("*******111 " + now)
  //Let's set the JWT Claims
  var jwtToken: String = Jwts.builder()

    .setSubject(memberID)
    .setIssuedAt(now)
    //.setIssuedAt(Date.from(Instant.now))
    //.setIssuedAt(Date.from(Instant.ofEpochSecond(nowMillis)))
    //.setExpiration(new Date(now.getTime + nowMillis))

    .claim("password", password)
    //.setId(UUID.randomUUID.toString)

    .signWith(SignatureAlgorithm.HS256, Secret.getBytes("UTF-8"))

    //Builds the JWT and serializes it to a compact, URL-safe string
    .compact()
  var token2: String = jwtToken
  var token3:String = "Bearer " + token2
  System.out.println(Secret)
  System.out.println(now)

  System.out.println(secretkeyBytes)
  println("USER ID   :"  + memberID)
  System.out.println(token2)
  System.out.println(password)
  System.out.println(token3)

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
    .inferHtmlResources(BlackList(), WhiteList())
    //    .inferHtmlResources(BlackList(""".*\.js""",
    //      """.*\.css""",
    //      """.*\.gif""",
    //      """.*\.jpeg""",
    //      """.*\.jpg""",
    //      """.*\.ico"""), WhiteList()
    //    )
    .header(name = "User_Agent", value = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36  Chrome/88.0.4324.190 Safari/537.36")
    .header(name = "content-type", value = "application/json; text/plain; charset=UTF-8")
    .header(name = "Accept", value = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
    .header(name = "Accept-Language", value = "en-US,en;q=0.9")
    .header(name = "Keep-Alive", value = "115")
    .header(name = "Accept-Encoding", value = "gzip, deflate, br")
    .header(name = "Accept-Charset", value = "utf-8;q=0.7,*;q=0.7")
    .acceptCharsetHeader("UTF-8")
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





  val login_header = Map("Accept" -> "application/json, text/plain, '/'")


  val sessionHeaders = Map("Authorization" -> s"Bearer ${token2}" )
  //val sessionHeaders = Map("Authorization Bearer " -> token2)

  val ses_header = Map("Cookie" -> "${jsessionB2B}")




  val productHeaders = Map("content-type" -> "application/json; text/plain; charset=UTF-8",
    "Accept" -> "*/*",
    "X-Requested-With" -> "XMLHttpRequest",
    "ADRUM" -> "isAjax:true"   )


  val headers_98 = Map(
    "accept" -> "*/*",
    "accept-encoding" -> "gzip, deflate, br",
    "accept-language" -> "en-US,en;q=0.9",
    "adrum" -> "isAjax:true",
    "content-type" -> "application/x-www-form-urlencoded; charset=UTF-8",
    "dnt" -> "1",
    "origin" -> "https://prf.doterra.com",
    "sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="90", "Google Chrome";v="90""",
    "sec-ch-ua-mobile" -> "?0",
    "sec-fetch-dest" -> "empty",
    "sec-fetch-mode" -> "cors",
    "sec-fetch-site" -> "same-origin",
    "x-requested-with" -> "XMLHttpRequest")


  val orderHeader= Map("content-type" -> "application/json",
    "X-Requested-With" -> "XMLHttpRequest",
    "ADRUM" -> "isAjax:true"   )








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
      .check(header("Set-Cookie") .saveAs("jsessionid"))
      .check(headerRegex("Set-Cookie", """JSESSIONID-B2BACC=(.+\.)""" ).saveAs("cookie1"))
      .check(headerRegex("Set-Cookie", """JSESSIONID=(.+\.)""" ).saveAs("cookie2"))

      .check(status.in(expected = 200, 201, 202, 304)))

    .exec({
      session =>
        println("++++++++-----++++++++++++++++++++++++++++++++++++++++++")
        println(session("csrftoken").as[String])
        println(token3)
        //println(session("jsessionid").as[String])
        println(session("cookie1").as[String])
        val str: String = session("cookie1").as[String]
        val str1:String =session("cookie2").as[String]

        println(str.substring(0,str.length() -1))
        val jsessionB2B:String = str.substring(0,str.length() -1)   // "jsession1" is the value from JSESSIONID-B2BACC
        val jsessionID:String  = str1.substring(0,str.length() -1)

        println( "jsessionID   :" + jsessionID)
        println(  "jsessionB2B  :" + jsessionB2B)
        println("-------------")
        println(session(jsessionB2B).as[String])
        println("++++++++++++++++++++++++++++++++++++++++++++++++++")
        session
    })
    .pause(10)


    //Step2


    .exec(http(requestName = "login")
      .get("/US/en/login/hybris-authenticate")
      .headers(login_header)
      //.signWithOAuth1("Authorization" -> "Bearer $token2")
      .headers(sessionHeaders)
      //.header("Authorization",  "${token3}" )
      //.check(currentLocationRegex("Location").ofType[(String)])
      .check(status.in(expected = 200, 201, 202, 304)))
    .pause(duration = 10)


    .exec(http(requestName = "GET CART ONE TIME ORDER")
      .get("/US/en/cart/oneTimeOrder")
      .check(regex("""<input type="hidden" name="CSRFToken" value="(.+?)" />""").saveAs("csrftoken"))
      .check(status.in(expected = 200, 201, 202, 304)))

    //Step4
    .exec(http(requestName = "GET isRetailOrAnonymous")
      .get("/US/en/isRetailOrAnonymous")
      .check(bodyString.saveAs("isRetailOrAnonymous"))
      .check(status.in(expected = 200, 201, 202, 304)))

    .exec({
      session =>
        println("+++++++++++++++  isRetailOrAnonymous  ++++++++++++++++++++++")
        println(session("isRetailOrAnonymous").as[String])
        println("++++++++++++++++++++++++++++++++++++++++++++++++++")
        session
    })


    //Step5
    .exec(http(requestName = "GET CheckCartContext")
      .get("/US/en/checkCartContext")
      .headers(home_page_header)
      //      .check(regex("""<input type="hidden" name="CSRFToken" value="(.+?)" />""").saveAs("csrftoken"))
      .check(status.in(expected = 200, 201, 202, 304)))

    .exec(http(requestName = "GET /my-cart")
      .get("/US/en/my-cart")
      .headers(home_page_header)
      //      .check(regex("""ACC.config.CSRFToken = "(.+?)" """).saveAs("csrftoken"))
      .check(bodyString.saveAs("Auth_Response"))
      .check(status.in(expected = 200, 201, 202, 304)))

    //Step5

    .exec(http(requestName = "QuickSearch")
      .get("US/en/pl/single-oils")
      .check(status.in(expected = 200, 201, 202, 304)))
    .pause(10)
    //  https://prf.doterra.com/US/en/cart/addproduct?qty=1&productCodePost=31020001
    //    .post("/US/en/cart/addproduct?CSRFToken=${csrftoken}")


    .feed(productFeeder, 1)

    .exec(http(requestName = "Add Products  ${sku}")
      .post("/US/en/cart/addproduct")
      .headers(headers_98)
      .queryParam("CSRFToken","${csrftoken}")
//      .queryParam("qty", "1")
//      .queryParam("productCodePost", "30010001")

      .queryParam("qty", "${qty}")
      .queryParam("productCodePost", "${sku}")

      .check(bodyString.saveAs("Auth_Response1"))
      .check(status.in(expected = 200, 201, 202, 304)))


    //      .body(StringBody("""qty=1&productCodePost=49360001""")).asMultipartForm
    //      .body(StringBody("""qty=1&productCodePost=49360001""")).asJson
    //      .body(StringBody("""qty=1""")).asJson
    //      .body(StringBody("""productCodePost=49360001""")).asJson


    //      .body(rawFileBodies("qty=1&productCodePost=49360001")

    //      .formParam("qty", "1")
    //      .formParam("productCodePost", "30120001")





    .exec({
      session =>
        println("+++++++++++++++ Auth_Response Product +++++++++++++++++++")
        println(session("Auth_Response").as[String])
        println("++++++++++++++++++++++++++++++++++++++++++++++++++")
        session
    })

    //    .exec(session => {
    //      val response = session("BODY").as[String]
    //      println(" ---- Response body--- : $response")
    //      session})



    .pause(4)











    //
    //
    //    //------- Add products
    //    .exec(http(requestName = "POST Add Products")
    //      //        .post("US/en/cart/addproduct")
    //      //        .queryParam("CSRFToken", "${csrftoken}")  //CSRFHandlerInterceptor required MUST a query parameters
    //      //        .queryParam("qty", "1")
    //      //        .queryParam("productCodePost", "30010001")
    //      //        .headers(login_header)
    //      //        .headers(ses_header)
    //      .post("/US/en/cart/addproduct?qty=1&productCodePost=30120001&CSRFToken=${csrftoken}")
    ////    .headers(productHeaders)
    //    .headers(headers_98)
    //    //        .post("/US/en/cart/addproduct?CSRFToken=${csrftoken}")
    //    .check(bodyString.saveAs("Auth_Response1"))
    //    .check(status.is(200))
    //  )
    //
    //    .exec({
    //      session =>
    //        println("+++++++++++++++ Auth_Response Product +++++++++++++++++++")
    //        println(session("Auth_Response").as[String])
    //        println("++++++++++++++++++++++++++++++++++++++++++++++++++")
    //        session
    //    })
    //
    //

    //                        .exec(http(requestName = "updateConsumedPoints")
    //                          .post("/cart/updateConsumedPoints")
    //                          .formParam("CSRFToken","${csrftoken}")
    //                          .formParam("entryNumber", "0")
    //                          .formParam("consumedPoints", "1")
    //                          .check(status.is(200)))
    //                        .pause(10)
    //    .exec(http(requestName = "saveOnUpgradeFoRetail")
    //      .get("/cart/saveOnUpgradeFoRetail")
    //      .check(status.is(200)))



    .exec(http(requestName = "saveTextNotification")
      .get("/cart/saveTextNotification")
      .queryParam("CSRFToken","${csrftoken}")
      .queryParam("sendTextNotifications", "true")
      .check(regex("""<input type="hidden" name="CSRFToken" value="(.+?)" />""").saveAs("csrftoken"))
      .check(status.in(expected = 200, 201, 202, 304)))



    .exec(http(requestName = "deliveryMode")
      .post("/cart/getSRP")
      .queryParam("CSRFToken","${csrftoken}")
      .queryParam("deliveryModeCode", "1_Next%20Day")

      .check(status.in(expected = 200, 201, 202, 304)))


    .exec(http(requestName = "/checkout/order-review")
      .get("/checkout/order-review")
      .check(bodyString.saveAs("orderreview"))
      .check(regex("""<input type="hidden" name="CSRFToken" value="(.+?)" />""").saveAs("csrftoken"))
      .check(status.in(expected = 200, 201, 202, 304)))

    .exec({
      session =>
        println("+++++++++++++++ Order Review  +++++++++++++++++++")
        println(session("csrftoken").as[String])
        println("++++++++++++++++++++++++++++++++++++++++++++++++++")
        session
    })



    .exec(http(requestName = "checkout/confirmOrderNow")
      .post("checkout/confirmOrderNow")
      .headers(orderHeader)
      .queryParam("CSRFToken", "${csrftoken}")

      .check(regex("""orderCode=(\b\d{9}\b)""").saveAs("order_number"))
      .check(status.in(expected = 200, 201, 202, 304)))

    .exec({
      session =>
        println("+++++++++++++++ Order Number OneTime  +++++++++++++++++++")
        println(session("order_number").as[String])
        println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
        session
    })













  //========================================================================================

  //-------------------------SETUP ----------------------------------------------------------------------------------------------------------------
  //-------------------------SETUP ----------------------------------------------------------------------------------------------------------------
  // Simulation Definition :


  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf.inferHtmlResources())
  //    .maxDuration(120.seconds)
  //    .assertions(
  //      global.responseTime.max.lt(30000),
  //      global.successfulRequests.percent.gt(95)
  //    )





}
