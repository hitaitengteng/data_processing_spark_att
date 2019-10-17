package data_processing;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;


public class BaseProcessor implements Serializable {

    protected static SparkContext doExecutePre(String taskName) {
        SparkConf sparkConf = new SparkConf()
                .setAppName(taskName).setMaster("local[2]");
        SparkContext sc = SparkContext.getOrCreate(sparkConf);
        System.out.println("qsdz_infos : " + sc.applicationId() + ";" + sc.uiWebUrl().get());
        SparkSession spark = SparkSession
                .builder()
                .appName("JavaKMeansExample")
                .getOrCreate();
        return sc;
    }
}
