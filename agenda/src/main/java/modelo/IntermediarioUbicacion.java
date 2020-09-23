package modelo;

import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import dto.LocalidadDTO;
import dto.PaisDTO;
import dto.ProvinciaDTO;

public class IntermediarioUbicacion {
	private static Agenda agenda;
	
	public IntermediarioUbicacion(Agenda agenda) {
		IntermediarioUbicacion.agenda = agenda;
	}
	
	public static void deshabilitarProvinciaYLocalidad(JComboBox<String> provincia, JComboBox<String> localidad) {
		provincia.setEnabled(false);
		localidad.setEnabled(false);
	}
	
	public static void obtenerListaPaises (JComboBox<String> cmbPais) {
		cmbPais.removeAllItems();
		cmbPais.setEnabled(true);
		List<PaisDTO> paises = agenda.obtenerPaíses();
		String[] nombrePaises = new String[paises.size()];
		for (int i = 0; i < paises.size(); i++) {
			nombrePaises[i] = paises.get(i).getNombre();
		}

		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nombrePaises);
		cmbPais.setModel(model);
	}
	
	public static void obtenerListaProvincias(JComboBox<String> comboBoxProvincia) {
		comboBoxProvincia.removeAllItems();
		comboBoxProvincia.setEnabled(true);
		List<ProvinciaDTO> provincias = agenda.obtenerProvincias();
		String[] nombreProvincias = new String[provincias.size()];
		for (int i = 0; i < provincias.size(); i++) {
			nombreProvincias[i] = provincias.get(i).getNombre();
		}

		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nombreProvincias);
		comboBoxProvincia.setModel(model);
	}
	
	public static void mostrarLocalidades(ProvinciaDTO seleccionada, JComboBox<String> cbLocalidades) {
		try {
			cbLocalidades.removeAllItems();
			cbLocalidades.setEnabled(false);
			List<LocalidadDTO> localidades = agenda.obtenerLocalidadesPorProv(seleccionada);
			if(!localidades.isEmpty()) {
				cbLocalidades.setEnabled(true);
				String[] nombreLocalidades = new String [localidades.size()];
				for(int i= 0; i< localidades.size(); i++) 
					nombreLocalidades[i] = localidades.get(i).getNombre();
			
				DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nombreLocalidades);
				cbLocalidades.setModel(model);
				cbLocalidades.setSelectedItem(model.getElementAt(0));
			}
		} catch (Exception e) {
			return;
		}
	}
	
	public static void mostrarProvincias(PaisDTO seleccionado, JComboBox<String> cbProvincias) {
		try {
			cbProvincias.removeAllItems();
			cbProvincias.setEnabled(false);
			List<ProvinciaDTO> provincias = agenda.obtenerProvinciasPorPais(seleccionado);
			if(!provincias.isEmpty()) {
				cbProvincias.setEnabled(true);
				String[] nombreProvincias = new String [provincias.size()];
				for(int i= 0; i< provincias.size(); i++) 
					nombreProvincias[i] = provincias.get(i).getNombre();
			
				DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nombreProvincias);
				cbProvincias.setModel(model);
				cbProvincias.setSelectedItem(model.getElementAt(0));
			}
		} 
		catch (Exception e) {
			return;
		}
	}
	
	public static ProvinciaDTO getProvinciaSeleccionada(JComboBox<String> provincias, PaisDTO paisSeleccionado) {
		Object seleccionado = provincias.getSelectedItem();
		if(seleccionado!= null)
			return getProvinciaSeleccionada(seleccionado.toString(), paisSeleccionado);
		return null;
	}
	
	public static ProvinciaDTO getProvinciaSeleccionada(String nombreProvincia, PaisDTO paisSeleccionado) {
		try {
			return agenda.obtenerProvincias().stream().filter(p ->  p.getPais().getIdPais() == paisSeleccionado.getIdPais() && p.getNombre().equals(nombreProvincia.toString())).findFirst().get();
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public static PaisDTO getPaisSeleccionado(String nombrePais) {
		try {
			return agenda.obtenerPaíses().stream().filter(p -> p.getNombre().equals(nombrePais)).findFirst().get();
		}
		catch(Exception e){
			return null;
		}
	}
	
	public static PaisDTO getPaisSeleccionado(JComboBox<String> paises) {
		Object seleccionado = paises.getSelectedItem();
		return seleccionado != null ? getPaisSeleccionado(seleccionado.toString()) : null;
	}
	
	//Generar menú desplegable 
	public static void agregarProvinciasAVentana(JComboBox<String> comboBoxPadre) {
		List<ProvinciaDTO> provincias = agenda.obtenerProvincias();
		for (ProvinciaDTO provincia : provincias) {
			comboBoxPadre.addItem(provincia.getNombre());
		}
	}
	
	public static void agregarPaisesAVentana(JComboBox<String> comboBoxPadre) {
		List<PaisDTO> paises = agenda.obtenerPaíses();
		for (PaisDTO pais : paises) {
			comboBoxPadre.addItem(pais.getNombre());
		}
	}
	

}
