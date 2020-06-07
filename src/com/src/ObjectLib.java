package com.src;

import java.io.File;

import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;

public abstract class ObjectLib {
	public WebDriver driver;
	public JSONObject props;
	public Objects obj;
	public File outputFolder;
	public JSONObject jsonOutput;
	
	public ObjectLib(Objects obj) {
		this.obj = obj;
		this.driver = obj.driver;
		this.props = obj.props;
		this.outputFolder = obj.outputFolder;
		this.jsonOutput = obj.jsonOutput;
	}
}
