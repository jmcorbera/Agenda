package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class ConfiguracionBD {
	private Logger log = Logger.getLogger(ConfiguracionBD.class);
	private static ConfiguracionBD INSTANCE;
	private Properties properties;
	private boolean existiaConfig;
	
	public static ConfiguracionBD getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ConfiguracionBD();
		return INSTANCE;
	}
	
	private ConfiguracionBD() {
		BasicConfigurator.configure();
		leerProperties();
	}

	public void guardar() {
		try {
			FileOutputStream fileOS = new FileOutputStream(System.getProperty("user.dir") + "/config.properties");
			properties.store(fileOS, null);
		} catch (Exception e) {
		}		
	}
	
	public Properties obtenerProperties() {
		return properties;
	}

	public String obtenerProperty(String key) {
		return properties.getProperty(key);
	}

	public void agregar(String key, String value) {
		properties.put(key, value);
	}

	private void leerProperties() {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(System.getProperty("user.dir") +"/config.properties"));
			existiaConfig = true;
		} catch (IOException e) {
			log.info("Creando nuevo archivo de configuración");
			File folder = new File("config");
			if (!folder.exists()) {
				folder.mkdir();
			}
			guardar();
			existiaConfig = false;
		}
	}
	
	public boolean existiaConfig() {
		return existiaConfig;
	}
}
