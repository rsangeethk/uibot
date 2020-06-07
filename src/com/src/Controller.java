package com.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;



public class Controller extends Commons
{
	  public Controller(Objects obj) {
			super(obj);
	  }
	  private int MAX_PAGES_TO_SEARCH = 1000;
	  private Set<String> pagesVisited = new HashSet<String>();
	  private List<String> pagesToVisit = new LinkedList<String>();
	  private List<String> linkNamesClicked = new ArrayList<String>();
	  StopWatch watch = new StopWatch();
	  
	  public void start() throws Exception{
		  String selectivePages = (String) getProperty("website-details>type");
		  System.out.println("Starting Validations");
		  if(selectivePages.equals("selective-pages")) {
			  selectivePageValidation();
		  }
		  else {
			  crawlnValidate();
		  }
	  }
	  
	  @SuppressWarnings("unchecked")
	public void crawlnValidate() throws Exception
	  {
		  waitJQueryAngular();
		  String url = driver.getCurrentUrl();
		  int count = 1;
		  
		  
		  if(getSelector(getProperty("website-details>header-element"))!=null) {
			  Crawler headerLeg = new Crawler(obj, linkNamesClicked);
			  headerLeg.crawl(url, getSelector(getProperty("website-details>header-element")), Arrays.asList(pagesVisited.toArray()));
			  this.pagesToVisit.addAll(headerLeg.getLinks());
		  }
		  if(getSelector(getProperty("website-details>footer-element"))!=null) {
			  Crawler footerLeg = new Crawler(obj, linkNamesClicked);
			  footerLeg.crawl(url, getSelector(getProperty("website-details>footer-element")), Arrays.asList(pagesVisited.toArray()));
			  this.pagesToVisit.addAll(footerLeg.getLinks());
		  }
		  By contentSelector;
		  if(getSelector(getProperty("website-details>content-element"))==null) {
			  contentSelector = By.tagName("body");
		  }
		  else {
			  contentSelector = getSelector(getProperty("website-details>content-element"));
		  }
		  MAX_PAGES_TO_SEARCH = Integer.parseInt((String)getProperty("website-details>maximum-pages"));
	      while(this.pagesVisited.size() < MAX_PAGES_TO_SEARCH && count < MAX_PAGES_TO_SEARCH)
	      {
	          String currentUrl;
	          Crawler leg = new Crawler(obj, linkNamesClicked);
	          if(this.pagesToVisit.isEmpty())
	          {
	              currentUrl = url;
	              this.pagesVisited.add(url);
	          }
	          else
	          {
	              currentUrl = this.nextUrl();
	          }
	          
	          if(currentUrl.equals("")) {
	        	  break;
	          }
	          
	          ArrayList<String> exceptionURLs = getExceptionURLs();
	          if(!exceptionURLs.contains(currentUrl)) {
	        	  	JSONObject curURLObj = new JSONObject();
	        	  	try {
		        	  	watch.reset();
	          			navigateTo(currentUrl);
	          			watch.start();
	          			waitJQueryAngular();
	          			watch.stop();
	          			JavascriptExecutor js = (JavascriptExecutor)driver;
	        	    	String doctype = (String)js.executeScript("return document.doctype.name");
        	    		if(doctype.equalsIgnoreCase("html")) {
		          			if(driver.getCurrentUrl().equals((String)getProperty("website-details>login-url"))) {
		          				new InitPage(obj);
		          				navigateTo(currentUrl);
		              			waitJQueryAngular();
		          			}
		          			waitJQueryAngular();
		          			String pageTitle = driver.getTitle();
		          			String curScreenshot = takeScreenshot(pageTitle);
		          			
			      			curURLObj.put("URL", currentUrl);
			      			curURLObj.put("Title", pageTitle);
			      			curURLObj.put("Screenshot", curScreenshot);
			      			curURLObj.put("Time to Load", TimeUnit.MILLISECONDS.toSeconds(watch.getTime()));
			      			addToJSONOutput("Performance", curURLObj);
			      			
		          			new Validator(obj, currentUrl, curScreenshot).init();
		          			Thread.sleep(1000);
		          			leg.crawl(currentUrl, contentSelector, Arrays.asList(pagesVisited.toArray()));
		          			this.pagesToVisit.addAll(leg.getLinks());
        	    		}
	        	  	}
	        	  	catch(Exception e) {
	        	  		if(isAlertPresent()) {
	        	  			driver.switchTo().alert().dismiss();
	        	  		}
	        	  		System.out.println("Error");
	        	  	}
          			count++;
	          }
	          System.out.println("Page to visit: " + pagesToVisit.size() + " - Page Visited: " + pagesVisited.size());
	          
	      }
	  }
	
	
	  private String nextUrl() throws Exception
	  {
	      String nextUrl = "";
	      do
	      {
	          nextUrl = this.pagesToVisit.remove(0);
	      } while(this.pagesVisited.contains(nextUrl) && this.pagesToVisit.size() > 0);
	      if(this.pagesToVisit.size()<=0) {
	    	  nextUrl = "";
	      }
	      else {
	    	  this.pagesVisited.add(nextUrl);
	      }
	      return nextUrl;
	  }
	  
	  @SuppressWarnings("unchecked")
	  private void selectivePageValidation() throws Exception{
		  JSONArray selectiveURLS = (JSONArray)getProperty("website-details>selective-pages");
		  ArrayList<String> exceptionURLs = getExceptionURLs();
		  for(Object eachPage : selectiveURLS) {
			  String curURL = (String) eachPage;
			  if(!exceptionURLs.contains(curURL) && !curURL.isEmpty()) {
				  try {
					  watch.reset();
					  navigateTo(curURL);
					  watch.start();
					  waitJQueryAngular();
					  watch.stop();
					  JavascriptExecutor js = (JavascriptExecutor)driver;
	        	      String doctype = (String)js.executeScript("return document.doctype.name");
        	    	  if(doctype.equalsIgnoreCase("html")) {
        	    		  	String pageTitle = driver.getTitle();
							String curScreenshot = takeScreenshot(pageTitle);
							JSONObject curURLObj = new JSONObject();
			      			curURLObj.put("URL", curURL);
			      			curURLObj.put("Title", pageTitle);
			      			curURLObj.put("Screenshot", curScreenshot);
			      			curURLObj.put("Time to Load", TimeUnit.MILLISECONDS.toSeconds(watch.getTime()));
			      			addToJSONOutput("Performance", curURLObj);
							Validator validator = new Validator(obj, curURL, curScreenshot);
							validator.init();
	        	      }
				  }
	        	  catch(Exception e) {
	        		if(isAlertPresent()) {
        	  			driver.switchTo().alert().dismiss();
        	  		}
        	  		System.out.println("Error");
	        	  }
			  }
		  }
	  }
	  
	  public boolean isAlertPresent() 
	  { 
	      try 
	      { 
	          driver.switchTo().alert(); 
	          return true; 
	      }   // try 
	      catch (NoAlertPresentException Ex) 
	      { 
	          return false; 
	      }   // catch 
	  }   // isAlertPresent()

}

