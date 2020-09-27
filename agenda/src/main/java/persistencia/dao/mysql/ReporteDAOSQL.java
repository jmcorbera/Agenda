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
	//private static final String readall = "SELECT * FROM personas";
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
		
		//String localidad = "San Miguel";
		/*int idLocalidad = 1;
		String calle = "Saint Exupery";
		String altura = "1637";
		String piso = "";
		*/
		int idDomicilio = resultSet.getInt("d.id");
		int idPais = resultSet.getInt("d.paisId");
		int idProvincia = resultSet.getInt("d.provinciaId");
		int idLocalidad = resultSet.getInt("d.localidadId");
		String calle = resultSet.getString("d.calle");
		String altura = resultSet.getString("d.altura");
		String piso = resultSet.getString("d.piso");
		PersonaDTO persona = new PersonaDTO(id, nombre, tel, nacimiento, email,contactoId);
		DomicilioDTO domicilio = new DomicilioDTO(idDomicilio, idPais, idProvincia, idLocalidad, calle, altura, piso);
		return new ReporteDTO(persona, domicilio);
	}
}
