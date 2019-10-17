package spark.data_processing

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.feature.{DCT, VectorAssembler}
import org.apache.spark.sql.SparkSession

/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 14:05 2019/10/11 
  */
object Transform_DCT {
  def main(args: Array[String]): Unit = {

    val taskName: String ="DCTE"//任务名称
    val inputPath: String = "D:\\spark_data\\DCT_json.json"//输入文件


    val sparkConf = new SparkConf().setAppName(taskName).setMaster("local")
    val sc = SparkContext.getOrCreate(sparkConf)
    val spark = SparkSession
      .builder
      .getOrCreate()

    val dataset =Read_data.read_json(sc,inputPath).toDF()
    val name_columns=dataset.columns

    val assembler = new VectorAssembler()
      .setInputCols(name_columns)
      .setOutputCol("features")

    val df = assembler.transform(dataset).select("features")



    val dct = new DCT()
      .setInputCol("features")
      .setOutputCol("featuresDCT")
      .setInverse(false)

    val dctDf = dct.transform(df)
    dctDf.select("featuresDCT").show()

    spark.stop()
  }
}
