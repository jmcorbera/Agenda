package presentacion.vista;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dto.PersonaDTO;

import javax.swing.JButton;

import persistencia.conexion.Conexion;
import presentacion.vista.Intermediario.IntermediarioVista;

public class Vista
{
	private JFrame frmAgenda;
	private JTable tablaPersonas;
	private JButton btnAgregar;
	private JButton btnBorrar;
	private JButton btnReporte;
	private JButton btnEditar;
	private JMenuItem menuItemLocalidad;
	private JMenuItem menuItemTipoContacto;
	private JMenuItem menuItemConfig;
	private DefaultTableModel modelPersonas;
	private  String[] nombreColumnas = {"Nombre y apellido","Telefono","Email"};
	
	public Vista() 
	{
		super();
		IntermediarioVista.cambiarLookAndFeel(Vista.class.getName());
		initialize();
	}


	private void initialize() 
	{
		configurarVentana();
		agregarComponentes();
	}


	private void agregarComponentes() {
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 489, 260);
		frmAgenda.getContentPane().add(panel);
		panel.setLayout(null);
		
		JScrollPane spPersonas = new JScrollPane();
		spPersonas.setBounds(8, 30, 471, 182);
		panel.add(spPersonas);
		
		modelPersonas = new DefaultTableModel(null,nombreColumnas);
		tablaPersonas = new JTable(modelPersonas);
		tablaPersonas.setDefaultEditor(Object.class, null);
		tablaPersonas.getColumnModel().getColumn(0).setPreferredWidth(103);
		tablaPersonas.getColumnModel().getColumn(0).setResizable(false);
		tablaPersonas.getColumnModel().getColumn(1).setPreferredWidth(100);
		tablaPersonas.getColumnModel().getColumn(1).setResizable(false);
		tablaPersonas.getColumnModel().getColumn(2).setPreferredWidth(97);
		tablaPersonas.getColumnModel().getColumn(2).setResizable(false);
		
		spPersonas.setViewportView(tablaPersonas);
		
		btnAgregar = new JButton("Agregar");
		btnAgregar.setBounds(8, 223, 112, 25);
		panel.add(btnAgregar);
		
		btnEditar = new JButton("Editar");
		btnEditar.setBounds(130, 223, 112, 25);
		panel.add(btnEditar);
		
		btnBorrar = new JButton("Borrar");
		btnBorrar.setBounds(249, 223, 112, 25);
		panel.add(btnBorrar);
		
		btnReporte = new JButton("Reporte");
		btnReporte.setBounds(367, 223, 112, 25);
		panel.add(btnReporte);
		
		// Agrega barra Menu
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 489, 25);
		panel.add(menuBar);
		
		// Agrega Menu
		JMenu menu = new JMenu("ABM");
		menu.setBackground(Color.LIGHT_GRAY);
		menuBar.add(menu);
		
		// Agrega Item Menu
		menuItemLocalidad = new JMenuItem("Localidad");
		menu.add(menuItemLocalidad);
		
		// Agrega Item Menu
		menuItemTipoContacto = new JMenuItem("Tipo Contacto");
		menu.add(menuItemTipoContacto);
		
		JMenu menuConn = new JMenu("Conexion");
		menuBar.add(menuConn);
		
		// Agrega Item MenuConn
		menuItemConfig = new JMenuItem("Configuracion");
		menuConn.add(menuItemConfig);
	}


	private void configurarVentana() {
		frmAgenda = new JFrame();
		frmAgenda.setTitle("Agenda");
		frmAgenda.setVisible(false);
		frmAgenda.setResizable(false);
		frmAgenda.setBounds(100, 100, 495, 289);
		frmAgenda.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAgenda.getContentPane().setLayout(null);
	}
	
	public void show()
	{
		this.frmAgenda.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frmAgenda.addWindowListener(new WindowAdapter() 
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
		this.frmAgenda.setVisible(true);
	}
	
	public JButton getBtnAgregar() 
	{
		return btnAgregar;
	}

	public JButton getBtnBorrar() 
	{
		return btnBorrar;
	}
	
	public JButton getBtnReporte() 
	{
		return btnReporte;
	}
	
	public JButton getBtnEditar() {
		return btnEditar;
	}
	
	public JMenuItem getMenuItemLocalidad() {
		return menuItemLocalidad;
	}
	
	public JMenuItem getMenuItemTipoContacto() {
		return menuItemTipoContacto;
	}
	
	public JMenuItem getMenuConexion() {
		return menuItemConfig;
	}
	
	public DefaultTableModel getModelPersonas() 
	{
		return modelPersonas;
	}
	
	public JTable getTablaPersonas()
	{
		return tablaPersonas;
	}

	public String[] getNombreColumnas() 
	{
		return nombreColumnas;
	}

	public void llenarTabla(List<PersonaDTO> personasEnTabla) {
		this.getModelPersonas().setRowCount(0); //Para vaciar la tabla
		this.getModelPersonas().setColumnCount(0);
		this.getModelPersonas().setColumnIdentifiers(this.getNombreColumnas());

		for (PersonaDTO p : personasEnTabla)
		{
			String nombre = p.getNombre();
			String tel = p.getTelefono();
			String email = p.getEmail();
			Object[] fila = {nombre, tel, email};
			this.getModelPersonas().addRow(fila);
		}
	}

	public void eliminarActionListeners() {
		IntermediarioVista.eliminarListener(btnAgregar);
		IntermediarioVista.eliminarListener(btnReporte);
		IntermediarioVista.eliminarListener(btnBorrar);
		IntermediarioVista.eliminarListener(btnEditar);
		IntermediarioVista.eliminarListener(menuItemLocalidad);
		IntermediarioVista.eliminarListener(menuItemTipoContacto);
	}


	public void ocultar() {
		frmAgenda.setVisible(false);
		frmAgenda.dispose();
	}


	
}
