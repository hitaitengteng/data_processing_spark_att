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

// 连续性数据二值化
package spark.data_processing

import org.apache.spark.ml.feature.Binarizer
// $example off$
import org.apache.spark.sql.SparkSession

object Data_Binarizer {
  def main(args: Array[String]): Unit = {

    val taskName: String = args(0)//任务名称
    val inputPath: String = args(1)//输入文件
    val outputPath: String = args(2)//结果输出文件
    val outputPath_predictions: String = args(3)//结果输出文件
    val outputPath_label: String = args(3)//结果输出文件
    val outmodelput: String = args(3)//模型输出文件


    val MaxDepth: String = args(4)//树的最大深度
    val Impurity: String = args(5)//impurity：不纯度，可选类型entropy，gini
    val MaxBins: String = args(6)//离散化连续特征的最大划分数
    val MinInfoGain: String = args(7)//一个节点分裂的最小信息增益，值为[0,1]
    val MinInstancesPerNode: String = args(8)//每个节点包含的最小样本数
    val Seed: String = args(9)//随机种子

    val sc = BaseProcessorscala.doExecutePrescla(taskName)
    val spark = SparkSession.builder().getOrCreate()


    // $example on$
    val data = Array((0, 0.1), (1, 0.8), (2, 0.2))
    val dataFrame = spark.createDataFrame(data).toDF("id", "feature")

    val binarizer: Binarizer = new Binarizer()
      .setInputCol("feature")
      .setOutputCol("binarized_feature")
      .setThreshold(0.5)

    val binarizedDataFrame = binarizer.transform(dataFrame)

    println(s"Binarizer output with Threshold = ${binarizer.getThreshold}")
    binarizedDataFrame.show()
    // $example off$

    spark.stop()
  }
}

