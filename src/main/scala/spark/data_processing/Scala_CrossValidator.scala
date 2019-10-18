package spark.data_processing

/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 20:08 2019/8/28 
  */

//导入包
//导包
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
import org.apache.spark.sql.SQLContext
import org.apache.spark.ml.tuning.{CrossValidator, ParamGridBuilder}
import org.apache.spark.sql.Row
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}
import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}
import org.apache.spark.ml.{Pipeline, PipelineModel}

object Scala_CrossValidator {
  //在主函数外部建立Iris类，否则会报错
  // 用case class定义一个schema:Iris，Iris就是需要的数据的结构；
  case class Iris(features: org.apache.spark.ml.linalg.Vector, label: String)
  def main(args: Array[String]) {
    //    val sparkConf = new SparkConf().setAppName("treeModuleSpark").setMaster("spark://hdp1:7077")
    val sparkConf = new SparkConf().setAppName("treeModuleSpark").setMaster("local[2]")
    val sc = SparkContext.getOrCreate(sparkConf)

    //创建context，然后导入sqlContext.implicits._来实现RDD到Dataframe的隐式转换。这个非常重要，否则无法读取数据
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    import sqlContext.implicits._

    val data = sc.textFile("D:\\spark_data\\iris.data.txt").map(_.split(",")).map(p => Iris(Vectors.dense(p(0).toDouble,p(1).toDouble,p(2).toDouble, p(3).toDouble), p(4).toString())).toDF()

    val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

    val labelIndexer = new StringIndexer().setInputCol("label").setOutputCol("indexedLabel").fit(data)

    val featureIndexer = new VectorIndexer().setInputCol("features").setOutputCol("indexedFeatures").fit(data)



    //然后，设置logistic的参数，这里我们统一用setter的方法来设置，也可以用ParamMap来设置（具体的可以查看spark mllib的官网）。这里设置了循环次数为10次，
    // 这后面的是此处没设置，但是最开始学的设置了：正则化项为0.3等，具体的可以设置的参数可以通过explainParams()来获取，还能看到程序已经设置的参数的结果。
    val lr = new LogisticRegression().setLabelCol("indexedLabel").setFeaturesCol("indexedFeatures").setMaxIter(50)

    val labelConverter = new IndexToString().setInputCol("prediction").setOutputCol("predictedLabel").setLabels(labelIndexer.labels)


    val lrPipeline = new Pipeline().setStages(Array(labelIndexer, featureIndexer, lr, labelConverter))

    //截止到上面，都是跟最开始学的那个一样
    //在最开始，这里已经只需要用pipeline再fit一下生成model，然后model再transform一下生成的是prediction，这个就是预测的结果

    //可以使用ParamGridBuilder方便构造参数网格。其中regParam参数定义规范化项的权重；elasticNetParam是Elastic net 参数，取值介于0和1之间。elasticNetParam设置2个值，regParam设置3个值。最终将有(3 * 2) = 6个不同的模型将被训练。

    val paramGrid = new ParamGridBuilder().
      addGrid(lr.elasticNetParam, Array(0.2,0.8)).
      addGrid(lr.regParam, Array(0.01, 0.1, 0.5)).
      build()

//paramgrid 是一个array[parammap]

    //再接下来，构建针对整个机器学习工作流的交叉验证类，定义验证模型、参数网格，以及数据集的折叠数，并调用fit方法进行模型训练。其中，对于回归问题评估器可选择RegressionEvaluator，二值数据可选择BinaryClassificationEvaluator，多分类问题可选择MulticlassClassificationEvaluator。评估器里默认的评估准则可通过setMetricName方法重写。

    val cv = new CrossValidator().
      setEstimator(lrPipeline).
      setEvaluator(new MulticlassClassificationEvaluator().setLabelCol("indexedLabel").setPredictionCol("prediction")).
      setEstimatorParamMaps(paramGrid).
      setNumFolds(3) // Use 3+ in practice


    val cvModel = cv.fit(trainingData)

    //接下来，调动transform方法对测试数据进行预测，并打印结果及精度。
    val lrPredictions=cvModel.transform(testData)

    lrPredictions.select("predictedLabel", "label", "features", "probability").
      collect().
      foreach{
        case Row(predictedLabel: String, label:String,features:Vector, prob:Vector) =>
          println(s"($label, $features) --> prob=$prob, predicted Label=$predictedLabel")
      }


    val evaluator = new MulticlassClassificationEvaluator().
      setLabelCol("indexedLabel").
      setPredictionCol("prediction")

    //模型评估
    val lrAccuracy = evaluator.evaluate(lrPredictions)

    //最后，还可以获取最优的逻辑斯蒂回归模型，并查看其具体的参数:
    val bestModel= cvModel.bestModel.asInstanceOf[PipelineModel]

    val lrModel = bestModel.stages(2).
      asInstanceOf[LogisticRegressionModel]

    println("Coefficients: " + lrModel.coefficientMatrix + "Intercept: "+lrModel.interceptVector+ "numClasses: "+lrModel.numClasses+"numFeatures: "+lrModel.numFeatures)

  }


}