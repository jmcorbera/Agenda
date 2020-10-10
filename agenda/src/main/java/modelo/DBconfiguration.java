package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class DBconfiguration {
	private static InputStream inputStream;
	
	private static Properties properties = new Properties();
	private static FileOutputStream outputStream;
	
	private static String propFileName = "/config.properties";
	
	public static DBconfiguration instance;
	
	public static boolean cargarConfiguracion() {
		boolean ret = false;
		
		try {		
			//inputStream = new FileInputStream("src/main/resources/" + propFileName);
			inputStream = DBconfiguration.class.getResourceAsStream(propFileName);
			
			if (inputStream != null)
			{
				properties.load(inputStream);
				ret = true;
			}		
			else
				throw new FileNotFoundException(String.format("La Property file '%s' no existe", propFileName));
			
		} catch (Exception e) {
			
			System.out.println("Exception: " + e);
			
		} finally {
			
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return ret;		
	}
	
	public static void GuardarConfig(String ip, String puerto, String usuario, String contraseña) {
		
		try {
			
			properties.setProperty("db_ip", ip);
			properties.setProperty("db_port", puerto);
			properties.setProperty("db_user", usuario);
			properties.setProperty("db_password", contraseña);
			
			//outputStream = new FileOutputStream("src/main/resources/" + propFileName);
			
			URL resourceUrl = DBdata.class.getResource(propFileName);
			File file = new File(resourceUrl.toURI());		
			outputStream = new FileOutputStream(file);
					 	
			properties.store(outputStream, null);
			
			outputStream.close();
			
			System.out.println("Almacenando configuracion: " + properties);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if (outputStream != null)
				
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			outputStream = null;
			
		}
		
	}
	
	public static String getIP() {
		return properties.getProperty("db_ip");
	}
	
	public static String getPort() {
		return properties.getProperty("db_port");
	}
	
	public static String getUser() {
		return properties.getProperty("db_user");
	}
	
	public static String getPassword() {
		return properties.getProperty("db_password");
	}
	

}
