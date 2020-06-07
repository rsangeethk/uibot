package com.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.GzipFilter;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;

import nu.validator.servlet.InboundGzipFilter;
import nu.validator.servlet.InboundSizeLimitFilter;
import nu.validator.servlet.Main;
import nu.validator.servlet.MultipartFormDataFilter;
import nu.validator.servlet.VerifierServlet;

public class Commons extends ObjectLib{
    private static WebDriverWait jsWait;
    private static JavascriptExecutor jsExec;
	private FileWriter outputFile;
	private FileWriter logFile;
	public  Server server;
    public Commons(Objects obj) {
    	super(obj);
	}
	
	public void quitBrowser() {
		 driver.close();
	     //driver.quit();
	}
	
	public void generateReport() throws Exception{
		String resourcesPath = "";
		if(new File(new File(Starter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParentFile().getPath() + "\\res").exists()) {
			resourcesPath = new File(Starter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParentFile().getPath() + "\\res";
		}
		else {
			resourcesPath = new File(Starter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath() + "\\res";
		}
		
		FileUtils.copyDirectory(new File(resourcesPath + "\\res"), new File(outputFolder + "\\res"));
		FileUtils.copyFileToDirectory(new File(resourcesPath + "\\index.htm"), outputFolder);
		String jsonOutputStr = jsonOutput.toJSONString()/*.replaceAll("\\\\", "")*/;
		outputFile = new FileWriter(outputFolder + "\\outputJSON.json");
		outputFile.write(jsonOutputStr);
		outputFile.flush();
		
		FileWriter jsOutput = new FileWriter(outputFolder + "\\res\\outputJSON.js");
		jsOutput.write("var myoutput = " + jsonOutputStr + ";");
		jsOutput.flush();
		jsOutput.close();
		
		createExcelReport();
	}
	
	public void createExcelReport() throws Exception{
		XSSFWorkbook workbook = new XSSFWorkbook();
		//XSSFSheet summarySheet = workbook.createSheet("Summary");
		try {
			for(Object eachVal : jsonOutput.keySet()) {
				String curValidation = (String) eachVal;
				JSONArray curValidationList = (JSONArray) jsonOutput.get(curValidation);
				if(curValidationList.size() >= 1) {
					XSSFSheet validationSheet = workbook.createSheet(curValidation);
					XSSFRow firstRow = validationSheet.createRow(0);
					firstRow.createCell(0).setCellValue("Title");
					firstRow.createCell(1).setCellValue("URL");
					int colIndex = 2;
					for(Object eachKey : ((JSONObject)curValidationList.get(0)).keySet()) {
						String eachKeyStr = (String) eachKey;
						if(!eachKeyStr.equalsIgnoreCase("url") && !eachKeyStr.equalsIgnoreCase("title")) {
							firstRow.createCell(colIndex).setCellValue(eachKeyStr);
							colIndex++;
						}
					}
					
					int eachRow = 1;
					for(Object eachURLVal : curValidationList) {
						XSSFRow currentRow = validationSheet.createRow(eachRow);
						currentRow.createCell(0).setCellValue((String)((JSONObject)eachURLVal).get("Title"));
						currentRow.createCell(1).setCellValue((String)((JSONObject)eachURLVal).get("URL"));
						colIndex = 2;
						for(Object eachKey : ((JSONObject)eachURLVal).keySet()) {
							String eachKeyStr = (String) eachKey;
							if(!eachKeyStr.equalsIgnoreCase("url") && !eachKeyStr.equalsIgnoreCase("title")) {
								String eachCellvalue = (String)((JSONObject)eachURLVal).get(eachKeyStr);
								currentRow.createCell(colIndex).setCellValue(eachCellvalue);
								colIndex++;
							}
						}
						eachRow++;
					}
				}
			}
		}
		catch(Exception e) {
			
		}
		
		FileOutputStream fileOutputStream = null;
		try	{
			fileOutputStream = new FileOutputStream(outputFolder + "\\excel-report.xlsx");
			workbook.write(fileOutputStream);
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void logErrorToFile(Exception e) throws Exception{
		StringWriter w = new StringWriter();
		e.printStackTrace(new PrintWriter(w));
		String errMsg = w.toString();
		String errorMsg;
		if (errMsg.toUpperCase().contains("WARNING")) {
			int index = errMsg.indexOf("WARNING");
			errorMsg = errMsg.substring(0, index - 1);
		} else if (errMsg.toUpperCase().contains("Command")) {
			int index = errMsg.indexOf("Command");
			errorMsg = errMsg.substring(0, index - 1);
		} else {
			errorMsg = errMsg;
		}
		logFile = new FileWriter(outputFolder + "\\error-log.txt");
		logFile.write(errorMsg);
		logFile.flush();
	}
	
	
	public By getSelector(Object propertyNode) {
		  By selector = null;
		  String selectorNode = (String) ((JSONObject)propertyNode).get("by");
		  String valueNode = (String)  ((JSONObject)propertyNode).get("selector");
		  switch(selectorNode) {
		  case "xpath" :
			  selector = By.xpath(valueNode);
			  break;
		  case "id" :
			  selector = By.id(valueNode);
			  break;
		  case "class" :
			  selector = By.className(valueNode);
			  break;
		  case "css" :
			  selector = By.cssSelector(valueNode);
			  break;
		  case "name" :
			  selector = By.name(valueNode);
			  break;
		  default :
			  break;
		  }
		  return selector; 
	  }
	
	public ArrayList<String> getExceptionURLs() {
		JSONArray exceptionURLs = (JSONArray)getProperty("exceptions>link-urls");
		ArrayList<String> list = new ArrayList<String>();
		for (int i=0; i<exceptionURLs.size(); i++) {
		    list.add( (String)exceptionURLs.get(i) );
		}
		return list;
	}
	
	public ArrayList<String> getExceptionLinks() {
		JSONArray exceptionLinks = (JSONArray)getProperty("exceptions>link-names");
		ArrayList<String> list = new ArrayList<String>();
		for (int i=0; i<exceptionLinks.size(); i++) {
		    list.add( (String)exceptionLinks.get(i) );
		}
		return list;
	}
	
	public String takeScreenshot(String title) {
		title = title.replace("|", "").replace(",", "").replace("/", "").replace(".", "").replace(":", "").replace("?", "");
		String screenshotPath = outputFolder.getAbsolutePath() + "\\Screenshots\\" + title  + ".png";
		 int count = 1;
		 while(new File(screenshotPath).exists()) {
			 screenshotPath = outputFolder.getAbsolutePath() + "\\Screenshots\\" + title + "_" + count + ".png";
			 count++;
		 }
		try {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(screenshotPath), true);
		} catch (Exception e) {
			
		}
		return screenshotPath;
	}
	

	public boolean waitTillObjectVisibility(By objectToWaitFor, int maximumWait, boolean throwException){
		boolean isObjAvailable = true;
		try
		{
			WebDriverWait webDrivWait = new WebDriverWait(driver,maximumWait);
			webDrivWait.until(ExpectedConditions.presenceOfElementLocated(objectToWaitFor));
			//webDrivWait.until(ExpectedConditions.visibilityOfElementLocated(objectToWaitFor));
			//webDrivWait.until(ExpectedConditions.elementToBeClickable(objectToWaitFor));			
		}
		catch(Exception e)
		{						
			isObjAvailable = false;			
			
		}
		
		return isObjAvailable;
	}
	
	public boolean waitTillLoader() throws Exception {
		boolean isObjAvailable = true;
		try
		{
			if(!getProperty("website-details>invisibility-element>by").toString().equals("")) {
				WebDriverWait webDrivWait = new WebDriverWait(driver,120);
				//if(webDrivWait.until(ExpectedConditions.visibilityOfElementLocated(getSelector(getProperty("website-details>invisibility-element")))) != null) {
				webDrivWait.until(ExpectedConditions.invisibilityOfElementLocated(getSelector(getProperty("website-details>invisibility-element"))));
				//}
			}
		}
		catch(Exception e)
		{						
			isObjAvailable = false;			
			
		}
		
		return isObjAvailable;
	}
	
	public boolean isdisplayed(WebElement element) {
		boolean bool = false;
		try {
			element.isDisplayed();
			element.isEnabled();
			bool = true;
		} catch (Exception e) {
			bool = false;
		}
		return bool;
	}
	
	//Wait for JQuery Load
	  public static void waitForJQueryLoad() {
	      //Wait for jQuery to load
	      ExpectedCondition<Boolean> jQueryLoad = driver -> ((Long) ((JavascriptExecutor) driver)
	              .executeScript("return jQuery.active") == 0);

	      //Get JQuery is Ready
	      boolean jqueryReady = (Boolean) jsExec.executeScript("return jQuery.active==0");

	      //Wait JQuery until it is Ready!
	      if(!jqueryReady) {
	          //Wait for jQuery to load
	          jsWait.until(jQueryLoad);
	      } else {
	      }
	  }


	  //Wait for Angular Load
	  public  void waitForAngularLoad() {
	      JavascriptExecutor jsExec = (JavascriptExecutor) driver;

	      String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";

	      //Wait for ANGULAR to load
	      ExpectedCondition<Boolean> angularLoad = driver -> Boolean.valueOf(((JavascriptExecutor) driver)
	              .executeScript(angularReadyScript).toString());

	      //Get Angular is Ready
	      boolean angularReady = Boolean.valueOf(jsExec.executeScript(angularReadyScript).toString());

	      //Wait ANGULAR until it is Ready!
	      if(!angularReady) {
	          //Wait for Angular to load
	          jsWait.until(angularLoad);
	      } else {
	      }
	  }

	  //Wait Until JS Ready
	  public void waitUntilJSReady() {
	      JavascriptExecutor jsExec = (JavascriptExecutor) driver;

	      //Wait for Javascript to load
	      ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) driver)
	              .executeScript("return document.readyState").toString().equals("complete");
	      
	      //Get JS is Ready
	      boolean jsReady =  (Boolean) jsExec.executeScript("return document.readyState").toString().equals("complete");

	      //Wait Javascript until it is Ready!
	      if(!jsReady) {
	          //Wait for Javascript to load
	          jsWait.until(jsLoad);
	          /*int counter = 0;
	          do{
	        	  try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        	  counter = counter+200;
	          }while(!(Boolean) jsExec.executeScript("return document.readyState").toString().equals("complete") && counter<(60*1000));*/ 
	      } else {
	      }
	  }

	  //Wait Until JQuery and JS Ready
	  public  void waitUntilJQueryReady() {
	  	jsExec = (JavascriptExecutor) driver;

	      //First check that JQuery is defined on the page. If it is, then wait AJAX
	      Boolean jQueryDefined = (Boolean) jsExec.executeScript("return typeof jQuery != 'undefined'");
	      if (jQueryDefined == true) {
	          //Pre Wait for stability (Optional)
	          sleep(20);

	          //Wait JQuery Load
	          waitForJQueryLoad();

	          //Wait JS Load
	          waitUntilJSReady();

	          //Post Wait for stability (Optional)
	          sleep(20);
	      }  else {
	          //System.out.println("jQuery is not defined on this site!");
	    	//Wait JS Load
	          waitUntilJSReady();
	      }
	  }

	  //Wait Until Angular and JS Ready
	  public  void waitUntilAngularReady() {
	      jsExec = (JavascriptExecutor) driver;

	      //First check that ANGULAR is defined on the page. If it is, then wait ANGULAR
	      Boolean angularUnDefined = (Boolean) jsExec.executeScript("return window.angular === undefined");
	      if (!angularUnDefined) {
	          Boolean angularInjectorUnDefined = (Boolean) jsExec.executeScript("return angular.element(document).injector() === undefined");
	          if(!angularInjectorUnDefined) {
	              //Pre Wait for stability (Optional)
	              sleep(20);

	              //Wait Angular Load
	              waitForAngularLoad();

	              //Wait JS Load
	              waitUntilJSReady();

	              //Post Wait for stability (Optional)
	              sleep(20);
	          } else {
	              //System.out.println("Angular injector is not defined on this site!");
	        	//Wait JS Load
	              waitUntilJSReady();
	          }
	      }  else {
	          //System.out.println("Angular is not defined on this site!");
	      }
	  }

	  //Wait Until JQuery Angular and JS is ready
	  public void waitJQueryAngular() {
	  	try {
	  			jsWait = new WebDriverWait(driver, 60);
	  			waitUntilJSReady();
	  			if((boolean)getProperty("website-details>jquery-wait")) {
	  				waitUntilJQueryReady();
	  			}
		        if((boolean)getProperty("website-details>angular-wait")) {
		        	waitUntilAngularReady();
		        }
		        
	  	}
	  	catch(Exception e) {
	  		
	  	}
	  }

	  public static void sleep (Integer seconds) {
	      long secondsLong = (long) seconds;
	      try {
	          Thread.sleep(secondsLong);
	      } catch (InterruptedException e) {
	          e.printStackTrace();
	      }
	  }
	  
	  public void jeClick(WebElement element) {
	  	try {
		    	JavascriptExecutor js = (JavascriptExecutor)driver;
		    	js.executeScript("arguments[0].click();", element);
	  	}
	  	catch(Exception e) {
	  		
	  	}
	  }
	  
	  public void navigateTo(String url) {
	  	String script = "window.location = \'"+url+"\'";
	  	((JavascriptExecutor) driver).executeScript(script);
	  }
	  
	  public Object getProperty(String object) {
	    	Object foundObject = (Object) props;
	    	
	    	for(String eacjObject : object.split(">"))
	    	{
	    		foundObject = ((JSONObject)foundObject).get(eacjObject);
	    	}
	    	return foundObject;
	    }
	  
	  @SuppressWarnings("unchecked")
	  public void addToJSONOutput(String valType, JSONObject objectjson) {
		  //objectjson.put("Screenshot", takeScreenshot());
		  JSONArray curURLOutput = (JSONArray)jsonOutput.get(valType);
		  curURLOutput.add(objectjson);
	  }
	  
	 public String generateGalenReport(List<GalenTestInfo> tests, JSONObject curValObject) throws Exception{
		 String title = ((String)curValObject.get("Title")).replace("|", "").replace(",", "");
		 String url = ((String)curValObject.get("URL")).replace((String)getProperty("website-details>URL"), "").replace("/", "");
		 
		 String galenPath = outputFolder.getAbsolutePath() + "\\Galen Reports\\" + title + " - " + url;
		 HtmlReportBuilder htmlReportBuilder=new HtmlReportBuilder();
		 htmlReportBuilder.build(tests, galenPath);
		 return galenPath;
	 }
	 
	 
	 public String generatehtmlReport(String pageSource, String source, JSONObject curValObject) throws Exception{
		 String title = ((String)curValObject.get("Title")).replace("|", "").replace(",", "").replace("/", "").replace(".", "").replace(":", "").replace("?", "");
		 //String url = ((String)curValObject.get("URL")).replace((String)getProperty("website-details>URL"), "").replace("/", "").replace(".", "").replace(":", "");
		 String w3chtmlPath = outputFolder.getAbsolutePath() + "\\W3C HTML Reports\\" + title  + ".html";
		 int count = 1;
		 while(new File(w3chtmlPath).exists()) {
			 w3chtmlPath = outputFolder.getAbsolutePath() + "\\W3C HTML Reports\\" + title + "_" + count + ".html";
			 count++;
		 }
		 
		 try {
			/*	 File htmlFile = new File(w3chtmlPath);
			 FileWriter outputFile = new FileWriter(htmlFile);
			 outputFile.write(source);
			 outputFile.flush();
			 outputFile.close();*/
			 //FileUtils.writeStringToFile(new File(outputFolder.getAbsolutePath() + "\\W3C HTML Reports\\HTML Source.html"), pageSource, "UTF-8");
			 FileUtils.writeStringToFile(new File(w3chtmlPath), source, "UTF-8");
		 }
		 catch(Exception e) {
			 System.out.println("Error writing HTML report");
		 }
		 return w3chtmlPath;
	 }
	 
	 public String generatehtmlSource(String pageSource, JSONObject curValObject) throws Exception{
		 String title = ((String)curValObject.get("Title")).replace("|", "").replace(",", "").replace("/", "").replace(".", "").replace(":", "").replace("?", "");
		 //String url = ((String)curValObject.get("URL")).replace((String)getProperty("website-details>URL"), "").replace("/", "").replace(".", "").replace(":", "");
		 String htmlPath = outputFolder.getAbsolutePath() + "\\HTML source\\" + title  + ".html";
		 int count = 1;
		 while(new File(htmlPath).exists()) {
			 htmlPath = outputFolder.getAbsolutePath() + "\\HTML source\\" + title + "_" + count + ".html";
			 count++;
		 }
		 
		 try {
			 FileUtils.writeStringToFile(new File(htmlPath), pageSource, "UTF-8");
		 }
		 catch(Exception e) {
			 System.out.println("Error writing HTML source");
		 }
		 return htmlPath;
	 }
	 
	 public void stopIfInterrupted() {
		 if(Thread.currentThread().isInterrupted()) {
			 
		 }
	 }
	 
	 private final long SIZE_LIMIT = Integer.parseInt(System.getProperty(
	            "nu.validator.servlet.max-file-size", "2097152"));
		public void startServlet() {
			try {
			if (!"1".equals(System.getProperty("nu.validator.servlet.read-local-log4j-properties"))) {
	            PropertyConfigurator.configure(Main.class.getClassLoader().getResource(
	                    "nu/validator/localentities/files/log4j.properties"));
	        } else {
	            PropertyConfigurator.configure(System.getProperty(
	                    "nu.validator.servlet.log4j-properties", "log4j.properties"));
	        }
	
	        ServletContextHandler contextHandler = new ServletContextHandler();
	        contextHandler.setContextPath("/");
	        contextHandler.addFilter(new FilterHolder(new GzipFilter()), "/*",
	                EnumSet.of(DispatcherType.REQUEST));
	        contextHandler.addFilter(new FilterHolder(new InboundSizeLimitFilter(
	                SIZE_LIMIT)), "/*", EnumSet.of(DispatcherType.REQUEST));
	        contextHandler.addFilter(new FilterHolder(new InboundGzipFilter()),
	                "/*", EnumSet.of(DispatcherType.REQUEST));
	        contextHandler.addFilter(
	                new FilterHolder(new MultipartFormDataFilter()), "/*",
	                EnumSet.of(DispatcherType.REQUEST));
	        contextHandler.addServlet(new ServletHolder(new VerifierServlet()),
	                "/*");
	
	        server = new Server(new QueuedThreadPool(100));
	        server.setHandler(contextHandler);
	
	        ServerConnector serverConnector = new ServerConnector(server,
	                new HttpConnectionFactory(new HttpConfiguration()));
	        int port = 8888;
	        serverConnector.setPort(port);
	        server.setConnectors(new Connector[] { serverConnector });
	
	        int stopPort = -1;
	        if (stopPort != -1) {
	            try (Socket clientSocket = new Socket(
	                    InetAddress.getByName("127.0.0.1"), stopPort);
	                    InputStream in = clientSocket.getInputStream()) {
	                in.read();
	            } catch (ConnectException e) {
	
	            }
	
	            server.start();
	
	            try (ServerSocket serverSocket = new ServerSocket(stopPort, 0,
	                    InetAddress.getByName("127.0.0.1"));
	                    Socket s = serverSocket.accept()) {
	                server.stop();
	
	                s.getOutputStream().close();
	            }
	        } else {
	            server.start();
	        }
			}
			catch(Exception e) {
				
			}
		}
		public boolean isServerReachable() {
			int timeout = 2000;
			String url = "http://localhost:8888/";
			url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.
	
		    try {
		        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		        connection.setConnectTimeout(timeout);
		        connection.setReadTimeout(timeout);
		        connection.setRequestMethod("HEAD");
		        int responseCode = connection.getResponseCode();
		        return (200 <= responseCode && responseCode <= 399);
		    } catch (IOException exception) {
		        return false;
		    }
		}
}
