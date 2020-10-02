package persistencia.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import persistencia.conexion.Conexion;
import persistencia.dao.interfaz.ReporteDAO;
import dto.LocalidadDTO;
import dto.PersonaDTO;
import dto.ReporteDTO;

public class ReporteDAOSQL implements ReporteDAO
{
	private static final String readall = "SELECT p.id, p.nombre, p.telefono, p.nacimiento, p.email, p.contactoId, p.contactoPreferente, l.id FROM personas AS p LEFT JOIN domicilios AS d USING (id) LEFT JOIN localidades AS l ON d.localidadId = l.id;";
	
	public List<ReporteDTO> readAllgroupBy()
	{
		PreparedStatement statement;
		ResultSet resultSet; //Guarda el resultado de la query
		ArrayList<ReporteDTO> reporte = new ArrayList<ReporteDTO>();
		Conexion conexion = Conexion.getConexion();
		try 
		{
			statement = conexion.getSQLConexion().prepareStatement(readall);
			resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				reporte.add(getReporteDTO(resultSet));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return reporte;
	}
	
	private ReporteDTO getReporteDTO(ResultSet resultSet) throws SQLException
	{
		int id = resultSet.getInt("p.id");
		String nombre = resultSet.getString("p.nombre");
		String tel = resultSet.getString("p.telefono");
		String nacimiento = resultSet.getString("p.nacimiento");
		String email = resultSet.getString("p.email");
		String contactoId = resultSet.getString("p.contactoId");
		String contactoPreferente = resultSet.getString("p.contactoPreferente");
		
		PersonaDTO persona = new PersonaDTO(id, nombre, tel, nacimiento, email,contactoId, contactoPreferente);
		
		int idLocalidad = resultSet.getInt("l.id");
			
		LocalidadDTO localidad = new LocalidadDTO();
		
		if(idLocalidad != 0)
		{
			LocalidadDAOSQL laux = new LocalidadDAOSQL();
			localidad = laux.getLocalidad(idLocalidad);
		}
		else
		{
			localidad.setNombre("Desconocida");
		}

		return new ReporteDTO(persona, localidad);
	}
}
