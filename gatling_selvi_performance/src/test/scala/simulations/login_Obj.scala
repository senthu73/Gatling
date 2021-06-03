package simulations

import io.jsonwebtoken.{Jwts, SignatureAlgorithm}

import java.util.{Base64, Date}
import scala.io.Source
import scala.reflect.internal.util.NoPosition.line

object Login {

  val testcsv = "data/user_data.csv"
  val count = Source.fromResource(testcsv).getLines().size

  println( "lines "+ count)
  for (line <- (Source.fromResource(testcsv).getLines())) {
    val cols = line.split(",").map(_.trim)
    println(s"${cols(0)}")
    var memberID: String = s"${cols(0)}"
    println(memberID)
    //          }






    val jsessionB2B: String = " "
  val Secret: String = "jwtkey"

  val secretkeyBytes: Array[Byte] = Secret.getBytes()
//  val memberID: String = "1211228" //"doterraId")
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


} }
