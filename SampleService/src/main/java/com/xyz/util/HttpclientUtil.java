package com.xyz.util;

import org.apache.http.HttpEntity;  
import org.apache.http.HttpHost;  
import org.apache.http.HttpResponse;  
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;  
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;  
import org.apache.http.client.methods.RequestBuilder;  
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;  
import org.apache.http.impl.client.HttpClientBuilder;  
import org.apache.http.impl.client.HttpClients;  
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;  
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.util.JSON;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;  

public class HttpclientUtil {

	private final static Logger logger = LoggerFactory.getLogger(HttpclientUtil.class);

	private static PoolingHttpClientConnectionManager connectionManager = null;  
    private static HttpClientBuilder httpBuilder = null;  
    private static RequestConfig requestConfig = null;  
    private static int MAXCONNECTION = 10;  
    private static int DEFAULTMAXCONNECTION = 5;  
    
    static {  
        requestConfig = RequestConfig.custom()  
                .setSocketTimeout(20 * 1000)  
                .setConnectTimeout(20 * 1000)  
                .setConnectionRequestTimeout(20 * 1000)  
                .build();  
    
        //HttpHost target = new HttpHost(IP, PORT);  
        connectionManager = new PoolingHttpClientConnectionManager();  
        connectionManager.setMaxTotal(MAXCONNECTION);
        connectionManager.setDefaultMaxPerRoute(DEFAULTMAXCONNECTION);
        //connectionManager.setMaxPerRoute(new HttpRoute(target), 20);  
        httpBuilder = HttpClients.custom();  
        httpBuilder.setConnectionManager(connectionManager);  
    }  
    
    public static CloseableHttpClient getConnection() {  
        CloseableHttpClient httpClient = httpBuilder.build();  
        return httpClient;  
    }  
    
    //curl -X POST -d '{"api_name": "stock_basic", "token": "5dfbbdf5953c683a061952a4a6c7eae376dc2a892ee3ce5ed4117d64", "params": {"list_stauts":"L"}, "fields": "ts_code,name,area,industry,list_date"}' http://api.tushare.pro
    public static List<JsonObject> post(String body, String url) {   
        CloseableHttpClient httpclient = HttpClients.createDefault();    
        HttpPost httppost = new HttpPost(url); 
        //httppost.setHeader("Content-Type","application/json;charset=utf-8");
        httppost.setHeader("Content-Type","application/json;charset=utf-8");
        httppost.setHeader("Accept","application/json;charset=utf-8");
        StringEntity reqEntity = new StringEntity(body, "utf-8"); 
        httppost.setEntity(reqEntity);  
        httppost.setConfig(requestConfig);  
        try {   
            //logger.info("executing request " + httppost.getURI() + " body: " + body);   
            CloseableHttpResponse response = httpclient.execute(httppost);   
            try {   
                HttpEntity entity = response.getEntity();   
                if (entity != null) {   
                	String result = EntityUtils.toString(entity);
                	JsonObject obj = new Gson().fromJson(result, JsonObject.class);
                	JsonElement data = obj.get("data");
                	if(data.isJsonNull()) {
                		logger.info("Can't get from tushare. Msg: " + obj.get("msg").getAsString());
                		if("-2002".equals(obj.get("code").getAsString())){
                			logger.info("Sleep for 60s and then retry..");
								Thread.sleep(60 * 1000);
                			return post(body, url);
                		}
                		
                		return new ArrayList<JsonObject>();
                	}
                	JsonArray fields = data.getAsJsonObject().get("fields").getAsJsonArray();
                	JsonArray items = data.getAsJsonObject().get("items").getAsJsonArray();
                	
                	if(items.size() == 0) {
                		logger.info("Query result is empty. Query Body is: " + body);
                		return new ArrayList<JsonObject>();
                	}
                	
                	List<JsonObject> results = new ArrayList<JsonObject>(); 
                	for(JsonElement e : items) {
                		JsonObject g = new JsonObject();
                		for(int i=0; i < e.getAsJsonArray().size() ; i++ ) {
                			g.addProperty(fields.get(i).getAsString(), e.getAsJsonArray().get(i).isJsonNull() ? "" : e.getAsJsonArray().get(i).getAsString());
                		}
                		results.add(g);
                	}
                    //logger.info(results.toString());
                    return results;
                }   
            } finally {   
                response.close();   
            }   
        } catch (Exception e) {   
        	logger.error("tushare error. ", e);
        } finally {   
            try {   
                httpclient.close();   
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        }
		return null;   
    }   
    
	    public static void main(String[] args) throws ClientProtocolException, IOException {
	        String body = "{\"api_name\": \"daily\", \"token\": \"5dfbbdf5953c683a061952a4a6c7eae376dc2a892ee3ce5ed4117d64\", \"params\": {\"ts_code\":\"603999.SH\", \"start_date\":\"20180101\", \"end_date\":\"20181231\"}}";
	        post(body, "http://api.tushare.pro");  
	        
	        
	        Map<String, String> map = new HashMap<String, String>();  
	        map.put("api_name", "stock_basic");  
	        map.put("token", "5dfbbdf5953c683a061952a4a6c7eae376dc2a892ee3ce5ed4117d64");  
	        //map.put("params", "{\"list_status\":\"L\"}");  
	        map.put("list_status", "L"); 
	        map.put("fields", "ts_code,name,area,industry,list_date");  
	        
	    	//token:5dfbbdf5953c683a061952a4a6c7eae376dc2a892ee3ce5ed4117d64
	        //String sr=sendPost("http://api.tushare.pro", 
	        //		"{\"api_name\": \"stock_basic\", \"token\": \"5dfbbdf5953c683a061952a4a6c7eae376dc2a892ee3ce5ed4117d64\","
	        //		+ " \"params\": {\"list_stauts\":\"L\"}, \"fields\": \"ts_code,name,area,industry,list_date\"}");
	        //logger.info(sr);
	    }
}
