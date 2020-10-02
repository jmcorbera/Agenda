package presentacion.vista.Intermediario;

import java.awt.event.ActionListener;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class IntermediarioVista {
	
	public static void cambiarLookAndFeel(String nameClass) {
		 try {
	            UIManager.setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
	        }
		 catch(Exception e){
			  java.util.logging.Logger.getLogger(nameClass).log(java.util.logging.Level.SEVERE, null, e);
		 } 
	}
	
	public static void actualizarComboBox(JComboBox<String> comboBox, String[] nombres) {
		comboBox.removeAllItems();
		if (nombres != null && nombres.length > 0) {
			comboBox.setEnabled(true);
			setModel(comboBox, nombres);
		}
		else
			comboBox.setEnabled(false);
	}

	public static void setModel(JComboBox<String> comboBox, String[] nombres) {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nombres);
		comboBox.setModel(model);
		comboBox.setSelectedIndex(0);
	}
	
	public static String obtenerNombreSeleccionado(Object seleccionado) {
		return seleccionado != null ? seleccionado.toString() : "";
	}
	
	public static void eliminarListener(JComboBox<String> componente) {
		ActionListener[] listeners;
		listeners = componente.getActionListeners();
		for(int i=0; i<listeners.length ;i++)
			componente.removeActionListener(listeners[i]);
	}
	public static void eliminarListener(JButton componente) {
		ActionListener[] listeners;
		listeners = componente.getActionListeners();
		for(int i=0; i<listeners.length ;i++)
			componente.removeActionListener(listeners[i]);
	}

	public static void eliminarListener(JMenuItem menuItem) {
		ActionListener[] listeners;
		listeners = menuItem.getActionListeners();
		for(int i=0; i<listeners.length ;i++)
			menuItem.removeActionListener(listeners[i]);
	}
	
	public static void mostrarSeleccionadoPrimero(JComboBox<String> cmb, String seleccionado) {
		ComboBoxModel<String> modeloLista = cmb.getModel();
		int largoLista = modeloLista.getSize();
		if (largoLista > 0) {
			String primero = modeloLista.getElementAt(0);
			modeloLista.setSelectedItem(seleccionado);
			@SuppressWarnings("unused")
			String cambiarPrimero = modeloLista.getElementAt(0);
			cambiarPrimero = seleccionado;
			@SuppressWarnings("unused")
			String cambiarUltimo = modeloLista.getElementAt(largoLista);
			cambiarUltimo = primero;
		}
		if(seleccionado == null || seleccionado.isEmpty())
			modeloLista.setSelectedItem(null);
	}
	
	public static void cambiarValores(JTextField textField, JButton btn, String cambiarText) {
		if (textField.isEnabled()) {
			btn.setText("Cambiar");
			textField.setEnabled(false);
			textField.setText(cambiarText);
		} else {
			btn.setText("Cancelar");
			textField.setText("");
			textField.setEnabled(true);
		}
	}

	
}
