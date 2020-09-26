package dto;


public class DomicilioDTO {
	private int id;
	private int idPais;
	private int idProvincia;
	private int idLocalidad;
	private String calle;
	private String altura;
	private String piso;	
	
	public DomicilioDTO(int id, int idPais, int idProvincia, int idLocalidad, String calle, String altura, String piso) {
		this.id = id;
		this.idPais = idPais;
		this.idProvincia = idProvincia;
		this.idLocalidad = idLocalidad;
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

	public int getIdLocalidad() {
		return this.idLocalidad;
	}

	public void setIdLocalidad(int idLocalidad) {
		this.idLocalidad = idLocalidad;
	}
	
	public int getIdPais() {
		return this.idPais;
	}

	public void setIdPais(int idPais) {
		this.idPais = idPais;
	}
	
	public int getIdProvincia() {
		return this.idProvincia;
	}

	public void setIdProvincia(int idProvincia) {
		this.idProvincia = idProvincia;
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

}
