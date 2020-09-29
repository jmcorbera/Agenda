package presentacion.vista;

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
}
