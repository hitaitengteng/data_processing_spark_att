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

    val frame = spark.read.format("json").load("C:\\Users\\user\\AppData\\Local\\Google\\Chrome\\User Data\\CertificateRevocation\\5443\\manifest.json")


    // 因为以下两个方法是将集群中的目标变量的所有数据取回到一个结点当中，所以当你的单台结点的内存不足以放下DataFrame
    //中包含的数据时就会出错因此，collec()、 collectAsList()不适用于特别大规模的数据集
    //collect()：返回一个数组array[row]，包含DataFrame中全部数据记录
    val frame_array=frame.collect()
    //collectAsList():返回一个java list:util.list[row]，包含DataFrame中全部数据记录
    val frame_javalist=frame.collectAsList()

    //todf，不带参数的话返回本身，带参数的话返回重命名的列名
    val frame_rename=frame.toDF("a","b","c")
    val frame_2=frame.toDF()
    //  col：获取指定字段
    //    　　只能获取一个字段，返回对象为Column类型。
    //  apply：获取指定字段
    //    　　只能获取一个字段，返回对象为Column类型
    //    select：获取指定字段值
    //    根据传入的String类型字段名，获取指定字段的值，以DataFrame类型返回
    val frame_column=frame.col("name")
    val frame_column_2=frame("name")
    val frame_column_3=frame.apply("name")
    val frame_columns=frame.select("name","version")
//　还有一个重载的select方法，不是传入String类型参数，而是传入Column类型参数。可以实现select id, id+1 from test这种逻辑,
    val frame_selcet=frame.select(frame( "id" ), frame( "id") + 1 )
//selectExpr：可以对指定字段进行特殊处理
//　　可以直接对指定字段调用UDF函数，或者指定别名等。传入String类型参数，得到DataFrame对象。
//　　示例，查询id字段，c3字段取别名time，c4字段四舍五
val frame_selcet_2=frame.selectExpr("name","version as time","round(manifest_version)")
//drop：去除指定字段，保留其他字段
    //　　返回一个新的DataFrame对象，其中不包含去除的字段
    val frame_drop_2=frame.drop("name","version")

    //??????????如何使用map
    //遍历操作,对所有数据进行遍历处理，处理函数写在括号内
    val frame_foreach=frame.foreach(x=>println(x))
    //    val frame_map=frame.map(x=>x)

    //sample取样
    //sample(withReplacement:Boolean,fraction:Double,seed:Long)
    //withReplacement=true表示重复抽样，fraction表示比例，seed指定因子抽样
    val frame_sample=frame.sample(false,0.5,12456)



    //describe：描述性统计：计数，平均值，标准差，max,min.返回sql.dataframe
    val frame_describe=frame.describe("name")
    //count：返回DataFrame的记录条数,long
    val frame_count=frame.count()
    //columns,返回array[string],返回列名
    val frame_cloumns=frame.columns
    //dtypes，返回array[string，string],返回列名和对应类型
    val frmae_dtypes=frame.dtypes
    //schema,返回数据结构
    val frame_schema=frame.schema



    //withColumnRenamed：重命名DataFrame中的指定字段名
    //　　如果指定的字段名不存在，不进行任何操作。下面示例中将jdbcDF中的id字段重命名为idx。
    //jdbcDF.withColumnRenamed( "id" , "idx" )


//    withColumn：往当前DataFrame中新增一列
//    　　whtiColumn(colName: String , col: Column)方法根据指定colName往DataFrame中新增一列，如果colName已存在，则会覆盖当前列。
//    　　以下代码往jdbcDF中新增一个名为id2的列，

    frame.withColumn("id2", frame("id")).show( false)


    //take函数的行为类似于数组。它接收一个整数值(比方说，n)作为参数，并返回数据集的前n个元素的数组,array[row]
