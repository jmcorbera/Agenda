package dto;

public class ContactoDTO {
	private String nombreContacto;

	public ContactoDTO(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	public String getNombreContacto() {
		return this.nombreContacto;
	}

	public void setNombre(String otroNombre) {
		this.nombreContacto = otroNombre;
	}
}
