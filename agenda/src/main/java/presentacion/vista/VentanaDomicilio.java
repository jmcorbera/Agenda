package presentacion.vista;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public class VentanaDomicilio extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JLabel lblDomicilio;
	private JLabel lblPais;
	private JLabel lblProvincia;
	private JLabel lblLocalidad;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private JButton btnCambiar;
	private JButton btnCambiarCalle;
	private JButton btnCambiarAltura;
	private JButton btnCambiarPiso;
	private JComboBox<String> cmbPais;
	private JComboBox<String> cmbProvincia;
	private JComboBox<String> cmbLocalidad;
	private JTextField txtCalle;
	private JTextField txtAltura;
	private JTextField txtPiso;
	
	public VentanaDomicilio() 
	{
		super();
		configurarVentana();
		configurarPanel();	
		agregarComponentes();
		verificarCampos();
	}
	
	private void configurarVentana() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 597, 312);
		this.setVisible(false);
	}
	
	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(10, 11, 561, 255);
		contentPane.add(panel);
		panel.setLayout(null);
	}
		
	private void agregarComponentes() {		
		this.lblPais = new JLabel("Pais");
		this.lblPais.setBounds(38, 77, 56, 16);
		this.panel.add(lblPais);
		
		this.lblProvincia = new JLabel("Provincia");
		this.lblProvincia.setBounds(38, 127, 56, 16);
		this.panel.add(lblProvincia);
		
		this.lblLocalidad = new JLabel("Localidad");
		this.lblLocalidad.setBounds(38, 177, 56, 16);
		this.panel.add(lblLocalidad);
		
		this.cmbPais = new JComboBox<String>();
		this.cmbPais.setBounds(104, 70, 177, 25);
		this.panel.add(cmbPais);
		
		this.cmbProvincia = new JComboBox<String>();
		this.cmbProvincia.setBounds(104, 120, 177, 25);
		this.panel.add(cmbProvincia);
		
		this.cmbLocalidad = new JComboBox<String>();
		this.cmbLocalidad.setBounds(104, 170, 177, 25);
		this.panel.add(cmbLocalidad);
		
		this.lblDomicilio = new JLabel("Datos del domicilio:");
		this.lblDomicilio.setFont(new Font("Tahoma", Font.BOLD, 20));
		this.lblDomicilio.setBounds(98, 11, 236, 25);
		this.panel.add(lblDomicilio);
		
		txtCalle = new JTextField();
		txtCalle.setBounds(360, 70, 187, 25);
		panel.add(txtCalle);
		txtCalle.setColumns(10);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(459, 221, 89, 23);
		panel.add(btnCancelar);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(360, 221, 89, 23);
		panel.add(btnAceptar);

		btnCambiar = new JButton("Cambiar");
		btnCambiar.setBounds(100, 221, 177, 23);
		panel.add(btnCambiar);
		
		txtAltura = new JTextField();
		txtAltura.setColumns(10);
		txtAltura.setBounds(360, 120, 187, 25);
		panel.add(txtAltura);
		
		txtPiso = new JTextField();
		txtPiso.setColumns(10);
		txtPiso.setBounds(360, 170, 187, 25);
		panel.add(txtPiso);
		
		JLabel lblCalle = new JLabel("Calle:");
		lblCalle.setBounds(311, 78, 46, 14);
		panel.add(lblCalle);
		
		JLabel lblPiso = new JLabel("Piso:");
		lblPiso.setBounds(318, 178, 46, 14);
		panel.add(lblPiso);
		
		JLabel lblAltura = new JLabel("Altura");
		lblAltura.setBounds(318, 127, 46, 14);
		panel.add(lblAltura);
		
		btnCambiarCalle = new JButton("Cambiar");
		btnCambiarCalle.setBounds(458, 45, 89, 23);
		panel.add(btnCambiarCalle);
		
		btnCambiarAltura = new JButton("Cambiar");
		btnCambiarAltura.setBounds(459, 95, 89, 23);
		panel.add(btnCambiarAltura);
		
		btnCambiarPiso = new JButton("Cambiar");
		btnCambiarPiso.setBounds(459, 145, 89, 23);
		panel.add(btnCambiarPiso);
	}
	
	private void verificarCampos() {
		txtCalle.addKeyListener((new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				ValidadorTeclado.aceptarLetrasYEspacios(e);
			}
		}));
		txtAltura.addKeyListener((new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				ValidadorTeclado.aceptarSoloNumeros(e);
			}
		}));
		txtPiso.addKeyListener((new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				ValidadorTeclado.aceptarLetrasNumerosYEspacios(e);
			}
		}));
	}
	
	public void mostrarVentana() {
		this.setVisible(true);
	}
	
	public void limpiarCombos() {
		limpiarComboPais();
		limpiarComboProvincia();
		limpiarComboLocalidad();
	}
	
	public void limpiarComboPais() {
		this.cmbPais.setSelectedIndex(-1);
	}
	
	public void limpiarComboProvincia() {
		this.cmbProvincia.setSelectedIndex(-1);
	}
	
	public void limpiarComboLocalidad() {
		this.cmbLocalidad.setSelectedIndex(-1);
	}
	
	public JComboBox<String> getComboBoxPais() {
		return this.cmbPais;
	}
	
	public JComboBox<String> getComboBoxProvincia() {
		return this.cmbProvincia;
	}
	
	public JComboBox<String> getComboBoxLocalidad() {
		return this.cmbLocalidad;
	}
		
	public JButton getBtnAceptar() {
		return btnAceptar;
	}
	
	public JButton getBtnCancelar() {
		return btnCancelar;
	}
	
	public JButton getBtnCambiar() {
		return btnCambiar;
	}
	
	public JButton getBtnCambiarCalle() {
		return btnCambiarCalle;
	}
	
	public JButton getBtnCambiarAltura() {
		return btnCambiarAltura;
	}
	
	public JButton getBtnCambiarPiso() {
		return btnCambiarPiso;
	}
	
	public JTextField getTxtCalle() {
		return txtCalle;
	}
	
	public JTextField getTxtAltura() {
		return txtAltura;
	}
	
	public JTextField getTxtPiso() {
		return txtPiso;
	}
	
	public void setTxtCalle(String calle) {
		txtCalle.setText(calle);
	}
	
	public void setTxtAltura(String altura) {
		txtAltura.setText(altura);
	}
	
	public void setTxtPiso(String piso) {
		txtPiso.setText(piso);
	}
	
	public void cerrar() {
		setVisible(false);
		this.dispose();
	}
	
	public void ocultarBotonesCambiar() {
		btnCambiarCalle.setVisible(false);
		btnCambiarAltura.setVisible(false);
		btnCambiarPiso.setVisible(false);
		btnCambiar.setVisible(false);	
	}
	
	public void mostrarBotonesCambiar() {
		btnCambiarCalle.setVisible(true);
		btnCambiarAltura.setVisible(true);
		btnCambiarPiso.setVisible(true);
		btnCambiar.setVisible(true);	
	}
	
	public void deshabilitarTxts() {
		txtCalle.setEnabled(false);
		txtAltura.setEnabled(false);
		txtPiso.setEnabled(false);
	}
	
	public void habilitarTxts() {
		txtCalle.setEnabled(true);
		txtAltura.setEnabled(true);
		txtPiso.setEnabled(true);
	}
	
	public void establecerTextoCambiar() {
		btnCambiarCalle.setText("Cambiar");
		btnCambiarAltura.setText("Cambiar");
		btnCambiarPiso.setText("Cambiar");
		btnCambiar.setText("Cambiar");
	}
}
