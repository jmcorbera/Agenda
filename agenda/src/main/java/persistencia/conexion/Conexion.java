package persistencia.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import modelo.ConfiguracionBD;
import modelo.DataBD;
import persistencia.dao.mysql.DAOSQLFactory;


public class Conexion 
{
	public static Conexion INSTANCE;
	private Connection connection;
	private Logger log = Logger.getLogger(Conexion.class);	
	private String url = "";
	private String ip = "";
	private String puerto = "";
	private String user = "";
	private String password = "";
	
	public boolean conectar() {
		boolean ret = false;
		try {	
			if(this.cargarDatosConfiguracion())
			{
				BasicConfigurator.configure();
				this.url = "jdbc:mysql://" + this.ip + ":" + this.puerto + "/grupo_8?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";	 
		
				if(dbExist())
				{
					log.info("Conexión exitosa"); 
					this.connection = DriverManager.getConnection(this.url, user, password);
					this.connection.setAutoCommit(false);
					ret = true;
					
					DAOSQLFactory factory = new DAOSQLFactory();
			    	if(DataBD.Initialize(factory, connection))
			    		return true;
			    	else
			    		return false;
				}
	        }
		} catch (Exception e) {
			log.error("Conexión fallida", e);
		}
		return ret;
	}
	
	private boolean dbExist() 
	{	
	
		String url = "jdbc:mysql://" + this.ip + ":" + this.puerto;	
		Properties properties = new Properties();
		properties.setProperty("allowPublicKeyRetrieval","true");
		properties.setProperty("user", this.user);
		properties.setProperty("password", this.password);
		properties.setProperty("useSSL", "false");
		properties.setProperty("serverTimezone", "UTC");
		
		// SQL command to create a database in MySQL.
        String sql = "CREATE DATABASE IF NOT EXISTS grupo_8";
		
        try (Connection conn = DriverManager.getConnection(url, properties);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
               stmt.execute();
               return true;
           } catch (Exception e) {
               e.printStackTrace();
               return false;
           }
        
      
	}
	
	public static Conexion getConexion()   
	{								
		if(INSTANCE == null)
		{
			INSTANCE = new Conexion();
		}
		return INSTANCE;
	}

	public Connection getSQLConexion() 
	{
		return this.connection;
	}
	
	public void cerrarConexion()
	{
		try 
		{
			this.connection.close();
			log.info("Conexion cerrada");
		}
		catch (SQLException e) 
		{
			log.error("Error al cerrar la conexión!", e);
		}
		INSTANCE = null;
	}
	
	private boolean cargarDatosConfiguracion() {
		if(ConfiguracionBD.cargarConfiguracion())
		{
			this.ip = ConfiguracionBD.getIP();
			this.puerto = ConfiguracionBD.getPort();
			this.user = ConfiguracionBD.getUser();
			this.password = ConfiguracionBD.getPassword();
			return true;
		}
		return false;
	}
}
