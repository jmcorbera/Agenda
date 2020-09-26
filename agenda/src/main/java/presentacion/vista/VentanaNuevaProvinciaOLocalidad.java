package presentacion.vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;

public class VentanaNuevaProvinciaOLocalidad extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JTextField nombre;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JButton btnCambiar;
	private JComboBox <String> comboBoxPadre;
	private JLabel lblPadreAlQuePertenece;

	public VentanaNuevaProvinciaOLocalidad() {
		super();
		configurarVentana();
		configurarPanel();
		agregarComponentes();
		verificarCampos();
	}

	private void agregarComponentes() {		
		JLabel lblIngresarNombre = new JLabel("Nombre:");
		lblIngresarNombre.setBounds(10, 83, 210, 24);
		panel.add(lblIngresarNombre);
		
		nombre = new JTextField();
		nombre.setBounds(10, 106, 227, 24);
		panel.add(nombre);
		nombre.setColumns(10);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(148, 153, 89, 23);
		panel.add(btnAceptar);
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(245, 153, 89, 23);
		panel.add(btnCancelar);
		
		comboBoxPadre = new JComboBox<String>();
		comboBoxPadre.setBounds(10, 52, 324, 20);
		panel.add(comboBoxPadre);
		
		lblPadreAlQuePertenece = new JLabel("Pais al que pertenece: ");
		lblPadreAlQuePertenece.setBounds(10, 25, 188, 14);
		panel.add(lblPadreAlQuePertenece);
		
		btnCambiar = new JButton("Cambiar");
		btnCambiar.setBounds(245, 106, 89, 24);
		panel.add(btnCambiar);
		
	}

	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(10, 11, 346, 187);
		contentPane.add(panel);
		panel.setLayout(null);
	}

	private void configurarVentana() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 377, 247);
		setVisible(true);
	}
	
	public void cerrar() {
		dispose();
	}
	
	public JTextField getTxtNombre() {
		return nombre;
	}
	
	public JComboBox<String> getComboBoxPadre() {
		return comboBoxPadre;
	}
	
	public JButton getBtnAceptar() {
		return btnAceptar;
	}
	
	public JButton getBtnCancelar() {
		return btnCancelar;
	}
	
	public JButton getBtnCambiar() {
		return btnCambiar;
	}

	public JLabel getLblPadreAlQuePertenece() {
		return lblPadreAlQuePertenece;
	}
	
	private void verificarCampos() {
		nombre.addKeyListener((new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				ValidadorTeclado.aceptarLetrasYEspacios(e);
			}
		}));
	}
}
