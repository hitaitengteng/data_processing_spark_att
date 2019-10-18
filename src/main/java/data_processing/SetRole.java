package data_processing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

//import com.google.common.base.Splitter;

public class SetRole extends BaseProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(SetRole.class);

    public static void main(String[] args) {

        if (args.length < 4) {
            LOG.error("invalid args : " + Arrays.toString(args));
            System.exit(1);
        }

        String taskName = args[0];
        String inputPath = args[1];
        String outputPath = args[2];
        //列名称
        final List<String> LabelColumn = Splitter.on(",").splitToList(args[3]);
        final List<String> FeatureColumn = Splitter.on(",").splitToList(args[4]);
        final String Featurename ="features" ;
        final String Labelname ="label" ;

        SparkContext sc = doExecutePre(taskName);
        JavaRDD<String> rawData = sc.textFile(inputPath, 10).toJavaRDD();
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
        }).saveAsTextFile(outputPath);
    }

}
