package persistencia.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import modelo.ConfiguracionBD;


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
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			
			if(this.cargarDatosConfiguracion())
			{
				this.url = "jdbc:mysql://" + this.ip + ":" + this.puerto + "/grupo_8?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
	        
				if(dbExist())
				{
					this.connection = DriverManager.getConnection(this.url, this.user, this.password);
					this.connection.setAutoCommit(false);
					log.info("Conexión exitosa"); 
					ret = true;
				}
	        }
		} catch (Exception e) {
			log.error("Conexión fallida", e);
		}
		return ret;
	}
	
	private boolean dbExist() 
	{
		boolean ret = true;
	
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
           } catch (Exception e) {
               e.printStackTrace();
               ret = false;
           }
        
		return ret;
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
		
		if(ConfiguracionBD.cargarConfiguracion());
		{
			this.ip = ConfiguracionBD.getIP();
			this.puerto = ConfiguracionBD.getPort();
			this.user = ConfiguracionBD.getUser();
			this.password = ConfiguracionBD.getPassword();
			return true;
		}
	}
}
