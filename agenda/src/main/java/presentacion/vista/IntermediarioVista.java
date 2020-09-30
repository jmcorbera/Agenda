package presentacion.vista;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.UIManager;

public class IntermediarioVista {
	
	public static void cambiarLookAndFeel(String nameClass) {
		 try {
	            UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");
	        }
		 catch(Exception e){
			  java.util.logging.Logger.getLogger(nameClass).log(java.util.logging.Level.SEVERE, null, e);
		 } 
	}
	
	public static void actualizarComboBox(JComboBox<String> comboBox, String[] nombres) {
		comboBox.removeAllItems();
		if (nombres != null) {
			comboBox.setEnabled(true);
			setModel(comboBox, nombres);
		}
	}

	public static void setModel(JComboBox<String> comboBox, String[] nombres) {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nombres);
		comboBox.setModel(model);
	}
	
	public static String obtenerNombreSeleccionado(Object seleccionado) {
		return seleccionado != null ? seleccionado.toString() : "";
	}
}
