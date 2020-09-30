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
import presentacion.reportes.ReporteAgenda;
import presentacion.vista.VentanaABMUbicacion;
import presentacion.vista.VentanaEditarContactoOPais;
import presentacion.vista.VentanaEditarPersona;
import presentacion.vista.VentanaNacimiento;
import presentacion.vista.VentanaNuevoPaisOContacto;
import presentacion.vista.VentanaPersona;
import presentacion.vista.ContactoPreferente;
import presentacion.vista.IntermediarioVista;
import presentacion.vista.VentanaABMTipoContacto;
import presentacion.vista.Vista;
import dto.ContactoDTO;
import dto.PersonaDTO;

public class Controlador implements ActionListener {
	private Vista vista;
	private List<PersonaDTO> personasEnTabla;
	private VentanaPersona ventanaPersona;
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
			JOptionPane.showMessageDialog(ventanaTipoContacto, mensajes[1]);
	}

	private void configurarVista(Vista vista) {
		this.vista = vista;
		if(vista.getBtnAgregar().getActionListeners().length == 0)
			this.vista.getBtnAgregar().addActionListener(a -> ventanaPersona.mostrarVentana());
		if(vista.getBtnBorrar().getActionListeners().length == 0)
			this.vista.getBtnBorrar().addActionListener(s -> borrarPersona());
		if(vista.getBtnEditar().getActionListeners().length == 0)
			this.vista.getBtnEditar().addActionListener(s -> configurarVentanaEditarPersona());
		if(vista.getBtnReporte().getActionListeners().length == 0)
			this.vista.getBtnReporte().addActionListener(r -> mostrarReporte());
		if(vista.getMenuItemLocalidad().getActionListeners().length == 0)
			this.vista.getMenuItemLocalidad().addActionListener(l -> controladorUbicacion.getVentanaABMLocalidad().mostrarVentana());
		if(vista.getMenuItemTipoContacto().getActionListeners().length == 0)
			this.vista.getMenuItemTipoContacto().addActionListener(t -> ventanaTipoContacto.mostrarVentana());
	}

	private void configurarVentanaTipoContacto() {
		this.ventanaTipoContacto = new VentanaABMTipoContacto();
		mostrarListaContactosPredeterminados();
		if(ventanaTipoContacto.getBtnEditarContacto().getActionListeners().length == 0)
			this.ventanaTipoContacto.getBtnEditarContacto().addActionListener(a -> configurarVentanaEditarContacto(getTipoContactoSeleccionado()));
		if(ventanaTipoContacto.getBtnNuevoContacto().getActionListeners().length == 0)
			this.ventanaTipoContacto.getBtnNuevoContacto().addActionListener(a -> configurarVentanaNuevoContacto());
		if(ventanaTipoContacto.getBtnAceptar().getActionListeners().length == 0)
			this.ventanaTipoContacto.getBtnAceptar().addActionListener(a -> ventanaTipoContacto.cerrar());
		if(ventanaTipoContacto.getBtnEliminarContacto().getActionListeners().length == 0)
			this.ventanaTipoContacto.getBtnEliminarContacto().addActionListener(a -> eliminarContacto());
	}

	private void configurarVentanaPersona() {
		this.ventanaPersona = VentanaPersona.getInstance();
		mostrarDesplegableTipoContacto(ventanaPersona.getCBTipoContacto());
		cambiarModeloContactoPreferente(ventanaPersona.getJComboBoxContactoPreferente());
		if(ventanaPersona.getBtnAgregarPersona().getActionListeners().length == 0)
			this.ventanaPersona.getBtnAgregarPersona().addActionListener(p -> guardarPersona(getPersonaAAgregar()));
		if(ventanaPersona.getBtnNacimiento().getActionListeners().length == 0)
			this.ventanaPersona.getBtnNacimiento().addActionListener(n -> ventanaNacimiento.mostrarVentana());
		if(ventanaPersona.getBtnDomicilio().getActionListeners().length == 0)
			this.ventanaPersona.getBtnDomicilio().addActionListener(n -> controladorUbicacion.configurarVentanaNuevoDomicilio(personasEnTabla.size()));
	}
	
	private void cambiarModeloContactoPreferente(JComboBox<String> cmbBoxPreferentes) {
		String[] preferentes = obtenerMedioContactoPreferente();
		IntermediarioVista.setModel(cmbBoxPreferentes,preferentes);	
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
		if (personaSeleccionada != null) {
			ventanaEditarPersona = VentanaEditarPersona.getInstance(personaSeleccionada.getId());
			ventanaEditarPersona.mostrar();
			cambiarValoresDeElementos(personaSeleccionada);
			agregarListeners(personaSeleccionada);
			cambiarModeloContactoPreferente(ventanaEditarPersona.getJComboBoxContactoPreferente());
			mostrarContactoPreferenteSeleccionadoPrimero(personaSeleccionada);
			if(ventanaEditarPersona.getBtnEditarNacimiento().getActionListeners().length == 0)
				ventanaEditarPersona.getBtnEditarNacimiento().addActionListener(a -> ventanaNacimiento.mostrarVentana());
			if(ventanaEditarPersona.getBtnDomicilio().getActionListeners().length == 0)
				ventanaEditarPersona.getBtnDomicilio().addActionListener(a -> controladorUbicacion.configurarVentanaEditarDomicilio(personaSeleccionada.getId()));
			if(ventanaEditarPersona.getBtnAceptar().getActionListeners().length == 0)
				ventanaEditarPersona.getBtnAceptar().addActionListener(a -> actualizarPersona(getPersonaEditada(personaSeleccionada)));
			if(ventanaEditarPersona.getBtnEliminarDomicilio().getActionListeners().length == 0)
				ventanaEditarPersona.getBtnEliminarDomicilio().addActionListener(a -> eliminarDomicilio(personaSeleccionada.getId()));
			if(ventanaEditarPersona.getBtnEliminarNacimiento().getActionListeners().length == 0)
				ventanaEditarPersona.getBtnEliminarNacimiento().addActionListener(a -> eliminarNacimiento(personaSeleccionada));
			} 
		else 
			JOptionPane.showMessageDialog(ventanaPersona, mensajes[0]);
	}
	
	private void eliminarNacimiento(PersonaDTO personaSeleccionada) {
		personaSeleccionada.setNacimiento("");
		ventanaEditarPersona.getTxtFechaNacimiento().setText("");
		JOptionPane.showMessageDialog(ventanaEditarPersona, mensajes[3]);
	}

	private void eliminarDomicilio(int id) {
		agenda.borrarDomicilio(id);
		JOptionPane.showMessageDialog(ventanaEditarPersona, mensajes[3]);
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
		if(!ventanaEditarPersona.getTxtFechaNacimiento().getText().isEmpty())
			ventanaEditarPersona.getTxtFechaNacimiento().setText(personaSeleccionada.getNacimiento().replace('-', '/'));
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
		if(personaSeleccionada.getContactoId() == null)
			modeloLista.setSelectedItem(null);
	}
	
	private void mostrarContactoPreferenteSeleccionadoPrimero(PersonaDTO personaSeleccionada) {
		ComboBoxModel<String> modeloLista = ventanaEditarPersona.getJComboBoxContactoPreferente().getModel();
		int largoLista = modeloLista.getSize();
		if (largoLista > 0) {
			String primero = modeloLista.getElementAt(0);
			modeloLista.setSelectedItem(personaSeleccionada.getContactoPreferente());
			@SuppressWarnings("unused")
			String cambiarPrimero = modeloLista.getElementAt(0);
			cambiarPrimero = personaSeleccionada.getContactoPreferente();
			@SuppressWarnings("unused")
			String cambiarUltimo = modeloLista.getElementAt(largoLista);
			cambiarUltimo = primero;
		}
		if(personaSeleccionada.getContactoId() == null)
			modeloLista.setSelectedItem(null);
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
		return new PersonaDTO(personasEnTabla.size()+1, ventanaPersona.getTxtNombre().getText(), ventanaPersona.getTxtTelefono().getText(),
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
		for (int i = 0; i < nombreContactos.length; i++) {
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
		if(controladorUbicacion.getDomicilio() != null) {
			controladorUbicacion.getDomicilio().setId(persona.getId());
			if(agenda.obtenerDomicilio(persona.getId()) == null){	
				this.agenda.agregarDomicilio(controladorUbicacion.getDomicilio());
				controladorUbicacion.setDomicilio(null);
			}
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
		else 
			JOptionPane.showMessageDialog(v, mensajeValidezContacto);
	}

	private void editarTipoContacto(VentanaEditarContactoOPais v) {
		String mensajeValidezContacto = new ContactoDTO(v.getTxtNuevo().getText().toString()).isValid();
		if(mensajeValidezContacto.isEmpty()) {
			agenda.editarContacto(v.getTxtNombreAnterior().getText(), v.getTxtNuevo().getText());
			v.cerrar();
			mostrarListaContactosPredeterminados();
			mostrarDesplegableTipoContacto(ventanaPersona.getCBTipoContacto());
		}
		else 
			JOptionPane.showMessageDialog(v, mensajeValidezContacto);
	}

	private void eliminarContacto() {
		String seleccionado = getTipoContactoSeleccionado();
		if (!seleccionado.isEmpty()) {
			this.agenda.borrarContacto(seleccionado);
			mostrarListaContactosPredeterminados();
			mostrarDesplegableTipoContacto(ventanaPersona.getCBTipoContacto());
		} else 
			JOptionPane.showMessageDialog(ventanaTipoContacto, mensajes[1]);
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
		this.personasEnTabla = agenda.obtenerPersonas();
		this.vista.llenarTabla(this.personasEnTabla);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

}