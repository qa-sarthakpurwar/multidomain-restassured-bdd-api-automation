package com.sarthak.api.utils;

import java.io.FileInputStream;
import java.util.Properties;

public class Utils {
	
	
	private static Properties properties = new Properties();
	
	  static {
	        try {
	            FileInputStream file = new FileInputStream(
	                "src\\test\\resources\\TestData\\TestData.properties");
	            properties.load(file);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	  
	  public static String getProperty(String key) {
	        return properties.getProperty(key);
	    }
	
	

}
