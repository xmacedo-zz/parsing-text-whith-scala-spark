object Trailer {
  val trailerConfig: Map[String, (Int, Int)] = Map(
    "LINHA_1" -> (0,3),
    "LINHA_2" -> (3,6),
    "LINHA_3" ->(6,25),
    "LINHA_4" -> (25, 26),
    "TOTAL_REG_HEADER_FATURA" -> (26, 35),
    "TOTAL_REG_DETALHE_FATURA" -> (35, 44),
    "LINHA_7" -> (44, 400)
  )
  def getValorPosicao(config: Map[String, (Int, Int)],
                      linha: String, campo: String): String = {
    val posicoes = config(campo)
    linha.substring(posicoes._1, posicoes._2)
  }
  def getValorTrailer(linha: String, campo: String): String = getValorPosicao(trailerConfig, linha, campo)
}
