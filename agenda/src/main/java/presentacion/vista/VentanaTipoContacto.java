package presentacion.vista;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dto.ContactoDTO;

public class VentanaTipoContacto extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private DefaultTableModel modelTipoContacto;
	private String[] nombreColumnas = { "Tipos de contacto predeterminados" };
	private JTable tablaTipoContactos;
	private JButton btnNuevo;
	private JButton btnEditarContacto;
	private JButton btnEliminarContacto;
	private JButton btnAceptar;

	public VentanaTipoContacto() {
		super();
		configurarVentana();
		configurarPanel();
		agregarComponentes();
	}

	private void agregarComponentes() {
		panel.setLayout(null);
		modelTipoContacto = new DefaultTableModel(nombreColumnas, 0);
		tablaTipoContactos = new JTable(modelTipoContacto);
		JScrollPane spTipoContactos = new JScrollPane();
		spTipoContactos.setBounds(0, 11, 416, 156);
		panel.add(spTipoContactos);

		spTipoContactos.setViewportView(tablaTipoContactos);

		btnNuevo = new JButton("Nuevo");
		btnNuevo.setBounds(10, 178, 118, 23);
		panel.add(btnNuevo);

		btnEditarContacto = new JButton("Editar");
		btnEditarContacto.setBounds(149, 178, 118, 23);
		panel.add(btnEditarContacto);

		btnEliminarContacto = new JButton("Eliminar");
		btnEliminarContacto.setBounds(277, 178, 129, 23);
		panel.add(btnEliminarContacto);

		btnAceptar = new JButton("Aceptar");

		btnAceptar.setBounds(10, 212, 396, 23);
		panel.add(btnAceptar);

	}

	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new JPanel();
		panel.setBounds(10, 11, 416, 246);
		contentPane.add(panel);
	}

	private void configurarVentana() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 452, 307);
		this.setVisible(false);
	}

	public void mostrarVentana() {
		this.setVisible(true);
	}

	public void cerrar() {
		this.setVisible(false);
		this.dispose();
	}

	public String[] getNombreColumnas() {
		return nombreColumnas;
	}

	public DefaultTableModel getModelTipoContactos() {
		return modelTipoContacto;
	}

	public void llenarTabla(List<ContactoDTO> contactosEnTabla) {
		this.getModelTipoContactos().setRowCount(0); // Para vaciar la tabla
		this.getModelTipoContactos().setColumnCount(0);
		this.getModelTipoContactos().setColumnIdentifiers(this.getNombreColumnas());

		for (ContactoDTO c : contactosEnTabla) {
			String nombre = c.getNombreContacto();
			Object[] fila = { nombre };
			this.getModelTipoContactos().addRow(fila);
		}
	}

	public JButton getBtnEditarContacto() {
		return btnEditarContacto;
	}

	public JButton getBtnNuevoContacto() {
		return btnNuevo;
	}

	public JButton getBtnAceptar() {
		return btnAceptar;
	}

	public JButton getBtnEliminarContacto() {
		return btnEliminarContacto;
	}

	public JTable getTableTipoContactos() {
		return tablaTipoContactos;
	}

	public void setModel(DefaultTableModel otroModelo) {
		modelTipoContacto = otroModelo;
	}
}
