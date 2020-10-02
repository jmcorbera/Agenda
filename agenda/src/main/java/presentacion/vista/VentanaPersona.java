package presentacion.vista;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import presentacion.vista.Intermediario.IntermediarioVista;
import presentacion.vista.Intermediario.ValidadorTeclado;

import javax.swing.JComboBox;
import java.awt.Font;

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
	private JComboBox<String> contactoPreferente;
	private JLabel lblTitulo;

	public static VentanaPersona getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new VentanaPersona();
			return new VentanaPersona();
		} else
			return INSTANCE;
	}

	private VentanaPersona() {
		super();
		IntermediarioVista.cambiarLookAndFeel(VentanaPersona.class.getName());
		configurarVentana();
		configurarPanel();
		agregarLabels();
		agregarTxts();
		agregarBotones();

		tipoContacto = new JComboBox<String>();
		tipoContacto.setBounds(133, 166, 154, 20);
		panel.add(tipoContacto);
		
		lblTitulo = new JLabel("Nueva Persona");
		lblTitulo.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblTitulo.setBounds(133, 11, 154, 31);
		panel.add(lblTitulo);
		
		contactoPreferente = new JComboBox<String>();
		contactoPreferente.setBounds(133, 206, 154, 20);
		panel.add(contactoPreferente);
		
		JLabel lblContactoPreferente = new JLabel("Contacto preferente:");
		lblContactoPreferente.setBounds(20, 209, 113, 14);
		panel.add(lblContactoPreferente);
	}

	public JComboBox<String> getJComboBoxTipoContacto() {
		return tipoContacto;
	}
	
	public JComboBox<String> getJComboBoxContactoPreferente() {
		return contactoPreferente;
	}

	private void agregarBotones() {
		btnAgregarPersona = new JButton("Aceptar");
		btnAgregarPersona.setBounds(20, 318, 357, 23);
		panel.add(btnAgregarPersona);

		btnNacimiento = new JButton("Agregar");
		btnNacimiento.setBounds(133, 244, 89, 23);
		panel.add(btnNacimiento);

		btnDomicilio = new JButton("Agregar");
		btnDomicilio.setBounds(133, 284, 89, 23);
		panel.add(btnDomicilio);
	}

	private void agregarTxts() {
		txtNombre = new JTextField();
		txtNombre.setBounds(133, 50, 244, 23);
		panel.add(txtNombre);
		txtNombre.setColumns(10);
		txtTelefono = new JTextField();
		txtTelefono.setBounds(133, 85, 154, 23);
		panel.add(txtTelefono);
		txtTelefono.setColumns(10);
		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(133, 125, 244, 23);
		panel.add(txtEmail);
		verificarCampos();
	}

	private void agregarLabels() {
		JLabel lblNombreYApellido = new JLabel("Nombre y apellido:");
		lblNombreYApellido.setBounds(20, 54, 113, 14);
		panel.add(lblNombreYApellido);

		JLabel lblTelfono = new JLabel("Telefono:");
		lblTelfono.setBounds(20, 89, 113, 14);
		panel.add(lblTelfono);

		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(20, 129, 46, 14);
		panel.add(lblEmail);

		JLabel lblNacimiento = new JLabel("Cumpleaños:");
		lblNacimiento.setBounds(20, 248, 113, 14);
		panel.add(lblNacimiento);

		JLabel lblDomicilio = new JLabel("Domicilio:");
		lblDomicilio.setBounds(20, 288, 113, 14);
		panel.add(lblDomicilio);

		JLabel lblTipoContacto = new JLabel("Tipo de Contacto:");
		lblTipoContacto.setBounds(20, 169, 113, 14);
		panel.add(lblTipoContacto);
	}

	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new JPanel();
		panel.setBounds(0, 0, 396, 351);
		contentPane.add(panel);
		panel.setLayout(null);
	}

	private void configurarVentana() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 402, 380);
		this.setVisible(false);
		setTitle("Persona");
		setResizable(false);
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

	public void eliminarActionListeners() {
		IntermediarioVista.eliminarListener(btnAgregarPersona);
		IntermediarioVista.eliminarListener(btnDomicilio);
		IntermediarioVista.eliminarListener(btnNacimiento);
		IntermediarioVista.eliminarListener(contactoPreferente);
		IntermediarioVista.eliminarListener(tipoContacto);
		
	}
}
