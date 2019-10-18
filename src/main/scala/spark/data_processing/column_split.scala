package spark.data_processing

//import breeze.linalg.split
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SparkSession, functions}
import com.google.common.base.Splitter
import org.apache.spark.api.java.function.FlatMapFunction
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.types._

import scala.collection.mutable
import java.util
/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 14:49 2019/10/18 
  */
object column_split {

  def main(args: Array[String]): Unit = {


    val sparkConf = new SparkConf().setAppName("ChiSquareTestExample").setMaster("local")
    val sc = SparkContext.getOrCreate(sparkConf)
    val spark = SparkSession
      .builder
      .getOrCreate()

     import spark.implicits._

    val schema = StructType(Seq(
      StructField("id", DoubleType),
      StructField("package", DoubleType)
//      StructField("activetime", LongType)
    ))

    val encoder = RowEncoder(schema)


    val df=spark.read.json("D:\\asd123").map(x=>x.toString())

//
//      .flatMap(new FlatMapFunction[Row, Row] {
//      override def call(r: Row): util.Iterator[Row] = {
//        val list = new util.ArrayList[Row]()
//        val datas = r.getAs[mutable.WrappedArray.ofRef[Row]]("features")
//        datas.foreach(data => {
//          list.add(Row(r.getAs[Double]("id"), data.getAs[Double](1)))
//        })
//        list.iterator()
//      }
//    }, encoder)
////    SELECT EXPLODE(data) FROM behavior
//
//      df.show()

    //方法2： 使用内置函数split，然后遍历添加列
    val separator = ","
    lazy val first = df.first()

    val numAttrs = first.toString().split(separator).length
    val attrs = Array.tabulate(numAttrs)(n => "col_" + n)
    //按指定分隔符拆分value列，生成splitCols列
    import org.apache.spark.sql.functions._
    var newDF = df.withColumn("splitCols", split($"features", separator))
    attrs.zipWithIndex.foreach(x => {
      newDF = newDF.withColumn(x._1, $"splitCols".getItem(x._2))
    })
    newDF.show()

  }
}
