import breeze.linalg.max
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SQLContext, SparkSession}

/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 17:36 2019/10/8 
  */
object Miss_data {


  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("treeModuleSpark").setMaster("local[2]")
    val sc = SparkContext.getOrCreate(sparkConf)
    val spark = SparkSession.builder().
      getOrCreate()
    val context = new SQLContext(sc)

    val frame = spark.read.format("json").load( "D:\\spark_data\\people.json")
    val ne=frame.na.toString
    println("-------------------------ne"+ne)
    val na_drop=frame.na.drop()
/**
如果“how”是“any”，则删除包含任何null或nan值的行。
  *如果“how”是“all”，则仅当该行的每列为空或NaN时才删除行。
  * */
    val na_drop_2=frame.na.drop("any")
    val na_drop_3=frame.na.drop("all")
    /**
      *返回一个新的“dataframe”，删除包含任何空值或NaN值的行
      *在指定的列中。**/
    val na_drop_4=frame.na.drop(cols = Seq("age", "name"))
//    def drop（minnonnulls:int，cols:seq[string]）
//仅当行至少有'minnonnulls'非空和非NaN值时才保留该行

//用“value”替换数值列中的空值或NaN值
    val na_fill=frame.na.fill(123)
// 返回一个新的“dataframe”，它替换指定数值列中的空值或NaN值
val na_fill_2=frame.na.fill(123,cols = Seq("age", "name") )




    println("-------------------------na_drop")
    na_drop.show()
    println("-------------------------na_drop")
    na_drop_2.show()
    println("-------------------------na_drop")
    na_drop_3.show()
    println("-------------------------na_fill")
    na_fill.show()
  }
}
