package com.src;


import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import com.galenframework.api.Galen;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.model.LayoutReport;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

@SuppressWarnings("unchecked")
public class Validator extends Commons{
	String curURL;
	String curScreenshot;
	public Validator(Objects obj, String curURL, String curScreenshot) {
		super(obj);
		this.curURL = curURL;
		this.curScreenshot = curScreenshot;
		System.out.println("Validations " + curURL);
	}
	
	public void init() throws Exception{
		
		if(((JSONArray) getProperty("validation-settings>text")).size()>0) {
			textValidations();
		}
		if(((JSONArray) getProperty("validation-settings>element")).size()>0) {
			elementsValidation();
		}
		if(((JSONArray) getProperty("validation-settings>galen-specs")).size()>0) {
			galenSpecValidation();
		}
		if(((JSONArray) getProperty("validation-settings>conditional")).size()>0) {
			conditionalValidation();
		}
		if(((boolean) getProperty("validation-settings>html"))) {
			htmlValidations();
		}
		if(((boolean) getProperty("validation-settings>ssl"))) {
			sslValidations();
		}
		if(((boolean) getProperty("website-details>source"))) {
			htmlScraping();
		}
		
	}
	
	public void textValidations() throws Exception{
		try {
			waitJQueryAngular();
			JSONObject curURLObj = new JSONObject();
			curURLObj.put("URL", driver.getCurrentUrl());
			waitTillObjectVisibility(By.tagName("html"), 120, false);
			//waitTillLoader();
			String pageText = driver.findElement(By.tagName("html")).getText();
			JSONArray textsToValidate = (JSONArray) getProperty("validation-settings>text");
			for(Object eachObject : textsToValidate) {
				String eachTextToValidate = (String) eachObject; 
				if(pageText.contains(eachTextToValidate)) {
					curURLObj.put(eachTextToValidate, "Yes");
				}
				else {
					curURLObj.put(eachTextToValidate, "No");
				}
			}
			curURLObj.put("Title", driver.getTitle());
			curURLObj.put("Screenshot", curScreenshot);
			addToJSONOutput("Text Validation", curURLObj);
		}
		catch(Exception e) {
			System.out.println("Error : " + curURL + " - Text validation");
		}
	}
	
	public void elementsValidation() throws Exception{
		try {
			waitJQueryAngular();
			JSONObject curURLObj = new JSONObject();
			curURLObj.put("URL", driver.getCurrentUrl());
			waitTillObjectVisibility(By.tagName("html"), 120, false);
			//waitTillLoader();
			JSONArray elementsToValidate = (JSONArray) getProperty("validation-settings>element");
			for(Object eachObject : elementsToValidate) {
				By eachElementToValidate = getSelector(eachObject); 
				if(waitTillObjectVisibility(eachElementToValidate, 1, false)) {
					curURLObj.put(((JSONObject)eachObject).get("by") + " : " + ((JSONObject)eachObject).get("selector"), "Yes");
				}
				else {
					curURLObj.put(((JSONObject)eachObject).get("by") + " : " + ((JSONObject)eachObject).get("selector"), "No");
				}
			}
			curURLObj.put("Title", driver.getTitle());
			curURLObj.put("Screenshot", curScreenshot);
			addToJSONOutput("Element Validation", curURLObj);
		}
		catch(Exception e) {
			System.out.println("Error : " + curURL + " - Element validation");
		}
	}
	
	
	public void galenSpecValidation() throws Exception {
		try{
			List<GalenTestInfo> tests=new LinkedList<GalenTestInfo>();
			waitJQueryAngular();
			JSONObject curURLObj = new JSONObject();
			curURLObj.put("URL", driver.getCurrentUrl());
			waitTillObjectVisibility(By.tagName("html"), 120, false);
			JSONArray specsToValidate = (JSONArray) getProperty("validation-settings>galen-specs");
			for(Object eachObject : specsToValidate) {
				String eachSpecToValidate = (String)((JSONObject) eachObject).get("file");
				String eachSpecName = (String)((JSONObject) eachObject).get("name"); 
				LayoutReport layoutReport=Galen.checkLayout(driver, eachSpecToValidate, Arrays.asList(new String[]{"desktop"}));
				GalenTestInfo test=GalenTestInfo.fromString(eachSpecName);
				test.getReport().layout(layoutReport, eachSpecName);
				tests.add(test);
				if(layoutReport.errors()>0) {
					curURLObj.put(eachSpecName, "Failed");
				}
				else {
					curURLObj.put(eachSpecName, "Passed");
				}
			}
			curURLObj.put("Title", driver.getTitle());
			curURLObj.put("Galen Report", generateGalenReport(tests, curURLObj));
			addToJSONOutput("Galen Validation", curURLObj);
		}
		catch(Exception e) {
			System.out.println("Error : " + curURL + " - Galen validation");
		}
	}
	
