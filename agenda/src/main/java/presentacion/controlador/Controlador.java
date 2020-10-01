package presentacion.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.Agenda;
import presentacion.reportes.ReporteAgenda;
import presentacion.vista.VentanaABMUbicacion;
import presentacion.vista.VentanaEditarContactoOPais;
import presentacion.vista.VentanaEditarPersona;
import presentacion.vista.VentanaNacimiento;
import presentacion.vista.VentanaNuevoPaisOContacto;
import presentacion.vista.VentanaPersona;
import presentacion.vista.VentanaABMTipoContacto;
import presentacion.vista.Vista;
import presentacion.vista.Intermediario.ContactoPreferente;
import presentacion.vista.Intermediario.IntermediarioVista;
import presentacion.vista.Intermediario.Mensajes;
import dto.ContactoDTO;
import dto.PersonaDTO;

public class Controlador implements ActionListener {
	private Vista vista;
	private VentanaPersona ventanaPersona;
	private Agenda agenda;
	private VentanaNacimiento ventanaNacimiento;
	private VentanaABMTipoContacto ventanaTipoContacto;
	private VentanaEditarPersona ventanaEditarPersona;
	private ControladorUbicacion controladorUbicacion;
	
	public Controlador(Vista vista, Agenda agenda) {
		this.agenda = agenda;
		configurarVentanaNacimiento();
		configurarVentanaPersona();
		configurarVentanaTipoContacto();
		this.controladorUbicacion = new ControladorUbicacion(agenda, new VentanaABMUbicacion());
		configurarVista(vista);
	}
	
	private void configurarVentanaNacimiento() {
		this.ventanaNacimiento = new VentanaNacimiento();
		if(ventanaNacimiento.getBtnAgregarNacimiento().getActionListeners().length == 0) 
			ventanaNacimiento.getBtnAgregarNacimiento().addActionListener(a -> actualizarFecha());
	}
	
	private void actualizarFecha() {
		ventanaNacimiento.cerrar();
		if(ventanaEditarPersona!= null) 
			ventanaEditarPersona.getTxtFechaNacimiento().setText(crearStringFecha(ventanaNacimiento.getFecha().getDate()));
	}
	
	private void configurarVentanaNuevoContacto() {
		VentanaNuevoPaisOContacto ventNuevoContacto = new VentanaNuevoPaisOContacto();
		ventNuevoContacto.getBtnAceptar().addActionListener(n -> agregarContacto(ventNuevoContacto));
		ventNuevoContacto.getBtnCancelar().addActionListener(c -> ventNuevoContacto.cerrar());
	}

	private void configurarVentanaEditarContacto(String contactoSeleccionado) {
		if (!contactoSeleccionado.isEmpty()) {
			VentanaEditarContactoOPais editarContacto = new VentanaEditarContactoOPais(contactoSeleccionado);	
			editarContacto.getBtnAceptar().addActionListener(c -> editarTipoContacto(editarContacto));
			editarContacto.getBtnCancelar().addActionListener(c -> editarContacto.cerrar());
			editarContacto.mostrar();
		} else 
			JOptionPane.showMessageDialog(ventanaTipoContacto, Mensajes.noSelecciono);
	}

	private void configurarVista(Vista vista) {
		this.vista = vista;
		vista.eliminarActionListeners();
		this.vista.getBtnAgregar().addActionListener(a -> ventanaPersona.mostrarVentana());
		this.vista.getBtnBorrar().addActionListener(s -> borrarPersona());
		this.vista.getBtnEditar().addActionListener(s -> configurarVentanaEditarPersona());
		this.vista.getBtnReporte().addActionListener(r -> mostrarReporte());
		this.vista.getMenuItemLocalidad().addActionListener(l -> setControladorUbicacion());
		this.vista.getMenuItemTipoContacto().addActionListener(t -> ventanaTipoContacto.mostrarVentana());
	}

	private void setControladorUbicacion() {
		controladorUbicacion = new ControladorUbicacion(agenda,new VentanaABMUbicacion());
		controladorUbicacion.getVentanaABMLocalidad().mostrarVentana();
	}

