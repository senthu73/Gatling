package doTerraSimulations.oneTimePackage

import io.gatling.core.Predef.{exec, pause}
import io.gatling.http.Predef.{http, status}
import io.jsonwebtoken.{Jwts, SignatureAlgorithm}

import java.util.{Base64, Date}
import scala.io.Source

object CreateJWTtokenRequest {





  def loginToken(memberID: String, password:String):String = {
              println("test member id "+ memberID)
              val Secret: String = "jwtkey"
              val secretkeyBytes: Array[Byte] = Secret.getBytes()
              //            val memberID: String =  "1211228" //"doterraId")
//              val password: String = "12341234" //pwd
              val secretKey: String = Base64.getEncoder().encodeToString(secretkeyBytes)
              val nowMillis: Long = System.currentTimeMillis()
              val now: Date = new Date(nowMillis)

              System.out.println("..selvi............................")
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

              return(token2)
            }








  }













