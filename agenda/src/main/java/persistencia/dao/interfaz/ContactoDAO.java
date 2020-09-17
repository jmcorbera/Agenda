package persistencia.dao.interfaz;

import java.util.List;

import dto.ContactoDTO;

public interface ContactoDAO {
	
		public boolean insert(String nombre);
		
		public boolean update (String contacto_a_cambiar, String nuevo_contacto);

		public List<ContactoDTO> readAll();

		public boolean delete(String nombre);

}
