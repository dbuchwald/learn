package net.dbuchwald.learn;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
	Properties prop=new Properties();
	String propertiesFileName="App.properties";

	InputStream inputStream = App.class.getClassLoader().getResourceAsStream(propertiesFileName);

	if (inputStream == null)
	{
       		System.out.println("Property file was not found!");
	} else {
		try {
			prop.load(inputStream);
			System.out.println("Message retrieved from properties file:");
                	System.out.println(prop.getProperty("hello.message"));
		} catch (IOException e) {
			System.out.println("Unable to load property file: " + e.getMessage());
		}
	}
    }
}
