package doTerraSimulations.oneTimePackage

import io.gatling.core.Predef._
import io.gatling.core.check.CheckBuilder
import io.gatling.http.Predef.{header, headerRegex, http, status}

object HomePage {

//        val homePage= exec(http(requestName = "Home Page")
//          .get("US/en")
//
//          .header(name = "User_Agent", value = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36  Chrome/88.0.4324.190 Safari/537.36")
//          .header(name = "content-type", value = "application/json; text/plain; charset=UTF-8")
//          .header(name = "Accept", value = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
//          .header(name = "Accept-Language", value = "en-US,en;q=0.9")
//          .header(name = "Keep-Alive", value = "115")
//          .header(name = "Accept-Encoding", value = "gzip, deflate, br")
//          .header(name = "Accept-Charset", value = "utf-8;q=0.7,*;q=0.7")
//
//          .acceptCharsetHeader("UTF-8")
//          .silentResources
//          .headers(home_page_header)
//          .check(regex("""<input type="hidden" name="CSRFToken" value="(.+?)" />""").saveAs(key="csrftoken"))
//



//        )



}
