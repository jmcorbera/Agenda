package dto;

public class ProvinciaDTO {
	private int idProvincia;
	private String nombre;
	private PaisDTO pais;
	
	public ProvinciaDTO(int idProvincia, String nombre, PaisDTO país) {
		this.idProvincia = idProvincia;
		this.nombre = nombre;
		this.setPais(pais);
	}
	
	public int getIdProvincia() {
		return idProvincia;
	}
	
	public void setIdProvincia(int idProvincia) {
		this.idProvincia = idProvincia;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public PaisDTO getPaís() {
		return pais;
	}

	public void setPais(PaisDTO pais) {
		this.pais = pais;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idProvincia;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((pais == null) ? 0 : pais.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProvinciaDTO other = (ProvinciaDTO) obj;
		if (idProvincia != other.idProvincia)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (pais == null) {
			if (other.pais != null)
				return false;
		} else if (!pais.equals(other.pais))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("ProvinciaID: %d, ProvinciaNombre: %s\n - - %s", this.idProvincia, this.nombre, this.pais.toString());
	}
}
