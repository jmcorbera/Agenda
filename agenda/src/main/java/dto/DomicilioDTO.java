package dto;


public class DomicilioDTO {
	private int id;
	private String pais;
	private String provincia;
	private String localidad;
	private String departamento;
	private String calle;
	private String altura;
	private String piso;
	
	public DomicilioDTO(int id, String pais, String provincia, String localidad, String departamento, String calle, String altura, String piso) {
		this.id = id;
		this.pais = pais;
		this.provincia = provincia;
		this.localidad = localidad;
		this.departamento = departamento;
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

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
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
	
	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String isValid() {
		return !this.pais.isEmpty() ? "": "El nombre del país es obligatorio!";
	}
	
	
}
