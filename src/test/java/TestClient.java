import data_processing.SetRoletest;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestClient {

@Test
public void testSetRoletest() {

    String inputpath = "D:\\downloads\\test.json";
    String  outputpath = "D:\\asd123";

    List<String> filterColumn = new ArrayList<>();
    filterColumn.add("b");


    List<String> filter = new ArrayList<>();
    filter.add("xh");
    filter.add("alfa_pyd");
//    filter.add("feature2");
//    filter.add("feature3");
//    filter.add("feature4");



    SetRoletest.main(inputpath, outputpath, filterColumn,filter);
    System.out.println("ok");
}


    @Test
    public void readjson() {

//        String inputpath = "C:\\Users\\user\\Desktop\\jsontest.json";
        String  inputpath = "C:\\Users\\user\\Desktop\\out23\\";

        SparkConf sparkConf = new SparkConf()
                .setAppName("yuad").setMaster("local[2]");
        SparkContext sc = SparkContext.getOrCreate(sparkConf);
        JavaRDD<String> rawData = sc.textFile(inputpath, 10).toJavaRDD();

        System.out.println(rawData);
    }


}
