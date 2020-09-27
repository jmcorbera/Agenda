package presentacion.controlador;

import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import dto.DomicilioDTO;
import dto.LocalidadDTO;
import dto.PaisDTO;
import dto.ProvinciaDTO;
import modelo.Agenda;
import presentacion.vista.VentanaABMUbicacion;
import presentacion.vista.VentanaDomicilio;
import presentacion.vista.VentanaEditarContactoOPais;
import presentacion.vista.VentanaNuevaProvinciaOLocalidad;
import presentacion.vista.VentanaNuevoPaisOContacto;

public class ControladorUbicacion {
	private VentanaABMUbicacion ventanaAMBLocalidad;
	private VentanaDomicilio ventanaDomicilio;
	private DomicilioDTO domicilio;
	private Agenda agenda;
	private String[] mensajes = { "El nombre es obligatorio!", "No ha seleccionado ningún país!",
			"No ha seleccionado ninguna provincia!", "No ha seleccionado ninguna localidad!",
			"Operación realizada con éxito :)", "Ya existe un país con ese nombre!",
			"Ya existe una provincia con ese nombre!", "Ya existe una localidad con ese nombre!" };

	public ControladorUbicacion(Agenda agenda, VentanaABMUbicacion ventanaAMBLocalidad) {
		this.ventanaAMBLocalidad = VentanaABMUbicacion.getInstance();
		this.agenda = agenda;
		configurarVentanaABMLocalidades();
	}

	private void configurarVentanaABMLocalidades() {

		actualizarComboBox(ventanaAMBLocalidad.getComboBoxPais(), ObtenerNombrePaises());
		deshabilitarCombosDependientes();
		if(ventanaAMBLocalidad.getBtnAgregarPais().getActionListeners().length == 0)
			this.ventanaAMBLocalidad.getBtnAgregarPais().addActionListener(l -> configurarVentanaNuevoPais());
		if(ventanaAMBLocalidad.getBtnEditarPais().getActionListeners().length == 0)	
			this.ventanaAMBLocalidad.getBtnEditarPais().addActionListener(l -> configurarVentanaEditarPais());
		if(ventanaAMBLocalidad.getBtnEliminarPais().getActionListeners().length == 0)
			this.ventanaAMBLocalidad.getBtnEliminarPais().addActionListener(l -> borrarPais());
		if(ventanaAMBLocalidad.getBtnAgregarProvincia().getActionListeners().length == 0)
			this.ventanaAMBLocalidad.getBtnAgregarProvincia().addActionListener(l -> configurarVentanaNuevaProvincia());
		if(ventanaAMBLocalidad.getBtnEditarProvincia().getActionListeners().length == 0)
			this.ventanaAMBLocalidad.getBtnEditarProvincia().addActionListener(l -> configurarVentanaEditarProvincia());
		if(ventanaAMBLocalidad.getBtnEliminarProvincia().getActionListeners().length == 0)
			this.ventanaAMBLocalidad.getBtnEliminarProvincia().addActionListener(l -> borrarProvincia());
		if(ventanaAMBLocalidad.getBtnAgregarLocalidad().getActionListeners().length == 0)
			this.ventanaAMBLocalidad.getBtnAgregarLocalidad().addActionListener(l -> configurarVentanaNuevaLocalidad());
		if(ventanaAMBLocalidad.getBtnEditarLocalidad().getActionListeners().length == 0)
			this.ventanaAMBLocalidad.getBtnEditarLocalidad().addActionListener(l -> configurarVentanaEditarLocalidad());
		if(ventanaAMBLocalidad.getBtnEliminarLocalidad().getActionListeners().length == 0)
			this.ventanaAMBLocalidad.getBtnEliminarLocalidad().addActionListener(l -> borrarLocalidad());
		if(ventanaAMBLocalidad.getComboBoxPais().getActionListeners().length == 0)
			this.ventanaAMBLocalidad.getComboBoxPais().addActionListener(l -> actualizarComboBox(ventanaAMBLocalidad.getComboBoxProvincia(), ObtenerNombreProvinciasPorPais(
						obtenerNombreSeleccionado(ventanaAMBLocalidad.getComboBoxPais().getSelectedItem()))));
		if(ventanaAMBLocalidad.getComboBoxProvincia().getActionListeners().length == 0)
			this.ventanaAMBLocalidad.getComboBoxProvincia().addActionListener(l -> actualizarComboBox(ventanaAMBLocalidad.getComboBoxLocalidad(),
						ObtenerNombreLocalidadadesPorPaisYProvincia(obtenerNombreSeleccionado(ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem()),
								obtenerNombreSeleccionado(ventanaAMBLocalidad.getComboBoxPais().getSelectedItem()))));
		this.refrescarListaPaises();
		this.refrescarListaProvincias();
		this.refrescarListaLocalidades();
	}

