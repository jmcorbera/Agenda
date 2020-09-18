package persistencia.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.PaisDTO;
import dto.ProvinciaDTO;
import persistencia.conexion.Conexion;
import persistencia.dao.interfaz.ProvinciaDAO;

public class ProvinciaDAOSQL implements ProvinciaDAO {
	
	private static final String insert = "INSERT INTO provincias(nombre, paisId) VALUES(?, ?)";
	private static final String delete = "DELETE FROM provincias WHERE id = ?";
	private static final String update = "UPDATE provincias SET nombre = ?, paisId = ? WHERE id = ?";
	private static final String readall = "SELECT * FROM provincias ORDER BY nombre";
	private static final String groupByPais = "SELECT * FROM provincias WHERE paisId = ? ORDER BY nombre";
	
	private static final String ifExist = "SELECT EXISTS (SELECT 1 FROM provincias)";

	@Override
	public boolean insert(ProvinciaDTO provincia) {
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isInsertExitoso = false;
		
		try
		{			
			statement = conexion.prepareStatement(insert);
			statement.setString(1, provincia.getNombre());
			statement.setInt(2, provincia.getPaís().getIdPais());
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

	@Override
	public boolean delete(ProvinciaDTO provincia_a_eliminar) {
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isdeleteExitoso = false;
		try 
		{
			statement = conexion.prepareStatement(delete);
			statement.setInt(1, provincia_a_eliminar.getIdProvincia());
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

	@Override
	public boolean update(ProvinciaDTO provincia_a_modificar) {
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isUpdateExitoso = false;
		try 
		{
			statement = conexion.prepareStatement(update);
			statement.setString(1, provincia_a_modificar.getNombre());
			statement.setInt(2, provincia_a_modificar.getPaís().getIdPais());
			statement.setInt(3, provincia_a_modificar.getIdProvincia());
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

	@Override
	public List<ProvinciaDTO> readAll() {
		PreparedStatement statement;
		ResultSet resultSet;
		ArrayList<ProvinciaDTO> provincias = new ArrayList<ProvinciaDTO>();
		Conexion conexion = Conexion.getConexion();
		try 
		{
			statement = conexion.getSQLConexion().prepareStatement(readall);
			resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				provincias.add(getProvinciaDTO(resultSet));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return provincias;
	}
	
	private ProvinciaDTO getProvinciaDTO(ResultSet resultSet) throws SQLException
	{
		int id = resultSet.getInt("id");
		String nombre = resultSet.getString("nombre");
		int paisId = resultSet.getInt("paisId");
		return new ProvinciaDTO(id, nombre, getPaisDTO(paisId));
	}
	
	private PaisDTO getPaisDTO(int id) {
		PreparedStatement statement;
		ResultSet resultSet = null;
		Conexion conexion = Conexion.getConexion();	
		String nombre = "";
		String readSingle = "SELECT * FROM paises WHERE id = " + id + ";";
		try
		{
			statement = conexion.getSQLConexion().prepareStatement(readSingle);
			resultSet = statement.executeQuery();
			
			while (resultSet.next())
				nombre = resultSet.getString("nombre");

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	
		return new PaisDTO(id, nombre);
	}

	@Override
	public List<ProvinciaDTO> groupBy(PaisDTO pais) {
		PreparedStatement statement;
		ResultSet resultSet;
		ArrayList<ProvinciaDTO> provincias = new ArrayList<ProvinciaDTO>();
		
		if (pais != null) {
		
			Conexion conexion = Conexion.getConexion();
			try 
			{
				statement = conexion.getSQLConexion().prepareStatement(groupByPais);
				statement.setInt(1, pais.getIdPais());
				resultSet = statement.executeQuery();
				while(resultSet.next())
				{
					provincias.add(getProvinciaDTO(resultSet));
				}
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			
		}
		
		return provincias;
	}
	
	@Override
	public boolean ifExist() {
		PreparedStatement statement;
		ResultSet resultSet;
		Conexion conexion = Conexion.getConexion();
		boolean dataExists = false;
		try 
		{
			statement = conexion.getSQLConexion().prepareStatement(ifExist);
			resultSet = statement.executeQuery();
			resultSet.next();
			dataExists = resultSet.getInt(1) == 1 ? true : false;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return dataExists;
	}

}
