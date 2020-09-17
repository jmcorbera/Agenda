package modelo;

import java.text.SimpleDateFormat;

public class ConvertorFecha {
	private String fecha;

	public ConvertorFecha(Object object) {
		try {
			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");
			fecha = formatoFecha.format(object);
		} catch (Exception e) {
			fecha = "";
		}
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(Object otraFecha) {
		ConvertorFecha convertor = new ConvertorFecha(otraFecha);
		fecha = convertor.getFecha();
	}

}
