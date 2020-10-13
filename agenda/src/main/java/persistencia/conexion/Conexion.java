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
	private ConfiguracionBD configBD = ConfiguracionBD.getInstance();
	private Logger log = Logger.getLogger(Conexion.class);	
	private String url;
	private boolean estaConectado;
	
	public boolean conectar() {
		try {
			BasicConfigurator.configure();
			dbExist();
			
			url = "jdbc:mysql://"+configBD.obtenerProperty("ip") + ":"+ configBD.obtenerProperty("port") + "/" +"grupo_8" + "?autoReconnect=true&useSSL=false";
			connection = DriverManager.getConnection(url, configBD.obtenerProperty("user"),configBD.obtenerProperty("password"));
		
			this.connection = DriverManager.getConnection(this.url, configBD.obtenerProperty("user"), configBD.obtenerProperty("password"));
			this.connection.setAutoCommit(false);
			
			if(!estaConectado) {
				DAOSQLFactory factory = new DAOSQLFactory();
				if(DataBD.Initialize(factory, connection))
					return true;
				else
					return false;
			}
			
			estaConectado = true;
			log.info("Conexión exitosa");
			return true;
		} catch (Exception e) {
			estaConectado = false;
			log.error("Conexión fallida");
		}
		return false;
	}
	
	private boolean dbExist() 
	{	
		try {
			url = "jdbc:mysql://" +configBD.obtenerProperty("ip")+ ":" + configBD.obtenerProperty("port");	
			Properties properties = new Properties();
			properties.setProperty("allowPublicKeyRetrieval","true");
			properties.setProperty("user", configBD.obtenerProperty("user"));
			properties.setProperty("password", configBD.obtenerProperty("password"));
			properties.setProperty("useSSL", "false");
			properties.setProperty("serverTimezone", "UTC");
		
			// SQL command to create a database in MySQL.
			String sql = "CREATE DATABASE IF NOT EXISTS grupo_8";
		
			try {
				Connection conn = DriverManager.getConnection(url, properties);
				PreparedStatement stmt = conn.prepareStatement(sql); 
				stmt.execute();
				return true;
			} catch (Exception e) {
               return false;
           }
		}
		catch(NullPointerException e) {
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
	
	
}
