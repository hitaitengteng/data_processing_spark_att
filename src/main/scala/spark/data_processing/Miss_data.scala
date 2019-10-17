package spark.data_processing

import org.apache.spark.sql.SparkSession
/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 17:36 2019/10/8 
  */
object Miss_data {


  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder
      .appName(s"${this.getClass.getSimpleName}")
      .getOrCreate()

    // $example on$
    // Loads data.
    val dataset = spark.read.json("D:\\spark_data\\missing_data.json")
    dataset.na.drop().show()


  }
}
