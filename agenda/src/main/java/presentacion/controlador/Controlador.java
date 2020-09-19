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
import presentacion.vista.VentanaDomicilio;
import presentacion.vista.VentanaEditarContacto;
import presentacion.vista.VentanaEditarPais;
import presentacion.vista.VentanaEditarPersona;
import presentacion.vista.VentanaNacimiento;
import presentacion.vista.VentanaNuevaProvinciaOLocalidad;
import presentacion.vista.VentanaNuevoContacto;
import presentacion.vista.VentanaNuevoPais;
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

	private List<PaisDTO> paisesEnLista = new ArrayList<PaisDTO>();
	private List<ProvinciaDTO> provinciasEnLista = new ArrayList<ProvinciaDTO>();
	private List<LocalidadDTO> localidadesEnLista = new ArrayList<LocalidadDTO>();
	private DomicilioDTO domicilio;
	private VentanaPersona ventanaPersona;
	private VentanaDomicilio ventanaDomicilio;
	private Agenda agenda;
	private VentanaNacimiento ventanaNacimiento;
	private VentanaTipoContacto ventanaTipoContacto;
	private VentanaEditarPersona ventanaEditarPersona;
	private VentanaABMLocalidad ventanaAMBLocalidad;
	private String[] mensajes = { 
			"No ha seleccionado ningun contacto!", 
			"El nombre es obligatorio!",
			"Debe ingresar al menos una forma de contacto (Email o telefono)", 
			"El email ingresado es invalido",
			"No ha seleccionado ninguna persona!",
			"No ha seleccionado ningún país!",
			"No ha seleccionado ninguna provincia!",
			"No ha seleccionado ninguna localidad!",
			"Operación realizada con éxito :)",
			"Ya existe un país con ese nombre!",
			"Ya existe una provincia con ese nombre!",
			"Ya existe una localidad con ese nombre!"};

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
		this.ventanaPersona.getBtnDomicilio().addActionListener(n -> configurarVentanaDomicilio());
	}
	
	private void configurarVentanaDomicilio() {
		ventanaDomicilio = VentanaDomicilio.getInstance();
		this.obtenerListaPaises(ventanaDomicilio.getComboBoxPais());
		deshabilitarProvinciaYLocalidad(ventanaDomicilio.getComboBoxProvincia(), ventanaDomicilio.getComboBoxLocalidad());
		ventanaDomicilio.getBtnAceptar().addActionListener(a -> guardarDomicilio(ventanaDomicilio));
		ventanaDomicilio.getBtnCancelar().addActionListener(a -> ventanaDomicilio.cerrar());
		ventanaDomicilio.getComboBoxPais().addActionListener(a -> mostrarProvincias(ventanaDomicilio));
		ventanaDomicilio.getComboBoxProvincia().addActionListener(a ->mostrarLocalidades(ventanaDomicilio));
		ventanaDomicilio.mostrarVentana();
		
	}
	
	private void mostrarLocalidades(VentanaDomicilio v) {
		try {
			String seleccionado = v.getComboBoxProvincia().getSelectedItem().toString();
			v.getComboBoxLocalidad().removeAllItems();
			v.getComboBoxLocalidad().setEnabled(true);

			List<LocalidadDTO> localidades = agenda.obtenerLocalidades();
			for (int i = 0; i < localidades.size(); i++) {
				ProvinciaDTO provincia = localidades.get(i).getProvincia();
				String nombreProvincia = provincia.getNombre();
				if (nombreProvincia.equals(seleccionado)) {
					v.getComboBoxLocalidad().addItem(localidades.get(i).getNombre());
				}	
			}
		} catch (Exception e) {
			return;
		}
	}

	private void guardarDomicilio(VentanaDomicilio ventanaDomicilio) {
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
		if(pais.isEmpty()) {
			mostrarMensaje(ventanaDomicilio, "El país es obligatorio!");
			return;
		}
		domicilio = new DomicilioDTO(0, pais, provincia, localidad, "", calle, altura, piso);
		ventanaDomicilio.cerrar();
		}
	
	private void mostrarProvincias(VentanaDomicilio v) {
		try {
			String seleccionado = v.getComboBoxPais().getSelectedItem().toString();
			v.getComboBoxProvincia().removeAllItems();
			v.getComboBoxLocalidad().removeAllItems();
			v.getComboBoxLocalidad().setEnabled(true);

			v.getComboBoxProvincia().setEnabled(true);
			List<ProvinciaDTO> provincias = agenda.obtenerProvincias();
			for (int i = 0; i < provincias.size(); i++) {
				PaisDTO pais = provincias.get(i).getPais();
				String nombrePais = pais.getNombre();
				if (nombrePais.equals(seleccionado)) {
					v.getComboBoxProvincia().addItem(provincias.get(i).getNombre());
				}	
			}
		} catch (Exception e) {
			return;
		}
	}
	private void configurarVentanaNacimiento() {
		this.ventanaNacimiento = new VentanaNacimiento();
		ventanaNacimiento.getBtnAgregarNacimiento().addActionListener(a -> ventanaNacimiento.cerrar());
	}

	private void configurarVentanaABMLocalidades() {
		this.ventanaAMBLocalidad = VentanaABMLocalidad.getInstance();
		this.obtenerListaPaises(ventanaAMBLocalidad.getComboBoxPais());
		deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
		this.ventanaAMBLocalidad.getBtnAgregarPais().addActionListener(l -> configurarVentanaNuevoPais());
		this.ventanaAMBLocalidad.getBtnEditarPais().addActionListener(l -> configurarVentanaEditarPais());
		this.ventanaAMBLocalidad.getBtnEliminarPais().addActionListener(l -> borrarPais());

		this.obtenerListaProvincias(ventanaAMBLocalidad.getComboBoxProvincia());
		this.ventanaAMBLocalidad.getBtnAgregarProvincia().addActionListener(l -> configurarVentanaNuevaProvincia());
		this.ventanaAMBLocalidad.getBtnEditarProvincia().addActionListener(l -> configurarVentanaEditarProvincia());
		this.ventanaAMBLocalidad.getBtnEliminarProvincia().addActionListener(l -> borrarProvincia());

		this.ventanaAMBLocalidad.getBtnAgregarLocalidad().addActionListener(l -> configurarVentanaNuevaLocalidad());	
		this.ventanaAMBLocalidad.getBtnEditarLocalidad().addActionListener(l -> configurarVentanaEditarLocalidad());			
		this.ventanaAMBLocalidad.getBtnEliminarLocalidad().addActionListener(l -> borrarLocalidad());		

		this.ventanaAMBLocalidad.getComboBoxPais().addActionListener(l -> mostrarProvincias());
		this.ventanaAMBLocalidad.getComboBoxProvincia().addActionListener(l -> mostrarLocalidades());
		this.refrescarListaPaises();
		this.refrescarListaProvincias();
		this.refrescarListaLocalidades();
	}
	
	//ABM Pais
	private void configurarVentanaNuevoPais() {
		VentanaNuevoPais ventNuevoPais = new VentanaNuevoPais();
		ventNuevoPais.getBtnAceptar().addActionListener(n -> agregarPais(ventNuevoPais, n));
		ventNuevoPais.getBtnCancelar().addActionListener(c -> ventNuevoPais.cerrar());
	}
	
	//Alta
	private void agregarPais(VentanaNuevoPais v, ActionEvent a) {
		String nuevo = v.getTxtContactoNuevo().getText();
		if (nuevo.isEmpty()) {
			mostrarMensaje(v, mensajes[1]);
		} else {
			guardarPais(v, nuevo);
			v.cerrar();
		}
	}
	
	private void guardarPais(VentanaNuevoPais v, String nuevoPais) {
		boolean exists = paisesEnLista.stream().anyMatch(e -> e.getNombre().equals(nuevoPais));
		if (exists) {
			mostrarMensaje(this.ventanaAMBLocalidad, mensajes[9]);
			return;
		}
		this.agenda.agregarPais(new PaisDTO(0, nuevoPais));
		this.refrescarListaPaises();
		deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
		mostrarMensaje(v, mensajes[8]);
	}
	
	//Baja
	private void borrarPais() {
		try {
				String nombre = this.ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString();
				PaisDTO pais = this.paisesEnLista.stream().filter(p -> p.getNombre().equals(nombre)).findFirst().get();
				this.agenda.borrarPais(pais);
				this.refrescarListaPaises();
				deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
				mostrarMensaje(ventanaAMBLocalidad, mensajes[8]);
		}
		catch(Exception e) {
				mostrarMensaje(this.ventanaAMBLocalidad,mensajes[5]);
		}
	}
	
	//Modificación
	private void configurarVentanaEditarPais() {
		try {
				String paisSeleccionado = this.ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString();
				VentanaEditarPais editarPais = new VentanaEditarPais(paisSeleccionado);
				editarPais.getBtnAceptar().addActionListener(c -> editarPais(editarPais, c));
				editarPais.getBtnCancelar().addActionListener(c -> editarPais.cerrar());
				editarPais.mostrar();

		} catch (Exception e) {
			mostrarMensaje(ventanaAMBLocalidad, mensajes[5]);
		}
	}
	
	private void editarPais(VentanaEditarPais v, ActionEvent a) {
		if (v.getTxtNuevo().getText().isEmpty()) {
			mostrarMensaje(v, mensajes[1]);
			return;
		}
		this.editarPais(v.getTxtNombreAnterior().getText(), v.getTxtNuevo().getText());
		v.cerrar();
	}
	
	private void editarPais(String txtNombreAnterior, String txtNuevo) {
		try {
			PaisDTO pais = this.paisesEnLista.stream().filter(p -> p.getNombre().equals(txtNombreAnterior)).findFirst().get();
			if (txtNuevo.equals("")) {
				mostrarMensaje(this.ventanaAMBLocalidad, mensajes[1]);
				return;
			}
			this.agenda.modificarPais(new PaisDTO(pais.getIdPais(), txtNuevo));
			this.refrescarListaPaises();
			deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
			mostrarMensaje(this.ventanaAMBLocalidad, mensajes[8]);
		}
		catch(Exception e) {
		}
	}
	
	//ABM Provincia
	private void configurarVentanaNuevaProvincia() {
		VentanaNuevaProvinciaOLocalidad ventanaNuevaProvincia = new VentanaNuevaProvinciaOLocalidad();
		agregarPaisesAVentana(ventanaNuevaProvincia);
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
				mostrarMensaje(ventanaNuevaProvincia, mensajes[1]);
			} else {
				guardarProvincia(ventanaNuevaProvincia, nuevo, pais);
				ventanaNuevaProvincia.cerrar();
			}
		}
		catch(Exception e) {
			mostrarMensaje(ventanaNuevaProvincia, mensajes[6]);
		}
	}

	private void guardarProvincia(VentanaNuevaProvinciaOLocalidad ventanaNuevaProvincia, String nuevo, PaisDTO pais) {
		try {
			boolean exists = provinciasEnLista.stream().anyMatch(e -> e.getNombre().equals(nuevo));
			if (exists) {
				mostrarMensaje(this.ventanaAMBLocalidad, mensajes[10]);
				return;
			}
			this.agenda.agregarProvincia(new ProvinciaDTO(0, nuevo, pais));
			this.refrescarListaProvincias();
			deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
			mostrarMensaje(ventanaNuevaProvincia, mensajes[8]);
		}
		catch(Exception e) {
		}
	}

	//Baja
	private void borrarProvincia() {
		try {
			String nombre = this.ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem().toString();
			ProvinciaDTO provincia = agenda.obtenerProvincias().stream().filter(p -> p.getNombre().equals(nombre)).findFirst().get();
			this.agenda.borrarProvincia(provincia);
			this.ventanaAMBLocalidad.limpiarCombos();
			deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
			mostrarMensaje(ventanaAMBLocalidad,mensajes[8]);
		}
		catch(Exception e) {
			mostrarMensaje(ventanaAMBLocalidad, mensajes[6]);
			return;	
		}
	}
	
	//Modificación
	private void configurarVentanaEditarProvincia() {
		try {
				String provinciaSeleccionada = this.ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem().toString();
				VentanaNuevaProvinciaOLocalidad editarProvincia = new VentanaNuevaProvinciaOLocalidad();
				agregarPaisesAVentana(editarProvincia);
				editarProvincia.getTxtNombre().setEnabled(false);
				editarProvincia.getTxtNombre().setText(provinciaSeleccionada);
				editarProvincia.getBtnCambiar().addActionListener(p -> configurarBoton(editarProvincia));
				editarProvincia.getBtnAceptar().addActionListener(c -> editarProvincia(editarProvincia, provinciaSeleccionada));
				editarProvincia.getBtnCancelar().addActionListener(c -> editarProvincia.cerrar());
		} catch (Exception e) {
			mostrarMensaje(ventanaAMBLocalidad,mensajes[6]);
		}
	}
	
	private void editarProvincia(VentanaNuevaProvinciaOLocalidad editarProvincia, String anterior) {
		try {
			String nuevo = editarProvincia.getTxtNombre().getText();
			ProvinciaDTO provincia = this.provinciasEnLista.stream().filter(p -> p.getNombre().equals(anterior)).findFirst().get();
			String nombrePaisSeleccionado = editarProvincia.getComboBoxPadre().getSelectedItem().toString();
			PaisDTO pais = this.paisesEnLista.stream().filter(p -> p.getNombre().equals(nombrePaisSeleccionado)).findFirst().get();
			if (nuevo.equals("")) {
				mostrarMensaje(this.ventanaAMBLocalidad, mensajes[1]);
				return;
			}
			this.agenda.modificarProvincia(new ProvinciaDTO(provincia.getIdProvincia(), nuevo, pais));
			this.refrescarListaProvincias();
			deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
			editarProvincia.cerrar();
			mostrarMensaje(this.ventanaAMBLocalidad, mensajes[8]);
			
		} catch (Exception e) {
			mostrarMensaje(this.ventanaAMBLocalidad,mensajes[6]);
		}
	}
	
	//ABM Localidad
	private void configurarVentanaNuevaLocalidad() {
		VentanaNuevaProvinciaOLocalidad ventanaNuevaLocalidad = new VentanaNuevaProvinciaOLocalidad();
		ventanaNuevaLocalidad.getLblPadreAlQuePertenece().setText("Provincia a la que pertenece: ");
		agregarProvinciasAVentana(ventanaNuevaLocalidad);
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
			mostrarMensaje(ventanaNuevaLocalidad, mensajes[1]);
		} else {
			guardarLocalidad(ventanaNuevaLocalidad, nuevo, provincia);
			ventanaNuevaLocalidad.cerrar();
		}
	}

	private void guardarLocalidad(VentanaNuevaProvinciaOLocalidad ventanaNuevaLocalidad, String nuevo, ProvinciaDTO provincia) {
		try {
			boolean exists = localidadesEnLista.stream().anyMatch(e -> e.getNombre().equals(nuevo));
			if (exists) {
				mostrarMensaje(this.ventanaAMBLocalidad, mensajes[11]);
				return;
			}
			this.agenda.agregarLocalidad(new LocalidadDTO(0, nuevo, provincia));
			this.refrescarListaLocalidades();
			deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
			mostrarMensaje(ventanaNuevaLocalidad, mensajes[8]);
		}
		catch(Exception e) {
		}
	}
	
	//Baja
	private void borrarLocalidad() {
		try {
			String nombre = this.ventanaAMBLocalidad.getComboBoxLocalidad().getSelectedItem().toString();
			LocalidadDTO localidad = agenda.obtenerLocalidades().stream().filter(p -> p.getNombre().equals(nombre)).findFirst().get();

			this.agenda.borrarLocalidad(localidad);
			this.ventanaAMBLocalidad.limpiarCombos();
			deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
			
				mostrarMensaje(ventanaAMBLocalidad,mensajes[8]);
			}
			catch(Exception e) {
				mostrarMensaje(ventanaAMBLocalidad,mensajes[7]);
				return;	
			}
	}
	
	//Modificación
	private void configurarVentanaEditarLocalidad() {
		try {
				String localidadSeleccionada = this.ventanaAMBLocalidad.getComboBoxLocalidad().getSelectedItem().toString();
				VentanaNuevaProvinciaOLocalidad editarLocalidad = new VentanaNuevaProvinciaOLocalidad();
				agregarProvinciasAVentana(editarLocalidad);
				editarLocalidad.getLblPadreAlQuePertenece().setText("Provincia a la que pertenece");
				editarLocalidad.getTxtNombre().setEnabled(false);
				editarLocalidad.getTxtNombre().setText(localidadSeleccionada);
				editarLocalidad.getBtnCambiar().addActionListener(p -> configurarBoton(editarLocalidad));
				editarLocalidad.getBtnAceptar().addActionListener(c -> editarLocalidad(editarLocalidad, localidadSeleccionada));
				editarLocalidad.getBtnCancelar().addActionListener(c -> editarLocalidad.cerrar());
		
		} catch (Exception e) {
			mostrarMensaje(this.ventanaAMBLocalidad,mensajes[7]);
		}
	}

	private void editarLocalidad(VentanaNuevaProvinciaOLocalidad editarLocalidad, String anterior) {
		try {
			String nuevo = editarLocalidad.getTxtNombre().getText();
			LocalidadDTO localidad = this.localidadesEnLista.stream().filter(p -> p.getNombre().equals(anterior)).findFirst().get();
			String nombreProvinciaSeleccionada = editarLocalidad.getComboBoxPadre().getSelectedItem().toString();
			ProvinciaDTO provincia = this.provinciasEnLista.stream().filter(p -> p.getNombre().equals(nombreProvinciaSeleccionada)).findFirst().get();
			this.agenda.modificarLocalidad(new LocalidadDTO(localidad.getIdLocalidad(), nuevo, provincia));
			this.refrescarListaLocalidades();
			deshabilitarProvinciaYLocalidad(ventanaAMBLocalidad.getComboBoxProvincia(), ventanaAMBLocalidad.getComboBoxLocalidad());
			editarLocalidad.cerrar();
			mostrarMensaje(editarLocalidad, mensajes[8]);
			
		} catch (Exception e) {
			mostrarMensaje(editarLocalidad,mensajes[7]);
		}
	}

	//Generar menú desplegable 
	private void agregarProvinciasAVentana(VentanaNuevaProvinciaOLocalidad ventanaNuevaLocalidad) {
		List<ProvinciaDTO> provincias = agenda.obtenerProvincias();
		for (ProvinciaDTO provincia : provincias) {
			ventanaNuevaLocalidad.getComboBoxPadre().addItem(provincia.getNombre());
		}
	}
	
	private void agregarPaisesAVentana(VentanaNuevaProvinciaOLocalidad ventanaNuevaProvincia) {
		List<PaisDTO> paises = agenda.obtenerPaíses();
		for (PaisDTO pais : paises) {
			ventanaNuevaProvincia.getComboBoxPadre().addItem(pais.getNombre());
		}
	}

	//Auxiliares
	private void deshabilitarProvinciaYLocalidad(JComboBox<String> provincia, JComboBox<String> localidad) {
		provincia.setEnabled(false);
		localidad.setEnabled(false);
	}

	private void refrescarListaLocalidades() {
		this.localidadesEnLista = agenda.obtenerLocalidades();
		this.obtenerListaProvincias(ventanaAMBLocalidad.getComboBoxLocalidad());
		this.ventanaAMBLocalidad.limpiarCombos();
	}
	
	private void refrescarListaPaises() {
		this.paisesEnLista = agenda.obtenerPaíses();
		this.obtenerListaPaises(ventanaAMBLocalidad.getComboBoxPais());
		this.ventanaAMBLocalidad.limpiarCombos();
	}

	private void refrescarListaProvincias() {
		this.provinciasEnLista = agenda.obtenerProvincias();
		this.obtenerListaProvincias(ventanaAMBLocalidad.getComboBoxProvincia());
		this.ventanaAMBLocalidad.limpiarCombos();
	}
	
	private void obtenerListaProvincias(JComboBox<String> comboBoxProvincia) {
		List<ProvinciaDTO> provincias = agenda.obtenerProvincias();
		String[] nombrePaises = new String[provincias.size()];
		for (int i = 0; i < provincias.size(); i++) {
			nombrePaises[i] = provincias.get(i).getNombre();
		}

		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nombrePaises);
		ventanaAMBLocalidad.getComboBoxProvincia().setModel(model);
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

	private void mostrarLocalidades() {
		try {
			String seleccionado = ventanaAMBLocalidad.getComboBoxProvincia().getSelectedItem().toString();
			ventanaAMBLocalidad.getComboBoxLocalidad().removeAllItems();
			List<LocalidadDTO> localidades = agenda.obtenerLocalidades();
			for (int i = 0; i < localidades.size(); i++) {
				ProvinciaDTO provincia = localidades.get(i).getProvincia();
				String nombreProvincia = provincia.getNombre();
				if (nombreProvincia.equals(seleccionado)) {
					ventanaAMBLocalidad.getComboBoxLocalidad().addItem(localidades.get(i).getNombre());
				}
			}
		} catch (Exception e) {
			return;
		}
	}

	private void mostrarProvincias() {
		try {
			String seleccionado = ventanaAMBLocalidad.getComboBoxPais().getSelectedItem().toString();
			ventanaAMBLocalidad.getComboBoxProvincia().removeAllItems();
			ventanaAMBLocalidad.getComboBoxLocalidad().removeAllItems();
			ventanaAMBLocalidad.getComboBoxLocalidad().setEnabled(true);

			ventanaAMBLocalidad.getComboBoxProvincia().setEnabled(true);
			List<ProvinciaDTO> provincias = agenda.obtenerProvincias();
			for (int i = 0; i < provincias.size(); i++) {
				PaisDTO pais = provincias.get(i).getPais();
				String nombrePais = pais.getNombre();
				if (nombrePais.equals(seleccionado)) {
					ventanaAMBLocalidad.getComboBoxProvincia().addItem(provincias.get(i).getNombre());
				}
			}
		} catch (Exception e) {
			return;
		}
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
			mostrarMensaje(ventanaPersona, mensajes[4]);
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
		return new PersonaDTO(0, ventanaPersona.getTxtNombre().getText(), ventanaPersona.getTxtTelefono().getText(),
				crearStringFecha(), ventanaPersona.getTxtEmail().getText(), ventanaPersona.getJComboBoxTipoContacto()
						.getItemAt(ventanaPersona.getJComboBoxTipoContacto().getSelectedIndex()));
	}

	private void actualizarPersona(PersonaDTO personaEditada, int id) {
		if (isValid(personaEditada)) {
			agenda.actualizarPersona(id, personaEditada);
			ventanaEditarPersona.cerrar();
			refrescarTabla();
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
	
	private boolean isValidDomicilio() {
		return !domicilio.getPais().isEmpty();
	}

	private void guardarPersona(PersonaDTO persona) {
		if (isValid(persona)) {
			this.agenda.agregarPersona(persona);
			if(isValidDomicilio()) {
			this.agenda.agregarDomicilio(new DomicilioDTO(persona.getId(), domicilio.getPais(),
					domicilio.getProvincia(), domicilio.getLocalidad(),
					domicilio.getDepartamento(), domicilio.getCalle(),
					domicilio.getAltura(), domicilio.getPiso()));
			}
			domicilio = null;
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