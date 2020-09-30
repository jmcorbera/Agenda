package dto;

import static org.junit.Assert.*;

import org.junit.Test;

import presentacion.vista.ContactoPreferente;

public class PersonaDTOTest {

	@Test
	public void personaValidaTest() {
		PersonaDTO persona = new PersonaDTO(0, "Nombre", "1142203912","2020/01/01","nombreMail@hotmail.com","",ContactoPreferente.Email.toString());
		assertTrue(persona.isValid().isEmpty());
	}
	
	@Test
	public void personaInvalidaTestEmailInvalidoFaltaArroba() {
		PersonaDTO persona = new PersonaDTO(0, "Nombre", "1142203912","2020/01/01","nombreMailhotmail.com","",ContactoPreferente.Email.toString());
		assertTrue(!persona.isValid().isEmpty());
	}
	
	@Test
	public void personaInvalidaTestEmailInvalidoFaltaPunto() {
		PersonaDTO persona = new PersonaDTO(0, "Nombre", "1142203912","2020/01/01","nombreMail@hotmailcom","",ContactoPreferente.Email.toString());
		assertTrue(!persona.isValid().isEmpty());
	}
	
	@Test
	public void personaInvalidaTestEmailInvalidoFaltaLetrasDespuesPunto() {
		PersonaDTO persona = new PersonaDTO(0, "Nombre", "1142203912","2020/01/01","nombreMailhotmail.","",ContactoPreferente.Email.toString());
		assertTrue(!persona.isValid().isEmpty());
	}
	
	@Test
	public void personaInvalidaTestFaltaFormaContacto() {
		PersonaDTO persona = new PersonaDTO(0, "Nombre", "","2020/01/01","","",ContactoPreferente.Email.toString());
		assertTrue(!persona.isValid().isEmpty());
	}
	
	@Test
	public void personaInvalidaTestFaltaNombre() {
		PersonaDTO persona = new PersonaDTO(0, "", "1142203912","2020/01/01","nombreMail@hotmail.com","",ContactoPreferente.Email.toString());
		assertTrue(!persona.isValid().isEmpty());
	}
	
	@Test
	public void personaInvalidaTestFaltaTelefono() {
		PersonaDTO persona = new PersonaDTO(0, "", "1142203912","2020/01/01","nombreMail@hotmail.com","",ContactoPreferente.Teléfono.toString());
		assertTrue(!persona.isValid().isEmpty());
	}

}
