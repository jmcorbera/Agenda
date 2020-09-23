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
	
	public String isValid() {
		return this.nombreContacto != null && !this.nombreContacto.isEmpty() ? "": "El nombre es obligatorio!";
	}

}
