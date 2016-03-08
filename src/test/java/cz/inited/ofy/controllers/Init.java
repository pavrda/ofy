package cz.inited.ofy.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Init {

	public static String baseURL = "http://127.0.0.1:8080/";
	
	public static void post(String apiurl, Map<String,Object> params) throws IOException {

        HttpURLConnection connection1;
        URL url;
        url = new URL(baseURL + apiurl);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        for ( int c = in.read(); c != -1; c = in.read() ) {
            System.out.print((char)c);        
        }
        System.out.println("");
	}
	
	public static void main(String[] args) {
		try {
	        Map<String,Object> params = new LinkedHashMap<>();
	        post("api2/system/init", params);
	        
	        params = new LinkedHashMap<>();
	        post("api2/user/getInfo", params);
	        
	        
	        if (1==1) return;
			
			
			
	        String username = "test" +  (new Date()).getTime() / 1000;
	        System.out.println("username:" + username);
	
	        params = new LinkedHashMap<>();
	        params.put("username", username);
	        params.put("password", "Heslo123");
	        params.put("email", username + "@pavrda.cz");
	        params.put("fullname", username);
	        post("api/registerUser", params);
	        
	        params = new LinkedHashMap<>();
	        params.put("title", "_credit");
	        post("api/createMoneyAccount", params);

	        params = new LinkedHashMap<>();
	        params.put("title", "_game");
	        post("api/createMoneyAccount", params);
	        
	        params = new LinkedHashMap<>();
	        params.put("username", username);
	        params.put("castka", "100");
	        post("api/addKredit", params);

	        params = new LinkedHashMap<>();
	        post("api/getInfo", params);

	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
