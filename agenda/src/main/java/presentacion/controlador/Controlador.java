package presentacion.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import modelo.Agenda;
import presentacion.reportes.ReporteAgenda;
import presentacion.vista.VentanaNacimiento;
import presentacion.vista.VentanaPersona;
import presentacion.vista.Vista;
import dto.PersonaDTO;

public class Controlador implements ActionListener {
	private Vista vista;
	private List<PersonaDTO> personasEnTabla;
	private VentanaPersona ventanaPersona;
	private Agenda agenda;
	private VentanaNacimiento ventanaNacimiento;

	public Controlador(Vista vista, Agenda agenda) {
		this.vista = vista;
		this.vista.getBtnAgregar().addActionListener(a -> ventanaAgregarPersona(a));
		this.vista.getBtnBorrar().addActionListener(s -> borrarPersona(s));
		this.vista.getBtnReporte().addActionListener(r -> mostrarReporte(r));

		this.ventanaPersona = VentanaPersona.getInstance();
		this.ventanaPersona.getBtnAgregarPersona().addActionListener(p -> guardarPersona(p));

		this.ventanaPersona.getBtnNacimiento().addActionListener(n -> ventanaNacimiento(n));

		this.ventanaNacimiento = VentanaNacimiento.getInstance();
		this.ventanaNacimiento.getBtnAgregarNacimiento().addActionListener(a -> cerrarVentanaNacimiento(a));

		this.agenda = agenda;
	}

	private void cerrarVentanaNacimiento(ActionEvent a) {
		this.ventanaNacimiento.cerrar();
	}

	private void ventanaNacimiento(ActionEvent n) {
		this.ventanaNacimiento.mostrarVentana();
	}

	private void ventanaAgregarPersona(ActionEvent a) {
		this.ventanaPersona.mostrarVentana();
	}

	private void guardarPersona(ActionEvent p) {
		String nombre = this.ventanaPersona.getTxtNombre().getText();
		String tel = ventanaPersona.getTxtTelefono().getText();
		String nacimiento = ventanaNacimiento.getFecha();
		String email = ventanaPersona.getTxtEmail().getText();
		PersonaDTO nuevaPersona = new PersonaDTO(0, nombre, tel, nacimiento, email);
		this.agenda.agregarPersona(nuevaPersona);
		this.refrescarTabla();
		this.ventanaPersona.cerrar();
	}

	private void mostrarReporte(ActionEvent r) {
		ReporteAgenda reporte = new ReporteAgenda(agenda.obtenerPersonas());
		reporte.mostrar();
	}

	public void borrarPersona(ActionEvent s) {
		int[] filasSeleccionadas = this.vista.getTablaPersonas().getSelectedRows();
		for (int fila : filasSeleccionadas) {
			this.agenda.borrarPersona(this.personasEnTabla.get(fila));
		}

		this.refrescarTabla();
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
