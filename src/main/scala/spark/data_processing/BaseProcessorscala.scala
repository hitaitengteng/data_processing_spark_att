package spark.data_processing

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 9:43 2019/9/17 
  */
object BaseProcessorscala {

  def doExecutePrescla(taskName: String): SparkContext = {
    val sparkConf = new SparkConf().setAppName(taskName)
    val sc = SparkContext.getOrCreate(sparkConf)
    System.out.println("qsdz_infos : " + sc.applicationId + ";" + sc.uiWebUrl.get)
    return sc
  }
}
