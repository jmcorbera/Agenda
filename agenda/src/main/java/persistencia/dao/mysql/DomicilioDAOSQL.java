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
	private static final String insert = "INSERT INTO domicilios(id, paisId, provinciaId, localidadId, calle, altura, piso) VALUES(?,?, ?, ?,?,?,?)";
	private static final String delete = "DELETE FROM domicilios WHERE id = ?";
	private static final String readall = "SELECT id, paisId, provinciaId, localidadId, calle, altura, piso FROM domicilios";
	private static final String update = "UPDATE domicilios SET paisId = ?, provinciaId = ?, localidadId = ?, calle = ?, altura = ?, piso = ? WHERE id = ? ";
	private static final String get = "SELECT id, paisId, provinciaId, localidadId, calle, altura, piso FROM domicilios WHERE id = ?";
	
	public boolean insert(DomicilioDTO domicilio)
	{
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isInsertExitoso = false;
		try
		{
			//id, paisId, provinciaId, localidadId, calle, altura piso,
			statement = conexion.prepareStatement(insert);
			statement.setInt(1, domicilio.getId());
			statement.setInt(2, domicilio.getIdPais());
			if(domicilio.getIdProvincia() != -1)
				statement.setInt(3, domicilio.getIdProvincia());
			else
				statement.setObject(3, null);
			if(domicilio.getIdLocalidad() != -1)
				statement.setInt(4, domicilio.getIdLocalidad());
			else
				statement.setObject(4, null);
			statement.setString(5, domicilio.getCalle());
			statement.setString(6, domicilio.getAltura());
			statement.setString(7, domicilio.getPiso());
			
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
				statement.setInt(1, domicilio.getIdPais());
				if(domicilio.getIdProvincia() != -1)
					statement.setInt(2, domicilio.getIdProvincia());
				else
					statement.setObject(2, null);
				if(domicilio.getIdLocalidad() != -1)
					statement.setInt(3, domicilio.getIdLocalidad());
				else
					statement.setObject(3, null);
				statement.setString(4, domicilio.getCalle());
				statement.setString(5, domicilio.getAltura());
				statement.setString(6, domicilio.getPiso());
				statement.setInt(7, domicilio.getId());
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
	{
		int id = resultSet.getInt("id");
		int paisId = resultSet.getInt("paisId");
		int provinciaId = resultSet.getInt("provinciaId");
		int localidadId = resultSet.getInt("localidadId");
		String calle = resultSet.getString("calle");
		String altura = resultSet.getString("altura");
		String piso = resultSet.getString("piso");
		return new DomicilioDTO(id,paisId,provinciaId,localidadId, calle, altura, piso);
	}

	@Override
	public DomicilioDTO getDomicilio(int id) {
		PreparedStatement statement;
		ResultSet resultSet; //Guarda el resultado de la query
		Conexion conexion = Conexion.getConexion();
		try 
		{
			statement = conexion.getSQLConexion().prepareStatement(get);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			if(resultSet.next())
			{
				return getDomicilioDTO(resultSet);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}
