package spark.data_processing

/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 19:53 2019/9/24
  */
object Parameter_determination {
  def main(args: Array[String]): Unit = {
    //数据，支持度，置信度
    val (filepath, supThreshold, confidenceThreshold)
    = if(args.length < 3) {
      println(s"Insuffient arguments. 3 are required but recieved ${args.length}.")
      println(s"Setting defult parameters")
      ("d:\\spark_data\\T10I4D100K.dat", 0.01, 0.5)
    } else {
      (args(0), args(1).toDouble, args(2).toDouble)
    }

  }
}
