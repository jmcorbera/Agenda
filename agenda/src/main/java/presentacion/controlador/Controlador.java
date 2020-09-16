package presentacion.controlador;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import modelo.Agenda;
import modelo.ConvertorFecha;
import presentacion.reportes.ReporteAgenda;
import presentacion.vista.VentanaEditarContacto;
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
	private String[] mensajes = {"No ha seleccionado ningun contacto","El nombre es obligatorio!","Debe ingresar al menos una forma de contacto (Email o telefono)"};

	public Controlador(Vista vista, Agenda agenda) {
		this.agenda = agenda;
		configurarVista(vista);
		configurarVentanaTipoContacto();
		configurarVentanaPersona();
		configurarVentanaNacimiento();
	}
	
	private void configurarVista(Vista vista) {
		this.vista = vista;
		this.vista.getBtnAgregar().addActionListener(a -> mostrarVentanaAgregarPersona(a));
		this.vista.getBtnBorrar().addActionListener(s -> borrarPersona(s));
		this.vista.getBtnReporte().addActionListener(r -> mostrarReporte(r));
	}

	private void configurarVentanaTipoContacto() {
		this.ventanaTipoContacto = VentanaTipoContacto.getInstance();
		mostrarListaContactosPredeterminados();
		this.ventanaTipoContacto.getBtnEditarContacto().addActionListener(a -> configurarVentanaEditarContacto(getTipoContactoSeleccionado(), a));
		this.ventanaTipoContacto.getBtnNuevoContacto().addActionListener(a -> configurarVentanaNuevoContacto(a));
		this.ventanaTipoContacto.getBtnAceptar().addActionListener(a -> cerrarVentanaTipoContacto(a));
		this.ventanaTipoContacto.getBtnEliminarContacto().addActionListener(a -> eliminarContacto(a));
	}
	
	private void configurarVentanaPersona() {
		this.ventanaPersona = VentanaPersona.getInstance();
		mostrarDesplegableTipoContacto();
		this.ventanaPersona.getBtnAgregarPersona().addActionListener(p -> guardarPersona(p));
		this.ventanaPersona.getBtnNacimiento().addActionListener(n -> mostrarVentanaNacimiento(n));
		this.ventanaPersona.getBtnEditarTipo().addActionListener(a -> mostrarVentanaTipoContacto(a));
	}
	
	private void configurarVentanaNacimiento() {
		this.ventanaNacimiento = VentanaNacimiento.getInstance();
		this.ventanaNacimiento.getBtnAgregarNacimiento().addActionListener(a -> cerrarVentanaNacimiento(a));
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
	
	private void mostrarDesplegableTipoContacto() {
		List<ContactoDTO> contactos = agenda.obtenerContactos();
		String[] nombreContactos = new String[contactos.size()];
		for (int i = 0; i < contactos.size(); i++) {
			nombreContactos[i] = contactos.get(i).getNombreContacto();
		}
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nombreContactos);
		ventanaPersona.getJComboBoxTipoContacto().setModel(model);
	}

	private String getTipoContactoSeleccionado() {
		DefaultTableModel modelo = ventanaTipoContacto.getModelTipoContactos();
		try {
			return (String) modelo.getValueAt(ventanaTipoContacto.getTableTipoContactos().getSelectedRow(), 0);
		} catch (Exception e) {
			return "";
		}
	}
	
	private void configurarVentanaNuevoContacto(ActionEvent a) {
		VentanaNuevoContacto ventNuevoContacto = new VentanaNuevoContacto();
		ventNuevoContacto.getBtnAceptar().addActionListener(n -> agregarContacto(ventNuevoContacto, n));
		ventNuevoContacto.getBtnCancelar().addActionListener(c -> cerrarVentanaNuevoContacto(ventNuevoContacto, c));
	}
	
	private void configurarVentanaEditarContacto(String contactoSeleccionado, ActionEvent a) {
		if (!contactoSeleccionado.isEmpty()) {
			VentanaEditarContacto editarContacto = new VentanaEditarContacto(contactoSeleccionado);
			editarContacto.getBtnAceptar().addActionListener(c -> editarTipoContacto(editarContacto, c));
			editarContacto.getBtnCancelar().addActionListener(c -> cerrarVentanaEditar(editarContacto, c));
			editarContacto.mostrar();

		} else {
			mostrarMensaje(ventanaTipoContacto, mensajes[0]);
		}
	}
	
	private void guardarPersona(ActionEvent p) {
		String nombre = this.ventanaPersona.getTxtNombre().getText();
		if(nombre.isEmpty()) {
			mostrarMensaje(ventanaPersona,mensajes[1]);
			return;
		}
		String tel = ventanaPersona.getTxtTelefono().getText();
		String nacimiento = crearStringFecha();
		String email = ventanaPersona.getTxtEmail().getText();
		if(tel.isEmpty() && email.isEmpty()) {
			mostrarMensaje(ventanaPersona, mensajes[2]);
			return;
		}
		PersonaDTO nuevaPersona = new PersonaDTO(0, nombre, tel, nacimiento, email);
		this.agenda.agregarPersona(nuevaPersona);
		this.refrescarTabla();
		this.ventanaPersona.cerrar();
	}
	
	public void borrarPersona(ActionEvent s) {
		int[] filasSeleccionadas = this.vista.getTablaPersonas().getSelectedRows();
		for (int fila : filasSeleccionadas) {
			this.agenda.borrarPersona(this.personasEnTabla.get(fila));
		}

		this.refrescarTabla();
	}
	
	private void agregarContacto(VentanaNuevoContacto v, ActionEvent a) {
		String nuevo = v.getTxtContactoNuevo().getText();
		if (nuevo.isEmpty()) {
			mostrarMensaje(v, mensajes[1]);
		} else {
			agenda.agregarContacto(nuevo);
			mostrarListaContactosPredeterminados();
			mostrarDesplegableTipoContacto();
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
		mostrarDesplegableTipoContacto();
	}
	
	private void eliminarContacto(ActionEvent a) {
		String seleccionado = getTipoContactoSeleccionado();
		if (!seleccionado.isEmpty()) {
			this.agenda.borrarContacto(seleccionado);
			mostrarListaContactosPredeterminados();
			mostrarDesplegableTipoContacto();
		}
		else {
			mostrarMensaje(ventanaTipoContacto,mensajes[0]);
		}
	}
	
	private void cerrarVentanaNuevoContacto(VentanaNuevoContacto ventNuevoContacto, ActionEvent c) {
		ventNuevoContacto.cerrar();
	}
	
	private void cerrarVentanaTipoContacto(ActionEvent a) {
		ventanaTipoContacto.cerrar();
	}

	private void cerrarVentanaEditar(VentanaEditarContacto editarContacto, ActionEvent c) {
		editarContacto.cerrar();
	}
	
	private void cerrarVentanaNacimiento(ActionEvent a) {
		this.ventanaNacimiento.cerrar();
	}

	private void mostrarVentanaTipoContacto(ActionEvent a) {
		ventanaTipoContacto.mostrarVentana();
	}

	private void mostrarVentanaNacimiento(ActionEvent n) {
		this.ventanaNacimiento.mostrarVentana();
	}

	private void mostrarVentanaAgregarPersona(ActionEvent a) {
		this.ventanaPersona.mostrarVentana();
	}

	private String crearStringFecha() {
		ConvertorFecha fecha = new ConvertorFecha(ventanaNacimiento.getFecha().getDate());
		return fecha.getFecha();
	}

	private void mostrarReporte(ActionEvent r) {
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
