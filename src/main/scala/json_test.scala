///**
//  * @Author: Aitengteng
//  * @Description:
//  * @Date: Create in 13:47 2019/10/9
//  */
//import org.apache.spark._
//import scala.util.parsing.json.JSON
//object json_test {
//  def main(args:Array[String]): Unit ={
//    //初始化配置：设置主机名和程序主类的名字
//    val conf = new SparkConf().setMaster("local").setAppName("JSONApp");
//    //通过conf来创建sparkcontext
//    val sc = new SparkContext(conf);
//
//    val inputFile = "D:\\spark_data\\people.json"//读取json文件
//    val jsonStr = sc.textFile(inputFile);
//    val result = jsonStr.map(s => JSON.parseFull(s));//逐个JSON字符串解析
//
//    result.foreach(
//      {
//        r => r match {
//          case Some(map:Map[String,Any]) => println(map)
//          case None => println("parsing failed!")
//          case other => println("unknown data structure" + other)
//        }
//      }
//    );
//  }
//}