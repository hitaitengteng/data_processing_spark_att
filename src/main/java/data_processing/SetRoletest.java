package data_processing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;



public class SetRoletest  {
    private static final Logger LOG = LoggerFactory.getLogger(SetRoletest.class);

    public static void main(String inputPath ,String outputPath, List<String> lablepame,List<String> featurepame) {
        SparkConf sparkConf = new SparkConf()
                .setAppName("yuad").setMaster("local[2]");
        SparkContext sc = SparkContext.getOrCreate(sparkConf);
        JavaRDD<String> rawData = sc.textFile(inputPath, 10).toJavaRDD();
        //列名称
        final List<String> LabelColumn = lablepame;
        final List<String> FeatureColumn = featurepame;
        final String Featurename ="features" ;
        final String Labelname ="label" ;


        rawData.map(new Function<String, String>() {
            public String call(String s) throws Exception {
                Map data = null;
                Map<String, List<String> > datanew = new IdentityHashMap<String, List<String> >();
                List<String>  listLabel = Lists.newArrayList();
                List<String>  listfeatures = Lists.newArrayList();
                try {
                    if (s.endsWith(","))
                        s = s.substring(0, s.length() - 1);
                    data = JSONObject.parseObject(s, Map.class);
                    Iterator<Map.Entry> iterator = data.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry next = iterator.next();
                        if (LabelColumn.contains(next.getKey())) {
                            listLabel.add(next.getValue().toString());
                        }
                        if (FeatureColumn.contains(next.getKey())) {
                            listfeatures.add(next.getValue().toString());
                        }
                        datanew.put(Labelname,listLabel );
                        datanew.put(Featurename,listfeatures );
                    }
                } catch (Throwable t) {
                    LOG.error("fail to parse : " + s, t);
                }

                return JSON.toJSONString(datanew);
            }
        }).repartition(1).saveAsTextFile(outputPath);
    }


}