	private void actualizarComboBox(JComboBox<String> comboBox, String[] nombres) {
		comboBox.removeAllItems();
		if (nombres != null) {
			comboBox.setEnabled(true);
			setModel(comboBox, nombres);
		}
	}

	private void setModel(JComboBox<String> comboBox, String[] nombres) {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nombres);
		comboBox.setModel(model);
	}

	private String[] ObtenerNombrePaises() {
		List<PaisDTO> paises = agenda.obtenerPaíses();
		String[] nombrePaises = new String[paises.size()];
		for (int i = 0; i < paises.size(); i++)
			nombrePaises[i] = paises.get(i).getNombre();
		return nombrePaises;
	}

	private String[] ObtenerNombreProvinciasPorPais(String seleccionado) {
		if (seleccionado.isEmpty())
			return null;
		List<ProvinciaDTO> provincias = agenda.obtenerProvinciasPorPais(agenda.obtenerPaisPorNombre(seleccionado));
		String[] nombreProvincias = new String[provincias.size()];
		for (int i = 0; i < provincias.size(); i++)
			nombreProvincias[i] = provincias.get(i).getNombre();
		return nombreProvincias;
	}

	private String[] ObtenerNombreLocalidadadesPorPaisYProvincia(String provincia, String pais) {
		if (provincia.isEmpty() || pais.isEmpty())
			return null;
		List<LocalidadDTO> localidades = agenda.obtenerLocalidadesPorProv(
				agenda.obtenerProvincia(provincia, agenda.obtenerPaisPorNombre(pais).getIdPais()));
		String[] nombreProvincias = new String[localidades.size()];
		for (int i = 0; i < localidades.size(); i++)
			nombreProvincias[i] = localidades.get(i).getNombre();
		return nombreProvincias;
	}

	private void refrescarListaLocalidades() {
		actualizarComboBox(ventanaAMBLocalidad.getComboBoxLocalidad(),
				ObtenerNombreLocalidadadesPorPaisYProvincia(
						obtenerNombreSeleccionado(ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem()),
						obtenerNombreSeleccionado(ventanaAMBLocalidad.getComboBoxPais().getSelectedItem())));
		this.ventanaAMBLocalidad.limpiarCombos();
	}

	private void refrescarListaPaises() {
		actualizarComboBox(ventanaAMBLocalidad.getComboBoxPais(), ObtenerNombrePaises());
		this.ventanaAMBLocalidad.limpiarCombos();
	}

	private void refrescarListaProvincias() {
		actualizarComboBox(ventanaAMBLocalidad.getComboBoxProvincia(), ObtenerNombreProvinciasPorPais(
				obtenerNombreSeleccionado(ventanaAMBLocalidad.getComboBoxPais().getSelectedItem())));
		this.ventanaAMBLocalidad.limpiarCombos();
	}

	// ABM Pais
	private void configurarVentanaNuevoPais() {
		VentanaNuevoPaisOContacto ventNuevoPais = new VentanaNuevoPaisOContacto();
		ventNuevoPais.getBtnAceptar().addActionListener(n -> agregarPais(ventNuevoPais));
		ventNuevoPais.getBtnCancelar().addActionListener(c -> ventNuevoPais.cerrar());
	}

	// Alta
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
			deshabilitarCombosDependientes();
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[4]);
		} catch (Exception e) {
		}
	}

	private void deshabilitarCombosDependientes() {
		ventanaAMBLocalidad.getComboBoxLocalidad().setEnabled(false);
		ventanaAMBLocalidad.getComboBoxProvincia().setEnabled(false);
	}

	// Baja
	private void borrarPais() {
		try {
			PaisDTO pais = agenda.obtenerPaisPorNombre(ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString());
			this.agenda.borrarPais(pais);
			this.refrescarListaPaises();
			deshabilitarCombosDependientes();
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[4]);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, mensajes[1]);
		}
	}

	// Modificación
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
			PaisDTO pais = agenda.obtenerPaisPorNombre(anterior);
			if (v.getTxtNuevo().getText().equals("")) {
				JOptionPane.showMessageDialog(v, mensajes[0]);
				return;
			}
			if (existePais(nuevo)) {
				JOptionPane.showMessageDialog(v, mensajes[5]);
				return;
			}
			this.agenda.modificarPais(new PaisDTO(pais.getIdPais(), nuevo));
			this.refrescarListaPaises();
			ventanaAMBLocalidad.limpiarComboProvincia();
			deshabilitarCombosDependientes();
			v.cerrar();
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[4]);
		} catch (Exception e) {
		}
	}

	// ABM Provincia
	private void configurarVentanaNuevaProvincia() {
		VentanaNuevaProvinciaOLocalidad ventanaNuevaProvincia = new VentanaNuevaProvinciaOLocalidad();
		actualizarComboBox(ventanaNuevaProvincia.getComboBoxPadre(), ObtenerNombrePaises());
		ventanaNuevaProvincia.getBtnAceptar().addActionListener(n -> agregarProvincia(ventanaNuevaProvincia));
		ventanaNuevaProvincia.getBtnCancelar().addActionListener(c -> ventanaNuevaProvincia.cerrar());
		ventanaNuevaProvincia.getBtnCambiar().setVisible(false);
	}

	// Alta
	private void agregarProvincia(VentanaNuevaProvinciaOLocalidad ventanaNuevaProvincia) {
		try {
			String nombre = ventanaNuevaProvincia.getComboBoxPadre().getSelectedItem().toString();
			PaisDTO pais = agenda.obtenerPaisPorNombre(nombre);
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
			deshabilitarCombosDependientes();
			ventanaNuevaProvincia.cerrar();
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[4]);
		} catch (Exception e) {
		}
	}

	// Baja
	private void borrarProvincia() {
		try {
			String nombreProvincia = ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem() != null
					? ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem().toString(): "";
			String nombrePais = ventanaAMBLocalidad.getComboBoxPais().getSelectedItem() != null
					? ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString(): "";
			if (nombrePais.isEmpty()) {
				JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[2]);
				return;
			}
			PaisDTO pais = agenda.obtenerPaisPorNombre(nombrePais);
			ProvinciaDTO provincia = agenda.obtenerProvincia(nombreProvincia, pais.getIdPais());
			this.agenda.borrarProvincia(provincia);
			refrescarListaProvincias();
			deshabilitarCombosDependientes();
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[4]);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[2]);
			return;
		}
	}

	// Modificación
	private void configurarVentanaEditarProvincia() {
		try {
			String provinciaSeleccionada = this.ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem().toString();
			VentanaNuevaProvinciaOLocalidad editarProvincia = new VentanaNuevaProvinciaOLocalidad();
			actualizarComboBox(editarProvincia.getComboBoxPadre(), ObtenerNombrePaises());
			
			editarProvincia.getTxtNombre().setEnabled(false);
			editarProvincia.getTxtNombre().setText(provinciaSeleccionada);
			editarProvincia.getBtnCambiar().addActionListener(p -> configurarBoton(editarProvincia));
			editarProvincia.getBtnAceptar().addActionListener(c -> editarProvincia(editarProvincia, provinciaSeleccionada));
			editarProvincia.getBtnCancelar().addActionListener(c -> editarProvincia.cerrar());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[2]);
		}
	}

	private void configurarBoton(VentanaNuevaProvinciaOLocalidad editarProvincia) {
		if (editarProvincia.getTxtNombre().isEnabled()) {
			editarProvincia.getTxtNombre().setEnabled(false);
			editarProvincia.getTxtNombre().setText(ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem().toString());
			editarProvincia.getBtnCambiar().setText("Cambiar");
		} else {
			editarProvincia.getTxtNombre().setEnabled(true);
			editarProvincia.getBtnCambiar().setText("Cancelar");
		}
	}

	private void editarProvincia(VentanaNuevaProvinciaOLocalidad editarProvincia, String anterior) {
		try {
			String nuevo = editarProvincia.getTxtNombre().getText();
			PaisDTO pais = agenda.obtenerPaisPorNombre(ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString());
			ProvinciaDTO provincia = agenda.obtenerProvincia(ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem().toString(), pais.getIdPais());
			
			if (nuevo.equals("")) {
				JOptionPane.showMessageDialog(editarProvincia, mensajes[0]);
				return;
			}
			if (existeProvincia(nuevo, pais.getIdPais())) {
				JOptionPane.showMessageDialog(editarProvincia, mensajes[6]);
				return;
			}
			editarProvincia.cerrar();
			this.agenda.modificarProvincia(new ProvinciaDTO(provincia.getIdProvincia(), nuevo, pais));
			this.refrescarListaProvincias();
			deshabilitarCombosDependientes();
			JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, mensajes[4]);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, mensajes[2]);
		}
	}

	// ABM Localidad
	private void configurarVentanaNuevaLocalidad() {
		if (obtenerNombreSeleccionado(ventanaAMBLocalidad.getComboBoxLocalidad().getSelectedItem()).isEmpty()) {
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[2]);
			return;
		}
		VentanaNuevaProvinciaOLocalidad ventanaNuevaLocalidad = new VentanaNuevaProvinciaOLocalidad();
		ventanaNuevaLocalidad.getLblPadreAlQuePertenece().setText("Provincia a la que pertenece: ");
		actualizarComboBox(ventanaNuevaLocalidad.getComboBoxPadre(),ObtenerNombreProvinciasPorPais(obtenerNombreSeleccionado(ventanaAMBLocalidad.getComboBoxPais())));
		ventanaNuevaLocalidad.getBtnAceptar().addActionListener(n -> agregarLocalidad(ventanaNuevaLocalidad));
		ventanaNuevaLocalidad.getBtnCancelar().addActionListener(c -> ventanaNuevaLocalidad.cerrar());
		ventanaNuevaLocalidad.getBtnCambiar().setVisible(false);
	}

	// Alta
	private void agregarLocalidad(VentanaNuevaProvinciaOLocalidad ventanaNuevaLocalidad) {
		ProvinciaDTO provincia = agenda.obtenerProvincia(ventanaNuevaLocalidad.getComboBoxPadre().getSelectedItem().toString(),
		agenda.obtenerPaisPorNombre(ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString()).getIdPais());
		String nuevo = ventanaNuevaLocalidad.getTxtNombre().getText();
		if (nuevo.isEmpty()) {
			JOptionPane.showMessageDialog(ventanaNuevaLocalidad, mensajes[0]);
			return;
		}
		if (existeLocalidad(nuevo, provincia.getIdProvincia())) {
			JOptionPane.showMessageDialog(ventanaNuevaLocalidad, mensajes[7]);
			return;
		}
		this.agenda.agregarLocalidad(new LocalidadDTO(0, nuevo, provincia));
		this.refrescarListaLocalidades();
		deshabilitarCombosDependientes();
		ventanaNuevaLocalidad.cerrar();
		JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[4]);
	}

	// Baja
	private void borrarLocalidad() {
		try {
			LocalidadDTO localidad = agenda.obtenerLocalidad(obtenerNombreSeleccionado(ventanaAMBLocalidad.getComboBoxLocalidad()), 
					agenda.obtenerPaisPorNombre(obtenerNombreSeleccionado(ventanaAMBLocalidad.getComboBoxPais())).getIdPais());
			this.agenda.borrarLocalidad(localidad);
			refrescarListaLocalidades();
			deshabilitarCombosDependientes();
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[4]);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, mensajes[3]);
			return;
		}
	}

	// Modificación
	private void configurarVentanaEditarLocalidad() {
		try {
			String localidadSeleccionada = this.ventanaAMBLocalidad.getComboBoxLocalidad().getSelectedItem().toString();
			VentanaNuevaProvinciaOLocalidad editarLocalidad = new VentanaNuevaProvinciaOLocalidad();
			actualizarComboBox(editarLocalidad.getComboBoxPadre(), ObtenerNombreProvinciasPorPais(obtenerNombreSeleccionado(ventanaAMBLocalidad.getComboBoxPais().getSelectedItem())));

			editarLocalidad.getLblPadreAlQuePertenece().setText("Provincia a la que pertenece");
			editarLocalidad.getTxtNombre().setEnabled(false);
			editarLocalidad.getTxtNombre().setText(localidadSeleccionada);
			editarLocalidad.getBtnCambiar().addActionListener(p -> configurarBoton(editarLocalidad));
			editarLocalidad.getBtnAceptar().addActionListener(c -> editarLocalidad(editarLocalidad, localidadSeleccionada));
			editarLocalidad.getBtnCancelar().addActionListener(c -> editarLocalidad.cerrar());

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, mensajes[3]);
		}
	}

	private void editarLocalidad(VentanaNuevaProvinciaOLocalidad editarLocalidad, String anterior) {
		try {
			String nuevo = editarLocalidad.getTxtNombre().getText();
			if (nuevo.isEmpty()) {
				JOptionPane.showMessageDialog(editarLocalidad, mensajes[0]);
				return;
			}
			ProvinciaDTO provincia = agenda.obtenerProvincia(
					editarLocalidad.getComboBoxPadre().getSelectedItem().toString(),
					agenda.obtenerPaisPorNombre(ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString()).getIdPais());
			LocalidadDTO localidad = agenda.obtenerLocalidad(anterior, provincia.getIdProvincia());
			if (existeLocalidad(nuevo, provincia.getIdProvincia())) {
				JOptionPane.showMessageDialog(editarLocalidad, mensajes[7]);
				return;
			}
			this.agenda.modificarLocalidad(new LocalidadDTO(localidad.getIdLocalidad(), nuevo, provincia));
			this.refrescarListaLocalidades();
			deshabilitarCombosDependientes();
			editarLocalidad.cerrar();
			JOptionPane.showMessageDialog(editarLocalidad, mensajes[4]);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(editarLocalidad, mensajes[3]);
		}
	}

	private String obtenerNombreSeleccionado(Object seleccionado) {
		return seleccionado != null ? seleccionado.toString() : "";
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

	private void configurarVentanaDomicilio(int id) {
		ventanaDomicilio = new VentanaDomicilio();
		vaciarCombosDomicilio();
		ventanaDomicilio.mostrarVentana();
		actualizarComboBox(ventanaDomicilio.getComboBoxPais(), ObtenerNombrePaises());
		ventanaDomicilio.getComboBoxProvincia().setEnabled(false);
		ventanaDomicilio.getComboBoxLocalidad().setEnabled(false);
		if (ventanaDomicilio.getBtnCancelar().getActionListeners().length == 0)
			ventanaDomicilio.getBtnCancelar().addActionListener(a -> cerrarVentanaDomicilio());
		if (ventanaDomicilio.getBtnAceptar().getActionListeners().length == 0)
			ventanaDomicilio.getBtnAceptar().addActionListener(a -> guardarDomicilio(id));
	}

	private void guardarDomicilio(int id) {
		Object seleccionado;
		seleccionado = ventanaDomicilio.getComboBoxPais().getSelectedItem();
		String nombrePais = seleccionado == null ? "" : seleccionado.toString();
		if (nombrePais.equals("")) {
			JOptionPane.showMessageDialog(ventanaDomicilio, "El nombre del país es obligatorio!");
			domicilio = null;
			return;
		}
		seleccionado = ventanaDomicilio.getComboBoxLocalidad().getSelectedItem();
		String nombreLocalidad = seleccionado == null ? "" : seleccionado.toString();
		seleccionado = ventanaDomicilio.getComboBoxProvincia().getSelectedItem();
		String nombreProvincia = seleccionado == null ? "" : seleccionado.toString();
		String calle = ventanaDomicilio.getTxtCalle().getText();
		String altura = ventanaDomicilio.getTxtAltura().getText();
		String piso = ventanaDomicilio.getTxtPiso().getText();
		PaisDTO pais = agenda.obtenerPaisPorNombre(nombrePais);
		ProvinciaDTO provincia = agenda.obtenerProvincia(nombreProvincia, pais.getIdPais());
		LocalidadDTO localidad = null;
		int idProvincia = provincia != null ? provincia.getIdProvincia() : -1;
		if (idProvincia != -1)
			localidad = agenda.obtenerLocalidad(nombreLocalidad, provincia.getIdProvincia());
		int idLocalidad = localidad != null ? localidad.getIdLocalidad() : -1;
		domicilio = new DomicilioDTO(id, pais.getIdPais(), idProvincia, idLocalidad, calle, altura, piso);
		cerrarVentanaDomicilio();
	}

	private void cerrarVentanaDomicilio() {
		if(ventanaDomicilio != null) {
			ventanaDomicilio.cerrar();
			ventanaDomicilio = null;
		}
	}

	protected void configurarVentanaNuevoDomicilio(int id) {
		configurarVentanaDomicilio(id);
		ventanaDomicilio.ocultarBotonesCambiar();
		agregarListenersMostrarUbicaciones();
	}

	protected void configurarVentanaEditarDomicilio(int idPersona) {
		configurarVentanaDomicilio(idPersona);
		obtenerDomicilioActual(idPersona);
		vaciarCombosDomicilio();
		ventanaDomicilio.mostrarBotonesCambiar();
		mostrarValoresPredeterminados();
		ventanaDomicilio.establecerTextoCambiar();
		configurarBtnCambiarTxts();
		configurarBtnCambiarUbicacion();
		agregarListenersMostrarUbicaciones();
	}

	private void configurarBtnCambiarTxts() {
		if (ventanaDomicilio.getBtnCambiarCalle().getActionListeners().length == 0)
			ventanaDomicilio.getBtnCambiarCalle().addActionListener(a -> configurarBtnCambiarCalle());
		if (ventanaDomicilio.getBtnCambiarAltura().getActionListeners().length == 0)
			ventanaDomicilio.getBtnCambiarAltura().addActionListener(a -> configurarBtnCambiarAltura());
		if (ventanaDomicilio.getBtnCambiarPiso().getActionListeners().length == 0)
			ventanaDomicilio.getBtnCambiarPiso().addActionListener(a -> configurarBtnCambiarPiso());
	}

	private void configurarBtnCambiarPiso() {
		if (ventanaDomicilio.getBtnCambiarPiso().getText().equals("Cambiar")) {
			ventanaDomicilio.getBtnCambiarPiso().setText("Cancelar");
			ventanaDomicilio.getTxtPiso().setText("");
			ventanaDomicilio.getTxtPiso().setEnabled(true);
		} else {
			ventanaDomicilio.getBtnCambiarPiso().setText("Cambiar");
			mostrarPisoPredeterminado();
		}
	}

	private void configurarBtnCambiarAltura() {
		if (ventanaDomicilio.getBtnCambiarAltura().getText().equals("Cambiar")) {
			ventanaDomicilio.getBtnCambiarAltura().setText("Cancelar");
			ventanaDomicilio.getTxtAltura().setText("");
			ventanaDomicilio.getTxtAltura().setEnabled(true);
		} else {
			ventanaDomicilio.getBtnCambiarAltura().setText("Cambiar");
			mostrarAlturaPredeterminada();
		}
	}

	private void configurarBtnCambiarCalle() {
		if (ventanaDomicilio.getBtnCambiarCalle().getText().equals("Cambiar")) {
			ventanaDomicilio.getBtnCambiarCalle().setText("Cancelar");
			ventanaDomicilio.getTxtCalle().setText("");
			ventanaDomicilio.getTxtCalle().setEnabled(true);
		} else {
			ventanaDomicilio.getBtnCambiarCalle().setText("Cambiar");
			mostrarCallePredeterminada();
		}
	}

	private void mostrarValoresPredeterminados() {
		try {
			mostrarTxtsPredeterminados();
			PaisDTO pais = agenda.obtenerPaisPorId(domicilio.getIdPais());
			if (pais != null) {
				mostrarSeleccionadoPrimero(pais.getNombre(), ventanaDomicilio.getComboBoxPais());
				ProvinciaDTO provincia = agenda.obtenerProvincia(domicilio.getIdProvincia());
				if (provincia != null) {
					mostrarSeleccionadoPrimero(provincia.getNombre(), ventanaDomicilio.getComboBoxProvincia());
					LocalidadDTO localidad = agenda.obtenerLocalidad(domicilio.getIdLocalidad());
					if (localidad != null)
						mostrarSeleccionadoPrimero(localidad.getNombre(), ventanaDomicilio.getComboBoxLocalidad());
				}
			}

		} catch (Exception e) {

		}
	}

	private void mostrarSeleccionadoPrimero(String seleccionado, JComboBox<String> combo) {
		if (seleccionado.isEmpty()) {
			combo.removeAllItems();
			return;
		}
		ComboBoxModel<String> modeloLista = combo.getModel();
		int largoLista = modeloLista.getSize();
		combo.setEnabled(false);
		if (largoLista > 0) {
			String primero = modeloLista.getElementAt(0);
			modeloLista.setSelectedItem(seleccionado);
			@SuppressWarnings("unused")
			String cambiarPrimero = modeloLista.getElementAt(0);
			cambiarPrimero = seleccionado;
			@SuppressWarnings("unused")
			String cambiarUltimo = modeloLista.getElementAt(largoLista);
			cambiarUltimo = primero;
		} else {
			modeloLista.setSelectedItem(seleccionado);
		}
	}

	private void mostrarTxtsPredeterminados() {
		mostrarCallePredeterminada();
		mostrarAlturaPredeterminada();
		mostrarPisoPredeterminado();
	}

	private void mostrarCallePredeterminada() {
		ventanaDomicilio.setTxtCalle(domicilio != null && !domicilio.getCalle().isEmpty() ? domicilio.getCalle() : "");
		ventanaDomicilio.getTxtCalle().setEnabled(false);
	}

	private void mostrarAlturaPredeterminada() {
		ventanaDomicilio
				.setTxtAltura(domicilio != null && !domicilio.getAltura().isEmpty() ? domicilio.getAltura() : "");
		ventanaDomicilio.getTxtAltura().setEnabled(false);
	}

	private void mostrarPisoPredeterminado() {
		ventanaDomicilio.setTxtPiso(domicilio != null && !domicilio.getPiso().isEmpty() ? domicilio.getPiso() : "");
		ventanaDomicilio.getTxtPiso().setEnabled(false);
	}

	private void configurarBtnCambiarUbicacion() {
		if (ventanaDomicilio.getBtnCambiar().getActionListeners().length == 0) 
			ventanaDomicilio.getBtnCambiar().addActionListener(a -> modificarValoresSegunClick());	
	}

	private void modificarValoresSegunClick() {
		vaciarCombosDomicilio();
		if (ventanaDomicilio.getBtnCambiar().getText().equals("Cambiar")) {
			actualizarComboBox(ventanaDomicilio.getComboBoxPais(), ObtenerNombrePaises());
			ventanaDomicilio.getBtnCambiar().setText("Restablecer");
		} else {
			mostrarValoresPredeterminados();
			ventanaDomicilio.getBtnCambiar().setText("Cambiar");
		}
	}

	private void obtenerDomicilioActual(int idPersona) {
		domicilio = agenda.obtenerDomicilio(idPersona) != null ? agenda.obtenerDomicilio(idPersona) : null;
	}

	private void agregarListenersMostrarUbicaciones() {
			if(ventanaDomicilio.getComboBoxPais().getActionListeners().length == 0)
					ventanaDomicilio.getComboBoxPais().addActionListener(a -> actualizarComboBox(ventanaDomicilio.getComboBoxProvincia(), 
					ObtenerNombreProvinciasPorPais(obtenerNombreSeleccionado(ventanaDomicilio.getComboBoxPais().getSelectedItem()))));
		    if(ventanaDomicilio.getComboBoxProvincia().getActionListeners().length == 0)
		    		ventanaDomicilio.getComboBoxProvincia().addActionListener(a -> actualizarComboBox(ventanaDomicilio.getComboBoxLocalidad(),
					ObtenerNombreLocalidadadesPorPaisYProvincia(obtenerNombreSeleccionado(ventanaDomicilio.getComboBoxProvincia().getSelectedItem()),
					obtenerNombreSeleccionado(ventanaDomicilio.getComboBoxPais().getSelectedItem()))));
	}

	private void vaciarCombosDomicilio() {
		ventanaDomicilio.getComboBoxPais().removeAllItems();
		ventanaDomicilio.getComboBoxProvincia().removeAllItems();
		ventanaDomicilio.getComboBoxLocalidad().removeAllItems();
	}

	protected DomicilioDTO getDomicilio() {
		return domicilio;
	}

	protected void setDomicilio(DomicilioDTO domicilio) {
		this.domicilio = domicilio;
	}

}
