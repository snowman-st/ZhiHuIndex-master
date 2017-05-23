package home.smart.fly.zhihuindex.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import home.smart.fly.zhihuindex.Problem;


/**
 * Created by zl on 2017/5/16.
 * gson工具类
 */

public class GsonTool {
    /**TODO 转换为json字符串
     * @param src  要转换成json格式的 对象
     * @return
     */
    public static String createListJsonString(List<Problem> problemList) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(problemList);
        return jsonString;
    }

    public static String createProblemJsonString(Problem problem) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(problem);
        return jsonString;
    }

    /**TODO 转换为指定的 对象
     * @param jsonString
     * @param type 指定对象的类型 ，即 T.class
     * @return
     */
    public static List<Problem> getList(String jsonString) {
        List<Problem> problemList = new ArrayList<Problem>();
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Problem>>(){}.getType();
            JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
            jsonReader.setLenient(true);
            problemList = gson.fromJson(jsonReader, type);
        } catch (JsonSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return problemList;
    }

    public static Problem getProblem(String jsonString) {
        Problem problem =null;
        try {
            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
            jsonReader.setLenient(true);
            problem= gson.fromJson(jsonReader,Problem.class);
        } catch (JsonSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return problem;
    }
}
