trait Validacao{

  def isHeaderArquivo(c: String): Boolean = c.substring(25,26)=="0"
  def isHeader       (c: String): Boolean = c.substring(25,26)=="1"
  def isDetail       (c: String): Boolean = c.substring(25,26)=="2"
  def isTrailer      (c: String): Boolean = c.substring(25,26)=="9"

  def filterHeaderAndTrailer(linha: String): Boolean = !isDetail(linha) && !isHeader(linha)
  def filterHeader(linha: String): Boolean = isHeader(linha)
  def filterDetail(linha: String): Boolean = isDetail(linha)


  def valida(dados: Dataset[String], numLinhas: Long) : String= {

    val headerTrailer = dados.rdd.zipWithIndex
      .filter(t => filterHeaderAndTrailer(t._1))
      .map(_.swap)
      .collectAsMap()

    val expectedIndexes  = List(0, numLinhas-1).sorted
    val retrievedIndexes = headerTrailer.keys.toList.sorted

    assert(retrievedIndexes == expectedIndexes,
      s"Expected ${expectedIndexes.mkString(",")} got ${retrievedIndexes.mkString(",")}")

    val fileHeader  = headerTrailer(0)
    val fileTrailer = headerTrailer(numLinhas-1)

    assert(isHeaderArquivo(fileHeader), "First index is not header")
    assert(isTrailer(fileTrailer), "Last index is not trailer")

    val expectedCount  = numLinhas.toLong - 2

    val totalLinhasHeader  = Trailer.getValorTrailer(fileTrailer, "TOTAL_REG_HEADER").toLong
    val totalLinhasDetalhe = Trailer.getValorTrailer(fileTrailer, "TOTAL_REG_DETALHE").toLong

    assert(expectedCount == totalLinhasHeader+totalLinhasDetalhe, s"${expectedCount} not ${totalLinhasHeader+totalLinhasDetalhe}")

    val dataArquivo = HeaderArquivo.getValorHeaderArquivo(headerTrailer(0),"DATA_ARQUIVO")

    dataArquivo
  }
}
