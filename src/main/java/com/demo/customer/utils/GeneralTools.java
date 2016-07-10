package com.demo.customer.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;

/**
 * Created by Kyle
 */
public final class GeneralTools {
    private static Logger logger = Logger.getLogger(GeneralTools.class);
    public static final int code_success = 200;
    public static final int code_notfound = 404;
    public static final int code_error = 500;

    public static final String info_done = "Done";
    public static final String info_no_customer = "No this Customer";
    public static final String info_duplicate_id = "Duplicate ID";
    public static final String info_Id_Missing = "Mssing Customer ID";
    public static final String info_customer_error = "Customer is error";




    public static <T> T jsonObjToJava(String jsonStr, Class<T> clazz){
        try {
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            T retObj = gson.fromJson(jsonStr, clazz);
            return retObj;
        }catch (Exception ex){
            logger.error("Json to Java", ex);
            return null;
        }
    }

    public static String javaToJson(Object obj, Type type){
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        String jsonStr = gson.toJson(obj, type);
        return  jsonStr;
    }
}
