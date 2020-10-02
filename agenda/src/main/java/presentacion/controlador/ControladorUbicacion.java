package presentacion.controlador;

import java.util.List;
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
import presentacion.vista.Intermediario.IntermediarioVista;
import presentacion.vista.Intermediario.Mensajes;

public class ControladorUbicacion {
	private VentanaABMUbicacion ventanaAMBLocalidad;
	private VentanaDomicilio ventanaDomicilio;
	private DomicilioDTO domicilio;
	private Agenda agenda;
	
	public ControladorUbicacion(Agenda agenda, VentanaABMUbicacion ventanaAMBLocalidad) {
		if(!ventanaAMBLocalidad.isVisible()) {
			this.ventanaAMBLocalidad = VentanaABMUbicacion.getInstance();
			this.agenda = agenda;
			configurarVentanaABMLocalidades();
		}
	}

	private void configurarVentanaABMLocalidades() {
		ventanaAMBLocalidad.deshabilitarDependientes();
		ventanaAMBLocalidad.eliminarActionListeners();
		this.ventanaAMBLocalidad.getBtnAgregarPais().addActionListener(l -> configurarVentanaNuevoPais());
		this.ventanaAMBLocalidad.getBtnEditarPais().addActionListener(l -> configurarVentanaEditarPais());
		this.ventanaAMBLocalidad.getBtnEliminarPais().addActionListener(l -> borrarPais());
		this.ventanaAMBLocalidad.getBtnAgregarProvincia().addActionListener(l -> configurarVentanaNuevaProvincia());
		this.ventanaAMBLocalidad.getBtnEditarProvincia().addActionListener(l -> configurarVentanaEditarProvincia());
		this.ventanaAMBLocalidad.getBtnEliminarProvincia().addActionListener(l -> borrarProvincia());
		this.ventanaAMBLocalidad.getBtnAgregarLocalidad().addActionListener(l -> configurarVentanaNuevaLocalidad());
		this.ventanaAMBLocalidad.getBtnEditarLocalidad().addActionListener(l -> configurarVentanaEditarLocalidad());
		this.ventanaAMBLocalidad.getBtnEliminarLocalidad().addActionListener(l -> borrarLocalidad());
		this.ventanaAMBLocalidad.getComboBoxPais().addActionListener(l -> mostrarProvinciasAsociadas(ventanaAMBLocalidad.getComboBoxProvincia(),
				ventanaAMBLocalidad.getComboBoxPais().getSelectedItem()));
		this.ventanaAMBLocalidad.getComboBoxProvincia().addActionListener(l -> 
			mostrarLocalidadesAsociadas(ventanaAMBLocalidad.getComboBoxLocalidad(),
				ventanaAMBLocalidad.getComboBoxPais().getSelectedItem(),
				ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem()));
		this.ventanaAMBLocalidad.getBtnVolver().addActionListener(a-> cerrarVentanaAMBLocalidad());
		this.refrescarListaPaises();
	}
	
	private void cerrarVentanaAMBLocalidad() {
		ventanaAMBLocalidad.cerrar();
	}

	private String getNombre(Object seleccionado) {
		return IntermediarioVista.obtenerNombreSeleccionado(seleccionado);
	}
	
	private void mostrarProvinciasAsociadas(JComboBox<String> cmbProvincia, Object paisSeleccionado) {
		IntermediarioVista.actualizarComboBox(cmbProvincia, 
		ObtenerNombreProvinciasPorPais(getNombre(paisSeleccionado)));
	}
	
	private void mostrarLocalidadesAsociadas(JComboBox<String> cmbLocalidad, Object paisSeleccionado, Object provinciaSeleccionada) {
		IntermediarioVista.actualizarComboBox(cmbLocalidad,
				ObtenerNombreLocalidadadesPorPaisYProvincia(getNombre(provinciaSeleccionada),
						getNombre(paisSeleccionado)));
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
		String[] nombreLocalidades = new String[localidades.size()];
		for (int i = 0; i < localidades.size(); i++)
			nombreLocalidades[i] = localidades.get(i).getNombre();
		return nombreLocalidades;
	}

	private void refrescarListaLocalidades() {
		mostrarLocalidadesAsociadas(ventanaAMBLocalidad.getComboBoxLocalidad(),
				ventanaAMBLocalidad.getComboBoxPais().getSelectedItem(),
				ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem());
		this.ventanaAMBLocalidad.limpiarCombos();
	}

