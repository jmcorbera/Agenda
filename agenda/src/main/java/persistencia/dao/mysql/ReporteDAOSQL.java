package persistencia.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import persistencia.conexion.Conexion;
import persistencia.dao.interfaz.ReporteDAO;
import dto.DomicilioDTO;
import dto.PersonaDTO;
import dto.ReporteDTO;

public class ReporteDAOSQL implements ReporteDAO
{
	private static final String readall = "SELECT * FROM personas";
	
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
		int id = resultSet.getInt("id");
		String nombre = resultSet.getString("nombre");
		String tel = resultSet.getString("telefono");
		String nacimiento = resultSet.getString("nacimiento");
		String email = resultSet.getString("email");
		String contactoId = resultSet.getString("contactoId");
		
		//String localidad = "San Miguel";
		int idLocalidad = 1;
		String calle = "Saint Exupery";
		String altura = "1637";
		String piso = "";
		
		PersonaDTO persona = new PersonaDTO(id, nombre, tel, nacimiento, email,contactoId);
		DomicilioDTO domicilio = new DomicilioDTO(id, 0, 0, idLocalidad, calle, altura, piso);
		
		return new ReporteDTO(persona, domicilio);
	}
}
