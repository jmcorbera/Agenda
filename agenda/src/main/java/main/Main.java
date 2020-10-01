package main;

import javax.swing.JOptionPane;

import modelo.Agenda;
import modelo.DBdata;
import persistencia.conexion.Conexion;
import persistencia.dao.mysql.DAOSQLFactory;
import presentacion.controlador.Controlador;
import presentacion.vista.VentanaLogin;
import presentacion.vista.Vista;


public class Main 
{

	public static void main(String[] args) 
	{	
		VentanaLogin ventanaLogin = new VentanaLogin();
		ventanaLogin.mostrar();
		ventanaLogin.getBtnAceptar().addActionListener(a -> ingresar(ventanaLogin));	
	}

	private static void ingresar(VentanaLogin ventanaLogin) {
		String user = ventanaLogin.getUser();
		String password = ventanaLogin.getPassword();
		if(Conexion.isCorrectUser(user) && Conexion.isCorrectPassword(password)) {
			// inicio carga de BD
			DAOSQLFactory factory = new DAOSQLFactory();
			DBdata.Initialize(factory);
			ventanaLogin.cerrar();
			ventanaLogin = null;
			JOptionPane.showMessageDialog(ventanaLogin, "Conexión exitosa");
			Vista vista = new Vista();
			Agenda modelo = new Agenda(new DAOSQLFactory());
			Controlador controlador = new Controlador(vista, modelo);
			controlador.inicializar();
		}
		else
			JOptionPane.showMessageDialog(ventanaLogin, "Usuario o contraseña incorrectos!");

	}
}
