package persistencia.dao.interfaz;


public interface DAOAbstractFactory 
{
	public PersonaDAO createPersonaDAO();
	public ContactoDAO createContactoDAO();
	public PaisDAO createPaisDAO();
	public ProvinciaDAO createProvinciaDAO();
	public LocalidadDAO createLocalidadDAO();
}
