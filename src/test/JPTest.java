package test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class JPTest {

	public static final String fileConfig 	= "config/configJPTest.properties";
	public static final String fileDataNews 	= "config/dataNewsTest.properties";
	public static final String fileDataJobs 	= "config/dataJobTest.properties";
	private String urlNews;
	private String urlJobs;
	private String browser;
	private int expectedCode = 200;
	private int code;
	private WebDriver driver;
	private SoftAssert assertion;
	private FileReader read;
	private URL url;
	
	@BeforeClass  // Read config file from 'configJPTest.properties'...
	public void readConfig() {
		
		try {
			read = new FileReader(JPTest.fileConfig);
		} catch (FileNotFoundException e) {
			System.out.println("Config File Not Found...!");
		}
		
		try {
			Properties prop = new Properties();
			prop.load(read);
			urlNews = prop.getProperty("URL_NEWS");
			urlJobs = prop.getProperty("URL_JOBS");
			browser = prop.getProperty("BROWSER");
			
		} catch (IOException e) {
			System.out.println("Cannot Read the Config File...!");
		}
		
			
	}
	
	@BeforeClass  // Setup the Headless Browser...
	public void setupBrowser() {	
	
		// Use Chrome Headless mode
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
			        
		// Create Chrome driver to drive the browser
		driver = new ChromeDriver(options);
			     				
		// Use Soft Assert ...
		assertion = new SoftAssert();
	
	}
	
	// Setup Test Page ...
	public void testPage(String page, String data) {
		
		try {
			url = new URL(page);
		} catch (MalformedURLException e1) {
			System.out.println("URL Not Exist...!");
		}
		
		
		try {
			HttpURLConnection connection;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestProperty("User-Agent",browser);
			connection.setRequestMethod("GET");
			connection.connect();
			code = connection.getResponseCode();
			
		} catch (IOException e1) {
			System.out.println("Connection Failed..!");
		}
		
			
		// Open home page URL..
		driver.get(page);

		// Display URL and Page Title
		System.out.println(driver.getCurrentUrl());
		System.out.println(driver.getTitle());
		System.out.println("");
					
		// Start Test Page...	
		
		try {
			read = new FileReader(data);
		} catch (FileNotFoundException e1) {
			System.out.println("Data File Not Found...!");
		}
		
		try {
			Properties prop = new Properties();
			prop.load(read);
			
			Enumeration<?> e = prop.propertyNames();
			while(e.hasMoreElements()) {
			
			Object key =  e.nextElement();
			String value = prop.getProperty((String) key);
			
			driver.get(page + key);
			
			//Print the response code ...
			StringBuffer sb = new StringBuffer(value);
			sb.append(" ");
			sb.append(code);
			sb.append(" ...TEST");
			System.out.println(sb);
			
			assertion.assertEquals(code,expectedCode,"...Response Code is not 200 !");
			assertion.assertEquals(driver.getTitle(),value,key+" is FAILED !!");
			System.out.println(driver.getTitle());
			assertion.assertAll();
			}
			
		} catch (IOException e1) {
			System.out.println("Cannot Read the Data File...!");
		}
	
	
		System.out.println("");	
		
	}
	
	@Test	// Run Test Jakpost News ...
	public void newsTest() {
			
		testPage(urlNews,JPTest.fileDataNews);
					
	}
	
	@Test  // Run Test Jakpost Jobs ...
	public void jobsTest() {
		
		testPage(urlJobs,JPTest.fileDataJobs);
			
	}
	
		
}