	public void htmlValidations() throws Exception{
		try {
			waitJQueryAngular();
			JSONObject curURLObj = new JSONObject();
			curURLObj.put("URL", driver.getCurrentUrl());
			waitTillObjectVisibility(By.tagName("html"), 120, false);
			
			JavascriptExecutor js = (JavascriptExecutor)driver;
	    	String userAgent = (String)js.executeScript("return navigator.userAgent;");
	    	String pageText = "<!DOCTYPE HTML>\n" + (String)js.executeScript("return document.documentElement.outerHTML;");
	    	Document doc = Jsoup.parse(pageText);
	    	pageText = doc.outerHtml();
			HttpResponse<String> uniResponse = Unirest.post("http://localhost:8888/")
				    .header("User-Agent", userAgent)
				    .header("Content-Type", "text/html; charset=UTF-8")
				    .queryString("out", "html")
				    .body(pageText)
				    .asString();
			String response = uniResponse.getBody();
			Document output = Jsoup.parse(response);
			int errorSize = output.getElementsByClass("error").size();
			int warningSize = output.getElementsByClass("warning").size();
			curURLObj.put("Title", driver.getTitle());
			curURLObj.put("W3C HTML Report", generatehtmlReport(pageText, response, curURLObj));
			curURLObj.put("Errors", errorSize);
			curURLObj.put("Warnings", warningSize);
			addToJSONOutput("HTML Validation", curURLObj);
		}
		catch(Exception e) {
			System.out.println("Error : " + curURL + " - W3C HTML validation");
		}
	}
	
	public void sslValidations() throws Exception{
		try {
			waitJQueryAngular();
			JSONObject curURLObj = new JSONObject();
			String urlStr = driver.getCurrentUrl();
			curURLObj.put("URL", driver.getCurrentUrl());
			waitTillObjectVisibility(By.tagName("html"), 120, false);
			URL url = new URL(urlStr);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            try {
            	if(isSecure(con)) {
    				curURLObj.put("SSL Connection", "Yes");
    			}
    			else {
    				curURLObj.put("SSL Connection", "No");
    			}
        	}  catch (Exception e){
        		e.printStackTrace();
        	}
			curURLObj.put("Title", driver.getTitle());
			curURLObj.put("Screenshot", curScreenshot);
			addToJSONOutput("SSL Validation", curURLObj);
		}
		catch(Exception e) {
			System.out.println("Error : " + curURL + " - Text validation");
		}
	}
	
	public void htmlScraping() throws Exception{
		try {
			waitJQueryAngular();
			JSONObject curURLObj = new JSONObject();
			curURLObj.put("URL", driver.getCurrentUrl());
			waitTillObjectVisibility(By.tagName("html"), 120, false);
			JavascriptExecutor js = (JavascriptExecutor)driver;
			String pageText = "<!DOCTYPE HTML>\n" + (String)js.executeScript("return document.documentElement.outerHTML;");
	    	Document doc = Jsoup.parse(pageText);
	    	pageText = doc.outerHtml();
			
			curURLObj.put("Title", driver.getTitle());
			curURLObj.put("Screenshot", curScreenshot);
			curURLObj.put("Source", generatehtmlSource(pageText, curURLObj));
			addToJSONOutput("HTML Source", curURLObj);
		}
		catch(Exception e) {
			System.out.println("Error : " + curURL + " - Text validation");
		}
	}
	
	public void conditionalValidation() throws Exception{
		try {
			waitJQueryAngular();
			waitTillObjectVisibility(By.tagName("html"), 120, false);
			JSONArray conditionsToValidate = (JSONArray) getProperty("validation-settings>conditional");
			String pageText = driver.findElement(By.tagName("html")).getText();
			String pageTitle = driver.getTitle();
			for(Object eachObject : conditionsToValidate) {
				JSONObject curCondObject = (JSONObject)eachObject;
				String condType  = curCondObject.get("type").toString();
				String condActual  = curCondObject.get("compare").toString();
				String condValue  = curCondObject.get("value").toString();
				switch(condType) {
					case "url":
						if(conditionalCheck(curURL, condActual, condValue)) {
							doConditionalValidation(curCondObject);
						}
					break;
					case "title":
						if(conditionalCheck(pageTitle, condActual, condValue)) {
							doConditionalValidation(curCondObject);
						}
					break;
					case "text":
						if(conditionalCheck(pageText, "contains", condValue)) {
							doConditionalValidation(curCondObject);
						}
					break;
					case "element":
						if(waitTillObjectVisibility(getSelector(curCondObject.get("value")) , 1, false)) {
							doConditionalValidation(curCondObject);
						}
					break;
				}
			}
			
		}
		catch(Exception e) {
			
		}
	}
	
