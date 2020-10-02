package persistencia.dao.interfaz;

import java.util.List;

import dto.DomicilioDTO;

public interface DomicilioDAO {
	
			public boolean insert(DomicilioDTO domicilio);
			
			public boolean update (DomicilioDTO domicilio);

			public List<DomicilioDTO> readAll();

			public boolean delete(int id);
			
			public DomicilioDTO getDomicilio (int id);
}
