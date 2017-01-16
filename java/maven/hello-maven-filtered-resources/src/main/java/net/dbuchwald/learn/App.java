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
	String propertiesFileName="/App.properties";

	InputStream inputStream = App.class.getResourceAsStream(propertiesFileName);

	if (inputStream == null)
	{
       		System.out.println("Property file was not found!");
	} else {
		try {
			prop.load(inputStream);
			System.out.println("Message retrieved from properties file:");
                	System.out.println("  " + prop.getProperty("hello.message"));
                        System.out.println("Application version:");
                        System.out.println("  " + prop.getProperty("hello.app.version"));
			System.out.println("External resource message:");
                        System.out.println("  " + prop.getProperty("external.hello.message"));
                        System.out.println("POM resource message:");
                        System.out.println("  " + prop.getProperty("pom.hello.message"));
                        System.out.println("Command line message:");
                        System.out.println("  " + prop.getProperty("build.param.message"));
		} catch (IOException e) {
			System.out.println("Unable to load property file: " + e.getMessage());
		}
	}
    }
}
