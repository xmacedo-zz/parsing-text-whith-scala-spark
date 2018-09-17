trait DatabaseUtils extends LoggingConf {

  def getOracleConnection(user: String, password: String, host: String): java.sql.Connection = {
    try {
      val ods = new OracleDataSource
      ods.setUser(user)
      ods.setPassword(password)
      ods.setURL(s"jdbc:oracle:thin:@${host}")
      ods.getConnection
    }catch {
      case e: Throwable => throw new SQLException("Erro ao obter conexao Oracle: "+e.getMessage)
    }
  }

  def insertStatement(schema: String, table: String, cols: String) : String =
    s"insert into ${schema}.${table} (${cols}) values (${List.fill(cols.split(",").length)("?").mkString(",")})"

  def parseSqlDate(input: String, format: String = "yyyyMMdd") : Date =
    try {
      new Date(new SimpleDateFormat(format).parse(input).getTime)
    }catch{
      case _: Throwable => null
    }

  def toBigDecimal(input: String) : BigDecimal =
    try {
      new BigDecimal(input)
    }catch{
      case _: Throwable => BigDecimal.ZERO
    }

}
