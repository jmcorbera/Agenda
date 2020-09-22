package presentacion.vista;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


import javax.swing.JComboBox;

public class VentanaPersona extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JTextField txtNombre;
	private JTextField txtTelefono;
	private JTextField txtEmail;
	private JButton btnAgregarPersona;
	private JButton btnNacimiento;
	private JButton btnDomicilio;
	private static VentanaPersona INSTANCE;
	private JComboBox<String> tipoContacto;

	public static VentanaPersona getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new VentanaPersona();
			return new VentanaPersona();
		} else
			return INSTANCE;
	}

	private VentanaPersona() {
		super();
		configurarVentana();
		configurarPanel();
		agregarLabels();
		agregarTxts();
		agregarBotones();

		tipoContacto = new JComboBox<String>();
		tipoContacto.setBounds(133, 181, 89, 20);
		panel.add(tipoContacto);
	}

	public JComboBox<String> getJComboBoxTipoContacto() {
		return tipoContacto;
	}

	private void agregarBotones() {
		btnAgregarPersona = new JButton("Agregar");
		btnAgregarPersona.setBounds(344, 214, 89, 23);
		panel.add(btnAgregarPersona);

		btnNacimiento = new JButton("Agregar");
		btnNacimiento.setBounds(133, 137, 89, 23);
		panel.add(btnNacimiento);

		btnDomicilio = new JButton("Agregar");
		btnDomicilio.setBounds(344, 137, 89, 23);
		panel.add(btnDomicilio);
	}

	private void agregarTxts() {
		txtNombre = new JTextField();
		txtNombre.setBounds(133, 8, 300, 17);
		panel.add(txtNombre);
		txtNombre.setColumns(10);
		txtTelefono = new JTextField();
		txtTelefono.setBounds(133, 50, 154, 17);
		panel.add(txtTelefono);
		txtTelefono.setColumns(10);
		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(133, 93, 300, 16);
		panel.add(txtEmail);
		verificarCampos();
	}

	private void agregarLabels() {
		JLabel lblNombreYApellido = new JLabel("Nombre y apellido");
		lblNombreYApellido.setBounds(10, 11, 113, 14);
		panel.add(lblNombreYApellido);

		JLabel lblTelfono = new JLabel("Telefono");
		lblTelfono.setBounds(10, 52, 113, 14);
		panel.add(lblTelfono);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(10, 95, 46, 14);
		panel.add(lblEmail);

		JLabel lblNacimiento = new JLabel("Fecha de Nacimiento");
		lblNacimiento.setBounds(10, 141, 113, 14);
		panel.add(lblNacimiento);

		JLabel lblDomicilio = new JLabel("Domicilio");
		lblDomicilio.setBounds(250, 141, 46, 14);
		panel.add(lblDomicilio);

		JLabel lblTipoContacto = new JLabel("Tipo de Contacto");
		lblTipoContacto.setBounds(10, 184, 89, 14);
		panel.add(lblTipoContacto);
	}

	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new JPanel();
		panel.setBounds(10, 11, 443, 248);
		contentPane.add(panel);
		panel.setLayout(null);
	}

	private void configurarVentana() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 479, 305);
		this.setVisible(false);
	}

	public void mostrarVentana() {
		this.setVisible(true);
	}

	public JTextField getTxtNombre() {
		return txtNombre;
	}

	public JTextField getTxtTelefono() {
		return txtTelefono;
	}

	public JButton getBtnAgregarPersona() {
		return btnAgregarPersona;
	}
	
	public JButton getBtnDomicilio() {
		return btnDomicilio;
	}
	
	public JComboBox<String> getCBTipoContacto(){
		return tipoContacto;
	}

	public void cerrar() {
		this.txtNombre.setText(null);
		this.txtTelefono.setText(null);
		this.txtEmail.setText(null);
		this.dispose();
	}

	public JButton getBtnNacimiento() {
		return btnNacimiento;
	}

	public JTextField getTxtEmail() {
		return txtEmail;
	}
	
	private void verificarCampos() {
		txtTelefono.addKeyListener((new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				ValidadorTeclado.aceptarSoloNumeros(e);
			}
		}));
		txtNombre.addKeyListener((new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				ValidadorTeclado.aceptarLetrasYEspacios(e);
			}
		}));
		txtEmail.addKeyListener((new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				ValidadorTeclado.aceptarMinusculaDigitoPuntoArrobaYGuiones(e);
			}
		}));
	}

}
