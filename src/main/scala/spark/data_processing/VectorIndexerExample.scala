package spark.data_processing

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.feature.{VectorAssembler, VectorIndexer}
import org.apache.spark.sql.SparkSession

//倘若所有特征都已经被组织在一个向量中，又想对其中某些单个分量进行处理时，
// Spark ML提供了VectorIndexer类来解决向量数据集中的类别性特征转换。
//通过为其提供maxCategories超参数，它可以自动识别哪些特征是类别型的，并且将原始值转换为类别索引。
// 它基于不同特征值的数量来识别哪些特征需要被类别化，那些取值可能性最多不超过maxCategories的特征需要会被认为是类别型的
//综述：若出现的种类少，则被认为是类别

object VectorIndexerExample {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("VectorIndexerExample").setMaster("local")
    val sc = SparkContext.getOrCreate(sparkConf)
    val spark = SparkSession
      .builder
      .getOrCreate()


    val dataset=spark.read.json("D:\\spark_data\\PCA_data.json").toDF()
    val name=dataset.columns

    val assembler = new VectorAssembler()
      .setInputCols(name)
      .setOutputCol("userFeatures")

    val data = assembler.transform(dataset)

    val indexer = new VectorIndexer()
      .setInputCol("userFeatures")
      .setOutputCol("indexed")
      .setMaxCategories(10)
///那些取值可能性最多不超过maxCategories的特征会被认为是类别型的,进而将原始值转换为类别索引

    val indexerModel = indexer.fit(data)

    val categoricalFeatures: Set[Int] = indexerModel.categoryMaps.keys.toSet
    println(s"Chose ${categoricalFeatures.size} " +
      s"categorical features: ${categoricalFeatures.mkString(", ")}")

    // Create new column "indexed" with categorical values transformed to indices
    val indexedData = indexerModel.transform(data)
    indexedData.show()
    // $example off$

    spark.stop()
  }
}
// scalastyle:on println
