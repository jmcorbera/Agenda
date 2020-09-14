package presentacion.vista;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class VentanaPersona extends JFrame 
{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JTextField txtNombre;
	private JTextField txtTelefono;
	private JTextField txtEmail;
	private JButton btnAgregarPersona;
	private JButton btnNacimiento;
	private JButton btnLocalidad;
	private static VentanaPersona INSTANCE;
	
	
	public static VentanaPersona getInstance()
	{
		if(INSTANCE == null)
		{
			INSTANCE = new VentanaPersona(); 	
			return new VentanaPersona();
		}
		else
			return INSTANCE;
	}

	private VentanaPersona() 
	{
		super();
		configurarVentana();
		configurarPanel();
		agregarLabels();
		agregarTxts();
		agregarBotones();
		
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Amigo", "Familia", "Trabajo"}));
		comboBox.setBounds(133, 242, 89, 20);
		panel.add(comboBox);
		
		
	}

	private void agregarBotones() {
		btnAgregarPersona = new JButton("Agregar");
		btnAgregarPersona.setBounds(212, 273, 89, 23);
		panel.add(btnAgregarPersona);
		
	
		
		btnNacimiento = new JButton("Agregar");
		
		btnNacimiento.setBounds(133, 137, 89, 23);
		panel.add(btnNacimiento);
		
		btnLocalidad = new JButton("Agregar");
		btnLocalidad.setBounds(133, 186, 89, 23);
		panel.add(btnLocalidad);
	}

	private void agregarTxts() {
		txtNombre = new JTextField();
		txtNombre.setBounds(133, 8, 164, 20);
		panel.add(txtNombre);
		txtNombre.setColumns(10);
		txtTelefono = new JTextField();
		txtTelefono.setBounds(133, 49, 164, 20);
		panel.add(txtTelefono);
		txtTelefono.setColumns(10);
		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(133, 92, 164, 20);
		panel.add(txtEmail);
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
		lblLocalidad.setBounds(10, 190, 46, 14);
		panel.add(lblLocalidad);
		
		JLabel lblTipoContacto = new JLabel("Tipo de Contacto");
		lblTipoContacto.setBounds(10, 245, 89, 14);
		panel.add(lblTipoContacto);
	}

	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(10, 11, 311, 301);
		contentPane.add(panel);
		panel.setLayout(null);
	}

	private void configurarVentana() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 342, 358);
		this.setVisible(false);
	}
	
	public void mostrarVentana()
	{
		this.setVisible(true);
	}
	
	public JTextField getTxtNombre() 
	{
		return txtNombre;
	}

	public JTextField getTxtTelefono() 
	{
		return txtTelefono;
	}

	public JButton getBtnAgregarPersona() 
	{
		return btnAgregarPersona;
	}

	public void cerrar()
	{
		this.txtNombre.setText(null);
		this.txtTelefono.setText(null);
		this.dispose();
	}

	public JButton getBtnNacimiento() {
		return btnNacimiento;
	}
}

