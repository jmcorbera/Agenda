package presentacion.vista;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import persistencia.conexion.Conexion;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class VentanaLogin
{
	private JFrame frame;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JTextField txtUser;
	private JPasswordField txtPassword;

	public VentanaLogin() 
	{
		super();
		initialize();
	}


	private void initialize() 
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 334, 212);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 318, 172);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cerrar();
			}
		});
		btnCancelar.setBounds(219, 133, 89, 23);
		panel.add(btnCancelar);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(120, 133, 89, 23);
		panel.add(btnAceptar);
		
		txtUser = new JTextField();
		txtUser.setBounds(101, 60, 207, 20);
		panel.add(txtUser);
		txtUser.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(101, 102, 207, 23);
		panel.add(txtPassword);
		
		JLabel lblUser = new JLabel("Usuario:");
		lblUser.setBounds(10, 63, 81, 14);
		panel.add(lblUser);
		
		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setBounds(10, 105, 81, 14);
		panel.add(lblPassword);
		
		JLabel lblIngreso = new JLabel("Ingrese los siguientes datos para continuar");
		lblIngreso.setVerticalAlignment(SwingConstants.TOP);
		lblIngreso.setBounds(10, 27, 298, 14);
		panel.add(lblIngreso);
	
	}
	
	public void show()
	{
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.addWindowListener(new WindowAdapter() 
		{
			@Override
		    public void windowClosing(WindowEvent e) {
		        int confirm = JOptionPane.showOptionDialog(
		             null, "Estas seguro que quieres salir de la Agenda?", 
		             "Confirmación", JOptionPane.YES_NO_OPTION,
		             JOptionPane.QUESTION_MESSAGE, null, null, null);
		        if (confirm == 0) {
		        	Conexion.getConexion().cerrarConexion();
		           System.exit(0);
		        }
		    }
		});
		this.frame.setVisible(true);
	}
	
	public JButton getBtnAceptar() 
	{
		return btnAceptar;
	}

	public JButton getBtnCancelar() 
	{
		return btnCancelar;
	}
	
	public String getUser() {
		return txtUser.getText();
	}
	
	public String getPassword() {
		return txtPassword != null ? new String(txtPassword.getPassword()) : "";
	}


	public JFrame getFrame() {
		return frame;
	}


	public void cerrar() {
		frame.setVisible(false);
		frame.dispose();
	}
}
