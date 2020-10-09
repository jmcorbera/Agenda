package presentacion.vista;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import presentacion.vista.Intermediario.IntermediarioVista;
import presentacion.vista.Intermediario.ValidadorTeclado;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;

public class VentanaEditarPersona extends JFrame {
	private static final long serialVersionUID = 1L;
	private JLabel lblNombre;
	private JLabel lblTelefono;
	private JButton btnAceptar;
	private JPanel contentPane;
	private JTextField txtNombre;
	private JTextField txtTelefono;
	private JPanel panel;
	private JButton btnCancelar;
	private JButton btnCambiarEmail;
	private JButton btnCambiarNombre;
	private JButton btnCambiarTelefono;
	private JButton btnCambiarNacimiento;
	private JButton btnEliminarNacimiento;
	private JTextField txtEmail;
	private JTextField txtFechaNacimiento;
	private JComboBox<String> cBTipoContacto;
	private JLabel lblFormatoFecha;
	private JButton btnDomicilio;
	private JLabel lblDomicilio;
	private static VentanaEditarPersona INSTANCE;
	private JButton btnEliminarDomicilio;
	private JLabel lblContactoPreferente;
	private JComboBox<String> cBContactoPreferente;
	private JLabel lblTitulo;
	
	public static VentanaEditarPersona getInstance(int idContacto) {
		if (INSTANCE == null) {
			INSTANCE = new VentanaEditarPersona(idContacto);
			return new VentanaEditarPersona(idContacto);
		} else
			return INSTANCE;
	}
	
	private VentanaEditarPersona(int idContacto) {
		super();
		IntermediarioVista.cambiarLookAndFeel(VentanaEditarPersona.class.getName());
		configurarVentana();
		configurarPanel();
		agregarComponentes(idContacto);
		verificarCampos();
	}

	private void agregarComponentes(int idContacto) {
		lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(22, 52, 109, 24);
		panel.add(lblNombre);
		
		lblTelefono = new JLabel("Telefono:");
		lblTelefono.setBounds(22, 96, 97, 14);
		panel.add(lblTelefono);
		
		txtNombre = new JTextField();
		txtNombre.setEnabled(false);
		txtNombre.setBounds(148, 52, 177, 24);
		panel.add(txtNombre);
		txtNombre.setColumns(10);
		
		txtTelefono = new JTextField();
		txtTelefono.setEnabled(false);
		txtTelefono.setColumns(10);
		txtTelefono.setBounds(146, 92, 179, 23);
		panel.add(txtTelefono);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(256, 351, 89, 23);
		panel.add(btnAceptar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(355, 351, 89, 23);
		panel.add(btnCancelar);	
		
		btnCambiarNombre = new JButton("Cambiar");
		btnCambiarNombre.setBounds(355, 53, 97, 23);
		panel.add(btnCambiarNombre);
		
		btnCambiarTelefono = new JButton("Cambiar");
	
		btnCambiarTelefono.setBounds(354, 92, 98, 23);
		panel.add(btnCambiarTelefono);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(22, 131, 97, 14);
		panel.add(lblEmail);
		
		JLabel lblNacimiento = new JLabel("Fecha de Nacimiento: ");
		lblNacimiento.setBounds(22, 171, 109, 14);
		panel.add(lblNacimiento);
		
		JLabel lblTipoContacto = new JLabel("Tipo de contacto:");
		lblTipoContacto.setBounds(22, 216, 97, 14);
		panel.add(lblTipoContacto);
		
		txtEmail = new JTextField();
		txtEmail.setEnabled(false);
		txtEmail.setColumns(10);
		txtEmail.setBounds(146, 127, 177, 22);
		panel.add(txtEmail);
		
		txtFechaNacimiento = new JTextField();
		txtFechaNacimiento.setEnabled(false);
		txtFechaNacimiento.setColumns(10);
		txtFechaNacimiento.setBounds(146, 166, 121, 24);
		panel.add(txtFechaNacimiento);
		
		cBTipoContacto = new JComboBox<String>();
		cBTipoContacto.setBounds(197, 211, 177, 24);
		panel.add(cBTipoContacto);
		
		btnCambiarEmail = new JButton("Cambiar");
		btnCambiarEmail.setBounds(354, 127, 98, 23);
		panel.add(btnCambiarEmail);
		
		btnCambiarNacimiento = new JButton("Cambiar");
		btnCambiarNacimiento.setBounds(354, 167, 98, 23);
		panel.add(btnCambiarNacimiento);
		
		lblFormatoFecha = new JLabel("Formato: YYYY//MM/DD");
		lblFormatoFecha.setEnabled(false);
		lblFormatoFecha.setBounds(146, 151, 148, 14);
		panel.add(lblFormatoFecha);
		
		btnDomicilio = new JButton("Editar");
		btnDomicilio.setBounds(197, 305, 178, 23);
		panel.add(btnDomicilio);
		
		lblDomicilio = new JLabel("Domicilio:");
		lblDomicilio.setBounds(22, 309, 97, 14);
		panel.add(lblDomicilio);
		ImageIcon borrar = new ImageIcon(getClass().getClassLoader().getResource("borrar.png"));
		btnEliminarNacimiento = new JButton("");
		btnEliminarNacimiento.setBounds(282, 166, 46, 24);
		btnEliminarNacimiento.setIcon(borrar);
		panel.add(btnEliminarNacimiento);
		
		btnEliminarDomicilio = new JButton("");
		btnEliminarDomicilio.setBounds(396, 305, 46, 23);
		btnEliminarDomicilio.setIcon(borrar);
		panel.add(btnEliminarDomicilio);
		
		lblContactoPreferente = new JLabel("Medio contacto preferente:");
		lblContactoPreferente.setBounds(22, 262, 150, 14);
		panel.add(lblContactoPreferente);
		
		cBContactoPreferente = new JComboBox<String>();
		cBContactoPreferente.setBounds(197, 257, 177, 24);
		panel.add(cBContactoPreferente);
		
		lblTitulo = new JLabel("Modificación de datos Persona");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTitulo.setBounds(136, 11, 291, 30);
		panel.add(lblTitulo);
	}

	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 473, 385);
		contentPane.add(panel);
		panel.setLayout(null);
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

