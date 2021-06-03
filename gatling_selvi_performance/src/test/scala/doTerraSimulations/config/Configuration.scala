package doTerraSimulations.config

class Configuration {

  val environment: String = System.getProperty("environment")
  val clientId: String = System.getProperty("CLIENT_ID")
  val clientSecret: String = System.getProperty("CLIENT_SECRET")
  val appURL: String = "https://some-sub-domain." + environment + "some-domain.com/api"
  var tokenPath: String = "https://some-sub-domain" + environment + ".eu.auth0.com/oauth/token"
  val userPath = "/identity/iaa/v1/users"

}
