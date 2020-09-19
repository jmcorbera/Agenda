package presentacion.controlador;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import presentacion.vista.VentanaABMLocalidad;
import presentacion.vista.VentanaEditarContacto;
import presentacion.vista.VentanaEditarLocalidad;
import presentacion.vista.VentanaEditarPersona;
import presentacion.vista.VentanaEditarProvincia;
import presentacion.vista.VentanaNacimiento;
import presentacion.vista.VentanaNuevaLocalidad;
import presentacion.vista.VentanaNuevoContacto;
import presentacion.vista.VentanaPersona;
import presentacion.vista.VentanaTipoContacto;
import presentacion.vista.Vista;
import dto.ContactoDTO;
import dto.LocalidadDTO;
import dto.PaisDTO;
import dto.PersonaDTO;
import dto.ProvinciaDTO;

public class Controlador implements ActionListener {
	private Vista vista;
	private List<PersonaDTO> personasEnTabla;

	private List<PaisDTO> paisesEnLista = new ArrayList<PaisDTO>();
	private List<ProvinciaDTO> provinciasEnLista = new ArrayList<ProvinciaDTO>();
	private List<LocalidadDTO> localidadesEnLista = new ArrayList<LocalidadDTO>();

	private VentanaPersona ventanaPersona;
	private Agenda agenda;
	private VentanaNacimiento ventanaNacimiento;
	private VentanaTipoContacto ventanaTipoContacto;
	private VentanaEditarPersona ventanaEditarPersona;
	private VentanaABMLocalidad ventanaAMBLocalidad;
	private String[] mensajes = { "No ha seleccionado ningun contacto", "El nombre es obligatorio!",
			"Debe ingresar al menos una forma de contacto (Email o telefono)", "El email ingresado es invalido",
	"No ha seleccionado ninguna persona" };

	public Controlador(Vista vista, Agenda agenda) {
		this.agenda = agenda;
		configurarVentanaNacimiento();
		configurarVentanaPersona();		
		configurarVentanaTipoContacto();
		configurarVentanaPersona();
		configurarVentanaABMLocalidades();
		configurarVista(vista);
	}

	private void configurarVista(Vista vista) {
		this.vista = vista;
		this.vista.getBtnAgregar().addActionListener(a -> ventanaPersona.mostrarVentana());
		this.vista.getBtnBorrar().addActionListener(s -> borrarPersona());
		this.vista.getBtnEditar().addActionListener(s -> configurarVentanaEditarPersona());
		this.vista.getBtnReporte().addActionListener(r -> mostrarReporte());
		this.vista.getMenuItemLocalidad().addActionListener(l -> ventanaAMBLocalidad.mostrarVentana());		
		this.vista.getMenuItemTipoContacto().addActionListener(t -> ventanaTipoContacto.mostrarVentana());
	}

	private void configurarVentanaTipoContacto() {
		this.ventanaTipoContacto = VentanaTipoContacto.getInstance();
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
	}

	private void configurarVentanaNacimiento() {
		this.ventanaNacimiento = new VentanaNacimiento();
		ventanaNacimiento.getBtnAgregarNacimiento().addActionListener(a -> ventanaNacimiento.cerrar());
	}

	private void configurarVentanaABMLocalidades()
	{
		this.ventanaAMBLocalidad = VentanaABMLocalidad.getInstance();
		this.obtenerListaPaises(ventanaAMBLocalidad.getComboBoxPais());

		this.ventanaAMBLocalidad.getBtnAgregarPais().addActionListener(l -> configurarVentanaNuevoPais());	
		this.ventanaAMBLocalidad.getBtnEditarPais().addActionListener(l -> configurarVentanaEditarPais());			
		this.ventanaAMBLocalidad.getBtnEliminarPais().addActionListener(l -> borrarPais(l));

		this.ventanaAMBLocalidad.getBtnAgregarProvincia().addActionListener(l -> configurarVentanaNuevoProvincia());	
		this.ventanaAMBLocalidad.getBtnEditarProvincia().addActionListener(l -> configurarVentanaEditarProvincia());			
		this.ventanaAMBLocalidad.getBtnEliminarProvincia().addActionListener(l -> borrarProvincia(l));

		this.ventanaAMBLocalidad.getBtnAgregarLocalidad().addActionListener(l -> configurarVentanaNuevaLocalidad());	
		this.ventanaAMBLocalidad.getBtnEditarLocalidad().addActionListener(l -> configurarVentanaEditarLocalidad());			
		this.ventanaAMBLocalidad.getBtnEliminarLocalidad().addActionListener(l -> borrarLocalidad(l));		

		this.ventanaAMBLocalidad.getComboBoxPais().addActionListener(l -> mostrarProvincias());
		this.ventanaAMBLocalidad.getComboBoxProvincia().addActionListener(l-> mostrarLocalidades());
		this.refrescarListaPaises();

	}