	private void configurarVentanaTipoContacto() {
		this.ventanaTipoContacto = new VentanaABMTipoContacto();
		ventanaTipoContacto.eliminarActionListeners();
		mostrarListaContactosPredeterminados();
		this.ventanaTipoContacto.getBtnEditarContacto().addActionListener(a -> configurarVentanaEditarContacto(getTipoContactoSeleccionado()));
		this.ventanaTipoContacto.getBtnNuevoContacto().addActionListener(a -> configurarVentanaNuevoContacto());
		this.ventanaTipoContacto.getBtnVolver().addActionListener(a -> ventanaTipoContacto.cerrar());
		this.ventanaTipoContacto.getBtnEliminarContacto().addActionListener(a -> eliminarContacto());
	}

	private void configurarVentanaPersona() {
		this.ventanaPersona = VentanaPersona.getInstance();
		ventanaPersona.eliminarActionListeners();
		IntermediarioVista.setModel(ventanaPersona.getCBTipoContacto(), getNombreTipoContactoPredeterminados());
		IntermediarioVista.setModel(ventanaPersona.getJComboBoxContactoPreferente(), obtenerMedioContactoPreferente());
		this.ventanaPersona.getBtnAgregarPersona().addActionListener(p -> guardarPersona(getPersonaAAgregar()));
		this.ventanaPersona.getBtnNacimiento().addActionListener(n -> ventanaNacimiento.mostrarVentana());
		this.ventanaPersona.getBtnDomicilio().addActionListener(n -> controladorUbicacion.configurarVentanaNuevoDomicilio(agenda.obtenerPersonas().size()));
	}
	
	private String[] obtenerMedioContactoPreferente() {
		String[] medioContactoPreferentes;
		int lengthPreferentes = ContactoPreferente.values().length;
		medioContactoPreferentes = new String[lengthPreferentes];
		for(int i = 0; i < lengthPreferentes ; i++)
			medioContactoPreferentes[i] = ContactoPreferente.values()[i].toString();
		return medioContactoPreferentes;
	}

	private void configurarVentanaEditarPersona() {
		PersonaDTO personaSeleccionada = getPersonaSeleccionada();
		if (personaSeleccionada != null && ventanaEditarPersona == null) {
			int id = personaSeleccionada.getId();
			ventanaEditarPersona = VentanaEditarPersona.getInstance(id);	
			ventanaEditarPersona.eliminarActionListeners();
			cambiarValoresDeElementos(getPersonaSeleccionada());
			agregarListeners(getPersonaSeleccionada());
			ventanaEditarPersona.getBtnEditarNacimiento().addActionListener(a -> ventanaNacimiento.mostrarVentana());
			ventanaEditarPersona.getBtnDomicilio().addActionListener(a -> controladorUbicacion.configurarVentanaEditarDomicilio(id));
			ventanaEditarPersona.getBtnAceptar().addActionListener(a -> actualizarPersona(getPersonaEditada(getPersonaSeleccionada())));
			ventanaEditarPersona.getBtnCancelar().addActionListener(a -> cerrarVentanaEditarPersona());
			ventanaEditarPersona.getBtnEliminarDomicilio().addActionListener(a -> eliminarDomicilio(id));
			ventanaEditarPersona.getBtnEliminarNacimiento().addActionListener(a -> eliminarNacimiento(getPersonaSeleccionada()));
			ventanaEditarPersona.mostrar();
		} 
		else 
			JOptionPane.showMessageDialog(ventanaPersona, Mensajes.noSelecciono);
	}
	
	private void cerrarVentanaEditarPersona() {
		ventanaEditarPersona.cerrar();
		ventanaEditarPersona = null;
	}

	private void eliminarNacimiento(PersonaDTO personaSeleccionada) {
		if(ventanaEditarPersona.getTxtFechaNacimiento().getText().isEmpty()) {
			JOptionPane.showMessageDialog(ventanaEditarPersona, Mensajes.noExiste);
			return;
		}
		agenda.eliminarFecha(personaSeleccionada.getId());
		ventanaEditarPersona.getTxtFechaNacimiento().setText("");
		JOptionPane.showMessageDialog(ventanaEditarPersona, Mensajes.operacionExitosa);
	}

	private void eliminarDomicilio(int id) {
		if(controladorUbicacion.getDomicilio() == null) {
			JOptionPane.showMessageDialog(ventanaEditarPersona, Mensajes.noExiste);
			return;
		}
		agenda.borrarDomicilio(id);
		controladorUbicacion.setDomicilio(null);
		JOptionPane.showMessageDialog(ventanaEditarPersona, Mensajes.operacionExitosa);
	}

	private PersonaDTO getPersonaSeleccionada() {
		int[] filasSeleccionadas = this.vista.getTablaPersonas().getSelectedRows();
		return filasSeleccionadas.length != 0 ? agenda.obtenerPersonas().get(filasSeleccionadas[0]) : null;
	}
	
