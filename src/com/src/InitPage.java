package com.src;

/*import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;*/

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class InitPage extends Commons{
	
	public InitPage(Objects obj) throws Exception {
		super(obj);
		//loginToMP();
		//loginToEP();
		driver.get((String) getProperty("website-details>URL"));
		waitJQueryAngular();
	}
	
	/*private static void addSoftwareLibrary(File file) throws Exception {
	    Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
	    method.setAccessible(true);
	    method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{file.toURI().toURL()});
	}*/
	public boolean loginToMP() throws Exception 
	{
		System.out.println("Logged in!");
		boolean login=false;
    	driver.get((String) getProperty("website-details>URL"));
    	waitTillObjectVisibility(By.id("login_username"), 10, false);
    	WebElement userid=driver.findElement(By.id("login_username"));
    	userid.click();
    	userid.sendKeys("farshi12");
		driver.findElement(By.name("submitBtn")).click();
		waitJQueryAngular();
		waitTillObjectVisibility(By.xpath(".//*[@id='login_password']"), 5, false);
		driver.findElement(By.xpath(".//*[@id='login_password']")).sendKeys("Test123");
		Thread.sleep(1000);
		driver.findElement(By.name("sub_but")).click();
		if(waitTillObjectVisibility(By.xpath("//p[contains(.,'The Username')]"), 5, false)) {
			driver.findElement(By.xpath(".//*[@id='login_password']")).sendKeys("Test123");
			driver.findElement(By.name("sub_but")).click();
		}
		if(waitTillObjectVisibility(By.name("challengeAns1"), 5, false)) {
			Thread.sleep(1000);
			driver.findElement(By.name("challengeAns1")).sendKeys("a");
			driver.findElement(By.name("challengeAns2")).sendKeys("a");
			driver.findElement(By.xpath(".//button[contains(.,'Submit')]")).click();
		}
		Thread.sleep(3000);
		waitJQueryAngular();
		if(waitTillObjectVisibility(By.xpath(".//*[@id='oo_no_thanks']"), 5, false)) {
			driver.findElement(By.xpath(".//*[@id='oo_no_thanks']")).click();
		}
		return login;
	}
	
	public boolean loginToEP() throws Exception 
	{
		boolean login=false;
    	driver.get((String) getProperty("website-details>URL"));
    	waitTillObjectVisibility(By.id("userId"), 10, false);
    	WebElement userid=driver.findElement(By.id("userId"));
    	userid.click();
    	userid.sendKeys("user_3nza");
		//driver.findElement(By.name("submitBtn")).click();
		//waitJQueryAngular();
		//waitTillObjectVisibility(By.xpath(".//*[@id='login_password']"), 5, false);
		driver.findElement(By.xpath(".//*[@id='password']")).sendKeys("Test123");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[contains(.,'Log In')]")).click();
		/*if(waitTillObjectVisibility(By.xpath("//p[contains(.,'The Username')]"), 5, false)) {
			driver.findElement(By.xpath(".//*[@id='login_password']")).sendKeys("Test123");
			driver.findElement(By.name("sub_but")).click();
		}
		if(waitTillObjectVisibility(By.name("challengeAns1"), 5, false)) {
			Thread.sleep(1000);
			driver.findElement(By.name("challengeAns1")).sendKeys("a");
			driver.findElement(By.name("challengeAns2")).sendKeys("a");
			driver.findElement(By.xpath(".//button[contains(.,'Submit')]")).click();
		}
		Thread.sleep(3000);*/
		waitJQueryAngular();
		/*if(waitTillObjectVisibility(By.xpath(".//*[@id='oo_no_thanks']"), 5, false)) {
			driver.findElement(By.xpath(".//*[@id='oo_no_thanks']")).click();
		}*/
		return login;
	}
}
