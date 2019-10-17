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

// 缺失值处理，补充为平均值。
import org.apache.spark.annotation.Since
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.feature.Imputer
// $example off$
import org.apache.spark.sql.SparkSession

/**
 * An example demonstrating Imputer.
 * Run with:
 *   bin/run-example ml.ImputerExample
 */
object ImputerExample {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("ImputerExample").setMaster("local")
    val sc = SparkContext.getOrCreate(sparkConf)
    val spark = SparkSession.builder
      .getOrCreate()

    // $example on$
    val dataset = spark.read.json("D:\\spark_data\\missing_data.json").toDF()
    val name_columns=dataset.columns
    val name_columns_out=dataset.columns.map(x=>x+"_out")

    dataset.printSchema()

    val imputer = new Imputer()
      .setInputCols(name_columns)
      .setOutputCols(name_columns_out)


    val model = imputer.fit(dataset)
    model.transform(dataset).show()

    spark.stop()
  }
}
