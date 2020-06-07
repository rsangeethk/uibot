package com.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.src.Starter;
import com.view.MainUI;


public class Settings {
	JSONObject jsonObj;
	String testSettingsPath = "";
	MainUI mainUI;
	String projectPath = "";
	public Settings(String projectPath, MainUI mainUI) throws Exception {
		this.projectPath = projectPath;
		this.jsonObj = (JSONObject) new JSONObject();
		this.mainUI = mainUI;
		initSettings();
	}
	
	public Settings(String path, String projectPath, MainUI mainUI) throws Exception {
		this.projectPath = projectPath;
		this.testSettingsPath = path;
		this.mainUI = mainUI;
		FileReader reader = new FileReader(testSettingsPath); 
		JSONParser jsonParser = new JSONParser(); 
		this.jsonObj = (JSONObject) jsonParser.parse(reader);
	}
	
	@SuppressWarnings("unchecked")
	public void initSettings() throws Exception {
		String toolPath = "";
		if(new File(new File(Starter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParentFile().getPath() + "\\Drivers").exists()) {
			toolPath = new File(Starter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParentFile().getPath();
		}
		else {
			toolPath = new File(Starter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath();
		}
		//String toolPath = new File(Starter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath();
		JSONObject firefoxObj = new JSONObject();
		firefoxObj.put("binary", "");
		firefoxObj.put("driver", toolPath + "\\Drivers\\geckodriver.exe");
		JSONObject chromeObj = new JSONObject();
		chromeObj.put("binary", "");
		chromeObj.put("driver", toolPath + "\\Drivers\\chromedriver.exe");
		chromeObj.put("headless", false);
		JSONObject ieObj = new JSONObject();
		ieObj.put("binary", "");
		ieObj.put("driver", toolPath + "\\Drivers\\IEDriverServer.exe");
		JSONObject rwObj = new JSONObject();
		rwObj.put("url", "");
		
		JSONObject selSettings = new JSONObject(); 
		selSettings.put("firefox", firefoxObj);
		selSettings.put("chrome", chromeObj);
		selSettings.put("ie", ieObj);
		selSettings.put("remote-driver", rwObj);
				
		
		JSONObject headerElement = new JSONObject();
		headerElement.put("by", "");
		headerElement.put("selector", "");
		
		JSONObject contentElement = new JSONObject();
		contentElement.put("by", "");
		contentElement.put("selector", "");
		
		JSONObject footerElement = new JSONObject();
		footerElement.put("by", "");
		footerElement.put("selector", "");
		
		JSONArray selectivePagesArray = new JSONArray();
		
		JSONObject websiteDetails = new JSONObject(); 
		websiteDetails.put("header-element", headerElement);
		websiteDetails.put("footer-element", footerElement);
		websiteDetails.put("content-element", contentElement);
		websiteDetails.put("selective-pages", selectivePagesArray);
		websiteDetails.put("angular-wait", false);
		websiteDetails.put("jquery-wait", false);
		websiteDetails.put("init-login", false);
		websiteDetails.put("source", false);
		
		
		JSONArray linkNamesArray = new JSONArray();
		JSONArray linkURLSArray = new JSONArray();
		JSONObject exceptions = new JSONObject();
		exceptions.put("link-names", linkNamesArray);
		exceptions.put("link-urls", linkURLSArray);
		
		
		JSONArray textsArray = new JSONArray();
		JSONArray elementArray = new JSONArray();
		JSONArray galenSpecsArray = new JSONArray();
		JSONObject validationSettingsObj = new JSONObject();
		validationSettingsObj.put("text", textsArray);
		validationSettingsObj.put("element", elementArray);
		validationSettingsObj.put("galen-specs", galenSpecsArray);
		validationSettingsObj.put("conditional", new JSONArray());
		validationSettingsObj.put("html", false);
		validationSettingsObj.put("ssl", false);
		
		
		JSONObject nameObj = new JSONObject();
		nameObj.put("name", "");
		
		JSONObject browserObj = new JSONObject();
		browserObj.put("browser", "");
		
		jsonObj.put("crawl-test", nameObj);
		jsonObj.put("website-details", websiteDetails);
		jsonObj.put("exceptions", exceptions);
		jsonObj.put("selenium-settings", selSettings);
		jsonObj.put("validation-settings", validationSettingsObj);
		jsonObj.put("run-settings", browserObj);
	}
	
	public Object getProperty(String object) {
    	Object foundObject = (Object) jsonObj;
    	for(String eacjObject : object.split(">"))
    	{
    		foundObject = ((JSONObject)foundObject).get(eacjObject);
    	}
    	return foundObject;
    }
	
	public String getProjectpath() {
		return projectPath;
	}
	
	public String getTestpath() {
		return testSettingsPath;
	}
	
	@SuppressWarnings("resource")
	public void writeTestFile(String name) {
		try {
			String jsonOutputStr = jsonObj.toJSONString()/*.replaceAll("\\\\", "")*/;
			testSettingsPath = projectPath + "\\" + name + ".uibot";
			FileWriter outputFile = new FileWriter(testSettingsPath);
			outputFile.write(jsonOutputStr);
			outputFile.flush();
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error creating new Crawl test");
		}
		
	}
	
	@SuppressWarnings("resource")
	public void writeTestFile() {
		try {
			String jsonOutputStr = jsonObj.toJSONString()/*.replaceAll("\\\\", "")*/;
			FileWriter outputFile = new FileWriter(testSettingsPath);
			outputFile.write(jsonOutputStr);
			outputFile.flush();
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error creating new Crawl test");
		}
		
	}
	
	public MainUI getMainWindow() {
		return mainUI;
	}
	
}
