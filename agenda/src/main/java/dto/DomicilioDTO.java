package dto;


public class DomicilioDTO {
	private int id;
	private String localidad;
	private String calle;
	private String altura;
	private String piso;
	
	public DomicilioDTO(int id, String localidad, String calle, String altura, String piso) {
		this.id = id;
		this.localidad = localidad;
		this.calle = calle;
		this.altura = altura;
		this.piso = piso;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getAltura() {
		return altura;
	}

	public void setAltura(String altura) {
		this.altura = altura;
	}

	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public String isValid() {
		return !this.localidad.isEmpty() ? "": "El nombre del país es obligatorio!";
	}	
}