//    first, head, take, takeAsList：获取若干行记录,以Row或者Array[Row]的形式返回一行或多行数据。first和head功能相同
//    take和takeAsList方法会将获得到的数据返回到Driver端，所以，使用这两个方法时需要注意数据量，以免Driver发生OutOfMemoryError
//    这里列出的四个方法比较类似，其中
//    （1）first获取第一行记录
//    （2）head获取第一行记录，head(n: Int)获取前n行记录
//    （3）take(n: Int)获取前n行数据
//    （4）takeAsList(n: Int)获取前n行数据，并以List的形式展现
    val frame_first=frame.first
    val frame_head=frame.head(10)
    val frame_take=frame.take(1)
    val frame_takeaslist=frame.takeAsList(1)



    //cache   将DataFrame缓存到内存中
    //persist（）、unpersist（）  ：将DataFrame以指定等级持久化内存和磁盘中
    frame.cache()
    frame.persist()
    frame.unpersist()



    //registerTempTable：将DataFrame注册为临时的表，注册成表后可用sql方法查询
    //    全局临时表(global temporary view)于临时表(temporay view)是相对的，全局临时表的作用范围是某个Spark应用程序内所有
    //    会话(SparkSession)，它会持续存在，在所有会话中共享，直到该Spark应用程序终止
    //    因此，若在同一个应用中不同的session中需重用一个临时表， 不妨将其注册为全局临时表，可避免多余IO,提高系统执行
    //    效率，当然如果某个临时表只在整个应用中的某个session中需使用，仅需注册为局部临时表，避免不必要的在内存中存储
    //    全局临时表
    //    另外，全局临时表与系统保留的数据库global temp相关联，引用时需用global temp标识
    frame.registerTempTable("frame_table")
    val sqltest=context.sql("select name from frame_table where version>500")
    frame.createGlobalTempView("table1")
    frame.createOrReplaceTempView("table2")
    frame.createTempView("table3")
    frame.createOrReplaceGlobalTempView("table4")




    //？？？？？？？？？？？？agg,聚合操作
    //　聚合操作调用的是agg方法，该方法有多种调用方式。一般与groupBy方法配合使用。
    //　　以下示例其中最简单直观的一种用法，对id字段求最大值，对c4字段求和。
    val frame_agg=frame.agg("id" -> "max", "c4" -> "sum")



    //去重
//    dinstinct：去掉重复的记录,返回当前DataFrame中不重复的Row记录
//    dropDuplicates:根据指定字段（可多个字段组合）去重
    val frame_dinstinct=frame.select("name").distinct()
    val frame_dropduplicates=frame.dropDuplicates("name")


    //过滤
    //where(conditionExpr: String)：SQL语言中where关键字后的条件传入筛选条件表达式，可以用and和or。得到DataFrame类型的返回结果，
    val frame_filter=frame.filter("name<100")
    val frame_filter_3=frame.where("name<100")




//    groupBy：使用一个或者多个列对DataFrame进行分组
    //groupBy方法有两种调用方式，可以传入String类型的字段名，也可传入Column类型的对象
    val frame_groupby=frame.groupBy("name")
    val frame_groupby_2=frame.groupBy(frame("name"))
    //排序
    //按指定字段排序。默认为升序，加个-表示降序排序。sort和orderBy使用方法相同，返回adtaset[row]
//    sortWithinPartitions
//    　　和上面的sort方法功能类似，区别在于sortWithinPartitions方法返回的是按Partition排好序的DataFrame对象
    val frame_sort=frame.sort("name")
    val frame_sort_2=frame.orderBy("name")
    val frame_sort_3=frame.sortWithinPartitions("name")

    //有时候需要根据某个字段内容进行分割，然后生成多行，这时可以使用explode方法
    //下面代码中，根据c3字段中的空格将字段内容进行分割，分割的内容存储在新的字段c3_中，如下所示
    frame.explode( "c3" , "c3_" ){time: String => time.split( " " )}


  }
}
