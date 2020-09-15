package presentacion.vista;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;

public class VentanaNacimiento extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JButton btnAgregarNacimiento;
	private JDateChooser dateChooser;
	private static VentanaNacimiento INSTANCE;

	public static VentanaNacimiento getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new VentanaNacimiento();
			return new VentanaNacimiento();
		} else
			return INSTANCE;
	}

	private VentanaNacimiento() {
		super();
		configurarVentana();
		configurarPanel();
		agregarComponentes();
	}

	private void agregarComponentes() {
		dateChooser = new JDateChooser();
		dateChooser.setBounds(10, 36, 269, 20);
		panel.add(dateChooser);
		btnAgregarNacimiento = new JButton("Aceptar");
	
		btnAgregarNacimiento.setBounds(204, 77, 89, 23);
		panel.add(btnAgregarNacimiento);

		JLabel lblNacimiento = new JLabel("Fecha de Nacimiento");
		lblNacimiento.setBounds(10, 11, 113, 14);
		panel.add(lblNacimiento);
	}
	
	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new JPanel();
		panel.setBounds(10, 11, 293, 100);
		contentPane.add(panel);
		panel.setLayout(null);
	}

	private void configurarVentana() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 331, 158);
		this.setVisible(false);
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
		this.setVisible(false);
		
	}
}
