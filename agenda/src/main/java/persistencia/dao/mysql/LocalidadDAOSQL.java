package persistencia.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.LocalidadDTO;
import dto.PaisDTO;
import dto.ProvinciaDTO;
import persistencia.conexion.Conexion;
import persistencia.dao.interfaz.LocalidadDAO;

public class LocalidadDAOSQL implements LocalidadDAO {
	private static final String insert = "INSERT INTO localidades(nombre, provinciaId) VALUES(?, ?)";
	private static final String delete = "DELETE FROM localidades WHERE id = ?";
	private static final String update = "UPDATE localidades SET nombre = ?, provinciaId = ? WHERE id = ?";
	private static final String readall = "SELECT * FROM localidades ORDER BY nombre";
	private static final String groupByProvincia = "SELECT * FROM localidades WHERE provinciaId = ? ORDER BY nombre";
	
	private static final String ifExist = "SELECT EXISTS (SELECT 1 FROM localidades)";

	@Override
	public boolean insert(LocalidadDTO localidad) {
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isInsertExitoso = false;
		try
		{
			statement = conexion.prepareStatement(insert);
			statement.setString(1, localidad.getNombre());
			statement.setInt(2, localidad.getProvincia().getIdProvincia());
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
	public boolean delete(LocalidadDTO localidad_a_eliminar) {
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isdeleteExitoso = false;
		try 
		{
			statement = conexion.prepareStatement(delete);
			statement.setString(1, Integer.toString(localidad_a_eliminar.getIdLocalidad()));
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
	public boolean update(LocalidadDTO localidad_a_modificar) {
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isUpdateExitoso = false;
		try
		{
			statement = conexion.prepareStatement(update);
			statement.setString(1, localidad_a_modificar.getNombre());
			statement.setInt(2, localidad_a_modificar.getProvincia().getIdProvincia());
			statement.setInt(3, localidad_a_modificar.getIdLocalidad());
			if(statement.executeUpdate() > 0)
			{
				conexion.commit();
				isUpdateExitoso = true;
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
		
		return isUpdateExitoso;
	}

	@Override
	public List<LocalidadDTO> readAll() {
		PreparedStatement statement;
		ResultSet resultSet; 
		ArrayList<LocalidadDTO> localidades = new ArrayList<LocalidadDTO>();
		Conexion conexion = Conexion.getConexion();
		try 
		{
			statement = conexion.getSQLConexion().prepareStatement(readall);
			resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				localidades.add(getLocalidadDTO(resultSet));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return localidades;
	}

	private LocalidadDTO getLocalidadDTO(ResultSet resultSet) throws SQLException
	{
		int id = resultSet.getInt("id");
		String nombre = resultSet.getString("nombre");
		int provinciaId = resultSet.getInt("provinciaId");
		return new LocalidadDTO(id, nombre, getProvinciaDTO(provinciaId));
	}
	
	public ProvinciaDTO getProvinciaDTO(int id) throws SQLException {
		PreparedStatement statement;
		ResultSet resultSet = null;
		Conexion conexion = Conexion.getConexion();	
		String nombre = "";
		int paisId = 0;
		String readSingle = "SELECT * FROM provincias WHERE id = " + id + ";";
		try
		{
			statement = conexion.getSQLConexion().prepareStatement(readSingle);
			resultSet = statement.executeQuery();
			while(resultSet.next()) {
				nombre = resultSet.getString("nombre");
				paisId = resultSet.getInt("paisId");
			}

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	
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
	public List<LocalidadDTO> groupBy(ProvinciaDTO provincia) {
		PreparedStatement statement;
		ResultSet resultSet;
		ArrayList<LocalidadDTO> localidades = new ArrayList<LocalidadDTO>();
		
		if (provincia != null) {
		
			Conexion conexion = Conexion.getConexion();
			try 
			{
				statement = conexion.getSQLConexion().prepareStatement(groupByProvincia);
				statement.setInt(1, provincia.getIdProvincia());
				resultSet = statement.executeQuery();
				while(resultSet.next())
				{
					localidades.add(getLocalidadDTO(resultSet));
				}
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			
		}
		
		return localidades;
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
