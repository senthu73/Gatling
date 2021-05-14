package simulations
//
//
//import io.gatling.core.Predef.{configuration, csv}
//import io.jsonwebtoken.{Jwts, SignatureAlgorithm}
//import com.github._
//
//import java.time.Instant
//import java.util.{Base64, Date}
//
//
//
//object login_Obj {
//  //def login(args: Array[String]): Unit = {
//
//  val csvUserFeeder = csv(fileName ="data/user_data.csv").queue //default all the  data's are store in  memory
//
//  val Ttl: Int = 3600
//
//  val Secret: String = "jwtkey"
//
//  val secretkeyBytes = Secret.getBytes()
//  val memberID: String = "1211228" //"doterraId")
//  val password: String = "12341234" //pwd
//  val secretKey: String = Base64.getEncoder().encodeToString(secretkeyBytes)
//  val nowMillis = System.currentTimeMillis()
//  val now: Date = new Date(nowMillis)
//
//  System.out.println("*******111" + now);
//
//  var jwtToken: String = Jwts.builder()
//
//    .setSubject("1211228")
//    .setIssuedAt(Date.from(Instant.now()))
//
//    .claim("password", "12341234")
//    //.setId(UUID.randomUUID.toString)
//
//    .signWith(SignatureAlgorithm.HS256, Secret)
//    .compact()
//
//
//
//  var token2:String = jwtToken
//
//
//
//  println(Secret)
//  println(memberID)
//  println(token2)
//  println(password)
//
////  log.info(password)
////
////  System.out.println("Token JWT :")
////  System.out.println(vars.get("token2"))
//
// // .getHeaderManager.add(new Nothing("Authorization", "Bearer " + vars.get("token2")))
//
//
//
//}
