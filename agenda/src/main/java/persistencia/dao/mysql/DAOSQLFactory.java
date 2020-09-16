/**
 * 
 */
package persistencia.dao.mysql;

import persistencia.dao.interfaz.ContactoDAO;
import persistencia.dao.interfaz.DAOAbstractFactory;
import persistencia.dao.interfaz.PersonaDAO;

public class DAOSQLFactory implements DAOAbstractFactory 
{
	/* (non-Javadoc)
	 * @see persistencia.dao.interfaz.DAOAbstractFactory#createPersonaDAO()
	 */
	public PersonaDAO createPersonaDAO() 
	{
				return new PersonaDAOSQL();
	}

	/* (non-Javadoc)
	 * @see persistencia.dao.interfaz.DAOAbstractFactory#createContactoDAO()
	 */
	public ContactoDAO createContactoDAO() {
		// TODO Auto-generated method stub
		return new ContactoDAOSQL();
	}

}
