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
	private static final String readall = "SELECT * FROM `personas` AS p,`domicilios` AS d,`provincias` AS pvs,`paises` as pss,`localidades` AS l WHERE  p.id = d.id AND d.provinciaId = pvs.id AND d.paisId = pss.id AND d.localidadId = l.id ORDER BY l.nombre, p.nombre;";
	
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
	
		int idLocalidad = resultSet.getInt("l.id");
		PersonaDTO persona = new PersonaDTO(id, nombre, tel, nacimiento, email,contactoId);
		LocalidadDAOSQL laux = new LocalidadDAOSQL();
		LocalidadDTO localidad = laux.getLocalidad(idLocalidad);
		return new ReporteDTO(persona, localidad);
	}
}
