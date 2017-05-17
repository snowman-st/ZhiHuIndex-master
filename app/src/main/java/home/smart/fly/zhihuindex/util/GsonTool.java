package home.smart.fly.zhihuindex.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by zl on 2017/5/16.
 */

public class GsonTool {
    /**TODO 转换为json字符串
     * @param src  要转换成json格式的 对象
     * @return
     */
    public static String createJsonString(Object src) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(src);
        return jsonString;
    }


    /**TODO 转换为指定的 对象
     * @param jsonString
     * @param type 指定对象的类型 ，即 T.class
     * @return
     */
    public static <Problem> Problem getObject(String jsonString, Class<Problem> type) {
        Problem problem = null;
        try {
            Gson gson = new Gson();
            problem = gson.fromJson(jsonString, type);
        } catch (JsonSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return problem;
    }
}
