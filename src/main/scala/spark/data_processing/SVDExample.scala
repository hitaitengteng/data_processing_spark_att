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

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.SparkSession
// $example on$
import org.apache.spark.mllib.linalg.Matrix
import org.apache.spark.mllib.linalg.SingularValueDecomposition
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.RowMatrix
// $example off$

/**
 * Example for SingularValueDecomposition.
 */
object SVDExample {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("SVDExample").setMaster("local")
    val sc = SparkContext.getOrCreate(sparkConf)
    val spark = SparkSession
      .builder
      .getOrCreate()
    import spark.implicits._


    // $example on$
    val dataset = spark.read.json("D:\\spark_data\\pca_data.json").toDF()
    val name_columns=dataset.columns

    val assembler = new VectorAssembler()
      .setInputCols(name_columns)
      .setOutputCol("features")

    //数据过程
    val row = assembler.transform(dataset).select("features")
    val row_tostring= row.rdd.map(_.toString())
    val rows=row_tostring.map(_.replaceAll("\\]*","")).map(_.replaceAll("\\[*","")).map(line =>
    { val vector= Vectors.dense(line.split(",").filter(p => p.matches("\\d*(\\.?)\\d*")).map(_.toDouble))
      vector
    })
   val size=rows.first().size
   val mat: RowMatrix = new RowMatrix(rows)



    //处理过程
    val svd: SingularValueDecomposition[RowMatrix, Matrix] = mat.computeSVD(size, computeU = true)
    val U: RowMatrix = svd.U  // The U factor is a RowMatrix.
    val s: Vector = svd.s     // The singular values are stored in a local dense vector.
    val V: Matrix = svd.V     // The V factor is a local dense matrix.
    // $example off$
    val collect = U.rows
    collect.saveAsTextFile("")
    println("U factor is:")

    collect.foreach { vector => println(vector) }
    println(s"Singular values are: $s")
    println(s"V factor is:\n$V")

    sc.stop()
  }
}
