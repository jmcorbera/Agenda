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
import presentacion.vista.Intermediario.IntermediarioVista;

import java.awt.Font;
import javax.swing.JLabel;

public class VentanaABMTipoContacto extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private DefaultTableModel modelTipoContacto;
	private String[] nombreColumnas = { "Tipos de contacto predeterminados" };
	private JTable tablaTipoContactos;
	private JButton btnNuevo;
	private JButton btnEditarContacto;
	private JButton btnEliminarContacto;
	private JButton btnVolver;

	public VentanaABMTipoContacto() {
		super();
		IntermediarioVista.cambiarLookAndFeel(VentanaABMTipoContacto.class.getName());
		configurarVentana();
		configurarPanel();
		agregarComponentes();
	}

	private void agregarComponentes() {
		panel.setLayout(null);
		modelTipoContacto = new DefaultTableModel(nombreColumnas, 0);
		tablaTipoContactos = new JTable(modelTipoContacto);
		JScrollPane spTipoContactos = new JScrollPane();
		spTipoContactos.setBounds(10, 53, 281, 156);
		panel.add(spTipoContactos);

		spTipoContactos.setViewportView(tablaTipoContactos);
		btnNuevo = new JButton("Nuevo");
		btnNuevo.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnNuevo.setBounds(10, 220, 84, 23);
		panel.add(btnNuevo);

		btnEditarContacto = new JButton("Editar");
		btnEditarContacto.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnEditarContacto.setBounds(104, 220, 90, 23);
		panel.add(btnEditarContacto);

		btnEliminarContacto = new JButton("Eliminar");
		btnEliminarContacto.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnEliminarContacto.setBounds(204, 220, 87, 23);
		panel.add(btnEliminarContacto);

		btnVolver = new JButton("Volver");
		btnVolver.setFont(new Font("Tahoma", Font.PLAIN, 11));

		btnVolver.setBounds(10, 254, 282, 22);
		panel.add(btnVolver);
		
		JLabel lblTitulo = new JLabel("Tipos de contacto preferentes");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTitulo.setBounds(49, 11, 242, 29);
		panel.add(lblTitulo);

	}

	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new JPanel();
		panel.setBounds(0, 0, 302, 287);
		contentPane.add(panel);
	}

	private void configurarVentana() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 308, 316);
		setResizable(false);
		setTitle("ABM Tipo Contacto");
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

	public JButton getBtnVolver() {
		return btnVolver;
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

	public void eliminarActionListeners() {
		IntermediarioVista.eliminarListener(btnVolver);
		IntermediarioVista.eliminarListener(btnEditarContacto);
		IntermediarioVista.eliminarListener(btnEliminarContacto);
		IntermediarioVista.eliminarListener(btnNuevo);
	}
}
