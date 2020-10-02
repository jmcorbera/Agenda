package presentacion.vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import presentacion.vista.Intermediario.IntermediarioVista;
import presentacion.vista.Intermediario.ValidadorTeclado;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.Font;

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
	private JLabel lblTitulo;

	public VentanaNuevaProvinciaOLocalidad() {
		super();
		setTitle("Editar");
		IntermediarioVista.cambiarLookAndFeel(VentanaNuevaProvinciaOLocalidad.class.getName());
		setResizable(false);
		configurarVentana();
		configurarPanel();
		agregarComponentes();
		verificarCampos();
	}

	private void agregarComponentes() {		
		JLabel lblIngresarNombre = new JLabel("Nombre:");
		lblIngresarNombre.setBounds(20, 123, 210, 24);
		panel.add(lblIngresarNombre);
		
		nombre = new JTextField();
		nombre.setBounds(20, 145, 179, 24);
		panel.add(nombre);
		nombre.setColumns(10);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(110, 191, 89, 23);
		panel.add(btnAceptar);
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(209, 191, 89, 23);
		panel.add(btnCancelar);
		
		comboBoxPadre = new JComboBox<String>();
		comboBoxPadre.setBounds(20, 88, 278, 24);
		panel.add(comboBoxPadre);
		
		lblPadreAlQuePertenece = new JLabel("Pais al que pertenece: ");
		lblPadreAlQuePertenece.setBounds(20, 63, 188, 14);
		panel.add(lblPadreAlQuePertenece);
		
		btnCambiar = new JButton("Cambiar");
		btnCambiar.setBounds(209, 145, 89, 24);
		panel.add(btnCambiar);
		
		lblTitulo = new JLabel("Modificación de datos");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTitulo.setBounds(70, 11, 195, 41);
		panel.add(lblTitulo);
		
	}

	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 315, 225);
		contentPane.add(panel);
		panel.setLayout(null);
	}

	private void configurarVentana() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 321, 254);
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
