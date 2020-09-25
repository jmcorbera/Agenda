package persistencia.dao.interfaz;

import java.util.List;

import dto.ReporteDTO;

public interface ReporteDAO 
{	
	public List<ReporteDTO> readAllgroupBy();
}
