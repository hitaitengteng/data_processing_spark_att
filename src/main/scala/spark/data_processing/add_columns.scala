package spark.data_processing

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{DoubleType, StringType}

/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 15:49 2019/10/17 
  */
object add_columns {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("ChiSquareTestExample").setMaster("local")
    val sc = SparkContext.getOrCreate(sparkConf)
    val spark = SparkSession
      .builder
      .getOrCreate()

    val data =spark.read.json("d:\\spark_data\\ChiS_data.json").toDF()
    val dataset=data.drop("label")
    val data_label =data.select("label")
    dataset.unionAll(data_label).show()
    //dataframe新增一列方法1，利用createDataFrame方法

//    val schema = dataset.schema.add("label", DoubleType, true)
//    val sample3 = spark.createDataFrame(data_label.rdd, schema).distinct()
//    sample3.show()

  }


}
