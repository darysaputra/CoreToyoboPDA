package core.trst.com.coretoyobopda.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonHelper {
//    public String[] getByRowFromArray(JSONArray param, int row) throws JSONException {
//        String[] values=new String[1];
//        for(int i=0;i<param.length();i++){
//            if(i==row){
//                JSONObject obj=param.getJSONObject(i);
//                values=new String[obj.length()];
//                int y=0;
//                for(Iterator<String> keys = obj.keys(); keys.hasNext();) {
//                    values[y] = obj.getString(keys.next());
//                    y++;
//                }
//            }
//        }
//        return values;
//    }

    public JSONObject getObjectFromArray(JSONArray param, int index) throws JSONException {
        JSONObject obj = null;
        for(int i=0;i<param.length();i++) {
            if (i == index) {
                obj=param.getJSONObject(i);
            }
        }
        return obj;
    }

    public String getStringFromObject(JSONObject param,String column) throws JSONException {
        String values;
        values=param.getString(column);
        return values;
    }

    public Double getDoubleFromObject(JSONObject param,String column) throws JSONException {
        Double values;
        values=param.getDouble(column);
        return values;
    }

    public int getIntegerFromObject(JSONObject param,String column) throws JSONException {
        int values;
        values=param.getInt(column);
        return values;
    }

    public JSONObject stringToObject(String param) throws JSONException {
        JSONObject values = new JSONObject(param.toString());
        return values;
    }
}