	private void mostrarLocalidades() {
		try {
			String provinciaseleccionada = ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem().toString();
			ventanaAMBLocalidad.getComboBoxLocalidad().removeAllItems();
			List<LocalidadDTO> localidades = agenda.obtenerLocalidades();
			for(int i=0 ; i< localidades.size() ; i++) {
				ProvinciaDTO provincia = localidades.get(i).getProvincia();
				String nombreProvincia = provincia.getNombre();
				if (nombreProvincia.equals(provinciaseleccionada)) {
					ventanaAMBLocalidad.getComboBoxLocalidad().addItem(localidades.get(i).getNombre());
				}
			}	
			
			refrescarListaLocalidades(this.provinciasEnLista.stream().filter(p -> p.getNombre().equals(provinciaseleccionada)).findFirst().get());

		}
		catch(Exception e) {
			return;
		}
	}

	private void mostrarProvincias() {
		try {
			String paisSeleccionado = ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString();
			ventanaAMBLocalidad.getComboBoxProvincia().removeAllItems();
			ventanaAMBLocalidad.getComboBoxLocalidad().removeAllItems();
			List<ProvinciaDTO> provincias = agenda.obtenerProvincias();
			for(int i=0 ; i< provincias.size() ; i++) {
				PaisDTO pais = provincias.get(i).getPais();
				String nombrePais = pais.getNombre();
				if (nombrePais.equals(paisSeleccionado)) {
					ventanaAMBLocalidad.getComboBoxProvincia().addItem(provincias.get(i).getNombre());
				}
			}	

			refrescarListaProvincias(this.paisesEnLista.stream().filter(p -> p.getNombre().equals(paisSeleccionado)).findFirst().get());
		}
		catch(Exception e) {
			return;
		}		
	}

	// Control ABM Pais

	private void configurarVentanaEditarPais() {

		String paisSeleccionado = this.ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString();

		if (!paisSeleccionado.isEmpty()) {
			VentanaEditarLocalidad editarPais = new VentanaEditarLocalidad(paisSeleccionado);
			editarPais.getBtnAceptar().addActionListener(c -> editarPais(editarPais, c));
			editarPais.getBtnCancelar().addActionListener(c -> editarPais.cerrar());
			editarPais.mostrar();

		} else {
			mostrarMensaje(ventanaTipoContacto, mensajes[0]);
		}
	}

	private void configurarVentanaNuevoPais() {
		VentanaNuevaLocalidad ventNuevoPais= new VentanaNuevaLocalidad();
		ventNuevoPais.getBtnAceptar().addActionListener(n -> agregarPais(ventNuevoPais, n));
		ventNuevoPais.getBtnCancelar().addActionListener(c -> ventNuevoPais.cerrar());
	}

	private void agregarPais(VentanaNuevaLocalidad v, ActionEvent a) {
		String nuevo = v.getTxtNuevo().getText();
		if (nuevo.isEmpty()) {
			mostrarMensaje(v, mensajes[1]);
		} else {
			guardarPais(v, nuevo);
			v.cerrar();		
		}
	}

	private void editarPais(VentanaEditarLocalidad v, ActionEvent a) {
		if (v.getTxtNuevo().getText().isEmpty()) {
			mostrarMensaje(v, mensajes[1]);
			return;
		}	

		this.editarPais(v.getTxtNombreAnterior().getText(), v.getTxtNuevo().getText());

		v.cerrar();
	}

