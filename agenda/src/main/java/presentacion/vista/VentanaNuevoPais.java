package presentacion.vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class VentanaNuevoPais extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JTextField txtContactoNuevo;
	private JButton btnAceptar;
	private JButton btnCancelar;

	
	
	public VentanaNuevoPais() {
		super();
		configurarVentana();
		configurarPanel();
		agregarComponentes();
	}

	private void agregarComponentes() {		
		JLabel lblIngresarNombre = new JLabel("Ingrese el nombre del nuevo contacto");
		lblIngresarNombre.setBounds(10, 11, 210, 24);
		panel.add(lblIngresarNombre);
		
		txtContactoNuevo = new JTextField();
		txtContactoNuevo.setBounds(10, 46, 193, 24);
		panel.add(txtContactoNuevo);
		txtContactoNuevo.setColumns(10);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(10, 81, 89, 23);
		panel.add(btnAceptar);
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(113, 81, 89, 23);
		panel.add(btnCancelar);
		
	}

	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(10, 11, 212, 116);
		contentPane.add(panel);
		panel.setLayout(null);
	}

	private void configurarVentana() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 245, 172);
		setVisible(true);
	}
	public void cerrar() {
		dispose();
	}
	
	public JTextField getTxtContactoNuevo() {
		return txtContactoNuevo;
	}
	public JButton getBtnAceptar() {
		return btnAceptar;
	}
	public JButton getBtnCancelar() {
		return btnCancelar;
	}
}
