trait SparkJob extends Serializable {
  implicit val spark: SparkSession = SparkSession.builder
    .master("local[2]")
    .appName("Processar")
    .config("spark.sql.warehouse.dir", "/opt/spark/spark-2.1.1-bin-hadoop2.7/spark-warehouse")
    .getOrCreate
}
