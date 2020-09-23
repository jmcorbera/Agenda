package presentacion.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.Agenda;
import modelo.IntermediarioUbicacion;
import presentacion.reportes.ReporteAgenda;
import presentacion.vista.VentanaABMUbicacion;
import presentacion.vista.VentanaDomicilio;
import presentacion.vista.VentanaEditarContactoOPais;
import presentacion.vista.VentanaEditarPersona;
import presentacion.vista.VentanaNacimiento;
import presentacion.vista.VentanaNuevoPaisOContacto;
import presentacion.vista.VentanaPersona;
import presentacion.vista.VentanaABMTipoContacto;
import presentacion.vista.Vista;
import dto.ContactoDTO;
import dto.DomicilioDTO;
import dto.PersonaDTO;

public class Controlador implements ActionListener {
	private Vista vista;
	private List<PersonaDTO> personasEnTabla;
	private DomicilioDTO domicilio;
	private VentanaPersona ventanaPersona;
	private VentanaDomicilio ventanaDomicilio;
	private Agenda agenda;
	private VentanaNacimiento ventanaNacimiento;
	private VentanaABMTipoContacto ventanaTipoContacto;
	private VentanaEditarPersona ventanaEditarPersona;
	private ControladorUbicacion controladorUbicacion;
	private String[] mensajes = { 
			"No ha seleccionado ninguna persona!", 
			"No ha seleccionado ningún contacto",
			"El nombre es obligatorio!",
			"Operación realizada con éxito"};

	public Controlador(Vista vista, Agenda agenda) {
		this.agenda = agenda;
		configurarVentanaNacimiento();
		configurarVentanaPersona();
		configurarVentanaTipoContacto();
		new IntermediarioUbicacion(agenda);
		this.controladorUbicacion = new ControladorUbicacion(agenda, new VentanaABMUbicacion());
		configurarVista(vista);
	}
	
	private void configurarVentanaNacimiento() {
		this.ventanaNacimiento = new VentanaNacimiento();
		if(ventanaNacimiento.getBtnAgregarNacimiento().getActionListeners().length == 0)
			ventanaNacimiento.getBtnAgregarNacimiento().addActionListener(a -> ventanaNacimiento.cerrar());
	}
	
	private void configurarVentanaNuevoContacto() {
		VentanaNuevoPaisOContacto ventNuevoContacto = new VentanaNuevoPaisOContacto();
		ventNuevoContacto.getBtnAceptar().addActionListener(n -> agregarContacto(ventNuevoContacto));
		ventNuevoContacto.getBtnCancelar().addActionListener(c -> ventNuevoContacto.cerrar());
	}

	private void configurarVentanaEditarContacto(String contactoSeleccionado) {
		if (!contactoSeleccionado.isEmpty()) {
			VentanaEditarContactoOPais editarContacto = new VentanaEditarContactoOPais(contactoSeleccionado);
			if(editarContacto.getBtnAceptar().getActionListeners().length == 0)
				editarContacto.getBtnAceptar().addActionListener(c -> editarTipoContacto(editarContacto));
			if(editarContacto.getBtnCancelar().getActionListeners().length == 0)
				editarContacto.getBtnCancelar().addActionListener(c -> editarContacto.cerrar());
			editarContacto.mostrar();
		} else {
			JOptionPane.showMessageDialog(ventanaTipoContacto, mensajes[1]);
		}
	}

	private void configurarVista(Vista vista) {
		this.vista = vista;
		this.vista.getBtnAgregar().addActionListener(a -> ventanaPersona.mostrarVentana());
		this.vista.getBtnBorrar().addActionListener(s -> borrarPersona());
		this.vista.getBtnEditar().addActionListener(s -> configurarVentanaEditarPersona());
		this.vista.getBtnReporte().addActionListener(r -> mostrarReporte());
		this.vista.getMenuItemLocalidad().addActionListener(l -> controladorUbicacion.getVentanaABMLocalidad().mostrarVentana());
		this.vista.getMenuItemTipoContacto().addActionListener(t -> ventanaTipoContacto.mostrarVentana());
	}

	private void configurarVentanaTipoContacto() {
		this.ventanaTipoContacto = new VentanaABMTipoContacto();
		mostrarListaContactosPredeterminados();
		this.ventanaTipoContacto.getBtnEditarContacto().addActionListener(a -> configurarVentanaEditarContacto(getTipoContactoSeleccionado()));
		this.ventanaTipoContacto.getBtnNuevoContacto().addActionListener(a -> configurarVentanaNuevoContacto());
		this.ventanaTipoContacto.getBtnAceptar().addActionListener(a -> ventanaTipoContacto.cerrar());
		this.ventanaTipoContacto.getBtnEliminarContacto().addActionListener(a -> eliminarContacto());
	}

