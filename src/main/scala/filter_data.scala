import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SQLContext, SparkSession}

/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 14:41 2019/10/9 
  */
object filter_data {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("treeModuleSpark").setMaster("local[2]")
    val sc = SparkContext.getOrCreate(sparkConf)
    val spark = SparkSession.builder().
      getOrCreate()
    val context = new SQLContext(sc)
    val frame = spark.read.format("json").load( "D:\\spark_data\\people.json")
    val filter_frame=frame.filter("age > 20")
    filter_frame.show()


  }
}
