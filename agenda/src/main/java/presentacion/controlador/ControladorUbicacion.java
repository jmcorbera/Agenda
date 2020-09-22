package presentacion.controlador;


import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;


import dto.LocalidadDTO;
import dto.PaisDTO;
import dto.ProvinciaDTO;
import modelo.Agenda;
import presentacion.vista.VentanaABMLocalidad;
import presentacion.vista.VentanaEditarPais;
import presentacion.vista.VentanaNuevaProvinciaOLocalidad;
import presentacion.vista.VentanaNuevoPais;

public class ControladorUbicacion {
	private List<PaisDTO> paisesEnLista = new ArrayList<PaisDTO>();
	private List<ProvinciaDTO> provinciasEnLista = new ArrayList<ProvinciaDTO>();
	private List<LocalidadDTO> localidadesEnLista = new ArrayList<LocalidadDTO>();
	private VentanaABMLocalidad ventanaAMBLocalidad;
	private Agenda agenda;
	private String[] mensajes = {
			"El nombre es obligatorio!",
			"No ha seleccionado ningún país!",
			"No ha seleccionado ninguna provincia!",
			"No ha seleccionado ninguna localidad!",
			"Operación realizada con éxito :)",
			"Ya existe un país con ese nombre!",
			"Ya existe una provincia con ese nombre!",
			"Ya existe una localidad con ese nombre!"};
	
	public ControladorUbicacion(Agenda agenda, VentanaABMLocalidad ventanaAMBLocalidad) {
		this.ventanaAMBLocalidad = VentanaABMLocalidad.getInstance();
		this.agenda = agenda;
		configurarVentanaABMLocalidades();
	}
	
	private void configurarVentanaABMLocalidades() {
		
		this.obtenerListaPaises(ventanaAMBLocalidad.getComboBoxPais());
		deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
		this.ventanaAMBLocalidad.getBtnAgregarPais().addActionListener(l -> configurarVentanaNuevoPais());
		this.ventanaAMBLocalidad.getBtnEditarPais().addActionListener(l -> configurarVentanaEditarPais());
		this.ventanaAMBLocalidad.getBtnEliminarPais().addActionListener(l -> borrarPais());

		this.obtenerListaProvincias(ventanaAMBLocalidad.getComboBoxProvincia());
		this.ventanaAMBLocalidad.getBtnAgregarProvincia().addActionListener(l -> configurarVentanaNuevaProvincia());
		this.ventanaAMBLocalidad.getBtnEditarProvincia().addActionListener(l -> configurarVentanaEditarProvincia());
		this.ventanaAMBLocalidad.getBtnEliminarProvincia().addActionListener(l -> borrarProvincia());

		this.ventanaAMBLocalidad.getBtnAgregarLocalidad().addActionListener(l -> configurarVentanaNuevaLocalidad());	
		this.ventanaAMBLocalidad.getBtnEditarLocalidad().addActionListener(l -> configurarVentanaEditarLocalidad());			
		this.ventanaAMBLocalidad.getBtnEliminarLocalidad().addActionListener(l -> borrarLocalidad());		

		this.ventanaAMBLocalidad.getComboBoxPais().addActionListener(l -> mostrarProvincias());
		this.ventanaAMBLocalidad.getComboBoxProvincia().addActionListener(l -> mostrarLocalidades());
		this.refrescarListaPaises();
		this.refrescarListaProvincias();
		this.refrescarListaLocalidades();
	}
	
