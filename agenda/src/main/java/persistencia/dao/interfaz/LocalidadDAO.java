package persistencia.dao.interfaz;

import java.util.List;

import dto.LocalidadDTO;
import dto.ProvinciaDTO;

public interface LocalidadDAO {
	
	public boolean insert(LocalidadDTO localidad);

	public boolean delete(LocalidadDTO localidad_a_eliminar);
	
	public boolean update(LocalidadDTO localidad_a_modificar);
	
	public List<LocalidadDTO> readAll();
	
	public boolean exists(String localidad, int provinciaId);

	public List<LocalidadDTO> groupBy(ProvinciaDTO provincia);
	
	public boolean ifExist();

}
