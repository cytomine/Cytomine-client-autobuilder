package be.cytomine.client;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/*
Copyright 2010-2013 University of Liège, Belgium.

This software is provided 'as-is', without any express or implied warranty.
In no event will the authors be held liable for any damages arising from the use of this software.

Permission is only granted to use this software for non-commercial purposes.
*/

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.apache.http.entity.mime.MultipartEntity;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 */
public class HttpClient {

    private static final Logger log = Logger.getLogger(HttpClient.class);

    //static Logger log = Logger.getLogger(HttpClient.class);

    DefaultHttpClient client;
    HttpHost targetHost;
    BasicHttpContext localcontext;
    java.net.URL URL;
    HttpResponse response;
    Header[] headersArray;

    boolean isAuthByPrivateKey;

    String publicKey;
    String privateKey;
    String host;

    public HttpClient() {
    }

    public HttpClient(String publicKey, String privateKey, String host) {
        this.publicKey = publicKey; this.privateKey = privateKey; this.host = host;
    }

    public void printCookies() {
        System.out.println("############# PRINT COOKIE #############");
        List<Cookie> cookies = client.getCookieStore().getCookies();
        System.out.println(cookies);
        if(cookies != null)
        {
            for(Cookie cookie : cookies)
            {
                String cookieString = cookie.getName() + "=" + cookie.getValue() + "; domain=" + cookie.getDomain();
                System.out.println(cookieString);
            }
        }
    }

    public List<Cookie> getCookies() {
        List<Cookie> cookies = client.getCookieStore().getCookies();
        return cookies;
    }


    public void addHeader(String name, String value) {
        Header[] headers;
        if (headersArray!=null)
            headers = new Header[headersArray.length + 1];
        else
            headers = new Header[1];

        for (int i = 0; i < headers.length - 1; i++) {
            headers[i] = headersArray[i];
        }
        headers[headers.length - 1] = new BasicHeader(name, value);
        headersArray = headers;
    }

    public void connect(String url, String username, String password) throws Exception {
        isAuthByPrivateKey = false;
        log.info("Connection to " + url + " with login=" + username + " and pass=" + password);
        URL = new URL(url);
        targetHost = new HttpHost(URL.getHost(), URL.getPort());
        client = new DefaultHttpClient();
        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local
        // auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        // Add AuthCache to the execution context
        localcontext = new BasicHttpContext();
        localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);
        // Set credentials
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
        client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
    }

    public void connect(String url) throws Exception {
        log.info("Connection to " + url);
        isAuthByPrivateKey = true;
        URL = new URL(url);
        targetHost = new HttpHost(URL.getHost(), URL.getPort());
        client = new DefaultHttpClient();
        localcontext = new BasicHttpContext();

    }

    public int get() throws Exception {
        HttpGet httpGet = new HttpGet(URL.toString());
        if (isAuthByPrivateKey) httpGet.setHeaders(headersArray);
        response = client.execute(targetHost, httpGet, localcontext);
        return response.getStatusLine().getStatusCode();
    }

    public int postCookie(String data) throws Exception {
        HttpPost httpPost = new HttpPost(URL.toString());
        System.out.println("change user agent");
        HttpContext HTTP_CONTEXT = new BasicHttpContext();
        client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2.13) Gecko/20101206 Ubuntu/10.10 (maverick) Firefox/3.6.13");

        HTTP_CONTEXT.setAttribute(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2.13) Gecko/20101206 Ubuntu/10.10 (maverick) Firefox/3.6.13");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2.13) Gecko/20101206 Ubuntu/10.10 (maverick) Firefox/3.6.13");

        if (isAuthByPrivateKey) httpPost.setHeaders(headersArray);
//        httpPost.addHeader("Content-Type","application/json")
//        httpPost.addHeader("host",this.host)
        log.debug("Post send :" + data.replace("\n", ""));
        //write data
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(data.getBytes()));
        entity.setContentLength((long)data.getBytes().length);
        httpPost.setEntity(entity);
        response = client.execute(targetHost, httpPost, localcontext);
        return response.getStatusLine().getStatusCode();
    }

