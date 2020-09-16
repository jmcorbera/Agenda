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
	private JButton btnLocalidad;
	private JButton btnEditarTipo;
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
		tipoContacto.setBounds(122, 177, 89, 20);
		panel.add(tipoContacto);
	}

	public JComboBox<String> getJComboBoxTipoContacto() {
		return tipoContacto;
	}

	private void agregarBotones() {
		btnAgregarPersona = new JButton("Agregar");
		btnAgregarPersona.setBounds(318, 214, 89, 23);
		panel.add(btnAgregarPersona);

		btnEditarTipo = new JButton("Editar valores predeterminados");
		btnEditarTipo.setBounds(221, 177, 183, 21);
		panel.add(btnEditarTipo);

		btnNacimiento = new JButton("Agregar");
		btnNacimiento.setBounds(122, 137, 89, 23);
		panel.add(btnNacimiento);

		btnLocalidad = new JButton("Agregar");
		btnLocalidad.setBounds(318, 137, 89, 23);
		panel.add(btnLocalidad);
	}

	private void agregarTxts() {
		txtNombre = new JTextField();
		txtNombre.setBounds(133, 8, 274, 17);
		panel.add(txtNombre);
		txtNombre.setColumns(10);
		txtTelefono = new JTextField();
		txtTelefono.setBounds(133, 49, 154, 17);
		panel.add(txtTelefono);
		txtTelefono.setColumns(10);
		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(133, 92, 274, 17);
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

		JLabel lblLocalidad = new JLabel("Localidad");
		lblLocalidad.setBounds(262, 141, 46, 14);
		panel.add(lblLocalidad);

		JLabel lblTipoContacto = new JLabel("Tipo de Contacto");
		lblTipoContacto.setBounds(10, 180, 89, 14);
		panel.add(lblTipoContacto);
	}

	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new JPanel();
		panel.setBounds(10, 11, 417, 237);
		contentPane.add(panel);
		panel.setLayout(null);
	}

	private void configurarVentana() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 453, 297);
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

	public JButton getBtnEditarTipo() {
		return btnEditarTipo;
	}

	public void cerrar() {
		this.txtNombre.setText(null);
		this.txtTelefono.setText(null);
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
				char caracter = e.getKeyChar();
				if (!Character.isDigit(caracter)) {
					e.consume(); // ignorar el evento de teclado
				}
			}
		}));
		txtNombre.addKeyListener((new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char caracter = e.getKeyChar();
				if (!Character.isLetter(caracter) && !Character.isSpaceChar(caracter)) {
					e.consume();
				}
			}
		}));
		txtEmail.addKeyListener((new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char caracter = e.getKeyChar();
				if (Character.isUpperCase(caracter) || !Character.isLetterOrDigit(caracter) && caracter != '.' 
						&& caracter !='-' && caracter !='_' && caracter != '@')  {
					e.consume();
				}
			}
		}));
	}
}
