object Importacao extends Validacao with InitJob with LoggingConf with SparkJob {

  def main(args: Array[String]) = {
    val params = getParams(args)

    // ***************************************
    log.info("===== Inicio IMPORTACAO ======")
    // ***************************************
    val db = DatabaseOps(params.dbUser,
      params.dbPassword,
      params.dbHost,
      params.dbSchema)

    var files = getListOfFiles(params.inputFileFatura)
    files.foreach(arqu => {
      breakable {

        val nomeArquivo = arqu.getName
        if(db.existeArquivo(nomeArquivo,"IMP")){
          log.info(s"O arquivo '${nomeArquivo}' ja foi importado, o mesmo nao sera processado duas vezes.")
          break
        }

        val dados = spark.read
          .textFile(arqu.getAbsolutePath)
          .cache
        val numLinhas = dados.count
        val dataArquivo = valida(dados, numLinhas)

        val arquivoId = db.insereNomeArquivo(nomeArquivo,dataArquivo)
        log.info("> Processando o arquivo ["+arquivoId+"]: "+nomeArquivo)

        val listaHeader = dados.rdd.filter(filterHeader).repartition(10)
        listaHeader.foreachPartition(x=> db.persisteDadosOperacaoHeader(x,arquivoId))

        //Em processo
        db.atualizaStatusArquivo(arquivoId,"5","F")

        val listaDetlhe = dados.rdd.filter(filterDetail).repartition(10)
        listaDetlhe.foreachPartition(x=> db.persisteDadosOperacaoDetail(x,arquivoId))

        //FIM
        db.atualizaStatusArquivo(arquivoId,"1","F")
        log.info("> FIM processamento do arquivo ["+arquivoId+"]: "+nomeArquivo)
        db.atualizaStatusProc(idProcesso,"1")
      }
    })
    log.info("===== Fim IMPORTACAO ======")
    ///
  }
  def getListOfFiles(dir: String):List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }
}
