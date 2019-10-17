/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// scalastyle:off println
package spark.data_processing

// $example on$
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors
// $example off$
import org.apache.spark.sql.SparkSession

object Merge_VectorAssembler {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("Merge_VectorAssembler").setMaster("local")
    val sc = SparkContext.getOrCreate(sparkConf)
    val spark = SparkSession
      .builder
      .getOrCreate()


    //支持的格式
    //        case DoubleType => dataset(c)
    //        case _: VectorUDT => dataset(c)
    //        case _: NumericType | BooleanType => dataset(c).cast(DoubleType).as(s"${c}_double_$uid")

    val dataset=spark.read.json("D:\\spark_data\\mytest_json.json").toDF("id","features")


    val assembler = new VectorAssembler()
      .setInputCols(Array("id", "features"))
      .setOutputCol("userFeatures")

    val output = assembler.transform(dataset)

    output.select("features").show(false)
    output.printSchema()
    // $example off$

    spark.stop()
  }
}
// scalastyle:on println
