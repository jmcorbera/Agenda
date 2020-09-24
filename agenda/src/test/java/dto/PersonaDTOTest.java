package dto;

import static org.junit.Assert.*;

import org.junit.Test;

public class PersonaDTOTest {

	@Test
	public void personaValidaTest() {
		PersonaDTO persona = new PersonaDTO(0, "Nombre", "1142203912","2020/01/01","nombreMail@hotmail.com","");
		assertTrue(persona.isValid().isEmpty());
	}
	
	@Test
	public void personaInvalidaTestEmailInvalidoFaltaArroba() {
		PersonaDTO persona = new PersonaDTO(0, "Nombre", "1142203912","2020/01/01","nombreMailhotmail.com","");
		assertTrue(!persona.isValid().isEmpty());
	}
	
	@Test
	public void personaInvalidaTestEmailInvalidoFaltaPunto() {
		PersonaDTO persona = new PersonaDTO(0, "Nombre", "1142203912","2020/01/01","nombreMail@hotmailcom","");
		assertTrue(!persona.isValid().isEmpty());
	}
	
	@Test
	public void personaInvalidaTestEmailInvalidoFaltaLetrasDespuesPunto() {
		PersonaDTO persona = new PersonaDTO(0, "Nombre", "1142203912","2020/01/01","nombreMailhotmail.","");
		assertTrue(!persona.isValid().isEmpty());
	}
	
	@Test
	public void personaInvalidaTestFaltaFormaContacto() {
		PersonaDTO persona = new PersonaDTO(0, "Nombre", "","2020/01/01","","");
		assertTrue(!persona.isValid().isEmpty());
	}
	
	@Test
	public void personaInvalidaTestFaltaNombre() {
		PersonaDTO persona = new PersonaDTO(0, "", "1142203912","2020/01/01","nombreMail@hotmail.com","");
		assertTrue(!persona.isValid().isEmpty());
	}

}
