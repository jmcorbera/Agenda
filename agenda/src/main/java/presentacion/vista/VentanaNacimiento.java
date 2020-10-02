package presentacion.vista;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;

import presentacion.vista.Intermediario.IntermediarioVista;
import java.awt.Font;

public class VentanaNacimiento extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JButton btnAgregarNacimiento;
	private JDateChooser dateChooser;
	private JLabel lblAgregarNacimiento;
	
	public VentanaNacimiento() {
		super();
		IntermediarioVista.cambiarLookAndFeel(VentanaNacimiento.class.getName());
		configurarVentana();
		configurarPanel();
		agregarComponentes();
	}

	private void agregarComponentes() {
		dateChooser = new JDateChooser();
		dateChooser.setBounds(10, 75, 213, 20);
		panel.add(dateChooser);
		btnAgregarNacimiento = new JButton("Aceptar");
	
		btnAgregarNacimiento.setBounds(134, 106, 89, 23);
		panel.add(btnAgregarNacimiento);

		JLabel lblNacimiento = new JLabel("Elija la fecha de cumpleaños:");
		lblNacimiento.setBounds(10, 55, 213, 14);
		panel.add(lblNacimiento);
		
		lblAgregarNacimiento = new JLabel("Ingresar cumpleaños");
		lblAgregarNacimiento.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblAgregarNacimiento.setBounds(39, 11, 193, 33);
		panel.add(lblAgregarNacimiento);
	}
	
	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new JPanel();
		panel.setBounds(0, 0, 232, 140);
		contentPane.add(panel);
		panel.setLayout(null);
	}

	private void configurarVentana() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 238, 169);
		this.setVisible(false);
		setTitle("Cumpleaños");
		setResizable(false);
	}

	public void mostrarVentana() {
		this.setVisible(true);
	}

	public JButton getBtnAgregarNacimiento() {
		return btnAgregarNacimiento;
	}

	public JDateChooser getFecha() {
		return dateChooser;
	}

	public void cerrar() {
		this.dispose();
		
	}
}
