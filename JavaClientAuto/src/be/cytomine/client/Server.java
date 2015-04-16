
package be.cytomine.client;

import be.cytomine.client.abst.AbstractServer;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


/**
 * 
 * 
 * @author ClientBuilder (Loïc Rollus)
 * @version 0.1
 * 
 * YOU CAN EDIT THIS FILE (DO NO EDIT THE ABSTRACT PARENT CLASS)
 * 
 */
public class Server
    extends AbstractServer
{

    private static final Logger log = Logger.getLogger(Server.class);

    static String cytomineProdPATH = "http://localhost:8090";
    static String publickey = "61a338c5-20b5-43f7-8578-f0c9b16da9de";
    static String privatekey = "e5cc752e-f193-4ae4-b582-2c98447676b1";


//    private <T extends Model> T saveModel(T model) throws Exception {
//        int code = client.post(model.toJSON());
//        String response = client.getResponseData();
//        client.disconnect();
//        JSONObject json = createJSONResponse(code, response);
//        analyzeCode(code, json);
//        model.setAttr((JSONObject) json.get(model.getDomainName()));
//        return model;
//    }


//    private <T extends Model> T updateModel(T model) throws Exception {
//        HttpClient client = null;
//        if (!isBasicAuth) {
//            String prefixUrl = model.toURL().split("\\?")[0];
//            client = new HttpClient(publicKey, privateKey, host);
//            client.authorize("PUT", prefixUrl, "", "application/json,*/*");
//            client.connect(host + model.toURL());
//        } else {
//            client = new HttpClient();
//            client.connect(host + model.toURL(), login, pass);
//        }
//        int code = client.put(model.toJSON());
//        String response = client.getResponseData();
//        client.disconnect();
//        JSONObject json = createJSONResponse(code, response);
//        analyzeCode(code, json);
//        model.setAttr((JSONObject) json.get(model.getDomainName()));
//        return model;
//    }

//    private void deleteModel(Model model) throws Exception {
//        HttpClient client = null;
//        if (!isBasicAuth) {
//            client = new HttpClient(publicKey, privateKey, host);
//            client.authorize("DELETE", model.toURL(), "", "application/json,*/*");
//            client.connect(host + model.toURL());
//        } else {
//            client = new HttpClient();
//            client.connect(host + model.toURL(), login, pass);
//        }
//        int code = client.delete();
//        String response = client.getResponseData();
//        client.disconnect();
//        JSONObject json = createJSONResponse(code, response);
//        analyzeCode(code, json);
//    }

    public void doDELETE(String path) throws Exception {
        HttpClient client = new HttpClient(publickey, privatekey, cytomineProdPATH);
        client.authorize("DELETE", path, "", "application/json,*/*");
        System.out.println(cytomineProdPATH + path);
        client.connect(cytomineProdPATH + path);
        int code = client.delete();
        String response = client.getResponseData();
        client.disconnect();
        JSONObject jsonResponse = createJSONResponse(code, response);
        System.out.println(jsonResponse);
        analyzeCode(code, jsonResponse);
    }

    public JSONObject doPUT(String path, JSONObject json) throws Exception {
        HttpClient client = new HttpClient(publickey, privatekey, cytomineProdPATH);
        client.authorize("PUT", path, "", "application/json,*/*");
        client.connect(cytomineProdPATH + path);
        int code = client.put(json.toString());
        String response = client.getResponseData();
        client.disconnect();
        JSONObject jsonResponse = createJSONResponse(code, response);
        analyzeCode(code, jsonResponse);
        return jsonResponse;
    }

    public JSONObject doPOST(String path, JSONObject json) throws Exception {
        HttpClient client = new HttpClient(publickey, privatekey, cytomineProdPATH);
        client.authorize("POST", path, "", "application/json,*/*");
        client.connect(cytomineProdPATH + path);
        int code = client.post(json.toString());
        String response = client.getResponseData();
        client.disconnect();
        JSONObject jsonResponse = createJSONResponse(code, response);
        analyzeCode(code, jsonResponse);
        return jsonResponse;
    }


    public JSONObject doGET(String path) throws Exception {
        HttpClient client = new HttpClient(publickey, privatekey, cytomineProdPATH);
        client.authorize("GET", path, "", "application/json,*/*");
        client.connect(cytomineProdPATH + path);
        int code = client.get();
        String response = client.getResponseData();
        client.disconnect();
        JSONObject json = createJSONResponse(code, response);
        analyzeCode(code, json);
        return json;
    }

    public JSONArray doGETList(String path) throws Exception {
        JSONObject json = doGET(path);
        return (JSONArray)json.get("collection");
    }


    private JSONObject createJSONResponse(int code, String response) throws Exception {
        try {
            Object obj = JSONValue.parse(response);
            JSONObject json = (JSONObject) obj;
            return json;
        } catch (Exception e) {
            log.error(e);
            throw new CytomineException(code, response);
        } catch (Error ex) {
            log.error(ex);
            throw new CytomineException(code, response);
        }
    }

    private JSONArray createJSONArrayResponse(int code, String response) throws Exception {
        try {
            Object obj = JSONValue.parse(response);
            JSONArray json = (JSONArray) obj;
            return json;
        } catch (Exception e) {
            throw new CytomineException(code, response);
        }
    }

    private void analyzeCode(int code, JSONObject json) throws Exception {
        if (code == 200 || code == 201 || code == 304) return;
        if (code == 400) throw new CytomineException(code, json);
        if (code == 401) throw new CytomineException(code, json);
        if (code == 404) throw new CytomineException(code, json);
        if (code == 500) throw new CytomineException(code, json);
        if (code == 302) throw new CytomineException(code, json);
    }

}