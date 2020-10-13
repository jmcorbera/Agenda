package modelo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfiguracionBD {
	private static InputStream inputStream;
	
	private static Properties properties = new Properties();
	private static FileOutputStream outputStream;
	
	private static String propFileName = "/config.properties";
	
	public static ConfiguracionBD instance;
	
	public static boolean cargarConfiguracion() {
		boolean ret = false;
		try {
			inputStream = ConfiguracionBD.class.getResourceAsStream(propFileName);
			
			if (inputStream != null)
			{
				properties.load(inputStream);
				ret = true;
			}		
			else {
	           throw new FileNotFoundException(String.format("Archivo .properties no Encontrado"));
			}
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
	
	public static void GuardarConfig(String ip, String port, String user, String password) {
		try {
			if(properties.getProperty("ip")== null)
				properties.put("ip", ip);
			else
				properties.setProperty("ip", ip);
			if(properties.getProperty("port")==null)
				properties.put("port", port);
			else
				properties.setProperty("port", port);
			if(properties.getProperty("user")==null)
				properties.put("user",user);
			else
				properties.setProperty("user", user);
			if(properties.getProperty("password")==null)
				properties.put("password", password);
			else
				properties.setProperty("password", password);
			
			DataBD.class.getResourceAsStream(propFileName);
			File file = new File(DataBD.class.getResourceAsStream(propFileName).toString());		
			outputStream = new FileOutputStream(file);
					 	
			properties.store(outputStream, null);
			
			outputStream.close();
			
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
		return properties.getProperty("ip")!= null ? properties.getProperty("ip") : "";
	}
	
	public static String getPort() {
		return properties.getProperty("port")!= null ? properties.getProperty("port") : "";
	}
	
	public static String getUser() {
		return properties.getProperty("user")!= null ? properties.getProperty("user"): "";
	}
	
	public static String getPassword() {
		return properties.getProperty("password")!=null ? properties.getProperty("password"): "";
	}
	

}
