object FaturaHeaderArquivo {
  val headerConfig: Map[String, (Int, Int)] = Map(
    "LINHA_1" -> (0,3),
    "LINHA_2" -> (3,6),
    "LINHA_3" ->(6,25),
    "LINHA_5" -> (25, 400)
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
  def getValorHeaderArquivo(linha: String, campo: String): String = getValorPosicao(headerConfig, linha, campo)
}
