package spark.data_processing

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.SparkSession

/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 15:53 2019/10/18 
  */
object Set_Role {


  def main(args: Array[String]): Unit = {


    val sparkConf = new SparkConf().setAppName("Set_Role").setMaster("local")
    val sc = SparkContext.getOrCreate(sparkConf)
    val spark = SparkSession
      .builder
      .getOrCreate()



    val dataset = spark.read.json("D:\\downloads\\test.json").toDF()
    val name_columns=dataset.columns

    val  dependent= List("xh")
    val  argument=List("alfa_pyd","b")

    val feature=dataset.columns.toList.diff(dependent).toArray
    val label=dataset.columns.toList.diff(argument)


    val assembler = new VectorAssembler()
      .setInputCols(feature)
      .setOutputCol("features")

    val df = assembler.transform(dataset).select("features")
df.printSchema()

    df.show()
  }





}
