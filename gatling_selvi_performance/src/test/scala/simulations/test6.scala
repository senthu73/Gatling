package simulations

import io.gatling.core.Predef.{BlackList, WhiteList, csv}
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef.http



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


class test6 extends Simulation{


  val userFeeder1 = csv("./src/test/resources/data/user_data.csv")

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

  val scn = scenario("Create, Boot From Volume, Delete Server")





}
