package presentacion.controlador;


import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import dto.LocalidadDTO;
import dto.PaisDTO;
import dto.ProvinciaDTO;
import modelo.Agenda;
import modelo.IntermediarioUbicacion;
import presentacion.vista.VentanaABMUbicacion;
import presentacion.vista.VentanaEditarContactoOPais;
import presentacion.vista.VentanaNuevaProvinciaOLocalidad;
import presentacion.vista.VentanaNuevoPaisOContacto;

public class ControladorUbicacion {
	private List<PaisDTO> paisesEnLista = new ArrayList<PaisDTO>();
	private List<ProvinciaDTO> provinciasEnLista = new ArrayList<ProvinciaDTO>();
	private List<LocalidadDTO> localidadesEnLista = new ArrayList<LocalidadDTO>();
	private VentanaABMUbicacion ventanaAMBLocalidad;
	
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
	
	public ControladorUbicacion(Agenda agenda, VentanaABMUbicacion ventanaAMBLocalidad) {
		this.ventanaAMBLocalidad = VentanaABMUbicacion.getInstance();
		this.agenda = agenda;
		configurarVentanaABMLocalidades();
	}
	
	private void configurarVentanaABMLocalidades() {
		IntermediarioUbicacion.obtenerListaPaises(ventanaAMBLocalidad.getComboBoxPais());
		IntermediarioUbicacion.deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
		this.ventanaAMBLocalidad.getBtnAgregarPais().addActionListener(l -> configurarVentanaNuevoPais());
		this.ventanaAMBLocalidad.getBtnEditarPais().addActionListener(l -> configurarVentanaEditarPais());
		this.ventanaAMBLocalidad.getBtnEliminarPais().addActionListener(l -> borrarPais());
		
		this.ventanaAMBLocalidad.getBtnAgregarProvincia().addActionListener(l -> configurarVentanaNuevaProvincia());
		this.ventanaAMBLocalidad.getBtnEditarProvincia().addActionListener(l -> configurarVentanaEditarProvincia());
		this.ventanaAMBLocalidad.getBtnEliminarProvincia().addActionListener(l -> borrarProvincia());

		this.ventanaAMBLocalidad.getBtnAgregarLocalidad().addActionListener(l -> configurarVentanaNuevaLocalidad());	
		this.ventanaAMBLocalidad.getBtnEditarLocalidad().addActionListener(l -> configurarVentanaEditarLocalidad());			
		this.ventanaAMBLocalidad.getBtnEliminarLocalidad().addActionListener(l -> borrarLocalidad());		

		this.ventanaAMBLocalidad.getComboBoxPais().addActionListener(l -> IntermediarioUbicacion.mostrarProvincias(IntermediarioUbicacion.getPaisSeleccionado(ventanaAMBLocalidad.getComboBoxPais()), ventanaAMBLocalidad.getComboBoxProvincia()));
		this.ventanaAMBLocalidad.getComboBoxProvincia().addActionListener(l -> IntermediarioUbicacion.mostrarLocalidades(IntermediarioUbicacion.getProvinciaSeleccionada(ventanaAMBLocalidad.getComboBoxProvincia(),IntermediarioUbicacion.getPaisSeleccionado(ventanaAMBLocalidad.getComboBoxPais())), ventanaAMBLocalidad.getComboBoxLocalidad()));
		this.refrescarListaPaises();
		this.refrescarListaProvincias();
		this.refrescarListaLocalidades();
	}
		
	private void refrescarListaLocalidades() {
		this.localidadesEnLista = agenda.obtenerLocalidades();
		IntermediarioUbicacion.obtenerListaProvincias(ventanaAMBLocalidad.getComboBoxLocalidad());
		this.ventanaAMBLocalidad.limpiarCombos();
	}
	
	private void refrescarListaPaises() {
		this.paisesEnLista = agenda.obtenerPaíses();
		IntermediarioUbicacion.obtenerListaPaises(ventanaAMBLocalidad.getComboBoxPais());
		this.ventanaAMBLocalidad.limpiarCombos();
	}

	private void refrescarListaProvincias() {
		this.provinciasEnLista = agenda.obtenerProvincias();
		IntermediarioUbicacion.obtenerListaProvincias(ventanaAMBLocalidad.getComboBoxProvincia());
		this.ventanaAMBLocalidad.limpiarCombos();
	}
	
	//ABM Pais
		private void configurarVentanaNuevoPais() {
			VentanaNuevoPaisOContacto ventNuevoPais = new VentanaNuevoPaisOContacto();
			ventNuevoPais.getBtnAceptar().addActionListener(n -> agregarPais(ventNuevoPais));
			ventNuevoPais.getBtnCancelar().addActionListener(c -> ventNuevoPais.cerrar());
		}
		
