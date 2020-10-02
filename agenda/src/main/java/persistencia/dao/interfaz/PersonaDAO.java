package persistencia.dao.interfaz;

import java.util.List;

import dto.PersonaDTO;

public interface PersonaDAO 
{
	
	public boolean insert(PersonaDTO persona);

	public boolean delete(PersonaDTO persona_a_eliminar);
	
	public List<PersonaDTO> readAll();
	
	public boolean update(int id, PersonaDTO persona_nueva);

	public boolean eliminarFecha(int id);

	public boolean borrrarTipoContacto(String seleccionado);
}
