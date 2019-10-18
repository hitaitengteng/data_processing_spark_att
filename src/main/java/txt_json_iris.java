/**
 * @Author: Aitengteng
 * @Description:
 * @Date: Create in 11:38 2019/10/18
 */

//规定bean后抽取每行转为json

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
//fastjson
import com.alibaba.fastjson.JSON;

public class txt_json_iris {

    public static void main(String[] args) throws IOException {
        InputStreamReader ins = new InputStreamReader(new FileInputStream("D:\\spark_data\\iris.data.txt"));
        BufferedReader br = new BufferedReader(ins);
        //存放bean对象
        List<Iris_Bean> tlist = new ArrayList<Iris_Bean>();

        //读取txt
        String line = null;
        List<String> list = new ArrayList<String>();
        while((line = br.readLine()) != null) {
            list.add(line);
        }
        br.close();

        //txt的每一行相当于一条数据，split按特定分隔符进行拆分。\\s+是正则表达式表示空格。
        for (String str : list) {
            String[] arrStr = str.split(",");

            Iris_Bean classA = new Iris_Bean();
            classA.setFeature1(Double.parseDouble(arrStr[0]));
            classA.setFeature2(Double.parseDouble(arrStr[1]));
            classA.setFeature3(Double.parseDouble(arrStr[2]));
            classA.setFeature4(Double.parseDouble(arrStr[3]));
            classA.setLabel(arrStr[4]);
            tlist.add(classA);
        }
        //JSON.toJSONString()方法：将对象数组（JSON格式的字符串也可以）转换成JSON数据。
        String json = JSON.toJSONString(tlist);
        System.out.println(json);

        //创建新文件
        File txtToJson = new File("D:\\json2.json");
        txtToJson.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(txtToJson));
        out.write(json);
        out.flush(); // 把缓存区内容压入文件
        out.close(); // 最后记得关闭文件

    }


}
