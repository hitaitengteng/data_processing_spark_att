package spark.data_processing

import org.apache.spark.SparkContext
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.{DataFrame, SparkSession}


//读取逗号分隔的数据文件
/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 16:47 2019/9/25 
  */
object Read_data {
  case class model_instance (features: org.apache.spark.ml.linalg.Vector)
  case class model_instance_label (features: org.apache.spark.ml.linalg.Vector,labels:String)

def read_features(sc:SparkContext,filename:String): DataFrame ={

  val spark = SparkSession.builder().
    getOrCreate()
  import spark.implicits._

  val rawData = sc.textFile(filename)

  val df = rawData.map(line =>
    model_instance( Vectors.dense(line.split(",").map(_.toDouble)))
  ).toDF()
  df
}


  def read_features_only(sc:SparkContext,filename:String): DataFrame ={
    val spark = SparkSession.builder().
      getOrCreate()
    import spark.implicits._

    val rawData = sc.textFile(filename)

    val df = rawData.map(line =>
    { val index=line.lastIndexOf(",")
      val df01=line.substring(0,index)
      val df02=line.substring(index+1,line.length)
      model_instance( Vectors.dense(df01.split(",").map(_.toDouble)))
    }).toDF()
    df
  }


  def read_features_label(sc:SparkContext,filename:String): DataFrame ={
    val spark = SparkSession.builder().
      getOrCreate()
    import spark.implicits._

    val rawData = sc.textFile(filename)

    val df = rawData.map(line =>
    { val index=line.lastIndexOf(",")
      val df01=line.substring(0,index)
      val df02=line.substring(index+1,line.length)
      model_instance_label(Vectors.dense(df01.split(",").map(_.toDouble)),df02.toString)
    }).toDF()
    df
  }


//  This applies to Parquet, ORC, CSV, JSON and " +
//  "LibSVM data sources.")

  def read_livswm(sc:SparkContext,filename:String): DataFrame ={


    val spark = SparkSession.builder().
      getOrCreate()

    val df = spark.read.format("libsvm").load(filename)
    df
  }

  def read_json(sc:SparkContext,filename:String): DataFrame ={


    val spark = SparkSession.builder().
      getOrCreate()
    val df = spark.read.format("json").load(filename)
    df
  }



}