	private void refrescarListaPaises() {
		mostrarPaisesPredeterminados(ventanaAMBLocalidad.getComboBoxPais());
		this.ventanaAMBLocalidad.limpiarCombos();
	}

	private void mostrarPaisesPredeterminados(JComboBox<String> cmbPais) {
		IntermediarioVista.actualizarComboBox(cmbPais, ObtenerNombrePaises());
	}

	private void refrescarListaProvincias(JComboBox<String> cmbProvincia, Object paisSeleccionado) {
		mostrarProvinciasAsociadas(cmbProvincia, paisSeleccionado);
		this.ventanaAMBLocalidad.limpiarCombos();
	}

	// ABM Pais
	private void configurarVentanaNuevoPais() {
		VentanaNuevoPaisOContacto ventNuevoPais = new VentanaNuevoPaisOContacto();
		ventNuevoPais.cambiarTitulo("Nuevo país");
		ventNuevoPais.getBtnAceptar().addActionListener(n -> agregarPais(ventNuevoPais));
		ventNuevoPais.getBtnCancelar().addActionListener(c -> ventNuevoPais.cerrar());
	}

	// Alta
	private void agregarPais(VentanaNuevoPaisOContacto v) {
		try {
			String nuevo = v.getTxtNuevoNombre().getText();
			if (nuevo.isEmpty()) {
				JOptionPane.showMessageDialog(v, Mensajes.esObligatorio);
				return;
			}
			if (agenda.existsPais(nuevo)) {
				JOptionPane.showMessageDialog(v, Mensajes.yaExiste);
				return;
			}
			this.agenda.agregarPais(new PaisDTO(0, nuevo));
			v.cerrar();
			this.refrescarListaPaises();
			ventanaAMBLocalidad.deshabilitarDependientes();
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, Mensajes.operacionExitosa);
		} catch (Exception e) {
		}
	}

	// Baja
	private void borrarPais() {
		try {
			PaisDTO pais = agenda.obtenerPaisPorNombre(getNombre(ventanaAMBLocalidad.getComboBoxPais().getSelectedItem()));
			this.agenda.borrarPais(pais);
			this.refrescarListaPaises();
			ventanaAMBLocalidad.deshabilitarDependientes();
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, Mensajes.operacionExitosa);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, Mensajes.noSelecciono);
		}
	}

	// Modificación
	private void configurarVentanaEditarPais() {
		try {
			String paisSeleccionado = getNombre(this.ventanaAMBLocalidad.getComboBoxPais().getSelectedItem());
			if(paisSeleccionado.isEmpty()){
				JOptionPane.showMessageDialog(ventanaAMBLocalidad, Mensajes.noSelecciono);
				return;
			}
			VentanaEditarContactoOPais editarPais = new VentanaEditarContactoOPais(paisSeleccionado);
			editarPais.cambiaTitulo("Edición país");
			editarPais.getBtnAceptar().addActionListener(c -> editarPais(editarPais));
			editarPais.getBtnCancelar().addActionListener(c -> editarPais.cerrar());
			editarPais.mostrar();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, Mensajes.noSelecciono);
		}
	}

	private void editarPais(VentanaEditarContactoOPais v) {
		try {
			String anterior = v.getTxtNombreAnterior().getText();
			String nuevo = v.getTxtNuevo().getText();
			PaisDTO pais = agenda.obtenerPaisPorNombre(anterior);
			if (v.getTxtNuevo().getText().equals("")) {
				JOptionPane.showMessageDialog(v, Mensajes.esObligatorio);
				return;
			}
			if (agenda.existsPais(nuevo)) {
				JOptionPane.showMessageDialog(v, Mensajes.yaExiste);
				return;
			}
			this.agenda.modificarPais(new PaisDTO(pais.getIdPais(), nuevo));
			this.refrescarListaPaises();
			ventanaAMBLocalidad.limpiarComboProvincia();
			ventanaAMBLocalidad.deshabilitarDependientes();
			v.cerrar();
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, Mensajes.operacionExitosa);
		} catch (Exception e) {
		}
	}

	// ABM Provincia
	private void configurarVentanaNuevaProvincia() {
		VentanaNuevaProvinciaOLocalidad ventanaNuevaProvincia = new VentanaNuevaProvinciaOLocalidad();
		mostrarPaisesPredeterminados(ventanaNuevaProvincia.getComboBoxPadre());
		ventanaNuevaProvincia.getBtnAceptar().addActionListener(n -> agregarProvincia(ventanaNuevaProvincia));
		ventanaNuevaProvincia.getBtnCancelar().addActionListener(c -> ventanaNuevaProvincia.cerrar());
		ventanaNuevaProvincia.getBtnCambiar().setVisible(false);
	}

	// Alta
	private void agregarProvincia(VentanaNuevaProvinciaOLocalidad ventanaNuevaProvincia) {
		try {
			String nombre = getNombre(ventanaNuevaProvincia.getComboBoxPadre().getSelectedItem());
			PaisDTO pais = agenda.obtenerPaisPorNombre(nombre);
			String nuevo = ventanaNuevaProvincia.getTxtNombre().getText();
			if (nuevo.isEmpty()) {
				JOptionPane.showMessageDialog(ventanaNuevaProvincia, Mensajes.esObligatorio);
				return;
			}
			if (agenda.existsProvincia(nuevo, pais.getIdPais())) {
				JOptionPane.showMessageDialog(ventanaNuevaProvincia, Mensajes.yaExiste);
				return;
			}
			this.agenda.agregarProvincia(new ProvinciaDTO(0, nuevo, pais));
			this.refrescarListaProvincias(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxPais().getSelectedItem());
			ventanaAMBLocalidad.deshabilitarDependientes();
			ventanaNuevaProvincia.cerrar();
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, Mensajes.operacionExitosa);
		} catch (Exception e) {
		}
	}

	// Baja
	private void borrarProvincia() {
		try {
			String nombreProvincia = getNombre(ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem());
			String nombrePais = getNombre(ventanaAMBLocalidad.getComboBoxPais().getSelectedItem());
			if (nombrePais.isEmpty()) {
				JOptionPane.showMessageDialog(ventanaAMBLocalidad, Mensajes.noSelecciono);
				return;
			}
			PaisDTO pais = agenda.obtenerPaisPorNombre(nombrePais);
			ProvinciaDTO provincia = agenda.obtenerProvincia(nombreProvincia, pais.getIdPais());
			this.agenda.borrarProvincia(provincia);
			refrescarListaProvincias(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxPais().getSelectedItem());
			ventanaAMBLocalidad.deshabilitarDependientes();
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, Mensajes.operacionExitosa);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, Mensajes.noSelecciono);
			return;
		}
	}

	// Modificación
	private void configurarVentanaEditarProvincia() {
		try {
			String provinciaSeleccionada = getNombre(ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem());
			VentanaNuevaProvinciaOLocalidad editarProvincia = new VentanaNuevaProvinciaOLocalidad();
			mostrarPaisesPredeterminados(editarProvincia.getComboBoxPadre());
			editarProvincia.getTxtNombre().setEnabled(false);
			editarProvincia.getTxtNombre().setText(provinciaSeleccionada);
			editarProvincia.getBtnCambiar().addActionListener(p -> configurarBoton(editarProvincia));
			editarProvincia.getBtnAceptar().addActionListener(c -> editarProvincia(editarProvincia, provinciaSeleccionada));
			editarProvincia.getBtnCancelar().addActionListener(c -> editarProvincia.cerrar());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, Mensajes.noSelecciono);
		}
	}

	private void configurarBoton(VentanaNuevaProvinciaOLocalidad editarProvincia) {
		if (editarProvincia.getTxtNombre().isEnabled()) {
			editarProvincia.getTxtNombre().setEnabled(false);
			editarProvincia.getTxtNombre().setText(getNombre(ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem()));
			editarProvincia.getBtnCambiar().setText("Cambiar");
		} else {
			editarProvincia.getTxtNombre().setEnabled(true);
			editarProvincia.getBtnCambiar().setText("Cancelar");
		}
	}

	private void editarProvincia(VentanaNuevaProvinciaOLocalidad editarProvincia, String anterior) {
		try {
			String nuevo = editarProvincia.getTxtNombre().getText();
			PaisDTO pais = agenda.obtenerPaisPorNombre(getNombre(ventanaAMBLocalidad.getComboBoxPais().getSelectedItem()));
			ProvinciaDTO provincia = agenda.obtenerProvincia(getNombre(ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem()), pais.getIdPais());
			
			if (nuevo.equals("")) {
				JOptionPane.showMessageDialog(editarProvincia, Mensajes.esObligatorio);
				return;
			}
			if (agenda.existsProvincia(nuevo, pais.getIdPais())) {
				JOptionPane.showMessageDialog(editarProvincia, Mensajes.yaExiste);
				return;
			}
			editarProvincia.cerrar();
			this.agenda.modificarProvincia(new ProvinciaDTO(provincia.getIdProvincia(), nuevo, pais));
			refrescarListaProvincias(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxPais().getSelectedItem());
			ventanaAMBLocalidad.deshabilitarDependientes();
			JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, Mensajes.operacionExitosa);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, Mensajes.noSelecciono);
		}
	}

	// ABM Localidad
	private void configurarVentanaNuevaLocalidad() {
		if (getNombre(ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem()).isEmpty()) {
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, Mensajes.noSelecciono);
			return;
		}
		VentanaNuevaProvinciaOLocalidad ventanaNuevaLocalidad = new VentanaNuevaProvinciaOLocalidad();
		ventanaNuevaLocalidad.getLblPadreAlQuePertenece().setText("Provincia a la que pertenece: ");
		mostrarProvinciasAsociadas(ventanaNuevaLocalidad.getComboBoxPadre(), ventanaAMBLocalidad.getComboBoxPais().getSelectedItem());
		ventanaNuevaLocalidad.getBtnAceptar().addActionListener(n -> agregarLocalidad(ventanaNuevaLocalidad));
		ventanaNuevaLocalidad.getBtnCancelar().addActionListener(c -> ventanaNuevaLocalidad.cerrar());
		ventanaNuevaLocalidad.getBtnCambiar().setVisible(false);
	}

	// Alta
	private void agregarLocalidad(VentanaNuevaProvinciaOLocalidad ventanaNuevaLocalidad) {
		try {
			ProvinciaDTO provincia = agenda.obtenerProvincia(getNombre(ventanaNuevaLocalidad.getComboBoxPadre().getSelectedItem()),
					agenda.obtenerPaisPorNombre(getNombre(ventanaAMBLocalidad.getComboBoxPais().getSelectedItem())).getIdPais());
			String nuevo = ventanaNuevaLocalidad.getTxtNombre().getText();
			if (nuevo.isEmpty()) {
				JOptionPane.showMessageDialog(ventanaNuevaLocalidad, Mensajes.esObligatorio);
				return;
			}
			if (agenda.existsLocalidad(nuevo, provincia.getIdProvincia())) {
				JOptionPane.showMessageDialog(ventanaNuevaLocalidad, Mensajes.yaExiste);
				return;
			}
			this.agenda.agregarLocalidad(new LocalidadDTO(0, nuevo, provincia));
			this.refrescarListaLocalidades();
			ventanaAMBLocalidad.deshabilitarDependientes();
			ventanaNuevaLocalidad.cerrar();
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, Mensajes.operacionExitosa);
			}
		catch(Exception e) {
		}
	}

	// Baja
	private void borrarLocalidad() {
		try {
			String nombreLocalidad = getNombre(ventanaAMBLocalidad.getComboBoxLocalidad().getSelectedItem());
			if(nombreLocalidad.isEmpty()) {
				JOptionPane.showMessageDialog(ventanaAMBLocalidad, Mensajes.noSelecciono);
				return;
			}
			String nombrePais = getNombre(ventanaAMBLocalidad.getComboBoxPais().getSelectedItem());
			PaisDTO pais = agenda.obtenerPaisPorNombre(nombrePais);
			LocalidadDTO localidad = agenda.obtenerLocalidad(nombreLocalidad, pais.getIdPais());
			this.agenda.borrarLocalidad(localidad);
			refrescarListaLocalidades();
			ventanaAMBLocalidad.deshabilitarDependientes();
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, Mensajes.operacionExitosa);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(ventanaAMBLocalidad, Mensajes.noSelecciono);
			return;
		}
	}

	// Modificación
	private void configurarVentanaEditarLocalidad() {
		try {
			String localidadSeleccionada = getNombre(ventanaAMBLocalidad.getComboBoxLocalidad().getSelectedItem());
			if(localidadSeleccionada.isEmpty()) {
				JOptionPane.showMessageDialog(ventanaAMBLocalidad, Mensajes.noSelecciono);
				return;
			}
			VentanaNuevaProvinciaOLocalidad editarLocalidad = new VentanaNuevaProvinciaOLocalidad();
			IntermediarioVista.actualizarComboBox(editarLocalidad.getComboBoxPadre(), ObtenerNombreProvinciasPorPais(getNombre(ventanaAMBLocalidad.getComboBoxPais().getSelectedItem())));
			editarLocalidad.getLblPadreAlQuePertenece().setText("Provincia a la que pertenece");
			editarLocalidad.getTxtNombre().setEnabled(false);
			editarLocalidad.getTxtNombre().setText(localidadSeleccionada);
			editarLocalidad.getBtnCambiar().addActionListener(p -> configurarBoton(editarLocalidad));
			editarLocalidad.getBtnAceptar().addActionListener(c -> editarLocalidad(editarLocalidad, localidadSeleccionada));
			editarLocalidad.getBtnCancelar().addActionListener(c -> editarLocalidad.cerrar());

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, Mensajes.noSelecciono);
		}
	}

	private void editarLocalidad(VentanaNuevaProvinciaOLocalidad editarLocalidad, String anterior) {
		try {
			String nuevo = editarLocalidad.getTxtNombre().getText();
			if (nuevo.isEmpty()) {
				JOptionPane.showMessageDialog(editarLocalidad, Mensajes.esObligatorio);
				return;
			}
			ProvinciaDTO provincia = agenda.obtenerProvincia(editarLocalidad.getComboBoxPadre().getSelectedItem().toString(),
					agenda.obtenerPaisPorNombre(getNombre(ventanaAMBLocalidad.getComboBoxPais().getSelectedItem())).getIdPais());
			LocalidadDTO localidad = agenda.obtenerLocalidad(anterior, provincia.getIdProvincia());
			if (agenda.existsLocalidad(nuevo, provincia.getIdProvincia())) {
				JOptionPane.showMessageDialog(editarLocalidad, Mensajes.yaExiste);
				return;
			}
			this.agenda.modificarLocalidad(new LocalidadDTO(localidad.getIdLocalidad(), nuevo, provincia));
			this.refrescarListaLocalidades();
			ventanaAMBLocalidad.deshabilitarDependientes();
			editarLocalidad.cerrar();
			JOptionPane.showMessageDialog(editarLocalidad, Mensajes.operacionExitosa);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(editarLocalidad, Mensajes.noSelecciono);
		}
	}

	public VentanaABMUbicacion getVentanaABMLocalidad() {
		return ventanaAMBLocalidad;
	}

	private void configurarVentanaDomicilio(int id) {
		ventanaDomicilio = new VentanaDomicilio();
		ventanaDomicilio.eliminarActionListeners();
		ventanaDomicilio.vaciarCombos();
		ventanaDomicilio.deshabilitarDependientes();
		ventanaDomicilio.getBtnCancelar().addActionListener(a -> cerrarVentanaDomicilio());
		ventanaDomicilio.getBtnAceptar().addActionListener(a -> guardarDomicilio(id));
		ventanaDomicilio.mostrarVentana();
	}

	private void guardarDomicilio(int id) {
		String nombrePais = getNombre(ventanaDomicilio.getComboBoxPais().getSelectedItem());
		if (nombrePais.equals("")) {
			JOptionPane.showMessageDialog(ventanaDomicilio, "El nombre del país es obligatorio!");
			domicilio = null;
			return;
		}
		String nombreLocalidad = getNombre(ventanaDomicilio.getComboBoxLocalidad().getSelectedItem());
		String nombreProvincia =getNombre(ventanaDomicilio.getComboBoxProvincia().getSelectedItem());
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
		if(ventanaDomicilio == null) {
			configurarVentanaDomicilio(id);
			ventanaDomicilio.ocultarBotonesCambiar();
			agregarListenersMostrarUbicaciones();
			mostrarPaisesPredeterminados(ventanaDomicilio.getComboBoxPais());		
		}
	}

	protected void configurarVentanaEditarDomicilio(int idPersona) {
		if(ventanaDomicilio ==  null) {
			configurarVentanaDomicilio(idPersona);
			agregarListenersMostrarUbicaciones();
			ventanaDomicilio.mostrarBotonesCambiar();
			ventanaDomicilio.establecerTextoCambiar();
			obtenerDomicilioActual(idPersona);
			mostrarValoresPredeterminados();
			mostrarTxtsPredeterminados();
			ventanaDomicilio.getBtnCambiarCalle().addActionListener(a -> configurarBtnCambiarCalle());
			ventanaDomicilio.getBtnCambiarAltura().addActionListener(a -> configurarBtnCambiarAltura());
			ventanaDomicilio.getBtnCambiarPiso().addActionListener(a -> configurarBtnCambiarPiso());
			ventanaDomicilio.getBtnCambiar().addActionListener(a -> modificarValoresSegunClick());	
		}
	}

	private void configurarBtnCambiarPiso() {
		IntermediarioVista.cambiarValores(ventanaDomicilio.getTxtPiso(), ventanaDomicilio.getBtnCambiarPiso(), obtenerPisoPredeterminado());
	}

	private void configurarBtnCambiarAltura() {
		IntermediarioVista.cambiarValores(ventanaDomicilio.getTxtAltura(), ventanaDomicilio.getBtnCambiarAltura(), obtenerAlturaPredeterminada());
	}

	private void configurarBtnCambiarCalle() {
		IntermediarioVista.cambiarValores(ventanaDomicilio.getTxtCalle(), ventanaDomicilio.getBtnCambiarCalle(), obtenerCallePredeterminada());
	}

	private void mostrarValoresPredeterminados() {
		try {
			ventanaDomicilio.vaciarCombos();
			PaisDTO pais = agenda.obtenerPaisPorId(domicilio.getIdPais());
			ProvinciaDTO provincia = agenda.obtenerProvincia(domicilio.getIdProvincia());
			LocalidadDTO localidad = agenda.obtenerLocalidad(domicilio.getIdLocalidad());
			if (pais != null && !pais.getNombre().isEmpty()) {
				mostrarPaisesPredeterminados(ventanaDomicilio.getComboBoxPais());
				IntermediarioVista.mostrarSeleccionadoPrimero(ventanaDomicilio.getComboBoxPais(),pais.getNombre());
				if (provincia != null && !provincia.getNombre().isEmpty()) 
					IntermediarioVista.mostrarSeleccionadoPrimero(ventanaDomicilio.getComboBoxProvincia(), provincia.getNombre());
				else {
					ventanaDomicilio.getComboBoxProvincia().removeAllItems();
					ventanaDomicilio.getComboBoxLocalidad().removeAllItems();
				}
				if (localidad != null && !localidad.getNombre().isEmpty()) 
						IntermediarioVista.mostrarSeleccionadoPrimero(ventanaDomicilio.getComboBoxLocalidad(),localidad.getNombre());
				else 
						ventanaDomicilio.getComboBoxLocalidad().removeAllItems();
			}
			else 
					ventanaDomicilio.vaciarCombos();

		} catch (Exception e) {
		}

		ventanaDomicilio.deshabilitarCombos();
	}

	private void mostrarTxtsPredeterminados() {
		ventanaDomicilio.getTxtCalle().setText(obtenerCallePredeterminada());
		ventanaDomicilio.getTxtAltura().setText(obtenerAlturaPredeterminada());
		ventanaDomicilio.getTxtPiso().setText(obtenerPisoPredeterminado());
		ventanaDomicilio.deshabilitarTxts();
	}

	private String obtenerCallePredeterminada() {
		return domicilio != null && !domicilio.getCalle().isEmpty() ? domicilio.getCalle() : "";
	}

	private String obtenerAlturaPredeterminada() {
		return domicilio != null && !domicilio.getAltura().isEmpty() ? domicilio.getAltura() : "";
	}

	private String obtenerPisoPredeterminado() {
		return domicilio != null && !domicilio.getPiso().isEmpty() ? domicilio.getPiso() : "";
	}

	private void modificarValoresSegunClick() {
		ventanaDomicilio.vaciarCombos();
		ventanaDomicilio.deshabilitarCombos();
		if (ventanaDomicilio.getBtnCambiar().getText().equals("Cambiar")) {
			mostrarPaisesPredeterminados(ventanaDomicilio.getComboBoxPais());
			ventanaDomicilio.getBtnCambiar().setText("Restablecer");
		} else {
			mostrarValoresPredeterminados();
			ventanaDomicilio.getBtnCambiar().setText("Cambiar");
		}
	}

	private void obtenerDomicilioActual(int idPersona) {
		domicilio = agenda.obtenerDomicilio(idPersona);
	}

	private void agregarListenersMostrarUbicaciones() {
		ventanaDomicilio.getComboBoxPais().addActionListener(a -> mostrarProvinciasAsociadas(ventanaDomicilio.getComboBoxProvincia(), 
				ventanaDomicilio.getComboBoxPais().getSelectedItem()));
		ventanaDomicilio.getComboBoxProvincia().addActionListener(a -> mostrarLocalidadesAsociadas(ventanaDomicilio.getComboBoxLocalidad(),
			ventanaDomicilio.getComboBoxPais().getSelectedItem(),
			ventanaDomicilio.getComboBoxProvincia().getSelectedItem()));
	}

	protected DomicilioDTO getDomicilio() {
		return domicilio;
	}
                                    
	protected void setDomicilio(DomicilioDTO domicilio) {
		this.domicilio = domicilio;
	}

}