	private void obtenerListaPaises(JComboBox<String> cmbPais) {
		List<PaisDTO> paises = agenda.obtenerPaíses();
		String[] nombrePaises = new String[paises.size()];
		for (int i = 0; i < paises.size(); i++) {
			nombrePaises[i] = paises.get(i).getNombre();
		}

		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nombrePaises);
		cmbPais.setModel(model);
	}
	
	private void obtenerListaProvincias(JComboBox<String> comboBoxProvincia) {
		List<ProvinciaDTO> provincias = agenda.obtenerProvincias();
		String[] nombrePaises = new String[provincias.size()];
		for (int i = 0; i < provincias.size(); i++) {
			nombrePaises[i] = provincias.get(i).getNombre();
		}

		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nombrePaises);
		ventanaAMBLocalidad.getComboBoxProvincia().setModel(model);
	}
	
	private void deshabilitarProvinciaYLocalidad(JComboBox<String> provincia, JComboBox<String> localidad) {
		provincia.setEnabled(false);
		localidad.setEnabled(false);
	}
	
	private void refrescarListaLocalidades() {
		this.localidadesEnLista = agenda.obtenerLocalidades();
		this.obtenerListaProvincias(ventanaAMBLocalidad.getComboBoxLocalidad());
		this.ventanaAMBLocalidad.limpiarCombos();
	}
	
	private void refrescarListaPaises() {
		this.paisesEnLista = agenda.obtenerPaíses();
		this.obtenerListaPaises(ventanaAMBLocalidad.getComboBoxPais());
		this.ventanaAMBLocalidad.limpiarCombos();
	}

	private void refrescarListaProvincias() {
		this.provinciasEnLista = agenda.obtenerProvincias();
		this.obtenerListaProvincias(ventanaAMBLocalidad.getComboBoxProvincia());
		this.ventanaAMBLocalidad.limpiarCombos();
	}
	
	//ABM Pais
		private void configurarVentanaNuevoPais() {
			VentanaNuevoPais ventNuevoPais = new VentanaNuevoPais();
			ventNuevoPais.getBtnAceptar().addActionListener(n -> agregarPais(ventNuevoPais, n));
			ventNuevoPais.getBtnCancelar().addActionListener(c -> ventNuevoPais.cerrar());
		}
		
		//Alta
		private void agregarPais(VentanaNuevoPais v, ActionEvent a) {
			String nuevo = v.getTxtContactoNuevo().getText();
			if (nuevo.isEmpty()) {
				JOptionPane.showMessageDialog(v, mensajes[0]);
			} else {
				guardarPais(v, nuevo);
				v.cerrar();
			}
		}

		private void guardarPais(VentanaNuevoPais v, String nuevoPais) {
			if (existePais(nuevoPais)) {
				JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, mensajes[5]);
				return;
			}
			this.agenda.agregarPais(new PaisDTO(0, nuevoPais));
			this.refrescarListaPaises();
			deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
			JOptionPane.showMessageDialog(v, mensajes[4]);
		}
		
		//Baja
		private void borrarPais() {
			try {
					String nombre = this.ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString();
					PaisDTO pais = this.paisesEnLista.stream().filter(p -> p.getNombre().equals(nombre)).findFirst().get();
					this.agenda.borrarPais(pais);
					this.refrescarListaPaises();
					deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
					JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[4]);
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(this.ventanaAMBLocalidad,mensajes[1]);
			}
		}
		
		//Modificación
		private void configurarVentanaEditarPais() {
			try {
					String paisSeleccionado = this.ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString();
					VentanaEditarPais editarPais = new VentanaEditarPais(paisSeleccionado);
					editarPais.getBtnAceptar().addActionListener(c -> editarPais(editarPais, c));
					editarPais.getBtnCancelar().addActionListener(c -> editarPais.cerrar());
					editarPais.mostrar();

			} catch (Exception e) {
				JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[1]);
			}
		}
		
		private void editarPais(VentanaEditarPais v, ActionEvent a) {
			if (v.getTxtNuevo().getText().isEmpty()) {
				JOptionPane.showMessageDialog(v, mensajes[0]);
				return;
			}
			this.editarPais(v.getTxtNombreAnterior().getText(), v.getTxtNuevo().getText());
			v.cerrar();
		}
		
		private void editarPais(String txtNombreAnterior, String txtNuevo) {
			try {
				PaisDTO pais = this.paisesEnLista.stream().filter(p -> p.getNombre().equals(txtNombreAnterior)).findFirst().get();
				if (txtNuevo.equals("")) {
					JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, mensajes[0]);
					return;
				}
				if(existePais(txtNuevo)) {
					JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[5]);
					return;
				}
				this.agenda.modificarPais(new PaisDTO(pais.getIdPais(), txtNuevo));
				this.refrescarListaPaises();
				deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
				JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, mensajes[4]);
			}
			catch(Exception e) {
			}
		}
		
		//ABM Provincia
		private void configurarVentanaNuevaProvincia() {
			VentanaNuevaProvinciaOLocalidad ventanaNuevaProvincia = new VentanaNuevaProvinciaOLocalidad();
			agregarPaisesAVentana(ventanaNuevaProvincia);
			ventanaNuevaProvincia.getBtnAceptar().addActionListener(n -> agregarProvincia(ventanaNuevaProvincia));
			ventanaNuevaProvincia.getBtnCancelar().addActionListener(c -> ventanaNuevaProvincia.cerrar());
			ventanaNuevaProvincia.getBtnCambiar().setVisible(false);
		}
		
		//Alta 
		private void agregarProvincia(VentanaNuevaProvinciaOLocalidad ventanaNuevaProvincia) {
			try {
				String nombre = ventanaNuevaProvincia.getComboBoxPadre().getSelectedItem().toString();
				PaisDTO pais = this.paisesEnLista.stream().filter(p -> p.getNombre().equals(nombre)).findFirst().get();
				String nuevo = ventanaNuevaProvincia.getTxtNombre().getText();
				if (nuevo.isEmpty()) {
					JOptionPane.showMessageDialog(ventanaNuevaProvincia, mensajes[0]);
				} else {
					guardarProvincia(ventanaNuevaProvincia, nuevo, pais);
					ventanaNuevaProvincia.cerrar();
				}
			}
			catch(Exception e) {
			}
		}

		private void guardarProvincia(VentanaNuevaProvinciaOLocalidad ventanaNuevaProvincia, String nuevo, PaisDTO pais) {
			try {
				if (existeProvincia(nuevo, pais.getIdPais())) {
					JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, mensajes[6]);
					return;
				}
				this.agenda.agregarProvincia(new ProvinciaDTO(0, nuevo, pais));
				this.refrescarListaProvincias();
				deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
				JOptionPane.showMessageDialog(ventanaNuevaProvincia, mensajes[4]);
			}
			catch(Exception e) {
			}
		}

		//Baja
		private void borrarProvincia() {
			try {
				String nombre = this.ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem().toString();
				ProvinciaDTO provincia = agenda.obtenerProvincias().stream().filter(p -> p.getNombre().equals(nombre)).findFirst().get();
				this.agenda.borrarProvincia(provincia);
				this.ventanaAMBLocalidad.limpiarCombos();
				deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
				JOptionPane.showMessageDialog(ventanaAMBLocalidad,mensajes[4]);
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[2]);
				return;	
			}
		}
		
		//Modificación
		private void configurarVentanaEditarProvincia() {
			try {
					String provinciaSeleccionada = this.ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem().toString();
					VentanaNuevaProvinciaOLocalidad editarProvincia = new VentanaNuevaProvinciaOLocalidad();
					agregarPaisesAVentana(editarProvincia);
					editarProvincia.getTxtNombre().setEnabled(false);
					editarProvincia.getTxtNombre().setText(provinciaSeleccionada);
					editarProvincia.getBtnCambiar().addActionListener(p -> configurarBoton(editarProvincia));
					editarProvincia.getBtnAceptar().addActionListener(c -> editarProvincia(editarProvincia, provinciaSeleccionada));
					editarProvincia.getBtnCancelar().addActionListener(c -> editarProvincia.cerrar());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(ventanaAMBLocalidad,mensajes[2]);
			}
		}
		
		private void configurarBoton(VentanaNuevaProvinciaOLocalidad editarProvincia) {
			if (editarProvincia.getTxtNombre().isEnabled()) {
				editarProvincia.getTxtNombre().setEnabled(false);
				editarProvincia.getTxtNombre()
						.setText(ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem().toString());
				editarProvincia.getBtnCambiar().setText("Cambiar");
			} else {
				editarProvincia.getTxtNombre().setEnabled(true);
				editarProvincia.getBtnCambiar().setText("Cancelar");
			}
		}		
		private void editarProvincia(VentanaNuevaProvinciaOLocalidad editarProvincia, String anterior) {
			try {
				String nuevo = editarProvincia.getTxtNombre().getText();
				ProvinciaDTO provincia = this.provinciasEnLista.stream().filter(p -> p.getNombre().equals(anterior)).findFirst().get();
				String nombrePaisSeleccionado = editarProvincia.getComboBoxPadre().getSelectedItem().toString();
				PaisDTO pais = this.paisesEnLista.stream().filter(p -> p.getNombre().equals(nombrePaisSeleccionado)).findFirst().get();
				if (nuevo.equals("")) {
					JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, mensajes[0]);
					return;
				}
				if(existeProvincia(nuevo, pais.getIdPais())) {
					JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[6]);
				}
				this.agenda.modificarProvincia(new ProvinciaDTO(provincia.getIdProvincia(), nuevo, pais));
				this.refrescarListaProvincias();
				deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
				editarProvincia.cerrar();
				JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, mensajes[4]);
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this.ventanaAMBLocalidad,mensajes[2]);
			}
		}
		
		//ABM Localidad
		private void configurarVentanaNuevaLocalidad() {
			VentanaNuevaProvinciaOLocalidad ventanaNuevaLocalidad = new VentanaNuevaProvinciaOLocalidad();
			ventanaNuevaLocalidad.getLblPadreAlQuePertenece().setText("Provincia a la que pertenece: ");
			agregarProvinciasAVentana(ventanaNuevaLocalidad);
			ventanaNuevaLocalidad.getBtnAceptar().addActionListener(n -> agregarLocalidad(ventanaNuevaLocalidad));
			ventanaNuevaLocalidad.getBtnCancelar().addActionListener(c -> ventanaNuevaLocalidad.cerrar());
			ventanaNuevaLocalidad.getBtnCambiar().setVisible(false);
		}
		
		//Alta 
		private void agregarLocalidad(VentanaNuevaProvinciaOLocalidad ventanaNuevaLocalidad) {
			String nombre = ventanaNuevaLocalidad.getComboBoxPadre().getSelectedItem().toString();
			ProvinciaDTO provincia = this.provinciasEnLista.stream().filter(p -> p.getNombre().equals(nombre)).findFirst().get();
			String nuevo = ventanaNuevaLocalidad.getTxtNombre().getText();
			if (nuevo.isEmpty()) {
				JOptionPane.showMessageDialog(ventanaNuevaLocalidad, mensajes[0]);
			} else {
				guardarLocalidad(ventanaNuevaLocalidad, nuevo, provincia);
				ventanaNuevaLocalidad.cerrar();
			}
		}

		private void guardarLocalidad(VentanaNuevaProvinciaOLocalidad ventanaNuevaLocalidad, String nuevo, ProvinciaDTO provincia) {
			try {
				if(existeLocalidad(nuevo, provincia.getIdProvincia())) {
					JOptionPane.showMessageDialog(ventanaNuevaLocalidad, mensajes[7]);
				}
				this.agenda.agregarLocalidad(new LocalidadDTO(0, nuevo, provincia));
				this.refrescarListaLocalidades();
				deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
				JOptionPane.showMessageDialog(ventanaNuevaLocalidad, mensajes[4]);
			}
			catch(Exception e) {
			}
		}
		
		//Baja
		private void borrarLocalidad() {
			try {
				String nombre = this.ventanaAMBLocalidad.getComboBoxLocalidad().getSelectedItem().toString();
				LocalidadDTO localidad = agenda.obtenerLocalidades().stream().filter(p -> p.getNombre().equals(nombre)).findFirst().get();

				this.agenda.borrarLocalidad(localidad);
				this.ventanaAMBLocalidad.limpiarCombos();
				deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
				
				JOptionPane.showMessageDialog(ventanaAMBLocalidad,mensajes[4]);
				}
				catch(Exception e) {
					JOptionPane.showMessageDialog(ventanaAMBLocalidad,mensajes[3]);
					return;	
				}
		}
		
		//Modificación
		private void configurarVentanaEditarLocalidad() {
			try {
					String localidadSeleccionada = this.ventanaAMBLocalidad.getComboBoxLocalidad().getSelectedItem().toString();
					VentanaNuevaProvinciaOLocalidad editarLocalidad = new VentanaNuevaProvinciaOLocalidad();
					agregarProvinciasAVentana(editarLocalidad);
					editarLocalidad.getLblPadreAlQuePertenece().setText("Provincia a la que pertenece");
					editarLocalidad.getTxtNombre().setEnabled(false);
					editarLocalidad.getTxtNombre().setText(localidadSeleccionada);
					editarLocalidad.getBtnCambiar().addActionListener(p -> configurarBoton(editarLocalidad));
					editarLocalidad.getBtnAceptar().addActionListener(c -> editarLocalidad(editarLocalidad, localidadSeleccionada));
					editarLocalidad.getBtnCancelar().addActionListener(c -> editarLocalidad.cerrar());
			
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this.ventanaAMBLocalidad,mensajes[3]);
			}
		}

		private void editarLocalidad(VentanaNuevaProvinciaOLocalidad editarLocalidad, String anterior) {
			try {
				String nuevo = editarLocalidad.getTxtNombre().getText();
				if(nuevo.isEmpty()) {
					JOptionPane.showMessageDialog(editarLocalidad, mensajes[0]);
					return;
				}
				LocalidadDTO localidad = this.localidadesEnLista.stream().filter(p -> p.getNombre().equals(anterior)).findFirst().get();
				String nombreProvinciaSeleccionada = editarLocalidad.getComboBoxPadre().getSelectedItem().toString();
				ProvinciaDTO provincia = this.provinciasEnLista.stream().filter(p -> p.getNombre().equals(nombreProvinciaSeleccionada)).findFirst().get();

				if(existeLocalidad(nuevo, provincia.getIdProvincia())) {
					JOptionPane.showMessageDialog(editarLocalidad, mensajes[7]);
				}
				this.agenda.modificarLocalidad(new LocalidadDTO(localidad.getIdLocalidad(), nuevo, provincia));
				this.refrescarListaLocalidades();
				deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
				editarLocalidad.cerrar();
				JOptionPane.showMessageDialog(editarLocalidad, mensajes[4]);
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(editarLocalidad,mensajes[3]);
			}
		}

		//Generar menú desplegable 
		private void agregarProvinciasAVentana(VentanaNuevaProvinciaOLocalidad ventanaNuevaLocalidad) {
			List<ProvinciaDTO> provincias = agenda.obtenerProvincias();
			for (ProvinciaDTO provincia : provincias) {
				ventanaNuevaLocalidad.getComboBoxPadre().addItem(provincia.getNombre());
			}
		}
		
		private void agregarPaisesAVentana(VentanaNuevaProvinciaOLocalidad ventanaNuevaProvincia) {
			List<PaisDTO> paises = agenda.obtenerPaíses();
			for (PaisDTO pais : paises) {
				ventanaNuevaProvincia.getComboBoxPadre().addItem(pais.getNombre());
			}
		}
		
		private void mostrarLocalidades() {
			try {
				String provinciaSeleccionada = ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem().toString();
				String paisSeleccionado = ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString();
				ventanaAMBLocalidad.getComboBoxLocalidad().removeAllItems();
				List<LocalidadDTO> localidades = agenda.obtenerLocalidades();
				
				for (int i = 0; i < localidades.size(); i++) {
					ProvinciaDTO provincia = localidades.get(i).getProvincia();
					String nombreProvincia = provincia.getNombre();
					if (nombreProvincia.equals(provinciaSeleccionada) && provincia.getPais().getNombre().equals(paisSeleccionado)) {
						ventanaAMBLocalidad.getComboBoxLocalidad().addItem(localidades.get(i).getNombre());
					}
				}
			} catch (Exception e) {
				return;
			}
		}

		private void mostrarProvincias() {
			try {
				String seleccionado = ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString();
				ventanaAMBLocalidad.getComboBoxProvincia().removeAllItems();
				ventanaAMBLocalidad.getComboBoxLocalidad().removeAllItems();
				ventanaAMBLocalidad.getComboBoxLocalidad().setEnabled(true);

				ventanaAMBLocalidad.getComboBoxProvincia().setEnabled(true);
				List<ProvinciaDTO> provincias = agenda.obtenerProvincias();
				for (int i = 0; i < provincias.size(); i++) {
					PaisDTO pais = provincias.get(i).getPais();
					String nombrePais = pais.getNombre();
					if (nombrePais.equals(seleccionado)) {
						ventanaAMBLocalidad.getComboBoxProvincia().addItem(provincias.get(i).getNombre());
					}
				}
			} catch (Exception e) {
				return;
			}
		}
	
	private boolean existePais(String pais) {
		return agenda.existsPais(pais);
	}
	
	private boolean existeProvincia(String provincia, int paisId) {
		return agenda.existsProvincia(provincia, paisId);
	}
	
	private boolean existeLocalidad(String localidad, int provinciaId) {
		return agenda.existsLocalidad(localidad, provinciaId);
	}
	public VentanaABMLocalidad getVentanaABMLocalidad() {
		return ventanaAMBLocalidad;
	}

}
