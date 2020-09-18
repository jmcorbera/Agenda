package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import presentacion.controlador.Controlador;

public class ControladorTest {

	@Test
	public void emailValidoTest() {
		assertTrue(Controlador.isValidEmail("nombre132_3.a@hotmail.com"));
	}
	
	@Test
	public void emailInvalidoTestSinPunto() {
		assertTrue(!Controlador.isValidEmail("nombre.123_apellido@hotmail"));
	}
	
	@Test
	public void emailInvalidoTestConPunto() {
		assertTrue(!Controlador.isValidEmail("nombre.123_apellido@hotmail."));
	}
	
	@Test
	public void emailInvalidoTestSinArroba() {
		assertTrue(!Controlador.isValidEmail("nombre.123_apellidohotmail.com"));
	}
	
	@Test
	public void emailInvalidoTestPuntoAntesArroba() {
		assertTrue(!Controlador.isValidEmail("nombre_apellido.@hotmail.com"));
	}
	
	@Test
	public void emailInvalidoTestGuionMedioAntesArroba() {
		assertTrue(!Controlador.isValidEmail("nombre_apellido-@hotmail.com"));
	}
	
	@Test
	public void emailInvalidoTestGuionBajoAntesArroba() {
		assertTrue(!Controlador.isValidEmail("nombre_apellido_@hotmail.com"));

	}

}
