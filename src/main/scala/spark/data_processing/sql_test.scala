package spark.data_processing

/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 15:23 2019/10/8 
  */
import org.apache.spark.SparkConf
import org.apache.spark.ml.feature.SQLTransformer
// $example off$
import org.apache.spark.sql.SparkSession

object sql_test {
  def main(args: Array[String]) {
    val sparkConf = new SparkConf();
    sparkConf.setMaster("local[*]").setAppName(this.getClass.getSimpleName)
    val spark = SparkSession
      .builder
      .config(sparkConf)
      .appName("SQLTransformerExample")
      .getOrCreate()

    // $example on$
    val df = spark.createDataFrame(
      Seq((0, 1.0, 3.0), (2, 2.0, 5.0))).toDF("id", "v1", "v2")

    val sqlTrans = new SQLTransformer().setStatement(
      "SELECT *, (v1 + v2) AS v3, (v1 * v2) AS v4 FROM __THIS__")

    sqlTrans.transform(df).show()
    // $example off$

    spark.stop()
  }
}
