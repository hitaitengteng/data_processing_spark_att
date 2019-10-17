package spark.data_processing

import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.types.StructField
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 11:13 2019/10/14
  */
object data_vectors {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("PCAExample").setMaster("local")
    val sc = SparkContext.getOrCreate(sparkConf)
    val spark = SparkSession
      .builder
      .getOrCreate()



  }

}
