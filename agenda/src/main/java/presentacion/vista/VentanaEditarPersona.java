package presentacion.vista;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

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
	private JButton btnEliminarTipoContacto;
	private JButton btnEliminarDomicilio;
	
	public static VentanaEditarPersona getInstance(int idContacto) {
		if (INSTANCE == null) {
			INSTANCE = new VentanaEditarPersona(idContacto);
			return new VentanaEditarPersona(idContacto);
		} else
			return INSTANCE;
	}
	
	private VentanaEditarPersona(int idContacto) {
		super();
		configurarVentana();
		configurarPanel();
		agregarComponentes(idContacto);
		verificarCampos();
	}

	private void agregarComponentes(int idContacto) {
	
		lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(10, 11, 109, 24);
		panel.add(lblNombre);
		
		lblTelefono = new JLabel("Telefono:");
		lblTelefono.setBounds(10, 57, 97, 14);
		panel.add(lblTelefono);
		
		txtNombre = new JTextField();
		txtNombre.setEnabled(false);
		txtNombre.setBounds(119, 11, 177, 24);
		panel.add(txtNombre);
		txtNombre.setColumns(10);
		
		txtTelefono = new JTextField();
		txtTelefono.setEnabled(false);
		txtTelefono.setColumns(10);
		txtTelefono.setBounds(117, 53, 179, 23);
		panel.add(txtTelefono);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(227, 256, 89, 23);
		panel.add(btnAceptar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancelar.setBounds(329, 256, 89, 23);
		panel.add(btnCancelar);	
		
		btnCambiarNombre = new JButton("Cambiar");
		btnCambiarNombre.setBounds(321, 12, 97, 23);
		panel.add(btnCambiarNombre);
		
		btnCambiarTelefono = new JButton("Cambiar");
	
		btnCambiarTelefono.setBounds(320, 53, 98, 23);
		panel.add(btnCambiarTelefono);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(10, 102, 97, 14);
		panel.add(lblEmail);
		
		JLabel lblNacimiento = new JLabel("Fecha de Nacimiento: ");
		lblNacimiento.setBounds(10, 146, 109, 14);
		panel.add(lblNacimiento);
		
		JLabel lblTipoContacto = new JLabel("Tipo de contacto:");
		lblTipoContacto.setBounds(10, 185, 97, 14);
		panel.add(lblTipoContacto);
		
		txtEmail = new JTextField();
		txtEmail.setEnabled(false);
		txtEmail.setColumns(10);
		txtEmail.setBounds(119, 98, 177, 22);
		panel.add(txtEmail);
		
		txtFechaNacimiento = new JTextField();
		txtFechaNacimiento.setEnabled(false);
		txtFechaNacimiento.setColumns(10);
		txtFechaNacimiento.setBounds(119, 141, 121, 24);
		panel.add(txtFechaNacimiento);
		
		cBTipoContacto = new JComboBox<String>();
		cBTipoContacto.setBounds(119, 180, 177, 24);
		panel.add(cBTipoContacto);
		
		btnCambiarEmail = new JButton("Cambiar");
		btnCambiarEmail.setBounds(320, 98, 98, 23);
		panel.add(btnCambiarEmail);
		
		btnCambiarNacimiento = new JButton("Cambiar");
		btnCambiarNacimiento.setBounds(320, 141, 98, 23);
		panel.add(btnCambiarNacimiento);
		
		lblFormatoFecha = new JLabel("Formato: YYYY//MM/DD");
		lblFormatoFecha.setEnabled(false);
		lblFormatoFecha.setBounds(119, 128, 148, 14);
		panel.add(lblFormatoFecha);
		
		btnDomicilio = new JButton("Editar");
		btnDomicilio.setBounds(118, 215, 178, 23);
		panel.add(btnDomicilio);
		
		lblDomicilio = new JLabel("Domicilio:");
		lblDomicilio.setBounds(10, 219, 97, 14);
		panel.add(lblDomicilio);
		ImageIcon borrar = new ImageIcon("./imagenes/borrar.png");
		btnEliminarNacimiento = new JButton("");
		btnEliminarNacimiento.setBounds(250, 141, 46, 24);
		btnEliminarNacimiento.setIcon(borrar);
		panel.add(btnEliminarNacimiento);
		
		btnEliminarTipoContacto = new JButton("");
		btnEliminarTipoContacto.setBounds(321, 181, 46, 23);
		btnEliminarTipoContacto.setIcon(borrar);
		panel.add(btnEliminarTipoContacto);
		
		btnEliminarDomicilio = new JButton("");
		btnEliminarDomicilio.setBounds(321, 215, 46, 23);
		btnEliminarDomicilio.setIcon(borrar);
		panel.add(btnEliminarDomicilio);
	}

	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(10, 11, 428, 287);
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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 464, 348);
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
	
	public JButton getBtnEliminarTipoContacto() {
		return btnEliminarTipoContacto;
	}
}