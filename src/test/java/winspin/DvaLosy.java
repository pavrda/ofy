package winspin;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by kissmael on 9.10.15.
 */
public class DvaLosy{

    static String link;

    static String response;

    static int status;

	public static void main(String[] args) throws Exception {
// login
        HttpURLConnection connection1;
        HttpURLConnection connection2;
        URL url;
        
        url = new URL("http://winspin01.appspot.com/api/login?username=testuser1&password=test1234");
        connection1 = (HttpURLConnection)url.openConnection();
        connection1.setRequestMethod("GET");
        status = connection1.getResponseCode();
        String cookie1 = connection1.getHeaderField("Set-Cookie");
        getResponse(connection1);
        
        url = new URL("http://winspin01.appspot.com/api/login?username=testuser3&password=test1234");
        connection2 = (HttpURLConnection)url.openConnection();
        connection2.setRequestMethod("GET");
        status = connection2.getResponseCode();
        String cookie2 = connection1.getHeaderField("Set-Cookie");
        getResponse(connection2);

// filtr mistnosti
        url = new URL("http://winspin01.appspot.com/api/getRoomList?ticketPrice=1");
        connection1 = (HttpURLConnection)url.openConnection();
        connection1.setRequestProperty("Cookie", cookie1);
        connection1.setRequestMethod("GET");
        status = connection1.getResponseCode();
        getResponse(connection1);


// nalezeni prazdne mistnosti
        Boolean full, konec = false;
        JSONObject json = new JSONObject(response);
        JSONArray arr = json.getJSONArray("list");
        
        for(int i = 0; i < arr.length(); i++){
        	full = arr.getJSONObject(i).getBoolean("full");
        	
        	if(!full){
        		long idRoom = arr.getJSONObject(i).getLong("idRoom");
        		JSONArray occupied = arr.getJSONObject(i).getJSONArray("occupied");
        		
        		for(int j = 0; j < arr.getJSONObject(i).getInt("ticketMax"); j++){
        			JSONObject ticket = occupied.getJSONObject(j);
        			
        			if(ticket.length() == 0){
        				
        				Async a1 = new Async("http://winspin01.appspot.com/api/buyTicket?idRoom=" + idRoom + "&ticketNo=" + Integer.toString(j), cookie1);
        				Async a2 = new Async("http://winspin01.appspot.com/api/buyTicket?idRoom=" + idRoom + "&ticketNo=" + Integer.toString(j), cookie2);

        				a1.run();
        				a2.run();
/*        				
        		    	url = new URL("http://winspin01.appspot.com/api/buyTicket?idRoom=" + idRoom + "&ticketNo=" + Integer.toString(j));
        		        connection1 = (HttpURLConnection)url.openConnection();
        		        connection1.setRequestProperty("Cookie", cookie1);
        		        connection1.setRequestMethod("GET");
        		        status = connection1.getResponseCode();
        		        getResponse(connection1);
        				
        		    	url = new URL("http://winspin01.appspot.com/api/buyTicket?idRoom=" + idRoom + "&ticketNo=" + Integer.toString(j));
        		        connection2 = (HttpURLConnection)url.openConnection();
        		        connection2.setRequestProperty("Cookie", cookie2);
        		        connection2.setRequestMethod("GET");
        		        status = connection2.getResponseCode();
        		        getResponse(connection2);
*/
        		        System.out.println("Konec programu");
        		        return;
        			}
        			System.out.println(j+". ticket: "+ticket);
        		}
        		System.out.println("----dalsi----");
        		
        	}
        }
    }

	public static void getResponse(HttpURLConnection connection) throws IOException {
    	BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer sb = new StringBuffer();

        while((inputLine = reader.readLine()) != null) {
            sb.append(inputLine);
        }
        reader.close();
        response = sb.toString();
        System.out.println(response);
    }

    public static int getHttpStatus() {
        return status;
    }
}

class Async implements Runnable {

	String url;
	String cookie;
	
	public Async(String url, String cookie) {
		this.url = url;
		this.cookie = cookie;
	}
	
	@Override
	public void run() {
		try {
			URL murl;
			System.out.println("run()");
			murl = new URL(url);
	    	HttpURLConnection connection1 = (HttpURLConnection)murl.openConnection();
	        connection1.setRequestProperty("Cookie", cookie);
	        connection1.setRequestMethod("GET");
	        int status = connection1.getResponseCode();
	        DvaLosy.getResponse(connection1);		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

