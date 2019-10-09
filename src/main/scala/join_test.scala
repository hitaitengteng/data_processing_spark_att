import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SQLContext, SparkSession}

/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 13:34 2019/10/9 
  */
object join_test {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("treeModuleSpark").setMaster("local[2]")
    val sc = SparkContext.getOrCreate(sparkConf)
    val spark = SparkSession.builder().
      getOrCreate()
    val context = new SQLContext(sc)

    val frame1 = spark.read.format("json").load("C:\\Users\\user\\Desktop\\jointest2.json")
    val frame2 = spark.read.format("json").load("C:\\Users\\user\\Desktop\\jointest.json")

val joinframe=frame1.join(frame2,"version")
    joinframe.show()

  }
}
