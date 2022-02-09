package es.florida.AE05_ServiciosRed;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.sun.net.httpserver.HttpServer;

public class ServidorHTTP {
	
	/*Function: main()
	 * ACTION:	monta y lanza el servidor
	 * INPUT:	host y puerto
	 * OUTPUT:	Mensaje de confirmación por consola. Arranca Servidor	*/
	public static void main(String[] args) throws IOException {
				
		String host = "localhost"; // 127.0.0.1
		int puerto = 7777;
		InetSocketAddress direccionTCPIP = new InetSocketAddress(host, puerto);

		int backlog = 0; 
		HttpServer servidor = HttpServer.create(direccionTCPIP, backlog); 

		GestorHTTP gestorHTTP = new GestorHTTP();
		String rutaRespuesta = "/estufa";
		servidor.createContext(rutaRespuesta, gestorHTTP); 

		ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		servidor.setExecutor(threadPoolExecutor);

		servidor.start();
		System.out.println("Servidor HTTP arracado >> Puerto: " + puerto);	
	}
}
