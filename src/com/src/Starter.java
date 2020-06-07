package com.src;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.view.HoldFrame;

public class Starter
{
	WebDriver driver;
	JSONObject props;
	Commons com;
	List<String> toBeValidated;
	String inputPath;
	Objects obj;
	
	public Starter(String inputPath) {
		this.inputPath = inputPath;
	}
	
	public void start() throws Exception {
		
			Starter curUI = this;
		try {
	    	props = initProperties(inputPath);
		}
		catch(Exception e) {
			JOptionPane.showConfirmDialog(null, "Error occured in initializing the UI Bot test file : " + e.getMessage());
		}
		try {
	    	driver = initBrowser();
		}
		catch(Exception e2) {
			JOptionPane.showConfirmDialog(null, "Error occured in initializing the browser : " + e2.getMessage());
		}
	    obj = new Objects(driver, props, inputPath);
	    try {
	    	com = new Commons(obj);
	    	new InitPage(obj);
	    	if((boolean)com.getProperty("website-details>init-login")) {
				new HoldFrame(curUI);
	    		startCrawl();
	    	}
	    	else {
	    		startCrawl();
	    	}
    	}
    	catch(Exception e) {
    		com.logErrorToFile(e);
    		com.generateReport();
    		com.quitBrowser();
    	}
	}
      
    public void startCrawl() throws Exception{
    	try {
		 	Controller controller = new Controller(obj);
	        controller.start();
	        com.generateReport();	        
	        com.quitBrowser();
    	}
    	catch(Exception e) {
    		com.logErrorToFile(e);
    		com.generateReport();
    		com.quitBrowser();
    	}
    }
    
