package modelo;

import java.util.List;

import dto.ContactoDTO;
import dto.PersonaDTO;
import persistencia.dao.interfaz.ContactoDAO;
import persistencia.dao.interfaz.DAOAbstractFactory;
import persistencia.dao.interfaz.PersonaDAO;

public class Agenda 
{
	private PersonaDAO persona;	
	private ContactoDAO contacto;
	
	public Agenda(DAOAbstractFactory metodo_persistencia)
	{
		this.persona = metodo_persistencia.createPersonaDAO();
		this.contacto = metodo_persistencia.createContactoDAO();
	}
	
	public void agregarPersona(PersonaDTO nuevaPersona)
	{
		this.persona.insert(nuevaPersona);
	}

	public void borrarPersona(PersonaDTO persona_a_eliminar) 
	{
		this.persona.delete(persona_a_eliminar);
	}
	
	public List<PersonaDTO> obtenerPersonas()
	{
		return this.persona.readAll();		
	}
	public void agregarContacto(String nuevoContacto)
	{
		this.contacto.insert(nuevoContacto);
	}

	public void borrarContacto(String contacto_a_eliminar) 
	{
		this.contacto.delete(contacto_a_eliminar);
	}
	
	public List<ContactoDTO> obtenerContactos()
	{
		return this.contacto.readAll();		
	}

	public boolean editarContacto(String contacto_a_cambiar, String nuevo_contacto) {
		return this.contacto.update(contacto_a_cambiar, nuevo_contacto);
	}

	
}
