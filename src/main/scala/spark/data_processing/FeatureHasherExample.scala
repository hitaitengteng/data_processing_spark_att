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

package spark.data_processing

// $example on$
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.feature.FeatureHasher
// $example off$
import org.apache.spark.sql.SparkSession

object FeatureHasherExample {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("FeatureHasherExample").setMaster("local")
    val sc = SparkContext.getOrCreate(sparkConf)
    val spark = SparkSession
      .builder
      .getOrCreate()

    // $example on$
//    val dataset = spark.createDataFrame(Seq(
//      (2.2, true, "1", "foo"),
//      (3.3, false, "2", "bar"),
//      (4.4, false, "3", "baz"),
//      (5.5, false, "4", "foo")
//    )).toDF("real", "bool", "stringNum", "string")

    val dataset=spark.read.json("D:\\spark_data\\pca_data.json")

    val clums=dataset.columns
    val hasher = new FeatureHasher()
      .setInputCols(clums)
      .setOutputCol("features")
dataset.printSchema()
    val featurized = hasher.transform(dataset)
    featurized.show(false)
    // $example off$
featurized.printSchema()
    spark.stop()
  }
}
