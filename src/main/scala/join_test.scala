import com.alibaba.fastjson.JSON
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


    val frame1 = spark.read.format("json").load("D:\\json2.json")
//    val frame2 = spark.read.format("json").load("C:\\Users\\user\\Desktop\\jointest.json")
//    val result= JSON.toJSONString(frame1)

    frame1.show(100)
//val joinframe=frame1.join(frame2,"version")
//    joinframe.show()

  }
}
