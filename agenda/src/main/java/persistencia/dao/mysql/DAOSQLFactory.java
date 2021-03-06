/**
 * 
 */
package persistencia.dao.mysql;

import persistencia.dao.interfaz.ContactoDAO;
import persistencia.dao.interfaz.DAOAbstractFactory;
import persistencia.dao.interfaz.DomicilioDAO;
import persistencia.dao.interfaz.LocalidadDAO;
import persistencia.dao.interfaz.PaisDAO;
import persistencia.dao.interfaz.PersonaDAO;
import persistencia.dao.interfaz.ProvinciaDAO;
import persistencia.dao.interfaz.ReporteDAO;

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
	/* (non-Javadoc)
	 * @see persistencia.dao.interfaz.DAOAbstractFactory#createPaisDAO()
	 */
	public PaisDAO createPaisDAO() {
		// TODO Auto-generated method stub
		return new PaisDAOSQL();
	}

	/* (non-Javadoc)
	 * @see persistencia.dao.interfaz.DAOAbstractFactory#createProvinciaDAO()
	 */
	public ProvinciaDAO createProvinciaDAO() {
		// TODO Auto-generated method stub
		return new ProvinciaDAOSQL();
	}

	/* (non-Javadoc)
	 * @see persistencia.dao.interfaz.DAOAbstractFactory#createLocalidadDAO()
	 */
	public LocalidadDAO createLocalidadDAO() {
		// TODO Auto-generated method stub
		return new LocalidadDAOSQL();
	}

	/* (non-Javadoc)
	 * @see persistencia.dao.interfaz.DAOAbstractFactory#createLocalidadDAO()
	 */
	public DomicilioDAO createDomicilioDAO() {
		// TODO Auto-generated method stub
		return new DomicilioDAOSQL();
	}

	@Override
	public ReporteDAO createReporteDAO() {
		// TODO Auto-generated method stub
		return new ReporteDAOSQL();
	}

}
