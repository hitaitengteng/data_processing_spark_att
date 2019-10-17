package spark.data_processing

import org.apache.spark.SparkContext
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.DataFrame

/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 10:39 2019/9/19
  */
object Input_reorganize {
  case class model_instance (features: org.apache.spark.ml.linalg.Vector)
  def Input_reorganize(sc: SparkContext,inpath:String): DataFrame = {
    import org.apache.spark.sql.types.{LongType, StructField, StructType}
    import org.apache.spark.sql.{Row, SparkSession}
    val spark = SparkSession.builder().config("spark.sql.crossJoin.enabled","true").
      getOrCreate()
    import spark.implicits._
    val rawData = sc.textFile(inpath)
    //      //计算文件宽度
    val filelenth = rawData.map(line => line.split(",").length
    ).toDF().rdd.map(x=>x(0)).first().toString.toInt
    val arraylenth=filelenth-1

    val namelist = new Array[String](filelenth)
    var datajh=new Array[DataFrame](filelenth)
    var dfWithPK=new Array[DataFrame](filelenth)

    for( a <- 0 to arraylenth){
       datajh(a) = rawData.map(line => line.split(",")
      ).map(x=> x(a)).toDF()
      val sdata = datajh(a).schema
      val rows = datajh(a).rdd.zipWithIndex.map{case (r: Row, id: Long) => Row.fromSeq(id +: r.toSeq)}
      // 再由 row 根据原表头进行转换
       dfWithPK(a) = spark.createDataFrame( rows, StructType(StructField("id", LongType, false) +: sdata.fields))
    }

    var dataaaa =dfWithPK(1)
//    dataaaa.show()
    val joinlenth=arraylenth-2
    for( a <- 0 to joinlenth){
      dataaaa=dataaaa.join(dfWithPK(a),Seq("id"))
    }
     val  datadropid=dataaaa.drop("id")
    val separator = ","
    val features=datadropid.map(_.toSeq.foldLeft("")(_ + separator + _).substring(1)).map(line => {
      model_instance(Vectors.dense(line.split(",")
        .map(_.toDouble)))}).toDF()
    val labels = rawData.map(line => line.split(",")
    ).map(x=> x(arraylenth).toDouble).toDF("label")

    val fsch=features.schema
    val frows = features.rdd.zipWithIndex.map{case (r: Row, id: Long) => Row.fromSeq(id +: r.toSeq)}
    val ffeatures=spark.createDataFrame( frows, StructType(StructField("id", LongType, false) +: fsch.fields))

    val lsch=labels.schema
    val lrows = labels.rdd.zipWithIndex.map{case (r: Row, id: Long) => Row.fromSeq(id +: r.toSeq)}
    val lfeatures=spark.createDataFrame( frows, StructType(StructField("id", LongType, false) +: lsch.fields))





    val df=ffeatures.join(lfeatures,Seq("id"))
    return df
  }
}
