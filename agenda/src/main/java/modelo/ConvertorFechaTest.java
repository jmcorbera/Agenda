package modelo;

import static org.junit.Assert.*;
import java.sql.Date;
import org.junit.Test;

public class ConvertorFechaTest {

	@Test
	public void fechaValidaTest() {
		Date fecha = Date.valueOf("2020-12-01");
		ConvertorFecha convertor = new ConvertorFecha(fecha);
		assertEquals("2020/12/01",convertor.getFecha());
	}
	
	@Test
	public void fechaInvalidaTest() {
		String fecha = "1969/12/31";
		ConvertorFecha convertor = new ConvertorFecha("1969/12/31");
		assertFalse(fecha.equals(convertor.getFecha()));
	}
	
	@Test
	public void setFechaValidaTest() {
		ConvertorFecha convertor = new ConvertorFecha("");
		convertor.setFecha(Date.valueOf("2020-12-01"));
		assertEquals(convertor.getFecha(),"2020/12/01");
	}


}