	private void configurarVentanaPersona() {
		this.ventanaPersona = VentanaPersona.getInstance();
		mostrarDesplegableTipoContacto(ventanaPersona.getCBTipoContacto());
		this.ventanaPersona.getBtnAgregarPersona().addActionListener(p -> guardarPersona(getPersonaAAgregar()));
		this.ventanaPersona.getBtnNacimiento().addActionListener(n -> ventanaNacimiento.mostrarVentana());
		this.ventanaPersona.getBtnDomicilio().addActionListener(n -> configurarVentanaNuevoDomicilio(personasEnTabla.size()));
	}
	
	private void configurarVentanaDomicilio(int id) {
		ventanaDomicilio = new VentanaDomicilio();
		vaciarCombosDomicilio();	
		ventanaDomicilio.mostrarVentana();
		IntermediarioUbicacion.obtenerListaPaises(ventanaDomicilio.getComboBoxPais());
		IntermediarioUbicacion.deshabilitarProvinciaYLocalidad(ventanaDomicilio.getComboBoxProvincia(), ventanaDomicilio.getComboBoxLocalidad());
		if(ventanaDomicilio.getBtnCancelar().getActionListeners().length == 0)
			ventanaDomicilio.getBtnCancelar().addActionListener(a -> cerrarVentanaDomicilio());
		if(ventanaDomicilio.getBtnAceptar().getActionListeners().length == 0)
			ventanaDomicilio.getBtnAceptar().addActionListener(a -> guardarDomicilio(id));	
	}

	private void cerrarVentanaDomicilio() {
		ventanaDomicilio.cerrar();
		ventanaDomicilio = null;
	}
	
	private void configurarVentanaNuevoDomicilio(int id) {
		configurarVentanaDomicilio(id);
		ventanaDomicilio.ocultarBotonesCambiar();
		agregarListenersMostrarUbicaciones();
	}
	
	private void configurarVentanaEditarDomicilio(int idPersona) {
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
		if(ventanaDomicilio.getBtnCambiarCalle().getActionListeners().length == 0)
			ventanaDomicilio.getBtnCambiarCalle().addActionListener(a -> configurarBtnCambiarCalle());
		if(ventanaDomicilio.getBtnCambiarAltura().getActionListeners().length == 0)
			ventanaDomicilio.getBtnCambiarAltura().addActionListener(a -> configurarBtnCambiarAltura());
		if(ventanaDomicilio.getBtnCambiarPiso().getActionListeners().length == 0)
			ventanaDomicilio.getBtnCambiarPiso().addActionListener(a -> configurarBtnCambiarPiso());
	}

	private void configurarBtnCambiarPiso() {
		if(ventanaDomicilio.getBtnCambiarPiso().getText().equals("Cambiar")) {
			ventanaDomicilio.getBtnCambiarPiso().setText("Cancelar");
			ventanaDomicilio.getTxtPiso().setText("");
			ventanaDomicilio.getTxtPiso().setEnabled(true);
		}
		else {
			ventanaDomicilio.getBtnCambiarPiso().setText("Cambiar");
			mostrarPisoPredeterminado();
		}
	}

	private void configurarBtnCambiarAltura() {
		if(ventanaDomicilio.getBtnCambiarAltura().getText().equals("Cambiar")) {
			ventanaDomicilio.getBtnCambiarAltura().setText("Cancelar");
			ventanaDomicilio.getTxtAltura().setText("");
			ventanaDomicilio.getTxtAltura().setEnabled(true);
		}
		else {
			ventanaDomicilio.getBtnCambiarAltura().setText("Cambiar");
			mostrarAlturaPredeterminada();
		}
	}

	private void configurarBtnCambiarCalle() {
		if(ventanaDomicilio.getBtnCambiarCalle().getText().equals("Cambiar")) {
			ventanaDomicilio.getBtnCambiarCalle().setText("Cancelar");
			ventanaDomicilio.getTxtCalle().setText("");
			ventanaDomicilio.getTxtCalle().setEnabled(true);
		}
		else {
			ventanaDomicilio.getBtnCambiarCalle().setText("Cambiar");
			mostrarCallePredeterminada();
		}
	}