	private void configurarVentana() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 479, 414);
		setTitle("Editar Persona");
		setResizable(false);
	}
	
	public JButton getBtnAceptar() {
		return btnAceptar;
	}
	
	public JTextField getTxtNombre() {
		return txtNombre;
	}
	
	public JTextField getTxtEmail() {
		return txtEmail;
	}
	
	public JTextField getTxtFechaNacimiento() {
		return txtFechaNacimiento;
	}
	
	public JTextField getTxtTelefono() {
		return txtTelefono;
	}
	
	public JComboBox<String> getComboBoxTipoContacto() {
		return cBTipoContacto;
	}
	public void cerrar() {
		setVisible(false);
		this.dispose();
	}
	public void mostrar() {
		setVisible(true);
	}

	public JButton getBtnEditarNacimiento() {
		return btnCambiarNacimiento;
	}
	
	public JButton getBtnCambiarEmail() {
		return btnCambiarEmail;
	}
	
	public JButton getBtnCambiarTelefono() {
		return btnCambiarTelefono;
	}
	
	public JButton getBtnCambiarNombre() {
		return btnCambiarNombre;
	}
	
	public JButton getBtnDomicilio() {
		return btnDomicilio;
	}

	public JButton getBtnEliminarDomicilio() {
		return btnEliminarDomicilio;
	}
	
	public JButton getBtnEliminarNacimiento() {
		return btnEliminarNacimiento;
	}

	public JComboBox<String> getJComboBoxContactoPreferente() {
		return cBContactoPreferente;
	}

	public void eliminarActionListeners() {
		IntermediarioVista.eliminarListener(btnDomicilio);
		IntermediarioVista.eliminarListener(btnCambiarNacimiento);
		IntermediarioVista.eliminarListener(btnCambiarNombre);
		IntermediarioVista.eliminarListener(btnCambiarEmail);
		IntermediarioVista.eliminarListener(btnCambiarTelefono);
		IntermediarioVista.eliminarListener(btnEliminarNacimiento);
		IntermediarioVista.eliminarListener(btnAceptar);
		IntermediarioVista.eliminarListener(btnCancelar);
		IntermediarioVista.eliminarListener(btnEliminarDomicilio);
	}

	public JButton getBtnCancelar() {
		return btnCancelar;
	}


}