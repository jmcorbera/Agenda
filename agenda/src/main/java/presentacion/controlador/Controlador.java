package presentacion.controlador;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import modelo.Agenda;
import modelo.ConvertorFecha;
import presentacion.reportes.ReporteAgenda;
import presentacion.vista.VentanaEditarContacto;
import presentacion.vista.VentanaEditarPersona;
import presentacion.vista.VentanaNacimiento;
import presentacion.vista.VentanaNuevoContacto;
import presentacion.vista.VentanaPersona;
import presentacion.vista.VentanaTipoContacto;
import presentacion.vista.Vista;
import dto.ContactoDTO;
import dto.PersonaDTO;

public class Controlador implements ActionListener {
	private Vista vista;
	private List<PersonaDTO> personasEnTabla;
	private VentanaPersona ventanaPersona;
	private Agenda agenda;
	private VentanaNacimiento ventanaNacimiento;
	private VentanaTipoContacto ventanaTipoContacto;
	private VentanaEditarPersona ventanaEditarPersona;
	private String[] mensajes = { "No ha seleccionado ningun contacto", "El nombre es obligatorio!",
			"Debe ingresar al menos una forma de contacto (Email o telefono)", "El email ingresado es invalido",
			"No ha seleccionado ninguna persona" };

	public Controlador(Vista vista, Agenda agenda) {
		this.agenda = agenda;
		configurarVentanaNacimiento();
		configurarVentanaPersona();
		configurarVista(vista);
		configurarVentanaTipoContacto();
		configurarVentanaPersona();
	}

	private void configurarVista(Vista vista) {
		this.vista = vista;
		this.vista.getBtnAgregar().addActionListener(a -> ventanaPersona.mostrarVentana());
		this.vista.getBtnBorrar().addActionListener(s -> borrarPersona());
		this.vista.getBtnEditar().addActionListener(s -> configurarVentanaEditarPersona());
		this.vista.getBtnReporte().addActionListener(r -> mostrarReporte());
	}

	private void configurarVentanaTipoContacto() {
		this.ventanaTipoContacto = VentanaTipoContacto.getInstance();
		mostrarListaContactosPredeterminados();
		this.ventanaTipoContacto.getBtnEditarContacto()
				.addActionListener(a -> configurarVentanaEditarContacto(getTipoContactoSeleccionado()));
		this.ventanaTipoContacto.getBtnNuevoContacto().addActionListener(a -> configurarVentanaNuevoContacto());
		this.ventanaTipoContacto.getBtnAceptar().addActionListener(a -> ventanaTipoContacto.cerrar());
		this.ventanaTipoContacto.getBtnEliminarContacto().addActionListener(a -> eliminarContacto());
	}

	private void configurarVentanaPersona() {
		this.ventanaPersona = VentanaPersona.getInstance();
		mostrarDesplegableTipoContacto(ventanaPersona.getCBTipoContacto());
		this.ventanaPersona.getBtnAgregarPersona().addActionListener(p -> guardarPersona());
		this.ventanaPersona.getBtnNacimiento().addActionListener(n -> ventanaNacimiento.mostrarVentana());
		this.ventanaPersona.getBtnEditarTipo().addActionListener(a -> ventanaTipoContacto.mostrarVentana());
	}

	private void configurarVentanaNacimiento() {
		this.ventanaNacimiento = new VentanaNacimiento();
		ventanaNacimiento.getBtnAgregarNacimiento().addActionListener(a -> ventanaNacimiento.cerrar());
	}

	private PersonaDTO getPersonaSeleccionada() {
		int[] filasSeleccionadas = this.vista.getTablaPersonas().getSelectedRows();
		return filasSeleccionadas.length != 0 ? personasEnTabla.get(filasSeleccionadas[0]) : null;
	}

	private void configurarVentanaEditarPersona() {
		PersonaDTO personaSeleccionada = getPersonaSeleccionada();
		if (personaSeleccionada != null) {
			ventanaEditarPersona = new VentanaEditarPersona(personaSeleccionada.getId());
			cambiarValoresDeElementos(personaSeleccionada);
			ventanaEditarPersona.getBtnEditarNacimiento().addActionListener(a -> ventanaNacimiento.mostrarVentana());
			ventanaEditarPersona.getBtnAceptar()
					.addActionListener(a -> actualizarPersona(getPersonaEditada(), personaSeleccionada.getId()));
			ventanaEditarPersona.mostrar();
		} else {
			mostrarMensaje(ventanaPersona, mensajes[4]);
		}
	}

