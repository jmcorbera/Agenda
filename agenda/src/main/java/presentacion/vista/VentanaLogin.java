package presentacion.vista;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import presentacion.vista.Intermediario.IntermediarioVista;
import java.awt.Font;

public class VentanaLogin extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JTextField txtIP;
	private JTextField txtPort;
	private JTextField txtUser;
	private JPasswordField txtPassword;
	private JLabel lblMensaje;
	
	public VentanaLogin () 
	{
		super();
		IntermediarioVista.cambiarLookAndFeel(VentanaLogin.class.getName());
		initialize();
	}

	private void initialize() 
	{
		setTitle("Login");
		setResizable(false);
		setBounds(100, 100, 326, 264);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 318, 235);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cerrar();
			}
		});
		btnCancelar.setBounds(219, 203, 89, 23);
		panel.add(btnCancelar);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(113, 203, 89, 23);
		panel.add(btnAceptar);
		
		txtIP = new JTextField();
		txtIP.setBounds(101, 67, 207, 23);
		panel.add(txtIP);
		txtIP.setColumns(10);
		
		txtPort = new JTextField();
		txtPort.setBounds(101, 101, 207, 23);
		panel.add(txtPort);
		
		JLabel lblIP = new JLabel("IP:");
		lblIP.setBounds(10, 71, 81, 14);
		panel.add(lblIP);
		
		JLabel lblPort = new JLabel("Puerto:");
		lblPort.setBounds(10, 108, 89, 14);
		panel.add(lblPort);
		
		JLabel lblTitulo = new JLabel("Conexion");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitulo.setVerticalAlignment(SwingConstants.TOP);
		lblTitulo.setBounds(124, 11, 124, 30);
		panel.add(lblTitulo);
		
		lblMensaje = new JLabel("Ingrese los datos de conexion para continuar");
		lblMensaje.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblMensaje.setBounds(10, 37, 298, 23);
		panel.add(lblMensaje);
		
		JLabel lblUser = new JLabel("Usuario:");
		lblUser.setBounds(10, 139, 81, 14);
		panel.add(lblUser);
		
		txtUser = new JTextField();
		txtUser.setColumns(10);
		txtUser.setBounds(101, 135, 207, 23);
		panel.add(txtUser);
		
		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setBounds(10, 176, 89, 14);
		panel.add(lblPassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(101, 169, 207, 23);
		panel.add(txtPassword);
	
	}
	
	public void mostrar()
	{
		setVisible(true);
	}
	
	public JButton getBtnAceptar() 
	{
		return btnAceptar;
	}

	public JButton getBtnCancelar() 
	{
		return btnCancelar;
	}
	
	public String getIp() {
		return txtIP.getText();
	}
	
	public String getPort() {
		return txtPort.getText();
	}
	
	public String getUser() {
		return txtUser.getText();
	}
	
	public String getPassword() {
		return txtPassword != null ? new String(txtPassword.getPassword()) : "";
	}
	
	public void setUser(String user) {
		txtUser.setText(user);
	}
	
	public void setPassword(String pass) {
		txtPassword.setText(pass);
	}

	public void setPort(String port) {
		txtPort.setText(port);
	}
	
	public void setIp(String ip) {
		txtIP.setText(ip);
	}

	public void cerrar() {
		dispose();
		setVisible(false);
	}

	public void borrarListeners() {
		IntermediarioVista.eliminarListener(btnAceptar);	
	}
}