	private void mostrarValoresPredeterminados() {
		String pais = domicilio!= null ? domicilio.getPais() : "";
		String provincia = domicilio != null ? domicilio.getProvincia() : "";
		String localidad = domicilio != null ? domicilio.getLocalidad() : "";
		if (pais!="")
			mostrarSeleccionadoPrimero(pais, ventanaDomicilio.getComboBoxPais());
		if(provincia!="") {
			IntermediarioUbicacion.mostrarProvincias(IntermediarioUbicacion.getPaisSeleccionado(pais), ventanaDomicilio.getComboBoxProvincia());
			mostrarSeleccionadoPrimero(provincia, ventanaDomicilio.getComboBoxProvincia());
		}
		if(localidad!="") {
			IntermediarioUbicacion.mostrarLocalidades(IntermediarioUbicacion.getProvinciaSeleccionada(provincia, IntermediarioUbicacion.getPaisSeleccionado(pais)), ventanaDomicilio.getComboBoxLocalidad());
			mostrarSeleccionadoPrimero(localidad, ventanaDomicilio.getComboBoxLocalidad());
		}
		mostrarTxtsPredeterminados();
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
		ventanaDomicilio.setTxtAltura(domicilio != null && !domicilio.getAltura().isEmpty() ? domicilio.getAltura() : "");
		ventanaDomicilio.getTxtAltura().setEnabled(false);
	}
	
	private void mostrarPisoPredeterminado() {
		ventanaDomicilio.setTxtPiso(domicilio != null && !domicilio.getPiso().isEmpty() ? domicilio.getPiso() : "");
		ventanaDomicilio.getTxtPiso().setEnabled(false);
	}
	
	private void configurarBtnCambiarUbicacion() {
		if(ventanaDomicilio.getBtnCambiar().getActionListeners().length == 0) {
			ventanaDomicilio.getBtnCambiar().addActionListener(a -> modificarValoresSegunClick());}
	}

	private void modificarValoresSegunClick() {
		vaciarCombosDomicilio();
		if(ventanaDomicilio.getBtnCambiar().getText().equals("Cambiar")) {
			IntermediarioUbicacion.obtenerListaPaises(ventanaDomicilio.getComboBoxPais());
			ventanaDomicilio.getBtnCambiar().setText("Restablecer");
		}
		else {
			mostrarValoresPredeterminados();
			ventanaDomicilio.getBtnCambiar().setText("Cambiar");
		}
	}	

	private void obtenerDomicilioActual(int idPersona) {
		domicilio = agenda.obtenerDomicilio(idPersona)!= null ? agenda.obtenerDomicilio(idPersona) : null;
	}
	
	private void agregarListenersMostrarUbicaciones() {
		if(ventanaDomicilio.getComboBoxPais().getActionListeners().length == 0)
			ventanaDomicilio.getComboBoxPais().addActionListener(a -> IntermediarioUbicacion.mostrarProvincias(IntermediarioUbicacion.getPaisSeleccionado(ventanaDomicilio.getComboBoxPais()), ventanaDomicilio.getComboBoxProvincia()));
		if(ventanaDomicilio.getComboBoxProvincia().getActionListeners().length == 0)
			ventanaDomicilio.getComboBoxProvincia().addActionListener(a ->IntermediarioUbicacion.mostrarLocalidades(IntermediarioUbicacion.getProvinciaSeleccionada(ventanaDomicilio.getComboBoxProvincia(),IntermediarioUbicacion.getPaisSeleccionado(ventanaDomicilio.getComboBoxPais())), ventanaDomicilio.getComboBoxLocalidad()));
	}
	
	private void vaciarCombosDomicilio() {
		ventanaDomicilio.getComboBoxPais().removeAllItems();
		ventanaDomicilio.getComboBoxProvincia().removeAllItems();
		ventanaDomicilio.getComboBoxLocalidad().removeAllItems();
	}
	
