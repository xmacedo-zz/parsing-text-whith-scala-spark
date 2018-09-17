object Detalhe {
  val detalheConfig: Map[String, (Int, Int)] = Map(
    "COLUNA_1" -> (0,3),
    "COLUNA_2" -> (3,6),
    "COLUNA_3" ->(6,25),
    "COLUNA_4" -> (25,26),
    "COLUNA_5" -> (26,500
  )
  def getValorPosicao(config: Map[String, (Int, Int)],
                      linha: String, campo: String): String = {
    val posicoes = config(campo)
    try{
      linha.substring(posicoes._1, posicoes._2)
    }catch {
      case _: IndexOutOfBoundsException => null
    }
  }
  def getValorDetalhe(linha: String, campo: String): String = getValorPosicao(detalheConfig, linha, campo)
}
