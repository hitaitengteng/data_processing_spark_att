package spark.data_processing

/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 20:08 2019/8/28 
  */

//导入包
//导包
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.feature.VectorIndexer
import org.apache.spark.ml.linalg.Vector
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.ml.tuning.{CrossValidator, ParamGridBuilder, TrainValidationSplit}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

object Regression_lin {


  def main(args: Array[String]) {


    val sparkConf = new SparkConf().setAppName("ChiSquareTestExample").setMaster("local")
    val sc = SparkContext.getOrCreate(sparkConf)
    val spark = SparkSession
      .builder
      .getOrCreate()


//    val data = spark.read.format("libsvm")
//      .load("D:\\spark_mlib\\mlib_example\\data\\sample_linear_regression_data.txt")
//    data.show()
val data=spark.read.json("D:\\asd123")
    val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

//    val labelIndexer = new StringIndexer().setInputCol("label").setOutputCol("indexedLabel").fit(data)




    //然后，设置logistic的参数，这里我们统一用setter的方法来设置，也可以用ParamMap来设置（具体的可以查看spark mllib的官网）。这里设置了循环次数为10次，
    // 这后面的是此处没设置，但是最开始学的设置了：正则化项为0.3等，具体的可以设置的参数可以通过explainParams()来获取，还能看到程序已经设置的参数的结果。
//    val lr = new LogisticRegression().setLabelCol("indexedLabel").setFeaturesCol("indexedFeatures").setMaxIter(50)
    val lr = new LinearRegression()


//    val labelConverter = new IndexToString().setInputCol("prediction").setOutputCol("predictedLabel")



val result=lr.fit(trainingData).transform(testData)
    result.select("label","prediction")show()






  }


}