package persistencia.dao.interfaz;

import java.util.List;

import dto.PaisDTO;
import dto.ProvinciaDTO;

public interface ProvinciaDAO {
	
	public boolean insert(ProvinciaDTO provincia);

	public boolean delete(ProvinciaDTO provincia_a_eliminar);
	
	public boolean update(ProvinciaDTO provincia_a_modificar);
	
	public List<ProvinciaDTO> readAll();
	
	public boolean exists(String provincia, int paisId);
		
	public List<ProvinciaDTO> groupBy(PaisDTO pais);
	
	public boolean ifExist();

}
