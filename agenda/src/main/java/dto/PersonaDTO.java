package dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import presentacion.vista.Intermediario.ContactoPreferente;

public class PersonaDTO 
{
	private int id;
	private String nombre;
	private String telefono;
	private String nacimiento;
	private String email;
    private String tipoContacto;
    private String contactoPreferente;
    
	public PersonaDTO(int id, String nombre, String telefono, String nacimiento, String email, String contactoId, String contactoPreferente)
	{
		this.id = id;
		this.nombre = nombre;
		this.telefono = telefono;
		this.nacimiento = nacimiento;
		this.email = email;
		this.tipoContacto = contactoId;
		this.contactoPreferente = contactoPreferente;
	}
	
	public int getId() 
	{
		return this.id;
	}

	public void setId(int idPersona) 
	{
		this.id = idPersona;
	}

	public String getNombre() 
	{
		return this.nombre;
	}

	public void setNombre(String nombre) 
	{
		this.nombre = nombre;
	}
	
	public String getContactoPreferente() 
	{
		return this.contactoPreferente;
	}

	public void setContactoPreferente(String contactoPreferente) 
	{
		this.contactoPreferente = contactoPreferente;
	}
	
	public String getTelefono() 
	{
		return this.telefono;
	}

	public void setTelefono(String telefono) 
	{
		this.telefono = telefono;
	}

	public String getNacimiento() {
		return this.nacimiento;
	}
	
	public String getEmail() {
		return this.email;
	}

	public String getContactoId() {
		return tipoContacto;
	}
	
	public void setContactoId(String otroTipoContacto) {
		tipoContacto = otroTipoContacto;
	}
	public String isValid() {
	if (this.getNombre().isEmpty()) 
		return "El nombre es obligatorio";
	if (this.getContactoId() == null || this.getContactoId().isEmpty())
		return "El tipo de contacto no puede estar vacío!";
	if(!this.getEmail().isEmpty() && !isValidEmail(this.getEmail()))
		return "Email ingresado inválido!";
	if(this.getContactoPreferente().equals(ContactoPreferente.Email.toString())) {
		if(this.getEmail().isEmpty())
			return "El medio de contacto preferente no puede estar vacío!";
	}
	else {
		if(this.getTelefono().isEmpty())
			return "El medio de contacto preferente no puede estar vacío!";
	}
		return "";
	}

	private boolean isValidEmail(String email) {
		Pattern pattern = Pattern.compile("[a-z](\\.-_[a-z0-9]+)*[a-z0-9]*@[a-z]+(\\.[a-z]+)+");
		Matcher mather = pattern.matcher(email);
		return !mather.find() ? false : true;
	}

	public void setNacimiento(String nacimiento) {
		this.nacimiento = nacimiento;
	}
}