	private void editarPais(String txtNombreAnterior, String txtNuevo)
	{		
		PaisDTO pais = this.paisesEnLista.stream().filter(p -> p.getNombre().equals(txtNombreAnterior)).findFirst().get();

		if (txtNuevo.equals("")) {
			JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, "No puede ingresar un nombre en blanco.");
			return;
		}

		this.agenda.modificarPais(new PaisDTO(pais.getIdPais(), txtNuevo));		
		this.refrescarListaPaises();
	}

	private void guardarPais(VentanaNuevaLocalidad v, String nuevoPais) {

		boolean exists = paisesEnLista.stream().anyMatch(e -> e.getNombre().equals(nuevoPais));
		if (exists) {
			JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, "Ya existe un país con ese nombre.");
			return;
		}

		this.agenda.agregarPais(new PaisDTO(0, nuevoPais));

		this.refrescarListaPaises();	
		mostrarMensaje(v, "Se ha ingresado un nuevo pais");
	}

	private void borrarPais(ActionEvent s)
	{
		String nombre = this.ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString();		
		PaisDTO pais = this.paisesEnLista.stream().filter(p -> p.getNombre().equals(nombre)).findFirst().get();

		if (pais == null) {
			JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, "Debe seleccionar un país de la lista para poder eliminar.");
			return;
		}

		this.agenda.borrarPais(pais);

		//		if (!this.personasEnTabla.stream().anyMatch(p -> p.getDomicilio().getLocalidad().getProvincia().getPaís().equals(nombre)))
		//			this.agenda.borrarPais(pais);
		//		else
		//			JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, String.format("No se puede eliminar el país '%s' porque al menos un domicilio pertenece a alguna de sus localidades", paísSeleccionado.getNombre()));

		this.refrescarListaPaises();

		JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, "El pais se ha borrado correctamente.");
	}

	/// Control ABM Provincia

	private void borrarProvincia(ActionEvent s)
	{
		String nombre = this.ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem().toString();
		ProvinciaDTO provincia = agenda.obtenerProvincias().stream().filter(p -> p.getNombre().equals(nombre)).findFirst().get();

		if(provincia == null) {
			JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, "Debe seleccionar una Provincia de la lista para poder eliminar.");
			return;
		}

		//		if (!this.personasEnTabla.stream().anyMatch(p -> p.getDomicilio().getLocalidad().getProvincia().equals(provinciaSeleccionada)))
		//			this.agenda.borrarProvincia(provinciaSeleccionada);
		//		else
		//			JOptionPane.showMessageDialog(this.ventanaUbicaciones, String.format("No se puede eliminar la provincia '%s' porque al menos un domicilio pertenece a alguna de sus localidades", provinciaSeleccionada.getNombre()));
		//		

		this.agenda.borrarProvincia(provincia);
		this.ventanaAMBLocalidad.limpiarCombos();
	}

	private void configurarVentanaEditarProvincia	() {
		try {
			String provincia = this.ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem().toString();

			if (!provincia.isEmpty()) {
				VentanaEditarProvincia editarProvincia = new VentanaEditarProvincia(provincia);
				editarProvincia.getBtnAceptar().addActionListener(c -> editarProvincia(editarProvincia, c));
				editarProvincia.getBtnCancelar().addActionListener(c -> editarProvincia.cerrar());
				editarProvincia.mostrar();

			} else {
				mostrarMensaje(ventanaTipoContacto, mensajes[0]);
			}
		}
		catch(Exception e) {
			return;
		}
	}

	private void editarProvincia(VentanaEditarProvincia v, ActionEvent a) {
		if (v.getTxtNuevo().getText().isEmpty()) {
			mostrarMensaje(v, mensajes[1]);
			return;
		}	

		this.editarProvincia(v.getTxtNombreAnterior().getText(), v.getTxtNuevo().getText());

		v.cerrar();
	}

	private void editarProvincia(String txtNombreAnterior, String txtNuevo)
	{
		try {
			ProvinciaDTO provincia = this.provinciasEnLista.stream().filter(p -> p.getNombre().equals(txtNombreAnterior)).findFirst().get();

			if (txtNuevo.equals("")) {
				JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, "No puede ingresar un nombre en blanco.");
				return;
			}

			this.agenda.modificarProvincia(new ProvinciaDTO(provincia.getIdProvincia(), txtNuevo, provincia.getPais()));	

			this.refrescarListaProvincias(provincia.getPais());
		}
		catch(Exception e) {
			return;
		}
	}
	
	private void configurarVentanaNuevoProvincia() {
		VentanaNuevaLocalidad ventanaNuevoProvinicia= new VentanaNuevaLocalidad();
		ventanaNuevoProvinicia.getBtnAceptar().addActionListener(n -> agregarProvincia(ventanaNuevoProvinicia, n));
		ventanaNuevoProvinicia.getBtnCancelar().addActionListener(c -> ventanaNuevoProvinicia.cerrar());
	}
	
	private void agregarProvincia(VentanaNuevaLocalidad v, ActionEvent a) {
		String nuevo = v.getTxtNuevo().getText();
		if (nuevo.isEmpty()) {
			mostrarMensaje(v, mensajes[1]);
		} else {
			guardarProvincia(v, nuevo);
			v.cerrar();		
		}
	}
	
	private void guardarProvincia(VentanaNuevaLocalidad v, String nuevaProvincia) {

		boolean exists = this.provinciasEnLista.stream().anyMatch(e -> e.getNombre().equals(nuevaProvincia));
		if (exists) {
			JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, "Ya existe un país con ese nombre.");
			return;
		}
		
		String nombre = this.ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString();
		PaisDTO pais = this.paisesEnLista.stream().filter(p -> p.getNombre().equals(nombre)).findFirst().get();

		this.agenda.agregarProvincia(new ProvinciaDTO(0, nuevaProvincia, pais));

		refrescarListaProvincias(pais);	
		mostrarMensaje(v, "Se ha ingresado un nueva provincia");
	}

	////
	
	/// Control ABM Localidades

	private void borrarLocalidad(ActionEvent s)
	{
		try {
			String nombre = this.ventanaAMBLocalidad.getComboBoxLocalidad().getSelectedItem().toString();
			LocalidadDTO localidad = this.localidadesEnLista.stream().filter(p -> p.getNombre().equals(nombre)).findFirst().get();

			if(localidad == null) {
				JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, "Debe seleccionar una localidad de la lista para poder eliminar.");
				return;
			}

			//		if (!this.personasEnTabla.stream().anyMatch(p -> p.getDomicilio().getLocalidad().getProvincia().equals(provinciaSeleccionada)))
			//			this.agenda.borrarProvincia(provinciaSeleccionada);
			//		else
			//			JOptionPane.showMessageDialog(this.ventanaUbicaciones, String.format("No se puede eliminar la provincia '%s' porque al menos un domicilio pertenece a alguna de sus localidades", provinciaSeleccionada.getNombre()));
			//		

			this.agenda.borrarLocalidad(localidad);
			refrescarListaLocalidades(localidad.getProvincia());
		}
		catch(Exception e) {
			return;
		}
	}

	private void configurarVentanaEditarLocalidad() {
		try {
			String localidad = this.ventanaAMBLocalidad.getComboBoxLocalidad().getSelectedItem().toString();

			if (!localidad.isEmpty()) {
				VentanaEditarLocalidad editarLocalidad = new VentanaEditarLocalidad(localidad);
				editarLocalidad.getBtnAceptar().addActionListener(c -> editarLocalidad(editarLocalidad, c));
				editarLocalidad.getBtnCancelar().addActionListener(c -> editarLocalidad.cerrar());
				editarLocalidad.mostrar();

			} else {
				mostrarMensaje(ventanaAMBLocalidad, mensajes[0]);
			}
		}
		catch(Exception e) {
			return;
		}
	}

	private void editarLocalidad(VentanaEditarLocalidad v, ActionEvent a) {
		try {
		if (v.getTxtNuevo().getText().isEmpty()) {
			mostrarMensaje(v, mensajes[1]);
			return;
		}	

		this.editarLocalidad(v.getTxtNombreAnterior().getText(), v.getTxtNuevo().getText());

		v.cerrar();
		}
		catch(Exception e) {
			return;
		}
	}

	private void editarLocalidad(String txtNombreAnterior, String txtNuevo)
	{
		try {
			LocalidadDTO localidad = this.localidadesEnLista.stream().filter(p -> p.getNombre().equals(txtNombreAnterior)).findFirst().get();

			if (txtNuevo.equals("")) {
				JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, "No puede ingresar un nombre en blanco.");
				return;
			}

			this.agenda.modificarLocalidad(new LocalidadDTO(localidad.getIdLocalidad(), txtNuevo, localidad.getProvincia()));	

			this.refrescarListaLocalidades(localidad.getProvincia());
		}
		catch(Exception e) {
			return;
		}
	}
	
	private void configurarVentanaNuevaLocalidad() {
		VentanaNuevaLocalidad ventanaNuevaLocalidad= new VentanaNuevaLocalidad();
		ventanaNuevaLocalidad.getBtnAceptar().addActionListener(n -> agregarLocalidad(ventanaNuevaLocalidad, n));
		ventanaNuevaLocalidad.getBtnCancelar().addActionListener(c -> ventanaNuevaLocalidad.cerrar());
	}
	
	private void agregarLocalidad(VentanaNuevaLocalidad v, ActionEvent a) {
		String nuevo = v.getTxtNuevo().getText();
		if (nuevo.isEmpty()) {
			mostrarMensaje(v, mensajes[1]);
		} else {
			guardarLocalidad(v, nuevo);
			v.cerrar();		
		}
	}
	
	private void guardarLocalidad(VentanaNuevaLocalidad v, String nuevaLocalidad) {

		boolean exists = this.localidadesEnLista.stream().anyMatch(e -> e.getNombre().equals(nuevaLocalidad));
		if (exists) {
			JOptionPane.showMessageDialog(this.ventanaAMBLocalidad, "Ya existe una localidad con ese nombre.");
			return;
		}
		
		String nombre = this.ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem().toString();
		ProvinciaDTO provincia = this.provinciasEnLista.stream().filter(p -> p.getNombre().equals(nombre)).findFirst().get();

		this.agenda.agregarLocalidad(new LocalidadDTO(0, nuevaLocalidad, provincia));

		refrescarListaLocalidades(provincia);	
		mostrarMensaje(v, "Se ha ingresado un nueva Localidad");
	}

	////

	private void refrescarListaPaises() {
		//this.paisesEnLista = agenda.obtenerPaíses();	
		this.obtenerListaPaises(ventanaAMBLocalidad.getComboBoxPais());
		this.ventanaAMBLocalidad.limpiarCombos();
	}

	private void refrescarListaProvincias(PaisDTO pais) {
		this.obtenerListaProvincias(ventanaAMBLocalidad.getComboBoxProvincia(), pais);
		this.ventanaAMBLocalidad.limpiarComboProvincia();
	}
	
	private void refrescarListaLocalidades(ProvinciaDTO provincia) {
		this.obtenerListaLocalidades(ventanaAMBLocalidad.getComboBoxLocalidad(), provincia);
		this.ventanaAMBLocalidad.limpiarComboLocalidad();
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
			ventanaEditarPersona.getBtnAceptar().addActionListener(a -> actualizarPersona(getPersonaEditada(), personaSeleccionada.getId()));
			ventanaEditarPersona.mostrar();
		} else {
			mostrarMensaje(ventanaPersona, mensajes[4]);
		}
	}

	private void agregarListeners(PersonaDTO personaSeleccionada) {
		ventanaEditarPersona.getBtnCambiarEmail().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ventanaEditarPersona.getTxtEmail().isEnabled()) {
					ventanaEditarPersona.getBtnCambiarEmail().setText("Cambiar");
					ventanaEditarPersona.getTxtEmail().setEnabled(false);
					ventanaEditarPersona.getTxtEmail().setText(personaSeleccionada.getEmail());
				}
				else {
					ventanaEditarPersona.getBtnCambiarEmail().setText("Cancelar");
					ventanaEditarPersona.getTxtEmail().setEnabled(true);
				}
			}
		});
		ventanaEditarPersona.getBtnCambiarNombre().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(ventanaEditarPersona.getTxtNombre().isEnabled()) {
					ventanaEditarPersona.getBtnCambiarNombre().setText("Cambiar");
					ventanaEditarPersona.getTxtNombre().setEnabled(false);
					ventanaEditarPersona.getTxtNombre().setText(personaSeleccionada.getNombre());
				}
				else {
					ventanaEditarPersona.getBtnCambiarNombre().setText("Cancelar");
					ventanaEditarPersona.getTxtNombre().setEnabled(true);
				}
			}
		});
		ventanaEditarPersona.getBtnCambiarTelefono().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ventanaEditarPersona.getTxtTelefono().isEnabled()) {
					ventanaEditarPersona.getBtnCambiarTelefono().setText("Cambiar");
					ventanaEditarPersona.getTxtTelefono().setEnabled(false);
					ventanaEditarPersona.getTxtTelefono().setText(personaSeleccionada.getTelefono());
				}
				else {
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

	private PersonaDTO getPersonaAAgregar() {
		return new PersonaDTO(0, ventanaPersona.getTxtNombre().getText(),
				ventanaPersona.getTxtTelefono().getText(),
				crearStringFecha(), 
				ventanaPersona.getTxtEmail().getText(),
				ventanaPersona.getJComboBoxTipoContacto()
				.getItemAt(ventanaPersona.getJComboBoxTipoContacto().getSelectedIndex()));
	}

	private void actualizarPersona(PersonaDTO personaEditada, int id) {
		if(isValid(personaEditada)) {
			agenda.actualizarPersona(id, personaEditada);
			ventanaEditarPersona.cerrar();
			refrescarTabla();
		}
	}

	private void obtenerListaPaises(JComboBox<String> cmbPais) {	
		this.paisesEnLista = agenda.obtenerPaíses();
		String[] nombrePaises = new String[paisesEnLista.size()];
		for (int i = 0; i < paisesEnLista.size(); i++) {
			nombrePaises[i] = paisesEnLista.get(i).getNombre();
		}

		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nombrePaises);
		cmbPais.setModel(model);
	}

	private void obtenerListaProvincias(JComboBox<String> cmbprovincia , PaisDTO pais) {
		this.provinciasEnLista = agenda.obtenerProvinciasPorPais(pais);	
		String[] nombreProvincias = new String[provinciasEnLista.size()];
		for (int i = 0; i < provinciasEnLista.size(); i++) {
			nombreProvincias[i] = provinciasEnLista.get(i).getNombre();
		}

		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nombreProvincias);
		cmbprovincia.setModel(model);
	}
	
	private void obtenerListaLocalidades(JComboBox<String> cmblocalidad , ProvinciaDTO provincia) {
		this.localidadesEnLista = agenda.obtenerLocalidadesPorProv(provincia);	
		String[] nombreLocalidades = new String[localidadesEnLista.size()];
		
		for (int i = 0; i < localidadesEnLista.size(); i++) {
			nombreLocalidades[i] = localidadesEnLista.get(i).getNombre();
		}

		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nombreLocalidades);
		cmblocalidad.setModel(model);
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

	private boolean isValid(PersonaDTO persona) {
		if (persona.getNombre().isEmpty()) {
			mostrarMensaje(ventanaPersona, mensajes[1]);
			return false;
		}
		if (persona.getTelefono().isEmpty() && persona.getEmail().isEmpty()) {
			mostrarMensaje(ventanaPersona, mensajes[2]);
			return false;
		}
		if (!isValidEmail(persona.getEmail())) {
			mostrarMensaje(ventanaPersona, mensajes[3]);
			return false;
		}
		return true;
	}

	private void guardarPersona(PersonaDTO persona) {
		if(isValid(persona)) {
			this.agenda.agregarPersona(persona);
			this.refrescarTabla();
			this.ventanaPersona.cerrar();
		}
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