	private void agregarListeners(PersonaDTO personaSeleccionada) {
		ventanaEditarPersona.getBtnCambiarNombre().addActionListener(a -> IntermediarioVista.cambiarValores(ventanaEditarPersona.getTxtNombre(),
				ventanaEditarPersona.getBtnCambiarNombre(), personaSeleccionada.getNombre()));
		
		ventanaEditarPersona.getBtnCambiarTelefono().addActionListener(a -> IntermediarioVista.cambiarValores(ventanaEditarPersona.getTxtTelefono(),
				ventanaEditarPersona.getBtnCambiarTelefono(), personaSeleccionada.getTelefono()));
		
		ventanaEditarPersona.getBtnCambiarEmail().addActionListener(a -> IntermediarioVista.cambiarValores(ventanaEditarPersona.getTxtEmail(),
				ventanaEditarPersona.getBtnCambiarEmail(), personaSeleccionada.getEmail()));
		
		ventanaNacimiento.getBtnAgregarNacimiento().addActionListener(a -> modificarTxtNacimiento(ventanaEditarPersona.getTxtFechaNacimiento()));
	}

	private void cambiarValoresDeElementos(PersonaDTO personaSeleccionada) {
		cambiarTxtsPorValoresPersona(personaSeleccionada);
		mostrarCmbSeleccionadoPrimero(personaSeleccionada);
	}

	private void cambiarTxtsPorValoresPersona(PersonaDTO personaSeleccionada) {
		if(personaSeleccionada.getNacimiento()!= null && !personaSeleccionada.getNacimiento().isEmpty())
			ventanaEditarPersona.getTxtFechaNacimiento().setText(personaSeleccionada.getNacimiento());
		if(!ventanaEditarPersona.getTxtFechaNacimiento().getText().isEmpty())
			ventanaEditarPersona.getTxtFechaNacimiento().setText(personaSeleccionada.getNacimiento().replace('-', '/'));
		ventanaEditarPersona.getTxtEmail().setText(personaSeleccionada.getEmail());
		ventanaEditarPersona.getTxtTelefono().setText(personaSeleccionada.getTelefono());
		ventanaEditarPersona.getTxtNombre().setText(personaSeleccionada.getNombre());
			
	}

	private void mostrarCmbSeleccionadoPrimero(PersonaDTO personaSeleccionada) {
		IntermediarioVista.setModel(ventanaEditarPersona.getComboBoxTipoContacto(), getNombreTipoContactoPredeterminados());
		IntermediarioVista.setModel(ventanaEditarPersona.getJComboBoxContactoPreferente(), obtenerMedioContactoPreferente());
		IntermediarioVista.mostrarSeleccionadoPrimero(ventanaEditarPersona.getComboBoxTipoContacto(),personaSeleccionada.getContactoId());
		IntermediarioVista.mostrarSeleccionadoPrimero(ventanaEditarPersona.getJComboBoxContactoPreferente(),personaSeleccionada.getContactoPreferente());
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
		String contactoId = !ventanaEditarPersona.getComboBoxTipoContacto().isEnabled() ? personaSinEditar.getContactoId(): IntermediarioVista.obtenerNombreSeleccionado(ventanaEditarPersona.getComboBoxTipoContacto().getSelectedItem());
		return new PersonaDTO(personaSinEditar.getId(), nombre, telefono, nacimiento, email, contactoId, IntermediarioVista.obtenerNombreSeleccionado(ventanaEditarPersona.getJComboBoxContactoPreferente().getSelectedItem()));
	}

	private PersonaDTO getPersonaAAgregar() {
		return new PersonaDTO(agenda.obtenerUltimoId()+1, ventanaPersona.getTxtNombre().getText(), ventanaPersona.getTxtTelefono().getText(),
				crearStringFecha(ventanaNacimiento.getFecha().getDate()), ventanaPersona.getTxtEmail().getText(), 
				IntermediarioVista.obtenerNombreSeleccionado(ventanaPersona.getJComboBoxTipoContacto().getSelectedItem()), 
				IntermediarioVista.obtenerNombreSeleccionado(ventanaPersona.getJComboBoxContactoPreferente().getSelectedItem()));
	}

