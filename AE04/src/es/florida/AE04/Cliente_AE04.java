package es.florida.AE04;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Cliente_AE04 {

	
	/* Method:	main()
	 * Action:	Arranca el cliente y lanza una petici�n al Servidor a trav�s del host y puerto en el 
	 * 			que est� a la escucha. El servidor le devuelve un objeto que el usuario debe rellenar.
	 * 			Lo devuelve al servidor para que este lo encripte y se lo devuelva; cunado lo vuelve 
	 * 			a recibir completo lo presenta por pantalla
	 * Input:	Host e IP del Servidor. Objetos desde el servidor. Por consola la contrase�a a encriptar.
	 * Output:	Muestra por pantalla la contrase�a en texto plano y encriptada*/
	
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

		//Arranca CLIENTE y manda petici�n a SERVIDOR.
		InetSocketAddress direccion = new InetSocketAddress("localhost", 1234);
		Socket socket = new Socket();
		socket.connect(direccion);
		System.out.println("CLIENTE >>> Arranca cliente. Enviando petici�n al servidor...");
	
		//Recibimos objeto contrase�a del servidor
		ObjectInputStream inObjeto = new ObjectInputStream(socket.getInputStream());
		Password pass = (Password) inObjeto.readObject();
		System.out.println("CLIENTE >>> Recibido objeto de SERVIDOR >>> " + pass.passwordToString());
		
		//Recogemos contrase�a escrita por el usuario:
		Scanner teclado = new Scanner (System.in);
		System.out.print("\nCLIENTE >>> Introduzca una contrase�a: ");
		String texto = teclado.nextLine();
		pass.setTextoPlano(texto);
				
		//Enviamos la contrase�a escrita por usuario a Servidor para que la encripte y complete el objeto
		ObjectOutputStream outObjeto = new ObjectOutputStream(socket.getOutputStream()); 
		outObjeto.writeObject(pass);
		System.out.println("CLIENTE >>> Enviada contrase�a en texto plano a SERVIDOR") ;
			
		//Recibimos objeto completo y lo presentamos
		inObjeto = new ObjectInputStream(socket.getInputStream());
		pass = (Password) inObjeto.readObject();
		Thread.sleep(2000);
		System.out.println("\nCLIENTE >>> Recibido objeto completo de SERVIDOR: \n		Contrase�a:	" + pass.getTextoPlano() + "\n		Encriptado:	" + pass.getEncrypt());
		
		
		//Cerramos todo
		inObjeto.close();
		outObjeto.close();
		socket.close();
		teclado.close();
	}
}
