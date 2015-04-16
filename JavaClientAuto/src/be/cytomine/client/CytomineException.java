package be.cytomine.client;
/*
Copyright 2010-2013 University of Liège, Belgium.

This software is provided 'as-is', without any express or implied warranty.
In no event will the authors be held liable for any damages arising from the use of this software.

Permission is only granted to use this software for non-commercial purposes.
*/
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 */
public class CytomineException extends Exception {

    private static final Logger log = Logger.getLogger(CytomineException.class);

    public int code;
    public String message = "";

    public CytomineException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CytomineException(int code, JSONObject json) {
        this.code = code;
        getMessage(json);
    }


    private String getMessage(JSONObject json) {
        try {
            String msg = "";
            if (json != null) {
                Iterator iter = json.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    msg = msg + entry.getValue();
                }
            }
            message = msg;
        } catch (Exception e) {
            log.error(e);
        }
        return message;
    }

    public String toString() {
        return code + " " + message;
    }
}
