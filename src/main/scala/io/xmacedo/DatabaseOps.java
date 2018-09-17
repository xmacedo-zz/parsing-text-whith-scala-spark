case class DatabaseOps(user: String, password: String, host: String,
                       schema: String) extends DatabaseUtils {

   def existeArquivo(nomeArquivo: String,nomeProc: String): Boolean ={

    val conn = getOracleConnection(user, password, host)
    val psb =
      if(nomeProc.equals("IMP")) conn.prepareStatement(s"SELECT 1 FROM ARQUIVOS WHERE NOME_ARQUIVO = '${nomeArquivo}'")

    val rs = psb.executeQuery()
    val retorno = if ( rs.next() ) true else false

    psb.close
    conn.close
    retorno
  }

  def insereNomeArquivo(nomeArquivo: String, dataArquivo: String): Int ={

    val conn = getOracleConnection(user, password, host)
    val stmtTable = "ARQUIVOS"
    val stmtCols  = "NOME_ARQUIVO, STATUS_ARQUIVO,DATA_GERACAO_ARQ"
    val ps = conn.prepareStatement(insertStatement(schema, stmtTable, stmtCols))

    log.info("QUERY: " + insertStatement(schema, stmtTable, stmtCols))

    ps.setString(1, nomeArquivo)
    ps.setString(2,"0")
    ps.setDate(3, parseSqlDate(dataArquivo,"ddMMyyyy"))
    ps.executeUpdate()
    conn.commit

    val psb = conn.prepareStatement(s"SELECT max(ID) FROM ARQUIVOS WHERE NOME_ARQUIVO = '${nomeArquivo}' AND STATUS_ARQUIVO=0")

    val rs = psb.executeQuery()
    conn.commit
    // Retrieve the auto generated key(s).
    val retorno = if ( rs.next() ) rs.getInt(1) else 0

    psb.close
    conn.close
    retorno
  }

  def atualizaStatusArquivo(idArquivo: Int, statusArquivo: String, tipoArquivo: String ) = {

    val conn = getOracleConnection(user, password, host)
    var psb =conn.prepareStatement(s"UPDATE ARQUIVOS SET STATUS_ARQUIVO=$statusArquivo  WHERE ID = $idArquivo")

    psb = conn.prepareStatement(s"UPDATE ARQUIVOS SET STATUS_ARQUIVO=$statusArquivo  WHERE ID = $idArquivo")

    psb.executeUpdate()
    conn.commit
    psb.close

    conn.close

  }

  def persisteDadosOperacaoHeader(detalhes: Iterator[String], idArquivo: Int) = {
    val conn = getOracleConnection(user, password, host)

    conn.setAutoCommit(false)

    val stmtTable = "HEADER"
    val stmtCols = "LINHA_1,LINHA_2,LINHA_3,LINHA_4,LINHA_5"

    val ps = conn.prepareStatement(insertStatement(schema, stmtTable, stmtCols))

    detalhes.foreach(detalhe => {

      ps.setInt(1, idArquivo) // 1
      ps.setString(2, Header.getValorHeader(detalhe, "LINHA_1"))
      ps.setDate(3, parseSqlDate(Header.getValorHeader(detalhe, "LINHA_2"),"ddMMyyyy"))
      ps.setBigDecimal(4, toBigDecimal(Header.getValorHeader(detalhe, "LINHA_3")))
      ps.setBigDecimal(5, toBigDecimal(Header.getValorHeader(detalhe, "LINHA_4")))
      ps.setBigDecimal(4, toBigDecimal(Header.getValorHeader(detalhe, "LINHA_5")))

      ps.addBatch

    })

    ps.executeBatch
    conn.commit

    ps.close
    conn.close
  }

  def persisteDadosOperacaoDetail(detalhes: Iterator[String], idArquivo: Int) = {

      val conn = getOracleConnection(user, password, host)

      conn.setAutoCommit(false)

        val stmtTable = "DETAIL"
        val stmtCols = "LINHA_1,LINHA_2,LINHA_3,LINHA_4,LINHA_5"

        val ps = conn.prepareStatement(insertStatement(schema, stmtTable, stmtCols))

        detalhes.foreach(detalhe => {

        ps.setInt(1, idArquivo) // 1
        ps.setString(2, Detail.getValorDetail(detalhe, "LINHA_1"))
        ps.setDate(3, parseSqlDate(Detail.getValorDetail(detalhe, "LINHA_2"),"ddMMyyyy"))
        ps.setBigDecimal(4, toBigDecimal(Detail.getValorDetail(detalhe, "LINHA_3")))
        ps.setBigDecimal(5, toBigDecimal(Detail.getValorDetail(detalhe, "LINHA_4")))
        ps.setBigDecimal(4, toBigDecimal(Detail.getValorDetail(detalhe, "LINHA_5")))

        ps.addBatch

        })

      ps.executeBatch
      conn.commit

      ps.close
      conn.close

  }
}
