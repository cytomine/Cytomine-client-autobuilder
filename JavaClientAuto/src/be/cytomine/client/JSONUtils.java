package be.cytomine.client;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lrollus on 4/6/14.
 */
public class JSONUtils {

    public static Object formatJSON(Object data) throws Exception {
        if(data!=null && data instanceof Date) {
            long value = ((Date) data).getTime();
            return value + "";
        }
        return data;
    }



    public static String extractJSONString(Object json) throws Exception {
        if(json!=null) {
            return json.toString();
        } else {
            return null;
        }
    }

    public static Long extractJSONLong(Object json) throws Exception {
        if(json!=null) {
            return Long.parseLong(json.toString());
        } else {
            return null;
        }
    }

    public static Integer extractJSONInteger(Object json) throws Exception {
        if(json!=null) {
            return Integer.parseInt(json.toString());
        } else {
            return null;
        }
    }

    public static Map extractJSONMap(Object json) throws Exception {
        if(json!=null) {
            return (Map)json;
        } else {
            return null;
        }
    }

    public static Double extractJSONDouble(Object json) throws Exception {
        if(json!=null) {
            return Double.parseDouble(json.toString());
        } else {
            return null;
        }
    }

    public static Date extractJSONDate(Object json) throws Exception {
        if(json!=null) {
            return new Date(extractJSONLong(json));
        } else {
            return null;
        }
    }

    public static Boolean extractJSONBoolean(Object json) throws Exception {
        if(json!=null) {
            return Boolean.parseBoolean(json.toString());
        } else {
            return false;
        }

    }

    public static List extractJSONList(Object json) throws Exception {
        if(json!=null) {
            if(json instanceof List) {
                return (List)json;
            }
            throw new Exception(json + " cannot be converted to List!");
        }
        return null;
    }

    public static Object extractJSONObject(Object json) throws Exception {
        return json;
    }
}