	private void cambiarValoresDeElementos(PersonaDTO personaSeleccionada) {
		ventanaEditarPersona.getTxtEmail().setText(personaSeleccionada.getEmail());
		ventanaEditarPersona.getTxtFechaNacimiento().setText(personaSeleccionada.getNacimiento());
		ventanaEditarPersona.getTxtTelefono().setText(personaSeleccionada.getTelefono());
		ventanaEditarPersona.getTxtNombre().setText(personaSeleccionada.getNombre());
		ventanaNacimiento.getBtnAgregarNacimiento()
				.addActionListener(a -> modificarTxtNacimiento(ventanaEditarPersona.getTxtFechaNacimiento()));
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

	private void modificarTxtNacimiento(JTextField txtNacimiento) {
		txtNacimiento.setText(crearStringFecha());
		ventanaNacimiento.cerrar();
	}

	private PersonaDTO getPersonaEditada() {
		PersonaDTO persona = getPersonaSeleccionada();
		String nombre = !ventanaEditarPersona.getTxtNombre().isEnabled() ? persona.getNombre()
				: ventanaEditarPersona.getTxtNombre().getText();
		String telefono = !ventanaEditarPersona.getTxtTelefono().isEnabled() ? persona.getTelefono()
				: ventanaEditarPersona.getTxtTelefono().getText();
		String nacimiento = !ventanaEditarPersona.getTxtFechaNacimiento().isEditable() ? persona.getNacimiento()
				: crearStringFecha();
		String email = !ventanaEditarPersona.getTxtEmail().isEnabled() ? persona.getEmail()
				: ventanaEditarPersona.getTxtEmail().getText();
		String contactoId = !ventanaEditarPersona.getComboBoxTipoContacto().isEnabled() ? persona.getContactoId()
				: ventanaEditarPersona.getComboBoxTipoContacto()
						.getItemAt(ventanaEditarPersona.getComboBoxTipoContacto().getSelectedIndex());
		return new PersonaDTO(0, nombre, telefono, nacimiento, email, contactoId);
	}

	private void actualizarPersona(PersonaDTO personaEditada, int id) {
		agenda.actualizarPersona(id, personaEditada);
		ventanaEditarPersona.cerrar();
		refrescarTabla();
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

	private void configurarVentanaNuevoContacto() {
		VentanaNuevoContacto ventNuevoContacto = new VentanaNuevoContacto();
		ventNuevoContacto.getBtnAceptar().addActionListener(n -> agregarContacto(ventNuevoContacto, n));
		ventNuevoContacto.getBtnCancelar().addActionListener(c -> cerrarVentanaNuevoContacto(ventNuevoContacto, c));
	}

	private void configurarVentanaEditarContacto(String contactoSeleccionado) {
		if (!contactoSeleccionado.isEmpty()) {
			VentanaEditarContacto editarContacto = new VentanaEditarContacto(contactoSeleccionado);
			editarContacto.getBtnAceptar().addActionListener(c -> editarTipoContacto(editarContacto, c));
			editarContacto.getBtnCancelar().addActionListener(c -> editarContacto.cerrar());
			editarContacto.mostrar();

		} else {
			mostrarMensaje(ventanaTipoContacto, mensajes[0]);
		}
	}

	private void guardarPersona() {
		String nombre = this.ventanaPersona.getTxtNombre().getText();
		if (nombre.isEmpty()) {
			mostrarMensaje(ventanaPersona, mensajes[1]);
			return;
		}
		String tel = ventanaPersona.getTxtTelefono().getText();
		String nacimiento = crearStringFecha();
		String email = ventanaPersona.getTxtEmail().getText();
		if (tel.isEmpty() && email.isEmpty()) {
			mostrarMensaje(ventanaPersona, mensajes[2]);
			return;
		}
		if (!isValidEmail(email)) {
			mostrarMensaje(ventanaPersona, mensajes[3]);
			return;
		}
		String tipoContacto = ventanaPersona.getJComboBoxTipoContacto()
				.getItemAt(ventanaPersona.getJComboBoxTipoContacto().getSelectedIndex());
		PersonaDTO nuevaPersona = new PersonaDTO(0, nombre, tel, nacimiento, email, tipoContacto);
		this.agenda.agregarPersona(nuevaPersona);
		this.refrescarTabla();
		this.ventanaPersona.cerrar();
	}

	public void borrarPersona() {
		PersonaDTO personaSeleccionada = getPersonaSeleccionada();
		if (personaSeleccionada != null) {
			agenda.borrarPersona(personaSeleccionada);
			refrescarTabla();
		} else {
			mostrarMensaje(ventanaPersona, mensajes[4]);
		}
	}

	public static boolean isValidEmail(String email) {
		Pattern pattern = Pattern.compile("[a-z](\\.-_[a-z0-9]+)*[a-z0-9]*@[a-z]+(\\.[a-z]+)+");
		Matcher mather = pattern.matcher(email);
		return !mather.find() && !email.isEmpty() ? false : true;
	}

	private void agregarContacto(VentanaNuevoContacto v, ActionEvent a) {
		String nuevo = v.getTxtContactoNuevo().getText();
		if (nuevo.isEmpty()) {
			mostrarMensaje(v, mensajes[1]);
		} else {
			agenda.agregarContacto(nuevo);
			mostrarListaContactosPredeterminados();
			mostrarDesplegableTipoContacto(ventanaPersona.getCBTipoContacto());
			v.cerrar();
		}
	}

	private void editarTipoContacto(VentanaEditarContacto v, ActionEvent a) {
		if (v.getTxtNuevo().getText().isEmpty()) {
			mostrarMensaje(v, mensajes[1]);
			return;
		}
		agenda.editarContacto(v.getTxtNombreAnterior().getText(), v.getTxtNuevo().getText());
		v.cerrar();
		mostrarListaContactosPredeterminados();
		mostrarDesplegableTipoContacto(ventanaPersona.getCBTipoContacto());
	}

	private void eliminarContacto() {
		String seleccionado = getTipoContactoSeleccionado();
		if (!seleccionado.isEmpty()) {
			this.agenda.borrarContacto(seleccionado);
			mostrarListaContactosPredeterminados();
			mostrarDesplegableTipoContacto(ventanaPersona.getCBTipoContacto());
		} else {
			mostrarMensaje(ventanaTipoContacto, mensajes[0]);
		}
	}

	private void cerrarVentanaNuevoContacto(VentanaNuevoContacto ventNuevoContacto, ActionEvent c) {
		ventNuevoContacto.cerrar();
	}

	private String crearStringFecha() {
		ConvertorFecha fecha = new ConvertorFecha(ventanaNacimiento.getFecha().getDate());
		return fecha.getFecha();
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

	private static void mostrarMensaje(Component padre, String mensaje) {
		JOptionPane.showMessageDialog(padre, mensaje);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

}
