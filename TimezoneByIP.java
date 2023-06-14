package Main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import Main.HttpRequestExample.proxy;

public class TimezoneByIP {
	
	static ArrayList<proxy> proxies;
	
	public static class proxy {
		String ip, port, user, pass;

		public proxy(String ip, String port, String user, String pass) {
			super();
			this.ip = ip;
			this.port = port;
			this.user = user;
			this.pass = pass;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public String getPort() {
			return port;
		}

		public void setPort(String port) {
			this.port = port;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public String getPass() {
			return pass;
		}

		public void setPass(String pass) {
			this.pass = pass;
		}
		
		
	}
	
	public static ArrayList<proxy> getProxy() throws IOException {
		ArrayList<proxy> proxies= new ArrayList<>();
		Path path = Paths.get("src/assets/listIP.txt");
		ArrayList<String> proxylist = (ArrayList<String>) Files.readAllLines(path);
		for(String str : proxylist) {
			String[] arr = str.split(":");
			
			proxy p= new proxy(arr[0], arr[1], arr[2], arr[3]);
			proxies.add(p);
		}
		
		return proxies;
	}

	
    public static void main(String[] args) throws IOException, InterruptedException {
    	
    	
    	TimeUnit time = TimeUnit.SECONDS;
    	proxies = getProxy();
		time.sleep(3);
		
		getListTimezone();
		
		


    }
   
    
    public static void getListTimezone() {
    	String path = "src/assets/timezone30.txt";
    	
		for(int i=0; i< proxies.size(); i++ ) {
			String str= getTimeZone(proxies.get(i).getIp(), i);
			appendUsingFileWriter(path, str.concat("\n"));
		}
    }
    
    public static void shuffleProxy() throws IOException {
		
		Path path = Paths.get("src/assets/500proxy.txt");
		String outpath = "src/assets/shuffleProxy.txt";
		ArrayList<String> proxylist = (ArrayList<String>) Files.readAllLines(path);
		Collections.shuffle(proxylist);
		for (String str : proxylist) {
			appendUsingFileWriter(outpath, str.concat("\n"));
		}

		
	}
    
    public static void writeFile(ArrayList<String> input) throws IOException {
		String path = "src/assets/wallet.txt";

		for (String str : input) {
			appendUsingFileWriter(path, str.concat("\n"));
		}

	}
    private static void appendUsingFileWriter(String filePath, String text) {
		File file = new File(filePath);
		FileWriter fr = null;
		try {
			// Below constructor argument decides whether to append or override
			fr = new FileWriter(file, true);
			fr.write(text);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    public static String getTimeZone(String ipAddress, int i) {

       // String ipAddress = "37.35.43.145"; // Replace with the IP address for which you want to get the timezone
    	 String timezone="";
        try {
            URL url = new URL("http://ip-api.com/json/" + ipAddress + "?fields=status,timezone");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HTTP error code: " + responseCode);
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            String responseBody = scanner.useDelimiter("\\A").next();

            scanner.close();
            conn.disconnect();

            // Parse the JSON response and extract the timezone
             timezone = new JSONObject(responseBody).getString("timezone");

            System.out.println( i+": " + timezone);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return timezone;
    }
    
    public static void getLocationByIP (String ipAddress) throws IOException {
    	 // String ipAddress = "171.247.206.32";
    	    String apiUrl = "http://ip-api.com/json/" + ipAddress;

    	    URL url = new URL(apiUrl);
    	    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	    con.setRequestMethod("GET");

    	    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    	    String inputLine;
    	    StringBuilder response = new StringBuilder();
    	    while ((inputLine = in.readLine()) != null) {
    	      response.append(inputLine);
    	    }
    	    in.close();

    	    JSONObject jsonObject = new JSONObject(response.toString());
    	    String country = jsonObject.getString("country");
    	    String region = jsonObject.getString("regionName");
    	    String city = jsonObject.getString("city");

    	    System.out.println("Location of " + ipAddress + ": " + city + ", " + region + ", " + country);
    	  
    }
}

