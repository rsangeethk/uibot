package com.src;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;


public class Crawler extends Commons
{
	private List<String> linkNamesClicked;
	public Crawler(Objects obj, List<String> linkNamesClicked) {
		super(obj);
		this.linkNamesClicked = linkNamesClicked;
	}
	
    private LinkedList<String> links = new LinkedList<String>();
    //private List<String> linkNames = new ArrayList<String>();


    public void crawl(String url, By root, List<?> pagesVisited) throws Exception
    {
        try
        {
        	waitJQueryAngular();
        	url = driver.getCurrentUrl();
        	waitJQueryAngular();
        	if(!waitTillObjectVisibility(root, 2, false)) {
        		root = By.tagName("body");
        	}
        	waitTillObjectVisibility(root, 10, false);
            List<WebElement> linksOnPage = driver.findElement(root).findElements(By.tagName("a")); 
            boolean pageLoaded = false;
            String baseURI = new URI((String)getProperty("website-details>URL")).getHost();
            for(int eachLink = 0; eachLink<=linksOnPage.size(); eachLink++)
            {
            	try {
            		if(pageLoaded){
            			linksOnPage = driver.findElement(root).findElements(By.tagName("a"));
            			pageLoaded = false;
            		}
	            	if(eachLink >= linksOnPage.size() - 1) {
	            		break;
	            	}
	            	WebElement currentEle = linksOnPage.get(eachLink);
	            	/*try {
	            		currentEle = driver.findElement(root).findElement(By.xpath("(.//a)["+eachLink+"]"));
	            	}
	            	catch(Exception e) {
	            		continue;
	            	}*/
	            	String curLinkText = ((String)((JavascriptExecutor) driver).executeScript("return arguments[0].innerText;",currentEle)).trim().replace("\n", "");
	            	if(!curLinkText.contains("Log") && !linkNamesClicked.contains(curLinkText)) {
	            		if(!curLinkText.equals("")) {
	            			linkNamesClicked.add(curLinkText);
	            			boolean foundLink = false;
	            			//String aHref = currentEle.getAttribute("href");
	            			String aHref = ((String)((JavascriptExecutor) driver).executeScript("return arguments[0].getAttribute('href');",currentEle));
	            			if(aHref!=null) {
	            				aHref = aHref.trim().replace("\n", "");
	            				if(!aHref.trim().equals("")) {
	            					foundLink = true;
	            					String newLink = "";
	            					if(!new URI(aHref).isAbsolute()) {
	            						if(new URI(aHref).getPath().startsWith("/")) {
	            							newLink = new URI(driver.getCurrentUrl()).getScheme() + "://" + new URI(driver.getCurrentUrl()).getHost().replace("/", "") + (new URI(aHref)).getPath();
	            						}
	            						else {
	            							newLink = new URI(driver.getCurrentUrl()).getScheme() + "://" + new URI(driver.getCurrentUrl()).getHost().replace("/", "") + "/" +(new URI(aHref)).getPath();
	            						}
	            						
	            					}
	            					else {
	            						newLink = aHref.trim();
	            					}
	            					try {
		            					if(!newLink.isEmpty()) {
			            					if(new URI(newLink).getHost().contains(baseURI) && !pagesVisited.contains(newLink) && !links.contains(newLink) && !newLink.endsWith(".pdf") && !newLink.endsWith(".json") && !newLink.endsWith(".xml")) {
							            		this.links.add(newLink);
							            	}
		            					}
	            					}
	            					catch(Exception e) {
	            						continue;
	            					}
	            				}
	            			}
	            			if(!foundLink) {
		            			jeClick(currentEle);
		            			pageLoaded = true;
				        		waitJQueryAngular();
				        		String curURL = driver.getCurrentUrl();
				        		try {
				            	if(new URI(curURL).getHost().contains(baseURI) && !pagesVisited.contains(curURL) && !links.contains(curURL) && !curURL.endsWith(".pdf") && !curURL.endsWith(".json") && !curURL.endsWith(".xml")) {
				            		this.links.add(curURL);
				            	}
				        		}
            					catch(Exception e) {
            						continue;
            					}
				            	if(!driver.getCurrentUrl().equals(url)) {
				            		navigateTo(url);
				            		pageLoaded = true;
					        		waitJQueryAngular();
					            	waitTillObjectVisibility(root, 10, false);
				            	}
	            			}
	            		}
	            	}
            	}
            	catch(Exception e) {
            		if(new URI(driver.getCurrentUrl()).getHost().contains(new URI((String)getProperty("website-details>URL")).getHost()) && !pagesVisited.contains(driver.getCurrentUrl()) && !links.contains(driver.getCurrentUrl())) {
	            		this.links.add(driver.getCurrentUrl());
	            	}
            		navigateTo(url);
            		pageLoaded = true;
	        		waitJQueryAngular();
	        		continue;
            	}
            }
            
        }
        catch(Exception ioe)
        {
        	System.out.println(url + "\tUnable to connect to page");
            //return false;
        }
        
    }


    public List<String> getLinks()
    {
        return this.links;
    }
    
    


}
