//package data_processing;
//
//import org.apache.spark.SparkConf;
//import org.apache.spark.SparkContext;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.function.Function;
//import org.apache.spark.sql.SparkSession;
//import scala.util.parsing.json.JSONArray;
//
//import java.util.List;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import java.util.*;
//import static data_processing.BaseProcessor.doExecutePre;
//
///**
// * @Author: Aitengteng
// * @Description:
// * @Date: Create in 16:06 2019/10/9
// */
//public class json_list {
//
//    public static void main(String[] args) {
//
//        SparkContext sc = doExecutePre("json_list");
//
//        JavaRDD<String> rawData = sc.textFile("asdf",2).toJavaRDD();
//
//        rawData.map(new Function<String, String>() {
//            public String call(String s) throws Exception {
//                Map data = null;
//                try {
//                    if (s.endsWith(","))
//                        s = s.substring(0, s.length() - 1);
//                    data = JSONObject.parseObject(s, Map.class);
//                    Iterator<Map.Entry> iterator = data.entrySet().iterator();
//                    while (iterator.hasNext()) {
//                        Map.Entry next = iterator.next();
//                        if (!filterColumn.contains(next.getKey())) {
//                            iterator.remove();
//                        }
//                    }
//                } catch (Throwable t) {
//                    LOG.error("fail to parse : " + s, t);
//                }
//                return JSON.toJSONString(data);
//            }
//        });
//
//
//    }
//
//        /**
//         * List<T> 转 json 保存到数据库
//         */
//        public static <T> String listToJson(List<T> ts) {
//            String jsons = JSON.toJSONString(ts);
//            return jsons;
//        }
//
//        /**
//         * json 转 List<T>
//         */
//        public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
//            @SuppressWarnings("unchecked")
//            List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
//            return ts;
//        }
//
//
//}
