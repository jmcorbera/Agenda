package persistencia.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.ContactoDTO;
import persistencia.conexion.Conexion;
import persistencia.dao.interfaz.ContactoDAO;

public class ContactoDAOSQL implements ContactoDAO {

	private static final String insert = "INSERT INTO tipoContacto(nombreContacto) VALUES(?)";
	private static final String delete = "DELETE FROM tipoContacto WHERE nombreContacto = ?";
	private static final String readall = "SELECT * FROM tipoContacto";
	private static final String update = "UPDATE tipoContacto SET nombreContacto = ? WHERE nombreContacto = ? ";
	private static final String exists = "SELECT COUNT(*) FROM tipoContacto WHERE nombreContacto = ? ";
		
	public boolean insert(String contacto)
	{
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isInsertExitoso = false;
		try
		{
			
			statement = conexion.prepareStatement(insert);
		
			statement.setString(1, contacto);
			if(statement.executeUpdate() > 0)
			{
				conexion.commit();
				isInsertExitoso = true;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			try {
				conexion.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		return isInsertExitoso;
	}
	
	public boolean delete(String contacto_a_eliminar)
	{
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isdeleteExitoso = false;
		try 
		{
			statement = conexion.prepareStatement(delete);
			statement.setString(1, contacto_a_eliminar);
			if(statement.executeUpdate() > 0)
			{
				conexion.commit();
				isdeleteExitoso = true;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return isdeleteExitoso;
	}
	
	public boolean update(String contacto_a_cambiar, String nuevo_contacto)
	{
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isUpdateExitoso = false;
		try 
		{
			statement = conexion.prepareStatement(update);
			statement.setString(1, nuevo_contacto);
			statement.setString(2, contacto_a_cambiar);
			if(statement.executeUpdate() > 0)
			{
				conexion.commit();
				isUpdateExitoso = true;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return isUpdateExitoso;
	}
	
	public List<ContactoDTO> readAll()
	{
		PreparedStatement statement;
		ResultSet resultSet; //Guarda el resultado de la query
		ArrayList<ContactoDTO> contactos = new ArrayList<ContactoDTO>();
		Conexion conexion = Conexion.getConexion();
		try 
		{
			statement = conexion.getSQLConexion().prepareStatement(readall);
			resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				contactos.add(getContactoDTO(resultSet));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return contactos;
	}
	
	private ContactoDTO getContactoDTO(ResultSet resultSet) throws SQLException
	{
		String nombre = resultSet.getString("nombreContacto");
		return new ContactoDTO(nombre);
	}

	public boolean exists(String nombre) {
		PreparedStatement statement;
		ResultSet resultSet; //Guarda el resultado de la query
		Conexion conexion = Conexion.getConexion();
		try 
		{
			statement = conexion.getSQLConexion().prepareStatement(exists);
			statement.setString(1, nombre);
			resultSet = statement.executeQuery();
			resultSet.next();
			return resultSet.getInt(1) == 1;
			} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
}
