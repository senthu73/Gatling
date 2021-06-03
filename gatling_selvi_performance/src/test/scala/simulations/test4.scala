package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef.{BlackList, WhiteList, regex, scenario}
import io.gatling.http.Predef.{flushCookieJar, flushHttpCache, flushSessionCookies, header, headerRegex, http, status}
import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.core.feeder.FeederBuilder
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



  val token2 = System.getProperty("token2")

  val httpConf = http.baseUrl(url = "https://prf.doterra.com/")
    .inferHtmlResources(BlackList(), WhiteList())
    .header(name = "User_Agent", value = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36  Chrome/88.0.4324.190 Safari/537.36")
    .header(name = "content-type", value = "application/json; text/plain; charset=UTF-8")
    .header(name = "Accept", value = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
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


  val sessionHeaders = Map("Authorization" -> s"Bearer ${token2}")
  //val sessionHeaders = Map("Authorization Bearer " -> token2)

  val ses_header = Map("Cookie" -> "${jsessionB2B}")
//
//



  //1. find no of roow in csv
  //2. use row sequentilally jwt token
  //3. assign the jwt tkken value to global var



  def logindef() = {

          val testcsv = "data/user_data.csv"
          var memberID = System.getProperty("memberID")
          val count = Source.fromResource(testcsv).getLines().size



          println( "lines "+ count)



          for (line <- (Source.fromResource(testcsv).getLines())) {
            val cols = line.split(",").map(_.trim)
            println(s"${cols(0)}")
            memberID = s"${cols(0)}"


            //          }

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
          }

            println("Starting here ........... ")
    println(memberID)
            pause(10)


            exec(http(requestName = "login")
              .get("/US/en/login/hybris-authenticate")
              .headers(login_header)
              //.signWithOAuth1("Authorization" -> "Bearer $token2")
              .headers(sessionHeaders)
              //.header("Authorization",  "${token3}" )
              //.check(currentLocationRegex("Location").ofType[(String)])
              .check(status.in(expected = 200, 201, 202, 304)))
              .pause(duration = 10)




  }



  val scn = scenario(scenarioName = "oneTimeOrder")

    //clear cache & cookies
    .exec(flushHttpCache) //   will clear cache
    .exec(flushSessionCookies)
    .exec(flushCookieJar)


    .exec(http(requestName = "Home Page")
      .get("/US/en")
      //.silent
      //.notSilent


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
        println("-------------")
        println("++++++++++++++++++++++++++++++++++++++++++++++++++")
        session
    })
    .pause(10)
    .exec(logindef()


    )
    println(("${memberID} "))






//





  setUp(
    scn.inject(atOnceUsers(4))
  ).protocols(httpConf.inferHtmlResources())
  //    .maxDuration(120.seconds)
  //    .assertions(
  //      global.responseTime.max.lt(30000),
  //      global.successfulRequests.percent.gt(95)
  //    )




} //main
