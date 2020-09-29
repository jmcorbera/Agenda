package persistencia.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Conexion 
{
	public static Conexion instancia;
	private Connection connection;
	private Logger log = Logger.getLogger(Conexion.class);	
	
	private String url = "";
	private final static String user = "root";
	private final static String password = "root";	
	
	private Conexion()
	{
		try
		{			
			this.url = "jdbc:mysql://localhost:3306/grupo_8?useSSL=false&serverTimezone=UTC";
			//this.user = "root";
			//this.password = "root";
			
			Class.forName("com.mysql.cj.jdbc.Driver"); // quitar si no es necesario
	        
	        if(dbExist())
	        {
		        this.connection = DriverManager.getConnection(this.url, Conexion.user, Conexion.password);
			     
				this.connection.setAutoCommit(false);
				log.info("Conexión exitosa");    	
	        }
		}
		catch(Exception e)
		{
			log.error("Conexión fallida", e);
		}
	}
	
	private boolean dbExist() 
	{
		boolean ret = true;
		
		String url = "jdbc:mysql://localhost:3306";
		
		Properties properties = new Properties();
		properties.setProperty("user", Conexion.user);
		properties.setProperty("password", Conexion.password);
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
		if(instancia == null)
		{
			instancia = new Conexion();
		}
		return instancia;
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
		instancia = null;
	}
	
	public static String getUser() {
		return Conexion.user;
	}
	
	public static String getPassword() {
		return Conexion.password;
		
	}
}
