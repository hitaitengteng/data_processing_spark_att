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
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.stat.ChiSquareTest
// $example off$
import org.apache.spark.sql.SparkSession

/**
 * An example for Chi-square hypothesis testing.
 * Run with
 * {{{
 * bin/run-example ml.ChiSquareTestExample
 * }}}
 */
object ChiSquareTestExample {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("ChiSquareTestExample").setMaster("local")
    val sc = SparkContext.getOrCreate(sparkConf)
    val spark = SparkSession
      .builder
      .getOrCreate()
    val data =spark.read.json("d:\\spark_data\\ChiS_data.json").toDF()

//表的分割和重组
    //非常重要的增加序列化索引的函数   monotonically_increasing_id
    import org.apache.spark.sql.functions.monotonically_increasing_id

    val dataset=data.drop("label")
    val name_columns=dataset.columns
    val data_label =data.select("label").withColumn("id1", monotonically_increasing_id()+1)



    val assembler = new VectorAssembler()
      .setInputCols(name_columns)
      .setOutputCol("features")
    val df = assembler.transform(dataset).select("features").withColumn("id1", monotonically_increasing_id()+1)
    val data_jion=df.join(data_label,"id1").drop("id1")


    val chi = ChiSquareTest.test(data_jion, "features", "label").head

    println(s"pValues = ${chi.getAs[Vector](0)}")
    println(s"degreesOfFreedom ${chi.getSeq[Int](1).mkString("[", ",", "]")}")
    println(s"statistics ${chi.getAs[Vector](2)}")

    spark.stop()
  }
}