    public JSONObject initProperties(String inputPath) throws Exception {
		FileReader reader = new FileReader(inputPath); 
		JSONParser jsonParser = new JSONParser(); 
		props = (JSONObject) jsonParser.parse(reader);
		//readExcelProps();
		return props;
	}
    
    
	public WebDriver initBrowser() throws Exception{
		JSONObject seleniummSettings = (JSONObject) props.get("selenium-settings");
		String browserName = ((String)((JSONObject)props.get("run-settings")).get("browser"));
		
		WebDriver driver = null;
		switch(browserName) {
			case "firefox":
				JSONObject ffObj = (JSONObject) seleniummSettings.get("firefox");
				String ffBinary = (String) ffObj.get("binary");
				String geckoDriver = (String) ffObj.get("driver");
				System.setProperty("webdriver.gecko.driver",  geckoDriver);
				FirefoxBinary binary = new FirefoxBinary(new File(ffBinary));
		        FirefoxProfile firefoxProfile = new FirefoxProfile();
		        firefoxProfile.setAcceptUntrustedCertificates(true);
		        firefoxProfile.setAssumeUntrustedCertificateIssuer(true);
		        FirefoxOptions ffoptions = new FirefoxOptions();
		        ffoptions.setProfile(firefoxProfile);
		        ffoptions.setBinary(binary);
		        /*ffoptions.setCapability("browser.link.open_newwindow", 1);
		        ffoptions.setCapability("browser.link.open_newwindow.restriction", 0);
		        ffoptions.setCapability("dom.disable_beforeunload", true);
		        ffoptions.setCapability("print.always_print_silent", true);
		        ffoptions.setCapability("unhandledPromptBehavior", "dismiss");
		        ffoptions.setCapability("acceptInsecureCerts", true);*/
				driver  = new FirefoxDriver(ffoptions);
				break;
			case "chrome":
				JSONObject chromeObj = (JSONObject) seleniummSettings.get("chrome");
				String chromeBinary = (String) chromeObj.get("binary");
				String chromeDriver = (String) chromeObj.get("driver");
				boolean headlessMode = (boolean) chromeObj.get("headless");
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				System.setProperty("webdriver.chrome.driver",  chromeDriver);
				ChromeOptions chromeOptions = new ChromeOptions();
				if(headlessMode) {
					chromeOptions.addArguments("headless");
					chromeOptions.addArguments("window-size=" + (int)dim.getWidth() + "x" + (int)dim.getHeight());
				}
				chromeOptions.setBinary(chromeBinary);
				driver = new ChromeDriver(chromeOptions);
				break;
			case "ie":
				JSONObject ieObj = (JSONObject) seleniummSettings.get("ie");
				String ieDriver = (String) ieObj.get("driver");
				System.setProperty("webdriver.ie.driver", ieDriver);
	            DesiredCapabilities DC = new DesiredCapabilities();               
	            DC.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
	            DC.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
	            DC.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
	            DC.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);
	            driver = new InternetExplorerDriver(DC);
				break;
			case "remote-driver":
				DesiredCapabilities caps = new DesiredCapabilities();
				JSONObject rwObj = (JSONObject) seleniummSettings.get("remote-driver");
				String url = (String)rwObj.get("url");
				JSONArray capsArr = (JSONArray) rwObj.get("capabilities");
				for(Object eachCaps : capsArr) {
					JSONObject eachCapObj = (JSONObject)eachCaps;
					String capValue = (String)eachCapObj.get("Value");
					if(capValue.startsWith("^") && capValue.endsWith("$")) {
						capValue = decryptText(capValue.substring(1, capValue.length() - 1));
					}
					caps.setCapability((String)eachCapObj.get("Key"), capValue);
				}
			    driver = new RemoteWebDriver(new URL(url), caps);
			/*case "phantom":
				JSONObject chromeHeadlessObj = (JSONObject) seleniummSettings.get("chrome");
				String chromeHeadlessBinary = (String) chromeHeadlessObj.get("binary");
				String chromeHeadlessDriver = (String) chromeHeadlessObj.get("driver");
				System.setProperty("webdriver.chrome.driver",  chromeHeadlessDriver);
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				ChromeOptions options = new ChromeOptions();
				options.setBinary(chromeHeadlessBinary);
		        options.addArguments("headless");
		        options.addArguments("window-size=" + (int)dim.getWidth() + "x" + (int)dim.getHeight());
		        driver = new ChromeDriver(options);
				break;*/
			default:
				break;
		
		}
		driver.manage().window().maximize();
		return driver;
	}
	
	@SuppressWarnings("unchecked")
	public void readExcelProps() throws Exception {
		FileInputStream inputStream = new FileInputStream(new File(inputPath));
		XSSFWorkbook inputBook = new XSSFWorkbook(inputStream);
		JSONObject valSet = new JSONObject();
		
		
		JSONArray textsArr = new JSONArray();
		XSSFSheet textValSht = inputBook.getSheet("Text Validations");
		for(int eachRow = 1; eachRow <= textValSht.getLastRowNum(); eachRow++) {
			if(textValSht.getRow(eachRow)!=null) {
				if(textValSht.getRow(eachRow).getCell(0)!=null) {
					textsArr.add(textValSht.getRow(eachRow).getCell(0).getStringCellValue());
				}
			}
		}
		valSet.put("text", textsArr);
	
		JSONArray eleArr = new JSONArray();
		XSSFSheet eleValSht = inputBook.getSheet("Element Validations");
		for(int eachRow = 1; eachRow <= eleValSht.getLastRowNum(); eachRow++) {
			if(eleValSht.getRow(eachRow)!=null) {
				if(eleValSht.getRow(eachRow).getCell(0)!=null) {
					JSONObject eachEle = new JSONObject();
					eachEle.put("name", eleValSht.getRow(eachRow).getCell(0).getStringCellValue());
					eachEle.put("by", eleValSht.getRow(eachRow).getCell(1).getStringCellValue());
					eachEle.put("selector", eleValSht.getRow(eachRow).getCell(2).getStringCellValue());
					eleArr.add(eachEle);
				}
			}
		}
		valSet.put("element", eleArr);
	
		JSONArray galenArr = new JSONArray();
		XSSFSheet galenValSht = inputBook.getSheet("Galen Validations");
		for(int eachRow = 1; eachRow <= galenValSht.getLastRowNum(); eachRow++) {
			if(galenValSht.getRow(eachRow)!=null) {
				if(galenValSht.getRow(eachRow).getCell(0)!=null) {
					JSONObject galenObj = new JSONObject();
					galenObj.put("name", galenValSht.getRow(eachRow).getCell(0).getStringCellValue());
					galenObj.put("file", galenValSht.getRow(eachRow).getCell(1).getStringCellValue());
					galenArr.add(galenObj);
				}
			}
		}
		valSet.put("galen-specs", galenArr);
		
		JSONArray pagesArr = new JSONArray();
		XSSFSheet selectValSht = inputBook.getSheet("Selective Pages");
		for(int eachRow = 1; eachRow <= selectValSht.getLastRowNum(); eachRow++) {
			if(selectValSht.getRow(eachRow)!=null) {
				if(selectValSht.getRow(eachRow).getCell(0)!=null) {
					pagesArr.add(selectValSht.getRow(eachRow).getCell(0).getStringCellValue());
				}
			}
		}
		valSet.put("selective-pages", pagesArr);
		
		JSONObject excObj = new JSONObject();
		JSONArray namesArr = new JSONArray();
		JSONArray urlsArr = new JSONArray();
		XSSFSheet exceptionSht = inputBook.getSheet("Exceptions");
		for(int eachRow = 1; eachRow <= exceptionSht.getLastRowNum(); eachRow++) {
			if(exceptionSht.getRow(eachRow)!=null) {
				if(exceptionSht.getRow(eachRow).getCell(0)!=null) {
					namesArr.add(exceptionSht.getRow(eachRow).getCell(0).getStringCellValue());
				}
				if(exceptionSht.getRow(eachRow).getCell(1)!=null) {
					urlsArr.add(exceptionSht.getRow(eachRow).getCell(1).getStringCellValue());
				}
			}
		}
		excObj.put("link-names", namesArr);
		excObj.put("link-urls", urlsArr);
		
		JSONObject valObj = new JSONObject();
		valObj.put("text", toBeValidated.contains("text") ? true : false);
		valObj.put("element", toBeValidated.contains("element") ? true : false);
		valObj.put("galen", toBeValidated.contains("galen") ? true : false);
		valObj.put("selective-pages", toBeValidated.contains("selective-pages") ? true : false);
		
		props.put("exceptions", excObj);
		props.put("validation-settings", valSet);
		props.put("validate", valObj);
		
	}
	
	public Commons getCom() {
		return com;	
	}
	
	//Method will decrypt the string which was encrypted early with same key 'CFAutomation'  
		public static String decryptText(String toDecrypt) {
			String decryptedText = null;

			try {
				// Get the key to encrypt with
				String secretKey = "CFAutomation";

				// initialize for encrypting
				Cipher cipher = Cipher.getInstance("Blowfish");
				cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey.getBytes(), "Blowfish"));
				byte[] decrypted = cipher.doFinal(hexToBytes(toDecrypt));
				decryptedText = new String(decrypted);
				} catch (InvalidKeyException e) {
					System.out.println("Invalid key, Please pass a valid key to encrypt.");
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					e.printStackTrace();
				} catch (BadPaddingException e) {
					e.printStackTrace();
				}
				return decryptedText;
		}

		public static byte[] hexToBytes(String str) {
			if (str == null) {
				return null;
			} else if (str.length() < 2) {
				return null;
			} else {
				int len = str.length() / 2;
				byte[] buffer = new byte[len];
				for (int i = 0; i < len; i++) {
					buffer[i] = (byte) Integer.parseInt(
							str.substring(i * 2, i * 2 + 2), 16);
				}
				return buffer;
			}

		}

		public static String bytesToHex(byte[] data) {
			if (data == null) {
				return null;
			} else {
				int len = data.length;
				String str = "";
				for (int i = 0; i < len; i++) {
					if ((data[i] & 0xFF) < 16)
						str = str + "0"
								+ java.lang.Integer.toHexString(data[i] & 0xFF);
					else
						str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
				}
				return str.toUpperCase();
			}
		}
}