//    public int post(String data) throws Exception {
//        log.debug("Post " + URL.getPath());
//        HttpPost httpPost = new HttpPost(URL.toString());
//        System.out.println("change user agent");
//               HttpContext HTTP_CONTEXT = new BasicHttpContext();
//               client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2.13) Gecko/20101206 Ubuntu/10.10 (maverick) Firefox/3.6.13");
//
//               HTTP_CONTEXT.setAttribute(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2.13) Gecko/20101206 Ubuntu/10.10 (maverick) Firefox/3.6.13");
//               httpPost.setHeader("User-Agent", "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2.13) Gecko/20101206 Ubuntu/10.10 (maverick) Firefox/3.6.13");
//
//        if (isAuthByPrivateKey) httpPost.setHeaders(headersArray);
////        httpPost.addHeader("Content-Type","application/json")
////        httpPost.addHeader("host",this.host)
//        log.debug("Post send :" + data.replace("\n", ""));
//        //write data
//        BasicHttpEntity entity = new BasicHttpEntity();
//        entity.setContent(new ByteArrayInputStream(data.getBytes()));
//        entity.setContentLength((long)data.getBytes().length);
//        httpPost.setEntity(entity);
//        response = client.execute(targetHost, httpPost, localcontext);
//        return response.getStatusLine().getStatusCode();
//    }




    public int get(String url, String dest) throws Exception {
        log.debug("get:" + url);
        URL URL = new URL(url);
        HttpHost targetHost = new HttpHost(URL.getHost(), URL.getPort());
        log.debug("targetHost:" + targetHost);
        DefaultHttpClient client = new DefaultHttpClient();
        log.debug("client:" + client);
        // Add AuthCache to the execution context
        BasicHttpContext localcontext = new BasicHttpContext();
        log.debug("localcontext:" + localcontext);

        headersArray = null;
        authorize("GET", URL.toString(), "", "application/json,*/*");

        HttpGet httpGet = new HttpGet(URL.getPath());
        httpGet.setHeaders(headersArray);

        HttpResponse response = client.execute(targetHost, httpGet, localcontext);
        int code = response.getStatusLine().getStatusCode();
        log.info("url=" + url + " is " + code + "(OK=" + HttpURLConnection.HTTP_OK + ",MOVED=" + HttpURLConnection.HTTP_MOVED_TEMP + ")");

        boolean isOK = (code == HttpURLConnection.HTTP_OK);
        boolean isFound = (code == HttpURLConnection.HTTP_MOVED_TEMP);
        boolean isErrorServer = (code == HttpURLConnection.HTTP_INTERNAL_ERROR);

        if (!isOK && !isFound & !isErrorServer) throw new IOException(url + " cannot be read: " + code);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            entity.writeTo(new FileOutputStream(dest));
        }
        return code;
    }

    public int delete() throws Exception {
        log.debug("Delete " + URL.getPath());
        HttpDelete httpDelete = new HttpDelete(URL.toString());
        if (isAuthByPrivateKey) httpDelete.setHeaders(headersArray);
        response = client.execute(targetHost, httpDelete, localcontext);
        return response.getStatusLine().getStatusCode();
    }

    public int post(String data) throws Exception {
        log.debug("Post " + URL.getPath());
        HttpPost httpPost = new HttpPost(URL.toString());
        if (isAuthByPrivateKey) httpPost.setHeaders(headersArray);
//        httpPost.addHeader("Content-Type","application/json")
//        httpPost.addHeader("host",this.host)
        log.debug("Post send :" + data.replace("\n", ""));
        //write data
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(data.getBytes()));
        entity.setContentLength((long)data.getBytes().length);
        httpPost.setEntity(entity);
        response = client.execute(targetHost, httpPost, localcontext);
        return response.getStatusLine().getStatusCode();
    }


    public int put(String data) throws Exception {
        log.debug("Put " + URL.getPath());
        HttpPut httpPut = new HttpPut(URL.toString());
        if (isAuthByPrivateKey) httpPut.setHeaders(headersArray);
        log.debug("Put send :" + data.replace("\n", ""));
        //write data
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(data.getBytes()));
        entity.setContentLength((long)data.getBytes().length);
        httpPut.setEntity(entity);
        response = client.execute(targetHost, httpPut, localcontext);
        return response.getStatusLine().getStatusCode();
    }

    public int post(byte[] data) throws Exception {
        log.debug("POST " + URL.toString());
        HttpPost httpPost = new HttpPost(URL.toString());
        if (isAuthByPrivateKey) httpPost.setHeaders(headersArray);
        log.debug("Post send :" + data.length);

        InputStreamEntity reqEntity = new InputStreamEntity(new ByteArrayInputStream(data), data.length);
        reqEntity.setContentType("binary/octet-stream");
        reqEntity.setChunked(false);
        BufferedHttpEntity myEntity = null;
        try {
            myEntity = new BufferedHttpEntity(reqEntity);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        httpPost.setEntity(myEntity);
        response = client.execute(targetHost, httpPost, localcontext);
        return response.getStatusLine().getStatusCode();
    }


//
//    MultipartEntity entity = new MultipartEntity();
//    entity.addPart("file", new FileBody(file));
//    post.setEntity(entity);

    public int post(MultipartEntity entity) throws Exception {
        log.debug("POST " + URL.toString());
        HttpPost httpPost = new HttpPost(URL.toString());
        if (isAuthByPrivateKey) httpPost.setHeaders(headersArray);
        log.debug("Post send :" + entity);
        httpPost.setEntity(entity);
        response = client.execute(targetHost, httpPost, localcontext);
        return response.getStatusLine().getStatusCode();

    }



    public int put(byte[] data) throws Exception {
        log.debug("Put " + URL.getPath());
        HttpPut httpPut = new HttpPut(URL.getPath());
        if (isAuthByPrivateKey) httpPut.setHeaders(headersArray);
        log.debug("Put send :" + data.length);

        InputStreamEntity reqEntity = new InputStreamEntity(new ByteArrayInputStream(data), data.length);
        reqEntity.setContentType("binary/octet-stream");
        reqEntity.setChunked(false);

        BufferedHttpEntity myEntity = null;
        try {
            myEntity = new BufferedHttpEntity(reqEntity);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        httpPut.setEntity(myEntity);
        response = client.execute(targetHost, httpPut, localcontext);
        return response.getStatusLine().getStatusCode();
    }

    public int put2(byte[] data) throws Exception {
        log.debug("Put " + URL.getPath());
        HttpPut httpPut = new HttpPut(URL.getPath());
        if (isAuthByPrivateKey) httpPut.setHeaders(headersArray);
        log.debug("Put send :" + data.length);

//        InputStreamEntity reqEntity = new InputStreamEntity(new ByteArrayInputStream(data), data.length);
//        reqEntity.setContentType("binary/octet-stream");
//        reqEntity.setChunked(false);
//
//        BufferedHttpEntity myEntity = null;
//                   try {
//                       myEntity = new BufferedHttpEntity(reqEntity);
//                   } catch (IOException e) {
//                       // TODO Auto-generated catch block
//                       e.printStackTrace();
//                   }
        MultipartEntity myEntity = new MultipartEntity();
        myEntity.addPart("files[]",new ByteArrayBody(data,"toto")) ;
        //int code = client.post(entity);


        httpPut.setEntity(myEntity);
        response = client.execute(targetHost, httpPut, localcontext);
        return response.getStatusLine().getStatusCode();
    }

//    public int post(MultipartEntity entity) throws Exception {
//        log.debug("POST " + URL.toString());
//        HttpPost httpPost = new HttpPost(URL.toString());
//        if (isAuthByPrivateKey) httpPost.setHeaders(headersArray);
//        log.debug("Post send :" + entity);
//        httpPost.setEntity(entity);
//        response = client.execute(targetHost, httpPost, localcontext);
//        return response.getStatusLine().getStatusCode();
//
//    }


    public int download(String dest) throws Exception {
        log.debug("Download " + URL.getPath());
        HttpGet httpGet = new HttpGet(URL.getPath());
        if (isAuthByPrivateKey) httpGet.setHeaders(headersArray);
        response = client.execute(targetHost, httpGet, localcontext);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            FileOutputStream fos = new FileOutputStream(dest);
            entity.writeTo(fos);
            fos.close();
        }
        return response.getStatusLine().getStatusCode();
    }

    public BufferedImage readBufferedImageFromURL(String url) throws Exception {
        log.debug("readBufferedImageFromURL:" + url);
        URL URL = new URL(url);
        HttpHost targetHost = new HttpHost(URL.getHost(),URL.getPort());
        log.debug("targetHost:" + targetHost);
        DefaultHttpClient client = new DefaultHttpClient();
        log.debug("client:" + client);
        // Add AuthCache to the execution context
        BasicHttpContext localcontext = new BasicHttpContext();
        log.debug("localcontext:" + localcontext);

        headersArray = null;
        authorize("GET", URL.toString(), "", "application/json,*/*");

        BufferedImage img = null;
        HttpGet httpGet = new HttpGet(URL.getPath());
        httpGet.setHeaders(headersArray);
        HttpResponse response = client.execute(targetHost, httpGet, localcontext);
        int code = response.getStatusLine().getStatusCode();
        log.info("url=" + url + " is " + code + "(OK=" + HttpURLConnection.HTTP_OK + ",MOVED=" + HttpURLConnection.HTTP_MOVED_TEMP + ")");

        boolean isOK = (code == HttpURLConnection.HTTP_OK);
        boolean isFound = (code == HttpURLConnection.HTTP_MOVED_TEMP);
        boolean isErrorServer = (code == HttpURLConnection.HTTP_INTERNAL_ERROR);

        if (!isOK && !isFound & !isErrorServer) throw new IOException(url + " cannot be read: " + code);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            img = ImageIO.read(entity.getContent());
            //img = ImageIO.read(new ByteArrayInputStream(entity.getContent().bytes));
        }
        return img;

    }

    static public BufferedImage readBufferedImageFromURLWithoutKey(String url, String loginHTTP, String passHTTP) throws MalformedURLException, IOException {

        URL URL = new URL(url);
        HttpHost targetHost = new HttpHost(URL.getHost(), URL.getPort());
        DefaultHttpClient client = new DefaultHttpClient();
        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local
        // auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        // Add AuthCache to the execution context
        BasicHttpContext localcontext = new BasicHttpContext();
        localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);
        // Set credentials
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(loginHTTP, passHTTP);
        client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);

        BufferedImage img = null;
        HttpGet httpGet = new HttpGet(URL.getPath());
        HttpResponse response = client.execute(targetHost, httpGet, localcontext);
        int code = response.getStatusLine().getStatusCode();
        log.info("url=" + url + " is " + code + "(OK=" + HttpURLConnection.HTTP_OK + ",MOVED=" + HttpURLConnection.HTTP_MOVED_TEMP + ")");

        boolean isOK = (code == HttpURLConnection.HTTP_OK);
        boolean isFound = (code == HttpURLConnection.HTTP_MOVED_TEMP);
        boolean isErrorServer = (code == HttpURLConnection.HTTP_INTERNAL_ERROR);

        if (!isOK && !isFound & !isErrorServer) throw new IOException(url + " cannot be read: " + code);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            img = ImageIO.read(entity.getContent());
            entity.getContent().close();
        }
        return img;

    }

    public void authorize(String action, String url, String contentType, String accept)  throws Exception {
        url = url.replace(host, "");
        url = url.replace("http://" + host, "");
        url = url.replace("https://" + host, "");

        TreeMap<String,String> headers = new TreeMap<String, String>();
        headers.put("accept",accept);
        headers.put("date",getActualDateStr());

        log.debug("AUTHORIZE: " + action + "\\n\\n" + contentType + "\\n" + headers.get("date") + "\n");

        String canonicalHeaders = action + "\n\n" + contentType + "\n" + headers.get("date") + "\n";

        String messageToSign = canonicalHeaders + url;

        SecretKeySpec privateKeySign = new SecretKeySpec(privateKey.getBytes(), "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");

        mac.init(privateKeySign);
        byte[] rawHmac = mac.doFinal(new String(messageToSign.getBytes(), "UTF-8").getBytes());

        byte[] signatureBytes = Base64.encodeBase64(rawHmac);

        String signature = new String(signatureBytes);

        String authorization = "CYTOMINE " + publicKey + ":" + signature;

        headers.put("authorization",authorization);

        for(String key : headers.keySet()) {
            addHeader(key, headers.get(key));
        }
    }

    public String getActualDateStr()  throws Exception {
        Date today = Calendar.getInstance().getTime();
        Strftime formatter = new Strftime("%a, %d %b %Y %H:%M:%S +0000", Locale.ENGLISH);
        return formatter.format(today);
    }


    public String getResponseData()  throws Exception {
        HttpEntity entityResponse = response.getEntity();
        return IOUtils.toString(entityResponse.getContent());
    }

    public int getResponseCode() throws Exception  {
        return response.getStatusLine().getStatusCode();
    }

    public void disconnect()  throws Exception{
        log.debug("Disconnect");
        try {
            client.getConnectionManager().shutdown();
        } catch (Exception e) {
            log.error(e);
        }
    }

    public static void writeToFile(InputStream is, File file)  throws Exception{
        try {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            InputStream is2 = is;
            boolean again = true;
            while (again) {
                if (is2.read() > -1) {
                    out.writeByte(is.read());
                }
                else again = false;
            }
            is.close();
            out.close();
        }
        catch (IOException e) {
            log.error("Error Writing/Reading Streams:" + e);
        }
    }
}
