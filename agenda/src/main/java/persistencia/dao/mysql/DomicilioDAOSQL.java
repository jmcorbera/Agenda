package persistencia.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.DomicilioDTO;
import persistencia.conexion.Conexion;
import persistencia.dao.interfaz.DomicilioDAO;

public class DomicilioDAOSQL implements DomicilioDAO{
	private static final String insert = "INSERT INTO domicilios(id, pais, provincia, localidad, departamento, calle, altura, piso) VALUES(?,?,?,?,?,?,?,?)";
	private static final String delete = "DELETE FROM domicilios WHERE id = ?";
	private static final String readall = "SELECT * FROM domicilios";
	private static final String update = "UPDATE domicilios SET pais = ?, provincia = ?, localidad = ?, departamento = ?, calle = ?, altura = ?, piso = ? WHERE id = ? ";
		
	public boolean insert(DomicilioDTO domicilio)
	{
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isInsertExitoso = false;
		try
		{
			
			statement = conexion.prepareStatement(insert);
		
			statement.setInt(1, domicilio.getId());
			statement.setString(2, domicilio.getPais());
			statement.setString(3, domicilio.getProvincia());
			statement.setString(4, domicilio.getLocalidad());
			statement.setString(5, domicilio.getDepartamento());
			statement.setString(6, domicilio.getCalle());
			statement.setString(7, domicilio.getAltura());
			statement.setString(8, domicilio.getPiso());
			
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
	
	public boolean delete(int id)
	{
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isdeleteExitoso = false;
		try 
		{
			statement = conexion.prepareStatement(delete);
			statement.setInt(1, id);
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
	
	public boolean update(DomicilioDTO domicilio)
	{
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isUpdateExitoso = false;
		try 
		{
			statement = conexion.prepareStatement(update);
			statement.setString(1, domicilio.getPais());
			statement.setString(2, domicilio.getProvincia());
			statement.setString(3, domicilio.getLocalidad());
			statement.setString(4, domicilio.getDepartamento());
			statement.setString(5, domicilio.getCalle());
			statement.setString(6, domicilio.getAltura());
			statement.setString(7, domicilio.getPiso());
			statement.setInt(8, domicilio.getId());
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
	
	public List<DomicilioDTO> readAll()
	{
		PreparedStatement statement;
		ResultSet resultSet; //Guarda el resultado de la query
		ArrayList<DomicilioDTO> domicilios = new ArrayList<DomicilioDTO>();
		Conexion conexion = Conexion.getConexion();
		try 
		{
			statement = conexion.getSQLConexion().prepareStatement(readall);
			resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				domicilios.add(getDomicilioDTO(resultSet));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return domicilios;
	}
	
	private DomicilioDTO getDomicilioDTO(ResultSet resultSet) throws SQLException
	{	int id = resultSet.getInt("id");
		String pais = resultSet.getString("pais");
		String provincia = resultSet.getString("provincia");
		String localidad = resultSet.getString("localidad");
		String departamento = resultSet.getString("departamento");
		String calle = resultSet.getString("calle");
		String altura = resultSet.getString("altura");
		String piso = resultSet.getString("piso");
		return new DomicilioDTO(id, pais, provincia, localidad, departamento, calle, altura, piso);
	}
}