	private void configurarVentanaEditarPersona() {
		PersonaDTO personaSeleccionada = getPersonaSeleccionada();
		if (personaSeleccionada != null) {
			ventanaEditarPersona = VentanaEditarPersona.getInstance(personaSeleccionada.getId());
			ventanaEditarPersona.mostrar();
			cambiarValoresDeElementos(personaSeleccionada);
			agregarListeners(personaSeleccionada);
			if(ventanaEditarPersona.getBtnEditarNacimiento().getActionListeners().length == 0)
				ventanaEditarPersona.getBtnEditarNacimiento().addActionListener(a -> ventanaNacimiento.mostrarVentana());
			if(ventanaEditarPersona.getBtnDomicilio().getActionListeners().length == 0)
				ventanaEditarPersona.getBtnDomicilio().addActionListener(a -> configurarVentanaEditarDomicilio(personaSeleccionada.getId()));
			if(ventanaEditarPersona.getBtnAceptar().getActionListeners().length == 0)
				ventanaEditarPersona.getBtnAceptar().addActionListener(a -> actualizarPersona(getPersonaEditada(personaSeleccionada)));
		} else {
			JOptionPane.showMessageDialog(ventanaPersona, mensajes[0]);
		}
	}
	
	private void guardarDomicilio(int id) {
		Object seleccionado;
		seleccionado = ventanaDomicilio.getComboBoxPais().getSelectedItem();
		String pais = seleccionado == null ? "" : seleccionado.toString();
		seleccionado = ventanaDomicilio.getComboBoxProvincia().getSelectedItem();
		String provincia = seleccionado == null ? "" : seleccionado.toString();
		seleccionado = ventanaDomicilio.getComboBoxLocalidad().getSelectedItem();
		String localidad = seleccionado == null ? "" : seleccionado.toString();
		String calle = ventanaDomicilio.getTxtCalle().getText();
		String altura = ventanaDomicilio.getTxtAltura().getText();
		String piso = ventanaDomicilio.getTxtPiso().getText();
		domicilio = new DomicilioDTO(id, pais, provincia, localidad, "", calle, altura, piso);
		if(!domicilio.isValid().isEmpty()) {
			JOptionPane.showMessageDialog(ventanaDomicilio, domicilio.isValid());
			domicilio = null;
			return;
		}
		cerrarVentanaDomicilio();
	}

	private PersonaDTO getPersonaSeleccionada() {
		int[] filasSeleccionadas = this.vista.getTablaPersonas().getSelectedRows();
		return filasSeleccionadas.length != 0 ? personasEnTabla.get(filasSeleccionadas[0]) : null;
	}
	
