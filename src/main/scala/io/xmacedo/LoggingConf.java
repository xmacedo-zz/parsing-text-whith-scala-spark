trait LoggingConf {

  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

  @transient lazy val log = Logger.getLogger(getClass.getName)
}
