package es.florida.AE04.Ampliada;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	 * Action:	Instrucciones para ejecutar las tareas del Servidor. 
	 * 			(1) Envía un objeto Password vacio a Cliente para que rellene la contraseña con texto 
	 * 				plano e indique el tipo de cifrado que desea. 
	 * 			(2) Recibe de vuelta el objeto con la contraseña y tipo de cifrado escrito por el usuario.
	 * 			(3) Encripta la contraseña en función del tipo que ha elegido el usuario y la sustituye
	 * 				en el objeto
	 * 			(4) Devuelve el objeto al Cliente para que lo presente por consola.
	 * Input:	Objeto con contraseña en texto plano.
	 * Output:	Objeto vacio para que rellene usuario desde Cliente / Objeto completo con la contraseña
	 * 			encriptada.*/
	@Override
	public void run() {

		try {
			// (1)
			ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream());
			Password contrasenya = new Password("Contraseña", "Encriptado", "Tipo Encriptado");
			outObjeto.writeObject(contrasenya);
			System.err.println("SERVIDOR >>> " + Thread.currentThread().getName() + " >>> Enviado objeto a CLIENTE: "
					+ contrasenya.passwordToString());

			// (2)
			ObjectInputStream inObjeto = new ObjectInputStream(socket.getInputStream());
			Password contrasenyaMod = (Password) inObjeto.readObject();
			System.err.println("SERVIDOR >>> " + Thread.currentThread().getName()
					+ " >>> Recibida contraseña de CLIENTE: " + contrasenyaMod.getTextoPlano());
			System.err.println("SERVIDOR >>> " + Thread.currentThread().getName() + " >>> Encriptando...");
			Thread.sleep(2000);

			// (3)
			String contrasenyaEncriptada = "";
			if (contrasenyaMod.tipo.equals("1")) contrasenyaEncriptada = encriptar(contrasenyaMod.textoPlano);
			else contrasenyaEncriptada = encriptarMD5(contrasenyaMod.textoPlano);

			contrasenyaMod.setEncrypt(contrasenyaEncriptada);
			System.err.println("SERVIDOR >>> " + Thread.currentThread().getName() + " >>> Encriptación finalizada: "
					+ contrasenyaEncriptada);
			Thread.sleep(1000);

			// (4)
			outObjeto = new ObjectOutputStream(socket.getOutputStream());
			outObjeto.writeObject(contrasenyaMod);
			System.err.println("SERVIDOR >>> " + Thread.currentThread().getName()
					+ " >>> Enviado objeto completo a CLIENTE: " + contrasenyaMod.passwordToString());

			inObjeto.close();
			outObjeto.close();
			socket.close();

		} catch (IOException | InterruptedException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/* Method:	encriptar()
	 * Action:	Recibe una contraseña y la encripta sumando un número al código ASCII de cada 
	 * 			'char' que la compone. Si es un caracter no imprimible (de 0 a 31) lo sustituye 
	 * 			por un '*' (DEC: 42) 
	 * Input:	String con la contraseña introducida por le usuario
	 * Output:	String con la contraseña encriptada*/
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
	
	
	/* Method 	encriptarMD5()
	 * Action:	Aplica el algoritmo MD5 a la contraseña introducida por el usuario 
	 * 			y la devuelve encriptada.
	 * Input:	Contraseña introducida por le usauario.
	 * Output:	String con la contraseña encriptada tras pasarla por el algoritmo */
	public static String encriptarMD5(String contrasenya) {

		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(contrasenya.getBytes(), 0, contrasenya.length());

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new BigInteger(1, m.digest()).toString(16);
	}
}
