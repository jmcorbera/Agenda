package dto;

public class ReporteDTO {
	private PersonaDTO persona;
	private LocalidadDTO localidad;
	
	public ReporteDTO(PersonaDTO persona, LocalidadDTO localidad)
	{
		this.persona = persona;
		this.localidad = localidad;
	}
	
	public PersonaDTO getPersona() 
	{
		return this.persona;
	}

	public void setId(PersonaDTO persona) 
	{
		this.persona = persona;
	}
	
	public LocalidadDTO getLocalidad() 
	{
		return this.localidad;
	}

	public void setLocalidad(LocalidadDTO localidad) 
	{
		this.localidad = localidad;
	}

}
