package modelo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;
import dto.LocalidadDTO;
import dto.PaisDTO;
import dto.ProvinciaDTO;
import persistencia.conexion.Conexion;
import persistencia.dao.interfaz.DAOAbstractFactory;
import persistencia.dao.interfaz.LocalidadDAO;
import persistencia.dao.interfaz.PaisDAO;
import persistencia.dao.interfaz.ProvinciaDAO;

public class DBdata {
	
	public static void Initialize(DAOAbstractFactory DAOFactory) {
		try {
			crearTablas(); // Initialize database
		} catch (Exception e) {
		}
		
		// Paises
		PaisDAO paisDAO = DAOFactory.createPaisDAO();
		List<PaisDTO> paises = getPaises(paisDAO);
		
		// Provincias
		ProvinciaDAO provinciaDAO = DAOFactory.createProvinciaDAO();
		List<ProvinciaDTO> provincias = getProvincias(provinciaDAO, paises);
		
		// Localidades
		LocalidadDAO localidadDAO = DAOFactory.createLocalidadDAO();
		@SuppressWarnings("unused")
		List<LocalidadDTO> localidades = getLocalidades(localidadDAO, provincias);
	}

	public static void crearTablas() throws Exception {
		Connection conn = Conexion.getConexion().getSQLConexion();
		ScriptRunner runner = new ScriptRunner(conn);
		InputStreamReader reader = null;		
        InputStream stream = DBdata.class.getResourceAsStream("/scriptAgenda.sql");
		
		try {
			reader = new InputStreamReader(stream, "UTF-8");
			runner.runScript(reader);
			reader.close();
			Conexion.getConexion().cerrarConexion();
		} finally {
			
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				reader = null;
			}			
		}		
	}

	private static List<PaisDTO> getPaises(PaisDAO paisDAO) {
		
		List<PaisDTO> paises = new ArrayList<PaisDTO>();
		
		if (!paisDAO.ifExist()) {
			
			paises.add(new PaisDTO(0, "Argentina"));
			paises.add(new PaisDTO(0, "Chile"));
			paises.add(new PaisDTO(0, "Brasil"));
			paises.add(new PaisDTO(0, "Paraguay"));
			paises.add(new PaisDTO(0, "Bolivia"));
			
			for (PaisDTO pais : paises)
				paisDAO.insert(pais);		
		}
		
		return paisDAO.readAll();		
	}

	private static List<ProvinciaDTO> getProvincias(ProvinciaDAO provinciaDAO, List<PaisDTO> paises) {
		
		List<ProvinciaDTO> provincias = new ArrayList<ProvinciaDTO>();
		
		//todo get json provincias
		
		if (!provinciaDAO.ifExist()) {
			
			PaisDTO argentina = paises.stream().filter(p -> p.getNombre().equals("Argentina")).findFirst().get();
			
			provincias.add(new ProvinciaDTO(0, "CABA", argentina));
			provincias.add(new ProvinciaDTO(0, "Tierra del Fuego", argentina));
			provincias.add(new ProvinciaDTO(0, "Buenos Aires", argentina));
			provincias.add(new ProvinciaDTO(0, "Catamarca", argentina));
			provincias.add(new ProvinciaDTO(0, "Chaco", argentina));
			provincias.add(new ProvinciaDTO(0, "Chubut", argentina));
			provincias.add(new ProvinciaDTO(0, "Córdoba", argentina));
			provincias.add(new ProvinciaDTO(0, "Corrientes", argentina));
			provincias.add(new ProvinciaDTO(0, "Entre Ríos", argentina));
			provincias.add(new ProvinciaDTO(0, "Formosa", argentina));
			provincias.add(new ProvinciaDTO(0, "Jujuy", argentina));
			provincias.add(new ProvinciaDTO(0, "La Pampa", argentina));
			provincias.add(new ProvinciaDTO(0, "La Rioja", argentina));
			provincias.add(new ProvinciaDTO(0, "Mendoza", argentina));
			provincias.add(new ProvinciaDTO(0, "Misiones", argentina));
			provincias.add(new ProvinciaDTO(0, "Neuquén", argentina));
			provincias.add(new ProvinciaDTO(0, "Río Negro", argentina));
			provincias.add(new ProvinciaDTO(0, "Salta", argentina));
			provincias.add(new ProvinciaDTO(0, "San Juan", argentina));
			provincias.add(new ProvinciaDTO(0, "San Luis", argentina));
			provincias.add(new ProvinciaDTO(0, "Santa Cruz", argentina));
			provincias.add(new ProvinciaDTO(0, "Santa Fe", argentina));
			provincias.add(new ProvinciaDTO(0, "Santiago del Estero", argentina));
			provincias.add(new ProvinciaDTO(0, "Tucumán", argentina));
			
			for (ProvinciaDTO provincia : provincias)
				provinciaDAO.insert(provincia);		
		}
		
		return provinciaDAO.readAll();	
	}
	
	private static List<LocalidadDTO> getLocalidades(LocalidadDAO localidadDAO, List<ProvinciaDTO> provincias) {
		
		List<LocalidadDTO> localidades = new ArrayList<LocalidadDTO>();
		
		if (!localidadDAO.ifExist()) {
		//todo get json localidades
			
		// Buenos Aires
			ProvinciaDTO buenosAires = provincias.stream().filter(p -> p.getNombre().equals("Buenos Aires")).findFirst().get();
				
			localidades.add(new LocalidadDTO(0, "San Miguel", buenosAires));
			localidades.add(new LocalidadDTO(0, "Bella Vista", buenosAires));
				
		// Ciudad Autónoma de Buenos Aires
			ProvinciaDTO caba = provincias.stream().filter(p -> p.getNombre().equals("CABA")).findFirst().get();
				
			localidades.add(new LocalidadDTO(0, "Palermo", caba));
			localidades.add(new LocalidadDTO(0, "Retiro", caba));
					
			for (LocalidadDTO localidad : localidades) {
				localidadDAO.insert(localidad);
			}
					
			System.out.println("Localidades cargadas con éxito.");
		}
					
		return localidadDAO.readAll();	
	}
	
}
