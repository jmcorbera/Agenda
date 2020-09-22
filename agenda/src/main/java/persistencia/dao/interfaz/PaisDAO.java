package persistencia.dao.interfaz;

import java.util.List;

import dto.PaisDTO;

public interface PaisDAO {
	
	public boolean insert(PaisDTO pais);

	public boolean delete(PaisDTO pais_a_eliminar);
	
	public boolean update(PaisDTO pais_a_modificar);
	
	public List<PaisDTO> readAll();
	
	public boolean exists(String pais);
	
	public boolean ifExist();
	
}
