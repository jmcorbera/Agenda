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
import presentacion.vista.VentanaABMLocalidad;
import presentacion.vista.VentanaDomicilio;
import presentacion.vista.VentanaEditarContacto;
import presentacion.vista.VentanaEditarPersona;
import presentacion.vista.VentanaNacimiento;
import presentacion.vista.VentanaNuevoContacto;
import presentacion.vista.VentanaPersona;
import presentacion.vista.VentanaTipoContacto;
import presentacion.vista.Vista;
import dto.ContactoDTO;
import dto.DomicilioDTO;
import dto.LocalidadDTO;
import dto.PaisDTO;
import dto.PersonaDTO;
import dto.ProvinciaDTO;

public class Controlador implements ActionListener {
	private Vista vista;
	private List<PersonaDTO> personasEnTabla;
	private DomicilioDTO domicilio;
	private VentanaPersona ventanaPersona;
	private VentanaDomicilio ventanaDomicilio;
	private Agenda agenda;
	private VentanaNacimiento ventanaNacimiento;
	private VentanaTipoContacto ventanaTipoContacto;
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
		this.controladorUbicacion = new ControladorUbicacion(agenda, new VentanaABMLocalidad());
		configurarVista(vista);
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
		this.ventanaTipoContacto = new VentanaTipoContacto();
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
		this.ventanaPersona.getBtnDomicilio().addActionListener(n -> configurarVentanaDomicilio());
	}
	
	private void configurarVentanaDomicilio() {
		ventanaDomicilio = new VentanaDomicilio();
		this.obtenerListaPaises(ventanaDomicilio.getComboBoxPais());
		deshabilitarProvinciaYLocalidad(ventanaDomicilio.getComboBoxProvincia(), ventanaDomicilio.getComboBoxLocalidad());
		ventanaDomicilio.getBtnAceptar().addActionListener(a -> guardarDomicilio(ventanaDomicilio, personasEnTabla.size()));
		ventanaDomicilio.getBtnCancelar().addActionListener(a -> ventanaDomicilio.cerrar());
		ventanaDomicilio.getComboBoxPais().addActionListener(a -> mostrarProvincias(getPaisSeleccionado(ventanaDomicilio.getComboBoxPais()), ventanaDomicilio.getComboBoxProvincia()));
		ventanaDomicilio.getComboBoxProvincia().addActionListener(a ->mostrarLocalidades(getProvinciaSeleccionada(ventanaDomicilio.getComboBoxProvincia()), ventanaDomicilio.getComboBoxLocalidad()));
		ventanaDomicilio.mostrarVentana();
	}
	
	private void mostrarProvincias(String seleccionado, JComboBox<String> cbProvincias) {
		try {
			cbProvincias.removeAllItems();
			cbProvincias.removeAllItems();
			cbProvincias.setEnabled(true);

			cbProvincias.setEnabled(true);
			List<ProvinciaDTO> provincias = agenda.obtenerProvincias();
			for (int i = 0; i < provincias.size(); i++) {
				PaisDTO pais = provincias.get(i).getPais();
				String nombrePais = pais.getNombre();
				if (nombrePais.equals(seleccionado)) {
					cbProvincias.addItem(provincias.get(i).getNombre());
				}	
			}
		} catch (Exception e) {
			return;
		}
	}
	
	private void mostrarLocalidades(String seleccionado, JComboBox<String> cbLocalidades) {
		try {
			cbLocalidades.removeAllItems();
			cbLocalidades.setEnabled(true);

			List<LocalidadDTO> localidades = agenda.obtenerLocalidades();
			for (int i = 0; i < localidades.size(); i++) {
				ProvinciaDTO provincia = localidades.get(i).getProvincia();
				String nombreProvincia = provincia.getNombre();
				if (nombreProvincia.equals(seleccionado)) {
					cbLocalidades.addItem(localidades.get(i).getNombre());
				}	
			}
		} catch (Exception e) {
			return;
		}
	}

	private String getProvinciaSeleccionada(JComboBox<String> provincias) {
		Object seleccionado = provincias.getSelectedItem();
		return seleccionado != null ? seleccionado.toString() : "";
	}
	
	private String getPaisSeleccionado(JComboBox<String> paises) {
		Object seleccionado = paises.getSelectedItem();
		return seleccionado != null ? seleccionado.toString() : "";
	}
	
	private void guardarDomicilio(VentanaDomicilio ventanaDomicilio, int id) {
		Object seleccionado;
		String pais;
		String provincia;
		String localidad;
		String calle;
		String altura;
		String piso;
		seleccionado = ventanaDomicilio.getComboBoxPais().getSelectedItem();
		pais = seleccionado == null ? "" : seleccionado.toString();
		seleccionado = ventanaDomicilio.getComboBoxProvincia().getSelectedItem();
		provincia = seleccionado == null ? "" : seleccionado.toString();
		seleccionado = ventanaDomicilio.getComboBoxLocalidad().getSelectedItem();
		localidad = seleccionado == null ? "" : seleccionado.toString();
		calle = ventanaDomicilio.getTxtCalle().getText();
		altura = ventanaDomicilio.getTxtAltura().getText();
		piso = ventanaDomicilio.getTxtPiso().getText();
		domicilio = new DomicilioDTO(id, pais, provincia, localidad, "", calle, altura, piso);
		ventanaDomicilio.cerrar();
		}
	
	private void configurarVentanaNacimiento() {
		this.ventanaNacimiento = new VentanaNacimiento();
		ventanaNacimiento.getBtnAgregarNacimiento().addActionListener(a -> ventanaNacimiento.cerrar());
	}

	//Auxiliares
	private void deshabilitarProvinciaYLocalidad(JComboBox<String> provincia, JComboBox<String> localidad) {
		provincia.setEnabled(false);
		localidad.setEnabled(false);
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
			agregarListeners(personaSeleccionada);
			ventanaEditarPersona.getBtnEditarNacimiento().addActionListener(a -> ventanaNacimiento.mostrarVentana());
			ventanaEditarPersona.getBtnAceptar()
					.addActionListener(a -> actualizarPersona(getPersonaEditada(), personaSeleccionada.getId()));
			ventanaEditarPersona.getBtnDomicilio().addActionListener(a -> cambiarLblsMostrandoValoresAnteriores(personaSeleccionada.getId()));
			ventanaEditarPersona.mostrar();
		} else {
			JOptionPane.showMessageDialog(ventanaPersona, mensajes[0]);
		}
	}
	private void cambiarLblsMostrandoValoresAnteriores(int idPersona) {
		configurarVentanaDomicilio();
		List<DomicilioDTO> domicilios = agenda.obtenerDomicilios();
		
		for(DomicilioDTO domicilio: domicilios) {
			if(domicilio.getId() == idPersona) {
				ventanaDomicilio.setLblCalleAnterior("Calle a editar: "+domicilio.getCalle());
				ventanaDomicilio.setLblAlturaAnterior("Altura a editar: "+domicilio.getAltura());
				ventanaDomicilio.setLblPisoAnterior("Piso a editar: "+domicilio.getPiso());
				ventanaDomicilio.getLblAlturaAnterior().setVisible(true);
				ventanaDomicilio.getLblCalleAnterior().setVisible(true);
				ventanaDomicilio.getLblPisoAnterior().setVisible(true);			
			}
		}
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
		txtNacimiento.setText(crearStringFecha(ventanaNacimiento.getFecha().getDate()));
		ventanaNacimiento.cerrar();
	}

	private PersonaDTO getPersonaEditada() {
		PersonaDTO persona = getPersonaSeleccionada();
		String nombre = !ventanaEditarPersona.getTxtNombre().isEnabled() ? persona.getNombre()
				: ventanaEditarPersona.getTxtNombre().getText();
		String telefono = !ventanaEditarPersona.getTxtTelefono().isEnabled() ? persona.getTelefono()
				: ventanaEditarPersona.getTxtTelefono().getText();
		String nacimiento = !ventanaEditarPersona.getTxtFechaNacimiento().isEditable() ? persona.getNacimiento()
				: crearStringFecha(ventanaNacimiento.getFecha().getDate());
		String email = !ventanaEditarPersona.getTxtEmail().isEnabled() ? persona.getEmail()
				: ventanaEditarPersona.getTxtEmail().getText();
		String contactoId = !ventanaEditarPersona.getComboBoxTipoContacto().isEnabled() ? persona.getContactoId()
				: ventanaEditarPersona.getComboBoxTipoContacto()
						.getItemAt(ventanaEditarPersona.getComboBoxTipoContacto().getSelectedIndex());
		return new PersonaDTO(0, nombre, telefono, nacimiento, email, contactoId);
	}

	private PersonaDTO getPersonaAAgregar() {
		return new PersonaDTO(personasEnTabla.size()+1, ventanaPersona.getTxtNombre().getText(), ventanaPersona.getTxtTelefono().getText(),
				crearStringFecha(ventanaNacimiento.getFecha().getDate()), ventanaPersona.getTxtEmail().getText(), ventanaPersona.getJComboBoxTipoContacto()
						.getItemAt(ventanaPersona.getJComboBoxTipoContacto().getSelectedIndex()));
	}

	private void actualizarPersona(PersonaDTO personaEditada, int id) {
		String mensajeValidezPersona = personaEditada.isValid();
		if (mensajeValidezPersona.isEmpty()) {
			agenda.actualizarPersona(id, personaEditada);
			agenda.modificarDomicilio(domicilio);
			ventanaEditarPersona.cerrar();
			refrescarTabla();
		}
		else {
			JOptionPane.showMessageDialog(ventanaEditarPersona, mensajeValidezPersona);
		}
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
		ventNuevoContacto.getBtnAceptar().addActionListener(n -> agregarContacto(ventNuevoContacto));
		ventNuevoContacto.getBtnCancelar().addActionListener(c -> cerrarVentanaNuevoContacto(ventNuevoContacto));
	}

	private void configurarVentanaEditarContacto(String contactoSeleccionado) {
		if (!contactoSeleccionado.isEmpty()) {
			VentanaEditarContacto editarContacto = new VentanaEditarContacto(contactoSeleccionado);
			editarContacto.getBtnAceptar().addActionListener(c -> editarTipoContacto(editarContacto));
			editarContacto.getBtnCancelar().addActionListener(c -> editarContacto.cerrar());
			editarContacto.mostrar();

		} else {
			JOptionPane.showMessageDialog(ventanaTipoContacto, mensajes[1]);
		}
	}

	private void guardarPersona(PersonaDTO persona) {
		String mensajeValidezPersona = persona.isValid();
		if(!mensajeValidezPersona.isEmpty()) {
			JOptionPane.showMessageDialog(ventanaPersona, mensajeValidezPersona);
			return;
		}
		if(domicilio == null) {
			this.agenda.agregarPersona(persona);
		}
		else {
			String mensajeValidezDomicilio = domicilio.isValid();
			if(!mensajeValidezDomicilio.isEmpty()) {
				JOptionPane.showMessageDialog(ventanaPersona, mensajeValidezDomicilio);
				return;
			}
			this.agenda.agregarPersona(persona);
			this.agenda.agregarDomicilio(new DomicilioDTO(agenda.obtenerPersonas().size(), domicilio.getPais(),
			domicilio.getProvincia(), domicilio.getLocalidad(),
			domicilio.getDepartamento(), domicilio.getCalle(),
			domicilio.getAltura(), domicilio.getPiso()));
			domicilio = null;
		}
		this.refrescarTabla();
		this.ventanaPersona.cerrar();
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
	
	private String isValid(JTextField contacto) {
		if (contacto == null || contacto.getText().isEmpty()) {
			return mensajes[2];
		}
		return agenda.existsContacto(contacto.getText()) ? "Ya existe un contacto con ese nombre!" : "";
	}
	
	private void agregarContacto(VentanaNuevoContacto v) {
		String mensajeValidezContacto = isValid(v.getTxtContactoNuevo());
		if(mensajeValidezContacto.isEmpty()) {
			agenda.agregarContacto(v.getTxtContactoNuevo().getText());
			v.cerrar();
			mostrarListaContactosPredeterminados();
			mostrarDesplegableTipoContacto(ventanaPersona.getCBTipoContacto());
		}
		else {
			JOptionPane.showMessageDialog(v, mensajeValidezContacto);
		}
	}

	private void editarTipoContacto(VentanaEditarContacto v) {
		String mensajeValidezContacto = isValid(v.getTxtNuevo());
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

	private void cerrarVentanaNuevoContacto(VentanaNuevoContacto ventNuevoContacto) {
		ventNuevoContacto.cerrar();
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