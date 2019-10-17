package spark.data_processing

import org.apache.commons.lang.RandomStringUtils

/**
  * @Author: Aitengteng
  * @Description:
  * @Date: Create in 9:39 2019/9/18
  */
object Random_filename {
  def getSubPath(statename: String): String = {
    val randomString = RandomStringUtils.random(8, "abcdefghijklmnopqrstuvwxyz")
    // 8位随机字符串
    val strings:Array[String] = randomString.split("")
    val randomnamefile=strings.mkString("/")
    val mynamefile = "lian/datamining/"+statename+"/"
    val outfilename=mynamefile+randomnamefile

    return outfilename

  }

  def main(args: Array[String]): Unit = {
    val namefile=getSubPath("input")
    println("*********************"+namefile)
  }
}
