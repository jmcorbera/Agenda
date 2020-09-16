package persistencia.dao.interfaz;


public interface DAOAbstractFactory 
{
	public PersonaDAO createPersonaDAO();
	public ContactoDAO createContactoDAO();
}
