package presentacion.vista;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;

public class VentanaEditarContactoOPais extends JFrame {
	private static final long serialVersionUID = 1L;
	private JLabel lblNombreAnterior;
	private JLabel lblNombreNuevo;
	private JButton btnAceptar;
	private JPanel contentPane;
	private JTextField txtNombreAnterior;
	private JTextField txtNombreNuevo;
	private JPanel panel;
	private JButton btnCancelar;

	public VentanaEditarContactoOPais(String contactoAEditar) {
		super();
		IntermediarioVista.cambiarLookAndFeel(VentanaEditarContactoOPais.class.getName());
		configurarVentana();
		configurarPanel();
		agregarComponentes(contactoAEditar);
		verificarCampos();
	}

	private void agregarComponentes(String contactoAEditar) {
		lblNombreAnterior = new JLabel("Nombre a editar: ");
		lblNombreAnterior.setBounds(10, 11, 109, 24);
		panel.add(lblNombreAnterior);
		
		lblNombreNuevo = new JLabel("Nombre nuevo: ");
		lblNombreNuevo.setBounds(10, 53, 97, 14);
		panel.add(lblNombreNuevo);
		
		txtNombreAnterior = new JTextField();
		txtNombreAnterior.setEditable(false);
		txtNombreAnterior.setText(contactoAEditar);
		txtNombreAnterior.setBounds(167, 11, 165, 24);
		panel.add(txtNombreAnterior);
		txtNombreAnterior.setColumns(10);
		
		txtNombreNuevo = new JTextField();
		txtNombreNuevo.setColumns(10);
		txtNombreNuevo.setBounds(167, 49, 165, 22);
		panel.add(txtNombreNuevo);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(144, 82, 89, 23);
		panel.add(btnAceptar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(243, 82, 89, 23);
		panel.add(btnCancelar);	
	}

	private void configurarPanel() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(10, 11, 342, 112);
		contentPane.add(panel);
		panel.setLayout(null);
	}

	private void configurarVentana() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 368, 161);
		setTitle("Editar");
		setResizable(false);
	}
	
	private void verificarCampos() {
		txtNombreNuevo.addKeyListener((new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				ValidadorTeclado.aceptarLetrasYEspacios(e);
			}
		}));
	}
	public JTextField getTxtNuevo() {
		return txtNombreNuevo;
	}
	public JButton getBtnAceptar() {
		return btnAceptar;
	}
	
	public JButton getBtnCancelar() {
		return btnCancelar;
	}
	
	public JTextField getTxtNombreAnterior() {
		return txtNombreAnterior;
	}
	
	public void cerrar() {
		setVisible(false);
	}
	public void mostrar() {
		setVisible(true);
	}
	
}