	private void actualizarPersona(PersonaDTO personaEditada) {
		String mensajeValidezPersona = personaEditada.isValid();
		if(!mensajeValidezPersona.isEmpty()) {
			JOptionPane.showMessageDialog(ventanaEditarPersona, mensajeValidezPersona);
			return;
		}
		this.agenda.actualizarPersona(personaEditada.getId(), personaEditada);
		
		if(controladorUbicacion.getDomicilio()!= null) {
			if(agenda.obtenerDomicilio(controladorUbicacion.getDomicilio().getId()) == null) {
				agenda.agregarDomicilio(controladorUbicacion.getDomicilio());
				controladorUbicacion.setDomicilio(null);
				return;
			}
			this.agenda.modificarDomicilio(controladorUbicacion.getDomicilio());
			controladorUbicacion.setDomicilio(null);
		}
		this.refrescarTabla();
		cerrarVentanaEditarPersona();
		JOptionPane.showMessageDialog(ventanaPersona, Mensajes.operacionExitosa);
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

	private String[] getNombreTipoContactoPredeterminados() {
		List<ContactoDTO> contactos = agenda.obtenerContactos();
		String[] nombreContactos = new String[contactos.size()];
		for (int i = 0; i < nombreContactos.length; i++) 
			nombreContactos[i] = contactos.get(i).getNombreContacto();
		return nombreContactos;
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
		if(controladorUbicacion.getDomicilio() != null) {
			controladorUbicacion.getDomicilio().setId(persona.getId());
			if(agenda.obtenerDomicilio(persona.getId()) == null){	
				this.agenda.agregarDomicilio(controladorUbicacion.getDomicilio());
				controladorUbicacion.setDomicilio(null);
			}
		}
		this.refrescarTabla();
		this.ventanaPersona.cerrar();
		JOptionPane.showMessageDialog(ventanaPersona, Mensajes.operacionExitosa);
	}

	public void borrarPersona() {
		PersonaDTO personaSeleccionada = getPersonaSeleccionada();
		if (personaSeleccionada != null) {
			agenda.borrarDomicilio(personaSeleccionada.getId());
			agenda.borrarPersona(personaSeleccionada);
			refrescarTabla();
		} else 
			JOptionPane.showMessageDialog(ventanaPersona, Mensajes.noSelecciono);	
	}
	
	private void agregarContacto(VentanaNuevoPaisOContacto v) {
		if(v.getTxtNuevoNombre().getText() == null || v.getTxtNuevoNombre().getText().isEmpty()) {
			JOptionPane.showMessageDialog(v, Mensajes.esObligatorio);
			return;
		}
		if(agenda.existsContacto(v.getTxtNuevoNombre().getText())) {	
			JOptionPane.showMessageDialog(v, Mensajes.yaExiste);
			return;
		}
		agenda.agregarContacto(v.getTxtNuevoNombre().getText());
		v.cerrar();
		mostrarListaContactosPredeterminados();
		IntermediarioVista.setModel(ventanaPersona.getCBTipoContacto(), getNombreTipoContactoPredeterminados());
	}

	private void editarTipoContacto(VentanaEditarContactoOPais v) {
		if(v.getTxtNuevo().getText() == null || v.getTxtNuevo().getText().isEmpty()) {
			JOptionPane.showMessageDialog(v, Mensajes.esObligatorio);
			return;
		}
		if(agenda.existsContacto(v.getTxtNuevo().getText())) {
			JOptionPane.showMessageDialog(v, Mensajes.yaExiste);
			return;
		}
		agenda.editarContacto(v.getTxtNombreAnterior().getText(), v.getTxtNuevo().getText());
		v.cerrar();
		mostrarListaContactosPredeterminados();
		IntermediarioVista.setModel(ventanaPersona.getCBTipoContacto(), getNombreTipoContactoPredeterminados());
	}

	private void eliminarContacto() {
		String seleccionado = getTipoContactoSeleccionado();
		if (!seleccionado.isEmpty()) {
			this.agenda.borrarContacto(seleccionado);
			mostrarListaContactosPredeterminados();
			IntermediarioVista.setModel(ventanaPersona.getCBTipoContacto(), getNombreTipoContactoPredeterminados());
		} else 
			JOptionPane.showMessageDialog(ventanaTipoContacto, Mensajes.noSelecciono);
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
		ReporteAgenda reporte = new ReporteAgenda(agenda.obtenerReportesAgrupadoPorDomicilio());
		reporte.mostrar();
	}

	public void inicializar() {
		this.refrescarTabla();
		this.vista.show();
	}

	private void refrescarTabla() {
		this.vista.llenarTabla(agenda.obtenerPersonas());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

}