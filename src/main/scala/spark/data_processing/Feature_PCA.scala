package spark.data_processing

import breeze.linalg.NumericOps.Arrays
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.feature.{Imputer, PCA, VectorAssembler}
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.{Encoders, SparkSession}

/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 11:44 2019/10/11 
  */
object Feature_PCA {
  case class model_instance (features: org.apache.spark.ml.linalg.Vector)

  def main(args: Array[String]): Unit = {


    val sparkConf = new SparkConf().setAppName("PCAExample").setMaster("local")
    val sc = SparkContext.getOrCreate(sparkConf)
    val spark = SparkSession
      .builder
      .getOrCreate()



    // $example on$
    val dataset = spark.read.json("D:\\spark_data\\pca_data.json").toDF()
    val name_columns=dataset.columns

    val assembler = new VectorAssembler()
      .setInputCols(name_columns)
      .setOutputCol("features")

    val df = assembler.transform(dataset).select("features")

    val pca = new PCA()
      .setInputCol("features")
      .setOutputCol("pcaFeatures")
      .setK(1)
      .fit(df)

    val result = pca.transform(df).select("pcaFeatures")
    result.show(false)
    // $example off$

    spark.stop()
  }
}
