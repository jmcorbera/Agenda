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
import java.awt.Font;

public class VentanaNuevoPaisOContacto extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JTextField txtNuevoNombre;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JLabel lblTitulo;

	
	
	public VentanaNuevoPaisOContacto() {
		super();
		setTitle("Nuevo");
		IntermediarioVista.cambiarLookAndFeel(VentanaNuevoPaisOContacto.class.getName());
		setResizable(false);
		configurarVentana();
		configurarPanel();
		agregarComponentes();
		verificarCampos();
	}

	private void agregarComponentes() {		
		JLabel lblIngresarNombre = new JLabel("Ingrese el nuevo nombre");
		lblIngresarNombre.setBounds(10, 45, 192, 34);
		panel.add(lblIngresarNombre);
		
		txtNuevoNombre = new JTextField();
		txtNuevoNombre.setBounds(10, 77, 241, 24);
		panel.add(txtNuevoNombre);
		txtNuevoNombre.setColumns(10);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(63, 112, 89, 23);
		panel.add(btnAceptar);
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(162, 112, 89, 23);
		panel.add(btnCancelar);
		
		lblTitulo = new JLabel("New label");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTitulo.setBounds(10, 11, 241, 34);
		panel.add(lblTitulo);
		
	}

	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 261, 142);
		contentPane.add(panel);
		panel.setLayout(null);
	}

	private void configurarVentana() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 267, 171);
		setVisible(true);
	}
	public void cerrar() {
		dispose();
	}
	
	public JTextField getTxtNuevoNombre() {
		return txtNuevoNombre;
	}
	
	public JButton getBtnAceptar() {
		return btnAceptar;
	}
	
	public JButton getBtnCancelar() {
		return btnCancelar;
	}
	
	public void cambiarTitulo(String titulo) {
		lblTitulo.setText(titulo);
	}
	
	private void verificarCampos() {
		txtNuevoNombre.addKeyListener((new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				ValidadorTeclado.aceptarLetrasYEspacios(e);
			}
		}));
		
	}
}
