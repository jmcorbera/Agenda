package dto;

public class ReporteDTO {
	private PersonaDTO persona;
	private DomicilioDTO domicilio;
	
	public ReporteDTO(PersonaDTO persona, DomicilioDTO domicilio)
	{
		this.persona = persona;
		this.domicilio = domicilio;
	}
	
	public PersonaDTO getPersona() 
	{
		return this.persona;
	}

	public void setId(PersonaDTO persona) 
	{
		this.persona = persona;
	}
	
	public DomicilioDTO getDomicilio() 
	{
		return this.domicilio;
	}

	public void setId(DomicilioDTO domicilio) 
	{
		this.domicilio = domicilio;
	}

}
