package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef.{BlackList, WhiteList, regex, scenario}
import io.gatling.http.Predef.{flushCookieJar, flushHttpCache, flushSessionCookies, header, headerRegex, http, status}
import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.core.feeder.FeederBuilder
import io.gatling.core.session.{Expression, Session}
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
import scala.util.parsing.combinator._
import scala.io.Source

import doTerraSimulations.oneTimePackage._


class test4 extends  Simulation {

        //  var clientId: String = System.getProperty("CLIENT_ID")
        //  var clientPwd: String = System.getProperty("CLIENT_PWD")
        //  var clientSecret: String =  System.getProperty("CLIENT_SECRET")
        //  var clientSecret1: String =  System.getProperty("CLIENT_SECRET")
        //  private var testToken:String = System.getProperty("TESTTOKEN")
        //  @volatile var clientSecret =""


        val userFeeder = csv("./src/test/resources/data/user_data.csv").circular

        val productFeeder = csv("./src/test/resources/data/test_configuration/product_data.csv").random

        val url = csv("./src/test/resources/data/test_configuration/initializationBaseURLSetup.csv")




        val httpConf = http.baseUrl(url = "https://prf.doterra.com/")
          .inferHtmlResources(BlackList(), WhiteList())
          .header(name = "User_Agent", value = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36  " +
            "                                     Chrome/88.0.4324.190 Safari/537.36")
          .header(name = "content-type", value = "application/json; text/plain; charset=UTF-8")
          .header(name = "Accept", value = "text/html,application/xhtml+xml,application/xml;q=0.9," +
                                              "image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
          .header(name = "Accept-Language", value = "en-US,en;q=0.9")
          .header(name = "Keep-Alive", value = "115")
          .header(name = "Accept-Encoding", value = "gzip, deflate, br")
          .header(name = "Accept-Charset", value = "utf-8;q=0.7,*;q=0.7")
          .acceptCharsetHeader("UTF-8")


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

        val ses_header = Map("Cookie" -> "${jsessionB2B}")

        val productHeaders = Map("content-type" -> "application/json; text/plain; charset=UTF-8",
          "Accept" -> "*/*",
          "X-Requested-With" -> "XMLHttpRequest",
          "ADRUM" -> "isAjax:true")


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


        val orderHeader = Map("content-type" -> "application/json",
          "X-Requested-With" -> "XMLHttpRequest",
          "ADRUM" -> "isAjax:true")


        def homePage(): ChainBuilder = {
          exec(http(requestName = "HOME PAGE")
            .get("/US/en")
            .headers(home_page_header)
            .check(regex("""<input type="hidden" name="CSRFToken" value="(.+?)" />""").saveAs("csrftoken"))
            .check(header("Set-Cookie").saveAs("jsessionid"))
            .check(headerRegex("Set-Cookie", """JSESSIONID-B2BACC=(.+\.)""").saveAs("cookie1"))
            .check(headerRegex("Set-Cookie", """JSESSIONID=(.+\.)""").saveAs("cookie2"))
            .check(status.in(expected = 200, 201, 202, 304)))

            .exec({
              session =>
                println("++++++++-----++++++++++++++++++++++++++++++++++++++++++")
                println(session("csrftoken").as[String])
                //println(session("jsessionid").as[String])
                println(session("cookie1").as[String])
                val str: String = session("cookie1").as[String]
                val str1: String = session("cookie2").as[String]

                println(str.substring(0, str.length() - 1))
                val jsessionB2B: String = str.substring(0, str.length() - 1) // "jsession1" is the value from JSESSIONID-B2BACC
                val jsessionID: String = str1.substring(0, str.length() - 1)
                println("jsessionID   :" + jsessionID)
                println("jsessionB2B  :" + jsessionB2B)
                //                println("-------------")
                ////                println("jsessionB2B").as[String])
                //                println("++++++++++++++++++++++++++++++++++++++++++++++++++")
                session
            })
        }


        def loginToken1(memberID: String, password: String): String = {

          val Secret: String = "jwtkey"
          val secretkeyBytes: Array[Byte] = Secret.getBytes()
          //            val memberID: String =  "1211228" //"doterraId")
          val password: String = "12341234" //pwd
          val secretKey: String = Base64.getEncoder().encodeToString(secretkeyBytes)
          val nowMillis: Long = System.currentTimeMillis()
          val now: Date = new Date(nowMillis)

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
          var token3: String = "Bearer " + token2
          System.out.println(Secret)
          System.out.println(now)

          System.out.println(secretkeyBytes)
          println("USER ID   :" + memberID)
          System.out.println(token2)
          System.out.println(password)
          System.out.println(token3)

          println(" AFTER JWT build request call : " + token2)

          println("Starting here ........... ")
          println(memberID)

          return (token2)
        }


        //  def loginWithJWT:Expression[Session] = session => {
        //
        ////    http(requestName = "login")
        ////      .get("/US/en/login/hybris-authenticate")
        ////      .headers(login_header)
        ////      .header("Authorization", "Bearer ${testToken}")
        //////      .header("Authorization", s"Bearer ${testA}")
        //////      .header("Authorization", "Bearer  ${csrftoken}")
        ////      .check(status.in(expected = 200, 201, 202, 304))
        //  session
        //  }


        def createJWTToken: Expression[Session] = session => {

          //    val  testToken: String =
          session.set("testToken", CreateJWTtokenRequest.loginToken(session("username").as[String],
            session("pwd").as[String]))

          //      //      val clientId:String = selvisession("username").as[String]
          //      //      val clientPwd:String = selvisession("password").as[String]
          //      //
          //      //      println( "USER:     " + clientId)
          //      //      println( "PASSWORD: " + clientPwd )
          //      //      clientSecret = session(CreateJWTtokenRequest.loginToken(clientId, clientPwd)).as[String]
          //      val clientSecret =  CreateJWTtokenRequest.loginToken(selvisession("username").as[String], selvisession("password").as[String])
          //      println("*Authorization Bearer 1" + clientSecret) //working
          //      selvisession.set("testToken", clientSecret)

          //    })


        }


        def addProducts() = {
          repeat(5) {
            exec(http(requestName = "Add Products  ${sku}")
              .post("/US/en/cart/addproduct")
              .headers(headers_98)
              .queryParam("CSRFToken", "${csrftoken}")
              .queryParam("qty", "${qty}")
              .queryParam("productCodePost", "${sku}")
              .check(bodyString.saveAs("Auth_Response1"))
              .check(status.in(expected = 200, 201, 202, 304)))
          }

        }


  //================   LRP PROCESS NOW test plan started =========================================

  val scn = scenario(scenarioName = "LRO PROCESS NOW ORDER SCENARIO")
    //clear cache & cookies
    .exec(flushHttpCache) //   will clear cache
    .exec(flushSessionCookies)
    .exec(flushCookieJar)
    //Step1

    .exec(homePage())

    //Step2
    .feed(userFeeder)

    .exec(createJWTToken)

    .pause(10)

    //    .exec(loginWithJWT)
    .exec {
      http(requestName = "login")
        .get("/US/en/login/hybris-authenticate")
        .headers(login_header)
        .header("Authorization", "Bearer ${testToken}")
        .check(regex("""en\/shop-home\/edit-LrpTemplate\?code=(\d+)""").saveAs("lrpTempID"))
        .check(status.in(expected = 200, 201, 202, 304))
    } .pause(duration = 10)

    .exec({
      session =>
        println("+++++++++++++++ LRP TEMP ID +++++++++++++++++++")
        println(session("lrpTempID").as[String])
        println("++++++++++++++++++++++++++++++++++++++++++++++++++")
        session
    })


    //Step 3

    .exec(http(requestName = "GET isRetailOrAnonymous")
      .get("/US/en/isRetailOrAnonymous")
      .check(bodyString.saveAs("isRetailOrAnonymous"))
      .check(status.is(200)))

    .exec({
      session =>
        println("+++++++++++++++  isRetailOrAnonymous  ++++++++++++++++++++++")
        println(session("isRetailOrAnonymous").as[String])
        println("++++++++++++++++++++++++++++++++++++++++++++++++++")
        session
    })


    //Step 4
    .exec(http(requestName = "GET CheckCartContext")
      .get("/US/en/checkCartContext")
      .headers(home_page_header)
      //      .check(regex("""<input type="hidden" name="CSRFToken" value="(.+?)" />""").saveAs("csrftoken"))
      .check(status.in(expected = 200, 201, 202, 304)))

    //Step 5


    .exec(http(requestName = "GET Edit-LrpTemplate")
      .get("/US/en/shop-home/edit-LrpTemplate")
      .headers(home_page_header)
      .queryParam("code","${lrpTempID}")
      .check(regex("""<input type="hidden" name="CSRFToken" value="(.+?)" />""").saveAs("csrftoken"))

      //      .check(regex("""<input type="hidden" name="CSRFToken" value="(.+?)" />""").saveAs("csrftoken"))
      .check(status.is(200)))


    //Step 6

    .exec(http(requestName = "GET isRetailOrAnonymous After edit LRP")
      .get("/US/en/isRetailOrAnonymous")
      .check(bodyString.saveAs("isRetailOrAnonymous"))
      .check(status.is(200)))

    .exec({
      session =>
        println("+++++++++++++++  isRetailOrAnonymous After edit LRP  ++++++++++++++++++++++")
        println(session("isRetailOrAnonymous").as[String])
        println("++++++++++++++++++++++++++++++++++++++++++++++++++")
        session
    })

    //Step 7
    .exec(http(requestName = "GET CheckCartContext")
      .get("/US/en/checkCartContext")
      .headers(home_page_header)
      //      .check(regex("""<input type="hidden" name="CSRFToken" value="(.+?)" />""").saveAs("csrftoken"))
      .check(status.in(expected = 200, 201, 202, 304)))


    //Step 12

    .exec(http(requestName = "POST LRP temp Add-Products")
      .post("/US/en/cart/add-product")
      .headers(headers_98)
      .queryParam("CSRFToken","${csrftoken}")
      .queryParam("quantity", "1")
      .queryParam("entryNumber", "-1")
      .queryParam("productCode", "30010001")

      .check(status.in(expected = 200, 201, 202, 304)))


    //Step 13

    .exec(http(requestName = "GET isRetailOrAnonymous After edit LRP")
      .get("/US/en/isRetailOrAnonymous")
      .check(bodyString.saveAs("isRetailOrAnonymous"))
      .check(status.in(expected = 200, 201, 202, 304)))

    .exec({
      session =>
        println("+++++++++++++++  isRetailOrAnonymous After edit LRP  ++++++++++++++++++++++")
        println(session("isRetailOrAnonymous").as[String])
        println("++++++++++++++++++++++++++++++++++++++++++++++++++")
        session
    })


    .exec(http(requestName = "GET saveOnUpgradeFoRetail")
      .get("/US/en/cart/saveOnUpgradeFoRetail")

      .check(status.in(expected = 200, 201, 202, 304)))


    //Step 14

    .exec(http(requestName = "GET cart/saveTextNotification")
      .get("/US/en/cart/saveTextNotification")
      .queryParam("CSRFToken","${csrftoken}")
      .queryParam("sendTextNotifications", "true")
      .check(status.in(expected = 200, 201, 202, 304)))

    //Step 15

    .exec(http(requestName = "POST /lrptemplate/getLrpStatus")
      .post("/US/en/lrptemplate/getLrpStatus")
      .queryParam("CSRFToken","${csrftoken}")
      .check(status.in(expected = 200, 201, 202, 304)))

    //Step 16

    .exec(http(requestName = "POST lrptemplate/validateCutOfTime")
      .post("/US/en/lrptemplate/validateCutOfTime")
      .queryParam("CSRFToken","${csrftoken}")
      .check(status.in(expected = 200, 201, 202, 304)))

    //Step 17

    .exec(http(requestName = "POST lrptemplate/process")
      .post("/US/en/lrptemplate/process")
      .queryParam("CSRFToken","${csrftoken}")
      .queryParam("processNow","true")
      .check(status.in(expected = 200, 201, 202, 304)))

    //Step 18

    .exec(http(requestName = "POST /cart/checkout")
      .post("/US/en/cart/checkout")
      .queryParam("CSRFToken","${csrftoken}")
      .check(status.in(expected = 200, 201, 202, 304)))

    //Step 19

    .exec(http(requestName = "POST checkout/confirmOrderNow LRP")
      .post("/US/en/checkout/confirmOrderNow")
      .headers(orderHeader)
      .queryParam("CSRFToken", "${csrftoken}")
      .check(regex("""orderCode=(\b\d{9}\b)""").saveAs("order_number"))
      .check(status.in(expected = 200, 201, 202, 304)))




    .exec({
      session =>
        println("++++++++++++  Order Number LRP  +++++++++++++++++++")
        println(session("order_number").as[String])
        println("++++++++++++++++++++++++++++++++++++++++++++++++++")
        session
    })
















  //========================================================================================

  //-------------------------SETUP ----------------------------------------------------------------------------------------------------------------
  //-------------------------SETUP ----------------------------------------------------------------------------------------------------------------
  // Simulation Definition :













  setUp(
    scn.inject(atOnceUsers(4))
  ).protocols(httpConf.inferHtmlResources())
  //    .maxDuration(120.seconds)
  //    .assertions(
  //      global.responseTime.max.lt(30000),
  //      global.successfulRequests.percent.gt(95)
  //    )




} //main