	public boolean conditionalCheck(String input, String type, String value) {
		boolean compareResult = false;
		if(type.equals("is")) {
			compareResult = input.equalsIgnoreCase(value);
		}
		else if(type.equals("contains")) {
			compareResult = input.contains(value);
		}
		return compareResult;
	}
	
	public void doConditionalValidation(JSONObject curCondObj) throws Exception{
		if(curCondObj.containsKey("text")) {
			try {
				JSONObject curURLObj = new JSONObject();
				curURLObj.put("URL", driver.getCurrentUrl());
				String pageText = driver.findElement(By.tagName("html")).getText();
				JSONArray textsToValidate = (JSONArray) curCondObj.get("text");
				for(Object eachObject : textsToValidate) {
					String eachTextToValidate = (String) eachObject; 
					if(pageText.contains(eachTextToValidate)) {
						curURLObj.put(eachTextToValidate, "Yes");
					}
					else {
						curURLObj.put(eachTextToValidate, "No");
					}
				}
				curURLObj.put("Title", driver.getTitle());
				curURLObj.put("Screenshot", curScreenshot);
				curURLObj.put("Type", "Text Validation");
				addToJSONOutput("Conditional Validation", curURLObj);
			}
			catch(Exception e) {
				System.out.println("Error : " + curURL + " - Text validation");
			}
		}
		else if(curCondObj.containsKey("element")) {
			try {
				JSONObject curURLObj = new JSONObject();
				curURLObj.put("URL", curURL);
				JSONArray elementsToValidate = (JSONArray) curCondObj.get("element");
				for(Object eachObject : elementsToValidate) {
					By eachElementToValidate = getSelector(eachObject); 
					if(waitTillObjectVisibility(eachElementToValidate, 1, false)) {
						curURLObj.put(((JSONObject)eachObject).get("by") + " : " + ((JSONObject)eachObject).get("selector"), "Yes");
					}
					else {
						curURLObj.put(((JSONObject)eachObject).get("by") + " : " + ((JSONObject)eachObject).get("selector"), "No");
					}
				}
				curURLObj.put("Title", driver.getTitle());
				curURLObj.put("Screenshot", curScreenshot);
				curURLObj.put("Type", "Element");
				addToJSONOutput("Conditional Validation", curURLObj);
			}
			catch(Exception e) {
				System.out.println("Error : " + curURL + " - Element validation");
			}
		}
		else if(curCondObj.containsKey("galen-specs")) {
			try{
				List<GalenTestInfo> tests=new LinkedList<GalenTestInfo>();
				JSONObject curURLObj = new JSONObject();
				curURLObj.put("URL", curURL);
				JSONArray specsToValidate = (JSONArray) getProperty("validation-settings>galen-specs");
				for(Object eachObject : specsToValidate) {
					String eachSpecToValidate = (String)((JSONObject) eachObject).get("file");
					String eachSpecName = (String)((JSONObject) eachObject).get("name"); 
					LayoutReport layoutReport=Galen.checkLayout(driver, eachSpecToValidate, Arrays.asList(new String[]{"desktop"}));
					GalenTestInfo test=GalenTestInfo.fromString(eachSpecName);
					test.getReport().layout(layoutReport, eachSpecName);
					tests.add(test);
					if(layoutReport.errors()>0) {
						curURLObj.put(eachSpecName, "Failed");
					}
					else {
						curURLObj.put(eachSpecName, "Passed");
					}
				}
				curURLObj.put("Title", driver.getTitle());
				curURLObj.put("Galen Report", generateGalenReport(tests, curURLObj));
				curURLObj.put("Type", "Galen");
				addToJSONOutput("Conditional Validation", curURLObj);
			}
			catch(Exception e) {
				System.out.println("Error : " + curURL + " - Galen validation");
			}
		}
	}
	
	public static boolean isSecure(HttpsURLConnection con) {
        try {
            con.getResponseCode(); // throws IllegalStateException if connected
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
