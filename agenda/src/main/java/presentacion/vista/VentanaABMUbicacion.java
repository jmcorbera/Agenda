package presentacion.vista;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import presentacion.vista.Intermediario.IntermediarioVista;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;

public class VentanaABMUbicacion extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JLabel lblAbmLocalidad;
	private JLabel lblPais;
	private JLabel lblProvincia;
	private JLabel lblLocalidad;
	private JComboBox<String> cmbPais;
	private JComboBox<String> cmbProvincia;
	private JComboBox<String> cmbLocalidad;
	private JButton btnEditarPais;
	private JButton btnAgregarPais;	
	private JButton btnEliminarPais;
	private JButton btnEditarProv;
	private JButton btnAgregarProv;
	private JButton btnEliminarProv;
	private JButton btnEditarLoc;
	private JButton btnAgregarLoc;
	private JButton btnEliminarLoc;
	private JButton btnVolver;
	
	private static VentanaABMUbicacion INSTANCE;

	public static VentanaABMUbicacion getInstance()
	{
		if(INSTANCE == null)
		{
			INSTANCE = new VentanaABMUbicacion(); 	
			return new VentanaABMUbicacion();
		}
		else
			return INSTANCE;
	}

	public VentanaABMUbicacion() 
	{
		super();
		IntermediarioVista.cambiarLookAndFeel(VentanaABMUbicacion.class.getName());
		configurarVentana();
		configurarPanel();	
		agregarComponentes();
	}
	
	private void configurarVentana() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 410, 254);
		this.setVisible(false);
		setTitle("ABM Ubicacion");
		setResizable(false);
	}
	
	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 402, 225);
		contentPane.add(panel);
		panel.setLayout(null);
	}
		
	private void agregarComponentes() {		
		
		this.lblPais = new JLabel("Pais");
		this.lblPais.setBounds(21, 60, 56, 16);
		this.panel.add(lblPais);
		
		this.lblProvincia = new JLabel("Provincia");
		this.lblProvincia.setBounds(21, 110, 56, 16);
		this.panel.add(lblProvincia);
		
		this.lblLocalidad = new JLabel("Localidad");
		this.lblLocalidad.setBounds(21, 160, 56, 16);
		this.panel.add(lblLocalidad);
		
		this.cmbPais = new JComboBox<String>();
		this.cmbPais.setBounds(104, 54, 177, 25);
		this.panel.add(cmbPais);
		
		this.cmbProvincia = new JComboBox<String>();
		this.cmbProvincia.setBounds(104, 104, 177, 25);
		this.panel.add(cmbProvincia);
		
		this.cmbLocalidad = new JComboBox<String>();
		this.cmbLocalidad.setBounds(104, 154, 177, 25);
		this.panel.add(cmbLocalidad);
		
		this.btnEditarPais = new JButton();
		this.btnEditarPais.setBounds(291, 54, 25, 25);
		this.panel.add(btnEditarPais);
				
		this.btnAgregarPais = new JButton();
			this.btnAgregarPais.setBounds(326, 54, 25, 25);				
		this.panel.add(btnAgregarPais);
		
		this.btnEliminarPais = new JButton();
		this.btnEliminarPais.setBounds(361, 54, 25, 25);
		this.panel.add(btnEliminarPais);
		
		this.btnEditarProv = new JButton();
		this.btnEditarProv.setBounds(291, 104, 25, 25);
		this.panel.add(btnEditarProv);
		
		this.btnAgregarProv = new JButton();
		this.btnAgregarProv.setBounds(326, 104, 25, 25);
		this.panel.add(btnAgregarProv);
		
		this.btnEliminarProv = new JButton();
		this.btnEliminarProv.setBounds(361, 104, 25, 25);
		this.panel.add(btnEliminarProv);
		
		this.btnEditarLoc = new JButton();
		this.btnEditarLoc.setBounds(291, 154, 25, 25);
		this.panel.add(btnEditarLoc);
		
		this.btnAgregarLoc = new JButton();
		this.btnAgregarLoc.setBounds(326, 154, 25, 25);
		this.panel.add(btnAgregarLoc);
		
		this.btnEliminarLoc = new JButton();
		this.btnEliminarLoc.setBounds(361, 154, 25, 25);
		this.panel.add(btnEliminarLoc);
		
		this.lblAbmLocalidad = new JLabel("ABM Ubicación");
		this.lblAbmLocalidad.setFont(new Font("Tahoma", Font.BOLD, 20));
		this.lblAbmLocalidad.setBounds(124, 11, 159, 25);
		this.panel.add(lblAbmLocalidad);
		
		btnVolver = new JButton("Volver");
		btnVolver.setBounds(291, 191, 95, 23);
		panel.add(btnVolver);
		
		cargarIconos();
	}
	
	private void cargarIconos() {
		this.btnEditarPais.setIcon(new ImageIcon(getClass().getClassLoader().getResource("editar.png")));
		this.btnEditarProv.setIcon(new ImageIcon(getClass().getClassLoader().getResource("editar.png")));
		this.btnEditarLoc.setIcon(new ImageIcon(getClass().getClassLoader().getResource("editar.png")));
		
		this.btnAgregarPais.setIcon(new ImageIcon(getClass().getClassLoader().getResource("guardar.png")));
		this.btnAgregarProv.setIcon(new ImageIcon(getClass().getClassLoader().getResource("guardar.png")));
		this.btnAgregarLoc.setIcon(new ImageIcon(getClass().getClassLoader().getResource("guardar.png")));
		
		this.btnEliminarPais.setIcon(new ImageIcon(getClass().getClassLoader().getResource("borrar.png")));
		this.btnEliminarProv.setIcon(new ImageIcon(getClass().getClassLoader().getResource("borrar.png")));
		this.btnEliminarLoc.setIcon(new ImageIcon(getClass().getClassLoader().getResource("borrar.png")));		
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
		
	public JButton getBtnAgregarPais() {
		return btnAgregarPais;
	}

	public JButton getBtnEditarPais() {
		return btnEditarPais;
	}
	
	public JButton getBtnEliminarPais() {
		return btnEliminarPais;
	}
	
	public JButton getBtnVolver() {
		return btnVolver;
	}
	
	public JButton getBtnAgregarProvincia() {
		return btnAgregarProv;
	}
	
	public JButton getBtnEditarProvincia() {
		return btnEditarProv;
	}
	
	public JButton getBtnEliminarProvincia() {
		return btnEliminarProv;
	}

	public JButton getBtnAgregarLocalidad() {
		return btnAgregarLoc;
	}

	public JButton getBtnEditarLocalidad() {
		return btnEditarLoc;
	}

	public JButton getBtnEliminarLocalidad() {
		return btnEliminarLoc;
	}
	
	public JLabel getLblABMLocalidad() {
		return lblAbmLocalidad;
	}
	
	public void cerrar() {
		setVisible(false);
		dispose();
	}

	public void eliminarActionListeners() {
		IntermediarioVista.eliminarListener(btnAgregarLoc);
		IntermediarioVista.eliminarListener(btnAgregarPais);
		IntermediarioVista.eliminarListener(btnAgregarProv);
		IntermediarioVista.eliminarListener(btnEditarPais);
		IntermediarioVista.eliminarListener(btnEditarProv);
		IntermediarioVista.eliminarListener(btnEditarLoc);
		IntermediarioVista.eliminarListener(btnEliminarPais);
		IntermediarioVista.eliminarListener(btnEliminarProv);
		IntermediarioVista.eliminarListener(btnEliminarLoc);
		IntermediarioVista.eliminarListener(btnVolver);
		IntermediarioVista.eliminarListener(cmbPais);
		IntermediarioVista.eliminarListener(cmbProvincia);

	}

	public void deshabilitarDependientes() {
		cmbProvincia.setEnabled(false);
		cmbLocalidad.setEnabled(false);
	}
}
