package modelo;

import java.util.List;

import dto.ContactoDTO;
import dto.DomicilioDTO;
import dto.LocalidadDTO;
import dto.PaisDTO;
import dto.PersonaDTO;
import dto.ProvinciaDTO;
import dto.ReporteDTO;
import persistencia.dao.interfaz.ContactoDAO;
import persistencia.dao.interfaz.DAOAbstractFactory;
import persistencia.dao.interfaz.DomicilioDAO;
import persistencia.dao.interfaz.LocalidadDAO;
import persistencia.dao.interfaz.PaisDAO;
import persistencia.dao.interfaz.PersonaDAO;
import persistencia.dao.interfaz.ProvinciaDAO;
import persistencia.dao.interfaz.ReporteDAO;

public class Agenda 
{
	private PersonaDAO persona;	
	private ContactoDAO contacto;
	private PaisDAO pais;
	private ProvinciaDAO provincia;
	private LocalidadDAO localidad;
	private DomicilioDAO domicilio;
	private ReporteDAO reporte; 
	
	public Agenda(DAOAbstractFactory metodo_persistencia)
	{
		this.persona = metodo_persistencia.createPersonaDAO();
		this.contacto = metodo_persistencia.createContactoDAO();
		this.pais = metodo_persistencia.createPaisDAO();
		this.provincia = metodo_persistencia.createProvinciaDAO();
		this.localidad = metodo_persistencia.createLocalidadDAO();
		this.domicilio = metodo_persistencia.createDomicilioDAO();
		this.reporte = metodo_persistencia.createReporteDAO();
	}
	
	public void agregarPersona(PersonaDTO nuevaPersona)
	{
		this.persona.insert(nuevaPersona);
	}
	
	public int obtenerUltimoId() {
		List<PersonaDTO> personas = obtenerPersonas();
		return !personas.isEmpty() ? personas.get(personas.size()-1).getId() : 0;
	}
	
	public void borrarPersona(PersonaDTO persona_a_eliminar) 
	{
		this.persona.delete(persona_a_eliminar);
	}
	
	public boolean actualizarPersona(int id, PersonaDTO nueva_persona) {
		return this.persona.update(id, nueva_persona);
	}
	
	public List<PersonaDTO> obtenerPersonas()
	{
		return this.persona.readAll();		
	}
	public void agregarContacto(String nuevoContacto)
	{
		this.contacto.insert(nuevoContacto);
	}

	public void borrarContacto(String contacto_a_eliminar) 
	{
		this.contacto.delete(contacto_a_eliminar);
	}
	
	public List<ContactoDTO> obtenerContactos()
	{
		return this.contacto.readAll();		
	}

	public boolean editarContacto(String contacto_a_cambiar, String nuevo_contacto) {
		return this.contacto.update(contacto_a_cambiar, nuevo_contacto);
	}

	// Paises
	
	public void agregarPais(PaisDTO pais) {
		this.pais.insert(pais);
	}
	
	public void modificarPais(PaisDTO pais_a_modificar) {
		this.pais.update(pais_a_modificar);
	}
	
	public void borrarPais(PaisDTO pais_a_eliminar) {
		this.pais.delete(pais_a_eliminar);
	}
	
	public List<PaisDTO> obtenerPaíses() {
		return this.pais.readAll();
	}
	
	// Provincias
	
	public void agregarProvincia(ProvinciaDTO provincia) {
		this.provincia.insert(provincia);
	}
	
	public void modificarProvincia(ProvinciaDTO provincia_a_modificar) {
		this.provincia.update(provincia_a_modificar);
	}
	
	public void borrarProvincia(ProvinciaDTO provincia_a_eliminar) {
		this.provincia.delete(provincia_a_eliminar);
	}
	
	public List<ProvinciaDTO> obtenerProvincias() {
		return this.provincia.readAll();
	}
	
	public List<ProvinciaDTO> obtenerProvinciasPorPais(PaisDTO pais) {
		return this.provincia.groupBy(pais);
	}
	
	// Localidades
	
	public void agregarLocalidad(LocalidadDTO localidad) {
		this.localidad.insert(localidad);
	}
	
	public void borrarLocalidad(LocalidadDTO localidad_a_eliminar) 
	{
		this.localidad.delete(localidad_a_eliminar);
	}
	
	public void modificarLocalidad(LocalidadDTO localidad_a_modificar) 
	{
		this.localidad.update(localidad_a_modificar);
	}
	
	public List<LocalidadDTO> obtenerLocalidades()
	{
		return this.localidad.readAll();		
	}
	
	public List<LocalidadDTO> obtenerLocalidadesPorProv(ProvinciaDTO provincia)
	{
		return this.localidad.groupBy(provincia);
	}
	
	//Domicilios
	public void agregarDomicilio(DomicilioDTO domicilio) {
		this.domicilio.insert(domicilio);
	}
	
	public void borrarDomicilio(int id) {
		this.domicilio.delete(id);
	}
	
	public void modificarDomicilio(DomicilioDTO domicilio) {
		this.domicilio.update(domicilio);
	}
	
	public List<DomicilioDTO> obtenerDomicilios(){
		return this.domicilio.readAll();
	}
	
	public DomicilioDTO obtenerDomicilio (int id) {
		return this.domicilio.getDomicilio(id);
	}
	
	public boolean existsContacto(String contacto) {
		return this.contacto.exists(contacto);
	}
	
	public boolean existsPais(String pais) {
		return this.pais.exists(pais);
	}
	
	public boolean existsProvincia(String provincia, int paisId) {
		return this.provincia.exists(provincia, paisId);
	}
	public boolean existsLocalidad(String localidad, int provinciaId) {
		return this.localidad.exists(localidad, provinciaId);
	}
	
	//Reportes
	
	public List<ReporteDTO> obtenerReportesAgrupadoPorDomicilio()
	{
		return this.reporte.readAllgroupBy();
	}
	
	public PaisDTO obtenerPaisPorNombre(String nombre) {
		return this.pais.getPais(nombre);
	}
	
	public PaisDTO obtenerPaisPorId(int id) {
		return this.pais.getPais(id);
	}
	
	public ProvinciaDTO obtenerProvincia(String nombreProvincia, int idPais) {
		return this.provincia.getProvincia(nombreProvincia, idPais);
	}
	
	public ProvinciaDTO obtenerProvincia(int id) {
		return this.provincia.getProvincia(id);
	}
	
	public LocalidadDTO obtenerLocalidad(String nombre, int idProvincia) {
		return this.localidad.getLocalidad(nombre, idProvincia);
	}
	
	public LocalidadDTO obtenerLocalidad(int idLocalidad) {
		return this.localidad.getLocalidad(idLocalidad);
	}

	public void eliminarFecha(int id) {
		this.persona.eliminarFecha(id);
		
	}
}
