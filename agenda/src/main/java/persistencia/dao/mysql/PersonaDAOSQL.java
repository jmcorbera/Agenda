package persistencia.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import persistencia.conexion.Conexion;
import persistencia.dao.interfaz.PersonaDAO;
import dto.PersonaDTO;

public class PersonaDAOSQL implements PersonaDAO
{
	private static final String insert = "INSERT INTO personas(id, nombre, telefono, nacimiento, email, contactoId, contactoPreferente) VALUES(?, ?, ?, ?, ?, ?, ?)";
	private static final String delete = "DELETE FROM personas WHERE id = ?";
	private static final String readall = "SELECT * FROM personas ORDER BY id";
	private static final String update = "UPDATE personas SET nombre = ?, telefono = ?, nacimiento = ?, email = ?, contactoId = ?, contactoPreferente = ? WHERE id = ? ";

		
	public boolean insert(PersonaDTO persona)
	{
		if(persona.getNombre().isEmpty() || persona.getTelefono().isEmpty() && persona.getEmail().isEmpty()) {
			return false;
		}
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isInsertExitoso = false;
		try
		{
			
			statement = conexion.prepareStatement(insert);
			statement.setInt(1, persona.getId());
			statement.setString(2, persona.getNombre());
			statement.setString(3, persona.getTelefono());
			String fechaNacimiento = persona.getNacimiento();
			if (fechaNacimiento.isEmpty()) 
				statement.setString(4, null);
			else 
				statement.setString(4, persona.getNacimiento());
			statement.setString(5, persona.getEmail());
			if(!persona.getContactoId().isEmpty())
				statement.setString(6, persona.getContactoId());
			else
				statement.setObject(6,null);
			statement.setString(7, persona.getContactoPreferente());
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
	
	public boolean update(int idPersona, PersonaDTO persona)
	{
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isInsertExitoso = false;
		try
		{
			
			statement = conexion.prepareStatement(update);
			statement.setString(1, persona.getNombre());
			statement.setString(2, persona.getTelefono());
			String fechaNacimiento = persona.getNacimiento();
			if (fechaNacimiento == null || fechaNacimiento!= null & fechaNacimiento.equals("")) {
				statement.setString(3, null);
			}
			else {
				statement.setString(3, persona.getNacimiento());
			}
			statement.setString(4, persona.getEmail());

			statement.setString(5, persona.getContactoId());
			statement.setString(6, persona.getContactoPreferente());
			statement.setInt(7, idPersona);
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
	
	public boolean delete(PersonaDTO persona_a_eliminar)
	{
		PreparedStatement statement;
		Connection conexion = Conexion.getConexion().getSQLConexion();
		boolean isdeleteExitoso = false;
		try 
		{
			statement = conexion.prepareStatement(delete);
			statement.setString(1, Integer.toString(persona_a_eliminar.getId()));
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
	
	public List<PersonaDTO> readAll()
	{
		PreparedStatement statement;
		ResultSet resultSet; //Guarda el resultado de la query
		ArrayList<PersonaDTO> personas = new ArrayList<PersonaDTO>();
		Conexion conexion = Conexion.getConexion();
		try 
		{
			statement = conexion.getSQLConexion().prepareStatement(readall);
			resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				personas.add(getPersonaDTO(resultSet));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return personas;
	}
	
	private PersonaDTO getPersonaDTO(ResultSet resultSet) throws SQLException
	{
		int id = resultSet.getInt("id");
		String nombre = resultSet.getString("nombre");
		String tel = resultSet.getString("telefono");
		String nacimiento = resultSet.getString("nacimiento");
		String email = resultSet.getString("email");
		String contactoId = resultSet.getString("contactoId");
		String contactoPreferente = resultSet.getString("contactoPreferente");
		return new PersonaDTO(id, nombre, tel, nacimiento, email,contactoId, contactoPreferente);
	}
}
