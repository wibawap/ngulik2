package test2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import java.util.Properties;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class NginxTest {
	
	public static final String FILEPATH = "config/config.properties";
	private String host;
	private String hostHttp;
	private int port = 80;
	private String browser;
	private int code;
	private FileReader reader;
	private Socket sock;
	
	@BeforeTest
	public void readConf() {
		
		//Read Config file ...!
		try {
			reader = new FileReader(NginxTest.FILEPATH);
			
		} catch (FileNotFoundException e) {
			System.out.println("Config File Not Found ..! ");
		}
		
		try {
			
			Properties properties = new Properties();
			properties.load(reader);
			
			host = properties.getProperty("NGINX_HOST");
			hostHttp = properties.getProperty("NGINX_HOST_HTTP");
			browser = properties.getProperty("BROWSER");
			
		} catch (IOException e) {
			System.out.println("Couldn't Read Config File..!");
		}
		
	}
	
	@Test
	public void checkPort() {
		
		try {
			
			sock = new Socket(host,port);
			
			if(sock != null) {
				StringBuffer sb = new StringBuffer("Port ");
				sb.append(port);
				sb.append(" is open...");
				System.out.println(sb);
			}else {
				System.out.println("Host or Port is not Open...!");
			}
				
		}catch (IOException e) {
			System.out.println("Connection to Host or Port Failed...!");
		}
		
		try {
			sock.close();
		} catch (IOException e) {
			System.out.println("Socket could't close...!");
		}
		
	}
	
	@Test
	public void checkPage() {
		
		String hostHttpPort = hostHttp+":"+port;
		
		try {
			//StringBuffer hostHttpPort = new StringBuffer(hostHttp);
			//hostHttpPort.append(":");
			//hostHttpPort.append(port);
			
			URL url = new URL(hostHttpPort);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestProperty("User-Agent",browser);
			connection.setRequestMethod("GET");
			connection.connect();
			code = connection.getResponseCode();
			
			StringBuffer sb = new StringBuffer("Response Code is : ");
			sb.append(code);
			System.out.println(sb);
			
		} catch (MalformedURLException e) {
			System.out.println("URL Not Exist...!");
		} catch (IOException e) {
			System.out.println("Connection Failed..!");
		}
		
		
	}

}
