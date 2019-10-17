package spark.data_processing
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed._
import org.apache.spark.{SparkConf, SparkContext}


/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 10:26 2019/10/17 
  */
object Vectors_Test {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName(this.getClass().getSimpleName().filter(!_.equals('$')))
    val sc = new SparkContext(conf)


    val rdd = sc.textFile("file/data/mllib/input/basic/MatrixRow.txt") //创建RDD文件路径
      .map(_.split(' ') //按“ ”分割
      .map(_.toDouble)) //转成Double类型
      .map(line => Vectors.dense(line)) //转成Vector格式

    val rm = new RowMatrix(rdd) //读入行矩阵


    val rdd2 = sc.textFile("file/data/mllib/input/basic/MatrixRow.txt") //创建RDD文件路径
      .map(_.split(' ') //按“ ”分割
      .map(_.toDouble)) //转成Double类型
      .map(line => Vectors.dense(line)) //转化成向量存储
      .map((vd) => new IndexedRow(vd.size, vd)) //转化格式

    val irm = new IndexedRowMatrix(rdd2) //建立索引行矩阵实


    val rdd3 = sc.textFile("file/data/mllib/input/basic/MatrixRow.txt") //创建RDD文件路径
      .map(_.split(' ') //按“ ”分割
      .map(_.toDouble)) //转成Double类型
      .map(vue => (vue(0).toLong, vue(1).toLong, vue(2))) //转化成坐标格式
      .map(vue2 => new MatrixEntry(vue2 _1, vue2 _2, vue2 _3)) //转化成坐标矩阵格式

    val crm = new CoordinateMatrix(rdd3) //实例化坐标矩阵




  }
}
