trait InitJob{

  val CONSUL_URL     = "CONSUL_SERVER"
  val CONSUL_TOKEN   = "CONSUL_ACL_TOKEN"
  val CONSUL_PORT    = "CONSUL_PORT"

  val CONNECT_TIME_OUT  = 5000
  val RESPONSE_TIME_OUT = 5000

  val BASE_HOST         = "config/database/host"
  val BASE_USER         = "config/database/username"
  val BASE_PASS         = "config/database/password"
  val BASE_SCHEMA       = "config/database/schema"
  val NUM_PARTITIONS    = "config/num_partitions"
  val DIR_ARQUIVOS      = "config/dir_arq_in"

  case class Params(inputFile:  String = "",
                    dbUser:     String = "",
                    dbPassword: String = "",
                    dbHost:     String = "",
                    dbSchema:   String = "",
                    numPartitions: Int = 2)
  def getParams(args: Array[String]): Params = {

    val parser = new OptionParser[Params]("Importacao") {
      head("Importacao", "1.0")
    }

    parser.parse(args, Params()) match {
      case Some(params) =>

        val consulUrl        = System.getenv(CONSUL_URL)
        val consulToken      = System.getenv(CONSUL_TOKEN)
        val consulPortVar    = System.getenv(CONSUL_PORT)

        assert(!consulUrl.isEmpty,     "The URL of the configured CONSUL is empty or null.")
        assert(!consulToken.isEmpty,   "The Token of the configured CONSUL is empty or null.")
        assert(!consulPortVar.isEmpty, "The Port of the configured CONSUL is empty or null.")

        val kvClient = new ConsulClient(consulUrl, consulPortVar.toInt)

        Params(
          getConsulStrParam(kvClient, consulToken, DIR_ARQUIVOS_FATURA),
          getConsulStrParam(kvClient, consulToken, BASE_USER),
          getConsulStrParam(kvClient, consulToken, BASE_PASS),
          getConsulStrParam(kvClient, consulToken, BASE_HOST),
          getConsulStrParam(kvClient, consulToken, BASE_SCHEMA),
          getConsulIntParam(kvClient, consulToken, NUM_PARTITIONS))

      case None =>
        throw new IllegalArgumentException("One or more parameters are invalid or missing")
    }
  }
  private

  def getConsulStrParam(kvClient: ConsulClient, consulToken: String, parameter: String): String =
    kvClient.getKVValue(parameter, consulToken).getValue.getDecodedValue

  def getConsulIntParam(kvClient: ConsulClient, consulToken: String, parameter: String): Int =
    getConsulStrParam(kvClient, consulToken, parameter).toInt

}
