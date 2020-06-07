package com.src;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;

public class Objects {
	public WebDriver driver;
	public JSONObject props;
	public File outputFolder;
	public JSONObject jsonOutput;
	public String inputPath;
	
	@SuppressWarnings("unchecked")
	public Objects(WebDriver driver, JSONObject props, String inputPath) {
		this.driver = driver;
		this.props = props;
		this.inputPath = inputPath;
		this.outputFolder = initResultFolder();
		JSONObject main = (JSONObject) new JSONObject();
		main.put("Text Validation", new JSONArray());
		main.put("Element Validation", new JSONArray());
		main.put("Galen Validation", new JSONArray());
		main.put("Performance", new JSONArray());
		main.put("Conditional Validation", new JSONArray());
		main.put("HTML Validation", new JSONArray());
		main.put("SSL Validation", new JSONArray());
		main.put("HTML Source", new JSONArray());
		this.jsonOutput = main;
	}
	
	public File initResultFolder() {
    	String folderName = "Run_" + new SimpleDateFormat("MM_dd_mm_ss_SS").format(new Date());
    	String inputFolder = inputPath.replace(".uibot", "");
    	File inputFolderObj = new File(inputFolder);
    	if(!inputFolderObj.exists()) inputFolderObj.mkdir(); 
    	new File(inputFolderObj + "\\" + folderName).mkdir();
    	new File(inputFolderObj + "\\" + folderName + "\\Screenshots").mkdir();
    	new File(inputFolderObj + "\\" + folderName + "\\Galen Reports").mkdir();
    	new File(inputFolderObj + "\\" + folderName + "\\res").mkdir();
    	return new File(inputFolderObj + "\\" + folderName);
    }
}