		//Alta
		private void agregarPais(VentanaNuevoPaisOContacto v) {
			try {
			String nuevo = v.getTxtNuevoNombre().getText();
			if (nuevo.isEmpty()) {
				JOptionPane.showMessageDialog(v, mensajes[0]);
				return;
			} 
			if (existePais(nuevo)) {
				JOptionPane.showMessageDialog(v, mensajes[5]);
				return;
			}
			this.agenda.agregarPais(new PaisDTO(0, nuevo));
			v.cerrar();
			this.refrescarListaPaises();
			IntermediarioUbicacion.deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[4]);
			}
			catch(Exception e) {
				
			}
		}
		
		//Baja
		private void borrarPais() {
			try {
					String nombre = this.ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString();
					PaisDTO pais = this.paisesEnLista.stream().filter(p -> p.getNombre().equals(nombre)).findFirst().get();
					this.agenda.borrarPais(pais);
					this.refrescarListaPaises();
					IntermediarioUbicacion.deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
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
					VentanaEditarContactoOPais editarPais = new VentanaEditarContactoOPais(paisSeleccionado);
					editarPais.getBtnAceptar().addActionListener(c -> editarPais(editarPais));
					editarPais.getBtnCancelar().addActionListener(c -> editarPais.cerrar());
					editarPais.mostrar();

			} catch (Exception e) {
				JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[1]);
			}
		}
		
		private void editarPais(VentanaEditarContactoOPais v) {
			try {
				String anterior = v.getTxtNombreAnterior().getText();
				String nuevo = v.getTxtNuevo().getText();
				PaisDTO pais = this.paisesEnLista.stream().filter(p -> p.getNombre().equals(anterior)).findFirst().get();
				if (v.getTxtNuevo().getText().equals("")) {
					JOptionPane.showMessageDialog(v, mensajes[0]);
					return;
				}
				if(existePais(nuevo)) {
					JOptionPane.showMessageDialog(v, mensajes[5]);
					return;
				}
				this.agenda.modificarPais(new PaisDTO(pais.getIdPais(), nuevo));
				this.refrescarListaPaises();
				IntermediarioUbicacion.deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
				v.cerrar();
				JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[4]);
			}
			catch(Exception e) {
			}
		}
		
		//ABM Provincia
		private void configurarVentanaNuevaProvincia() {
			VentanaNuevaProvinciaOLocalidad ventanaNuevaProvincia = new VentanaNuevaProvinciaOLocalidad();
			IntermediarioUbicacion.agregarPaisesAVentana(ventanaNuevaProvincia.getComboBoxPadre());
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
					return;
				} 
				if (existeProvincia(nuevo, pais.getIdPais())) {
					JOptionPane.showMessageDialog(ventanaNuevaProvincia, mensajes[6]);
					return;
				}
				this.agenda.agregarProvincia(new ProvinciaDTO(0, nuevo, pais));
				this.refrescarListaProvincias();
				IntermediarioUbicacion.deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
				ventanaNuevaProvincia.cerrar();
				JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[4]);
			}
			catch(Exception e) {
			}
		}

		//Baja
		private void borrarProvincia() {
			try {
				ProvinciaDTO provincia = IntermediarioUbicacion.getProvinciaSeleccionada(ventanaAMBLocalidad.getComboBoxProvincia(), IntermediarioUbicacion.getPaisSeleccionado(ventanaAMBLocalidad.getComboBoxPais()));
				this.agenda.borrarProvincia(provincia);
				this.ventanaAMBLocalidad.limpiarCombos();
				IntermediarioUbicacion.deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
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
					IntermediarioUbicacion.agregarPaisesAVentana(editarProvincia.getComboBoxPadre());
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
					JOptionPane.showMessageDialog(editarProvincia, mensajes[0]);
					return;
				}
				if(existeProvincia(nuevo, pais.getIdPais())) {
					JOptionPane.showMessageDialog(editarProvincia, mensajes[6]);
					return;
				}
				editarProvincia.cerrar();
				this.agenda.modificarProvincia(new ProvinciaDTO(provincia.getIdProvincia(), nuevo, pais));
				this.refrescarListaProvincias();
				IntermediarioUbicacion.deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());	
				JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, mensajes[4]);
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this.ventanaAMBLocalidad,mensajes[2]);
			}
		}
		
		//ABM Localidad
		private void configurarVentanaNuevaLocalidad() {
			VentanaNuevaProvinciaOLocalidad ventanaNuevaLocalidad = new VentanaNuevaProvinciaOLocalidad();
			ventanaNuevaLocalidad.getLblPadreAlQuePertenece().setText("Provincia a la que pertenece: ");
			IntermediarioUbicacion.agregarProvinciasAVentana(ventanaNuevaLocalidad.getComboBoxPadre());
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
				return;
			} 
			if(existeLocalidad(nuevo, provincia.getIdProvincia())) {
				JOptionPane.showMessageDialog(ventanaNuevaLocalidad, mensajes[7]);
				return;
			}
			this.agenda.agregarLocalidad(new LocalidadDTO(0, nuevo, provincia));
			this.refrescarListaLocalidades();
			IntermediarioUbicacion.deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
			ventanaNuevaLocalidad.cerrar();
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[4]);
		}
		
		//Baja
		private void borrarLocalidad() {
			try {
				String nombre = this.ventanaAMBLocalidad.getComboBoxLocalidad().getSelectedItem().toString();
				LocalidadDTO localidad = agenda.obtenerLocalidades().stream().filter(p -> p.getNombre().equals(nombre)).findFirst().get();

				this.agenda.borrarLocalidad(localidad);
				this.ventanaAMBLocalidad.limpiarCombos();
				IntermediarioUbicacion.deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
				
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
					IntermediarioUbicacion.agregarProvinciasAVentana(editarLocalidad.getComboBoxPadre());
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
					return;
				}
				this.agenda.modificarLocalidad(new LocalidadDTO(localidad.getIdLocalidad(), nuevo, provincia));
				this.refrescarListaLocalidades();
				IntermediarioUbicacion.deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
				editarLocalidad.cerrar();
				JOptionPane.showMessageDialog(editarLocalidad, mensajes[4]);
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(editarLocalidad,mensajes[3]);
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
	public VentanaABMUbicacion getVentanaABMLocalidad() {
		return ventanaAMBLocalidad;
	}

}
