package es.florida.AE04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor_AE04_Hilo implements Runnable {

	BufferedReader br;
	PrintWriter pw;
	Socket socket;

	//Constructor para el hilo
	public Servidor_AE04_Hilo(Socket socket) {
		this.socket = socket;
	}

	
	/* Method:	run()
	 * Action:	instrucciones para ejecutar las tareas del Servidor. Primero env�a un objeto Password 
	 * 			vacio a Cliente para que rellene s�lo la contrase�a con texto plano. Cuando lo recibe
	 * 			de vuelta, la encripta con la funci�n 'encriptar(contrasenya), la sustituye en el 
	 * 			objeto y lo devuelve al Cliente para que la presente por consola.
	 * Input:	Nombre del hilo / Objeto con contrase�a en texto plano, 
	 * Output:	Objeto vacio para que rellene usuario desde Cliente / Objeto completo con la contrase�a
	 * 			encriptada.*/
	@Override
	public void run() {

		try {
		
			String threadName = Servidor_AE04.name;	//Obtenemos nombre del hilo desde clase Servidor_AE04
		
			//Enviamos objeto Password vacio a Cliente para que rellene la contrase�a en texto plano
			ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
			Password contrasenya = new Password("Contrase�a", "Encriptado");
			outObjeto.writeObject(contrasenya);
			System.err.println("SERVIDOR >>> " + threadName + " >>> Enviado objeto a CLIENTE: " + contrasenya.passwordToString());

			//Recibimos contrase�a en texto plano
			ObjectInputStream inObjeto = new ObjectInputStream(socket.getInputStream());	
			Password contrasenyaMod = (Password) inObjeto.readObject();						
			System.err.println("SERVIDOR >>> " + threadName + " >>> Recibida contrase�a de CLIENTE: " + contrasenyaMod.getTextoPlano());
			System.err.println("SERVIDOR >>> " + threadName + " >>> Encriptando...");
			Thread.sleep(2000);
			
			//Encriptamos contrase�a y a�adimos a objeto
			String contrasenyaEncriptada = encriptar(contrasenyaMod.textoPlano);
			contrasenyaMod.setEncrypt(contrasenyaEncriptada);
			System.err.println("SERVIDOR >>> " + threadName + " >>> Encriptaci�n finalizada: " + contrasenyaEncriptada);
			Thread.sleep(1000);
			
			//Enviamos objeto completo a CLIENTE para que lo muestre
			outObjeto = new ObjectOutputStream(socket.getOutputStream());
			outObjeto.writeObject(contrasenyaMod);
			System.err.println(	"SERVIDOR >>> " + threadName + " >>> Enviado objeto completo a CLIENTE: " + contrasenyaMod.passwordToString() );
			
			inObjeto.close();
			outObjeto.close();
			socket.close();
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}

	
	/* Method:	encriptar()
	 * Action:	recibe una contrase�a y la encripta sumando un n�mero al c�digo ASCII de cada 
	 * 			'char' que la compone. Si es un caracter no imprimible (de 0 a 31) lo sustituye 
	 * 			por un '*' (DEC: 42) 
	 * Input:	String con la contrase�a introducida por le usuario
	 * Output:	String con la contrase�a encriptada*/
	public static String encriptar(String contrasenya) {

		ArrayList<String> passEncrypted = new ArrayList<String>();

		int letra = 0;
		for (int i = 0; i < contrasenya.length(); i++) {
			letra = contrasenya.charAt(i) + 1;
			if (letra >= 0 && letra <= 31) letra = 42;
			passEncrypted.add(String.valueOf((char) letra));
		}
		String paswordEncryted = String.join("", passEncrypted);
		return paswordEncryted;
	}

}