	private void agregarListeners(PersonaDTO personaSeleccionada) {
		ventanaEditarPersona.getBtnCambiarEmail().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ventanaEditarPersona.getTxtEmail().isEnabled()) {
					ventanaEditarPersona.getBtnCambiarEmail().setText("Cambiar");
					ventanaEditarPersona.getTxtEmail().setEnabled(false);
					ventanaEditarPersona.getTxtEmail().setText(personaSeleccionada.getEmail());
				} else {
					ventanaEditarPersona.getBtnCambiarEmail().setText("Cancelar");
					ventanaEditarPersona.getTxtEmail().setEnabled(true);
				}
			}
		});
		ventanaEditarPersona.getBtnCambiarNombre().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (ventanaEditarPersona.getTxtNombre().isEnabled()) {
					ventanaEditarPersona.getBtnCambiarNombre().setText("Cambiar");
					ventanaEditarPersona.getTxtNombre().setEnabled(false);
					ventanaEditarPersona.getTxtNombre().setText(personaSeleccionada.getNombre());
				} else {
					ventanaEditarPersona.getBtnCambiarNombre().setText("Cancelar");
					ventanaEditarPersona.getTxtNombre().setEnabled(true);
				}
			}
		});
		ventanaEditarPersona.getBtnCambiarTelefono().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ventanaEditarPersona.getTxtTelefono().isEnabled()) {
					ventanaEditarPersona.getBtnCambiarTelefono().setText("Cambiar");
					ventanaEditarPersona.getTxtTelefono().setEnabled(false);
					ventanaEditarPersona.getTxtTelefono().setText(personaSeleccionada.getTelefono());
				} else {
					ventanaEditarPersona.getBtnCambiarTelefono().setText("Cancelar");
					ventanaEditarPersona.getTxtTelefono().setEnabled(true);
				}
			}
		});
	}

	private void cambiarValoresDeElementos(PersonaDTO personaSeleccionada) {
		ventanaEditarPersona.getTxtEmail().setText(personaSeleccionada.getEmail());
		ventanaEditarPersona.getTxtFechaNacimiento().setText(personaSeleccionada.getNacimiento());
		ventanaEditarPersona.getTxtTelefono().setText(personaSeleccionada.getTelefono());
		ventanaEditarPersona.getTxtNombre().setText(personaSeleccionada.getNombre());
		if(ventanaNacimiento.getBtnAgregarNacimiento().getActionListeners().length == 0)
			ventanaNacimiento.getBtnAgregarNacimiento().addActionListener(a -> modificarTxtNacimiento(ventanaEditarPersona.getTxtFechaNacimiento()));
		mostrarDesplegableTipoContacto(ventanaEditarPersona.getComboBoxTipoContacto());
		mostrarTipoContactoSeleccionadoPrimero(personaSeleccionada);
	}

	private void mostrarTipoContactoSeleccionadoPrimero(PersonaDTO personaSeleccionada) {
		ComboBoxModel<String> modeloLista = ventanaEditarPersona.getComboBoxTipoContacto().getModel();
		int largoLista = modeloLista.getSize();
		if (largoLista > 0) {
			String primero = modeloLista.getElementAt(0);
			modeloLista.setSelectedItem(personaSeleccionada.getContactoId());
			@SuppressWarnings("unused")
			String cambiarPrimero = modeloLista.getElementAt(0);
			cambiarPrimero = personaSeleccionada.getContactoId();
			@SuppressWarnings("unused")
			String cambiarUltimo = modeloLista.getElementAt(largoLista);
			cambiarUltimo = primero;
		}
	}
	
	private void mostrarSeleccionadoPrimero(String seleccionado, JComboBox<String> combo) {
		if(seleccionado.isEmpty()) {
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
		}
		else {
			modeloLista.setSelectedItem(seleccionado);
		}
	}

	private void modificarTxtNacimiento(JTextField txtNacimiento) {
		txtNacimiento.setText(crearStringFecha(ventanaNacimiento.getFecha().getDate()));
		ventanaNacimiento.cerrar();
	}

	private PersonaDTO getPersonaEditada(PersonaDTO personaSinEditar) {
		String nombre = !ventanaEditarPersona.getTxtNombre().isEnabled() ? personaSinEditar.getNombre(): ventanaEditarPersona.getTxtNombre().getText();
		String telefono = !ventanaEditarPersona.getTxtTelefono().isEnabled() ? personaSinEditar.getTelefono(): ventanaEditarPersona.getTxtTelefono().getText();
		String nacimiento = !ventanaEditarPersona.getTxtFechaNacimiento().isEditable() ? personaSinEditar.getNacimiento(): crearStringFecha(ventanaNacimiento.getFecha().getDate());
		String email = !ventanaEditarPersona.getTxtEmail().isEnabled() ? personaSinEditar.getEmail(): ventanaEditarPersona.getTxtEmail().getText();
		String contactoId = !ventanaEditarPersona.getComboBoxTipoContacto().isEnabled() ? personaSinEditar.getContactoId(): ventanaEditarPersona.getComboBoxTipoContacto().getItemAt(ventanaEditarPersona.getComboBoxTipoContacto().getSelectedIndex());
		return new PersonaDTO(personaSinEditar.getId(), nombre, telefono, nacimiento, email, contactoId);
	}

	private PersonaDTO getPersonaAAgregar() {
		return new PersonaDTO(personasEnTabla.size()+1, ventanaPersona.getTxtNombre().getText(), ventanaPersona.getTxtTelefono().getText(),
				crearStringFecha(ventanaNacimiento.getFecha().getDate()), ventanaPersona.getTxtEmail().getText(), ventanaPersona.getJComboBoxTipoContacto()
						.getItemAt(ventanaPersona.getJComboBoxTipoContacto().getSelectedIndex()));
	}

	private void actualizarPersona(PersonaDTO personaEditada) {
		String mensajeValidezPersona = personaEditada.isValid();
		if(!mensajeValidezPersona.isEmpty()) {
			JOptionPane.showMessageDialog(ventanaEditarPersona, mensajeValidezPersona);
			return;
		}
		this.agenda.actualizarPersona(personaEditada.getId(), personaEditada);
		
		if(domicilio!= null) {
			if(agenda.obtenerDomicilio(domicilio.getId()) == null) {
				agenda.agregarDomicilio(domicilio);
				domicilio = null;
				return;
			}
			this.agenda.modificarDomicilio(domicilio);
			domicilio = null;
		}
	
		this.refrescarTabla();
		this.ventanaEditarPersona.cerrar();
		JOptionPane.showMessageDialog(ventanaPersona, mensajes[3]);
	}

	private void mostrarListaContactosPredeterminados() {
		ventanaTipoContacto.getModelTipoContactos().setRowCount(0);
		List<ContactoDTO> contactos = agenda.obtenerContactos();
		for (int i = 0; i < contactos.size(); i++) {
			String nombre = contactos.get(i).getNombreContacto();
			Object[] fila = { nombre };
			ventanaTipoContacto.getModelTipoContactos().addRow(fila);
		}
	}

	private void mostrarDesplegableTipoContacto(JComboBox<String> desplegableContactos) {
		List<ContactoDTO> contactos = agenda.obtenerContactos();
		String[] nombreContactos = new String[contactos.size()];
		for (int i = 0; i < contactos.size(); i++) {
			nombreContactos[i] = contactos.get(i).getNombreContacto();
		}
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nombreContactos);
		desplegableContactos.setModel(model);
	}

	private String getTipoContactoSeleccionado() {
		DefaultTableModel modelo = ventanaTipoContacto.getModelTipoContactos();
		try {
			return (String) modelo.getValueAt(ventanaTipoContacto.getTableTipoContactos().getSelectedRow(), 0);
		} catch (Exception e) {
			return "";
		}
	}

	private void guardarPersona(PersonaDTO persona) {
		String mensajeValidezPersona = persona.isValid();
		if(!mensajeValidezPersona.isEmpty()) {
			JOptionPane.showMessageDialog(ventanaPersona, mensajeValidezPersona);
			return;
		}
		this.agenda.agregarPersona(persona);
		if(domicilio != null) {
			this.agenda.agregarDomicilio(new DomicilioDTO(agenda.obtenerPersonas().size(), domicilio.getPais(),
			domicilio.getProvincia(), domicilio.getLocalidad(),
			domicilio.getDepartamento(), domicilio.getCalle(),
			domicilio.getAltura(), domicilio.getPiso()));
			domicilio = null;
		}
		
		this.refrescarTabla();
		this.ventanaPersona.cerrar();
		JOptionPane.showMessageDialog(ventanaPersona, mensajes[3]);
	}

	public void borrarPersona() {
		PersonaDTO personaSeleccionada = getPersonaSeleccionada();
		if (personaSeleccionada != null) {
			agenda.borrarDomicilio(personaSeleccionada.getId());
			agenda.borrarPersona(personaSeleccionada);
			refrescarTabla();
		} else {
			JOptionPane.showMessageDialog(ventanaPersona, mensajes[0]);
		}
	}
	
	private void agregarContacto(VentanaNuevoPaisOContacto v) {
		String mensajeValidezContacto = new ContactoDTO(v.getTxtNuevoNombre().toString()).isValid();
		if(mensajeValidezContacto.isEmpty()) {
			agenda.agregarContacto(v.getTxtNuevoNombre().getText());
			v.cerrar();
			mostrarListaContactosPredeterminados();
			mostrarDesplegableTipoContacto(ventanaPersona.getCBTipoContacto());
		}
		else {
			JOptionPane.showMessageDialog(v, mensajeValidezContacto);
		}
	}

	private void editarTipoContacto(VentanaEditarContactoOPais v) {
		String mensajeValidezContacto = new ContactoDTO(v.getTxtNuevo().getText().toString()).isValid();
		if(mensajeValidezContacto.isEmpty()) {
			agenda.editarContacto(v.getTxtNombreAnterior().getText(), v.getTxtNuevo().getText());
			v.cerrar();
			mostrarListaContactosPredeterminados();
			mostrarDesplegableTipoContacto(ventanaPersona.getCBTipoContacto());
		}
		else {
			JOptionPane.showMessageDialog(v, mensajeValidezContacto);
		}
	}

	private void eliminarContacto() {
		String seleccionado = getTipoContactoSeleccionado();
		if (!seleccionado.isEmpty()) {
			this.agenda.borrarContacto(seleccionado);
			mostrarListaContactosPredeterminados();
			mostrarDesplegableTipoContacto(ventanaPersona.getCBTipoContacto());
		} else {
			JOptionPane.showMessageDialog(ventanaTipoContacto, mensajes[1]);
		}
	}

	private String crearStringFecha(Object object) {
		try {
			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");
			return formatoFecha.format(object);
		} catch (Exception e) {
			return "";
		}
	}

	private void mostrarReporte() {
		ReporteAgenda reporte = new ReporteAgenda(agenda.obtenerPersonas());
		reporte.mostrar();
	}

	public void inicializar() {
		this.refrescarTabla();
		this.vista.show();
	}

	private void refrescarTabla() {
		this.personasEnTabla = agenda.obtenerPersonas();
		this.vista.llenarTabla(this.personasEnTabla);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

}