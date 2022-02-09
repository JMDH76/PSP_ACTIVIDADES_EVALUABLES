package es.florida.AE05_ServiciosRed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GestorHTTP implements HttpHandler {

	String temperaturaActual = "15";
	String temperaturaTermostato = "15";
	String correoAverias = "";
	String passCorreoAverias = "";

	
	/* FUNCTION: handle()
	 * ACTION:	discrimina si la petición es GET o POST y llama a la función que corresponda
	 * para ejecutar la petición y devolver un mensaje al cliente.
	 * INPUT:	Objeto HttpExchange  (URI tipo GET o POST)
	 * OUTPUT: 	String con mensaje de respuesta al cliente tras ejecutar la petición */
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		
		String requestParamValue = null;

		if ("GET".equals(httpExchange.getRequestMethod())) { 
			requestParamValue = handleGetRequest(httpExchange);
			handleGETResponse(httpExchange, requestParamValue); 

		} else if ("POST".equals(httpExchange.getRequestMethod())) { 
			try {
				requestParamValue = handlePostRequest(httpExchange);
				handlePOSTResponse(httpExchange, requestParamValue);
				
			} catch ( InterruptedException | MessagingException e) {
				e.printStackTrace();
			}
		}
	}
	
	//OPERACIONES GET
	/*Function:	handleGetRequest() 
	 * ACTION:	obtiene la información que recibimos en el URI del cliente con el GET.
	 * INPUT: 	Objeto HttpExchange (URI tipo GET)
	 * OUTPUT:	String con la información que necesitamos para realizar la peticion. */
	private String handleGetRequest(HttpExchange httpExchange) {
		System.out.println("\nTemperatura actual: " + temperaturaActual);
		return temperaturaActual; 
	}
	
	/*Function: handleGETResponse()
	 * ACTION: 	obtiene la temperatura actual y la del termostato, genera con ellas un mensaje 
	 * en Html que devuelve al cliente.
	 * INPUT:	Objeto HttpExchange e informacion de handGetRequest()
	 * OUTPUT:  String con Mensaje en HTML para el cliente*/
	private void handleGETResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {
		OutputStream outputStream = httpExchange.getResponseBody();

		String htmlResponse = "<html><body><h1>Current temperature: " + temperaturaActual + "</h1>"
				+ "<h1>Thermostat temperature: " + temperaturaTermostato + "</h1></body></html>";
		httpExchange.sendResponseHeaders(200, htmlResponse.length());
		outputStream.write(htmlResponse.getBytes());
		outputStream.flush();
		outputStream.close();
		System.err.println("Devuelve respuesta HTML: " + htmlResponse);
	}

	
	
	//OPERACIONES POST
	/*Function:	handlePostRequest() 
	 * ACTION:	obtiene la información que recibimos en el URI del cliente con el POST. Lo lee
	 * linea a linea y lo introduce en un String que luego devuelve.
	 * INPUT: 	Objeto HttpExchange (URI tipo POST)
	 * OUTPUT:	String con la información del URI. */
	private String handlePostRequest(HttpExchange httpExchange) throws InterruptedException {

		InputStream is = httpExchange.getRequestBody();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/*Function: handlePOSTResponse()
	 * ACTION: 	recibe la información y en función de si es una avería o un ajuste del termostato
	 * efectua una oparacion u otra. 
	 * 		Avería: obtiene direccion de coreo y password del remitente, 
	 * 			genera mensaje HTML y envía correo al técnico. 
	 * 		Ajuste del Termostato: obtiene la temperatura del termostato, genera mensaje HTML sube
	 *  		o baja la temperatura actual hasta igualarlas. 
	 * INPUT:	Objeto HttpExchange e informacion de handPostRequest()
	 * OUTPUT:  String con Mensaje en HTML para el cliente después de llamar a las funciones correspondientes*/
	private void handlePOSTResponse(HttpExchange httpExchange, String requestParamValue)
			throws IOException, InterruptedException, MessagingException {

		OutputStream outputStream = httpExchange.getResponseBody();
		
		//avería
		if (requestParamValue.split(":")[0].substring(0, 9).equals(("notificar"))) {
			correoAverias = requestParamValue.split("=")[1].split(";")[0];
			passCorreoAverias = requestParamValue.split(";")[1].split("=")[1];

			String htmlResponse = "<html><body><h1>Malfunction warning: Overheating Error</h1></body></html>";

			httpExchange.sendResponseHeaders(200, htmlResponse.length());
			outputStream.write(htmlResponse.getBytes());
			outputStream.flush();
			outputStream.close();
			System.err.println("\nDevuelve respuesta HTML: " + htmlResponse);

			System.out.print("¡AVISO! Avería >> Comunicando al responsable");
			envioMail(correoAverias, passCorreoAverias);

		// ajuste termostato
		} else {

			temperaturaTermostato = requestParamValue.split("=")[1];
			String htmlResponse = "<html><body><h1>Thermostat temperature: " + temperaturaTermostato
					+ "</h1></body></html>";

			httpExchange.sendResponseHeaders(200, htmlResponse.length());
			outputStream.write(htmlResponse.getBytes());
			outputStream.flush();
			outputStream.close();
			System.err.println("\nDevuelve respuesta HTML: " + htmlResponse);
			regularTemperatura(temperaturaActual, temperaturaTermostato);
		}
	}

	
	/*Function: regularTemperatura()
	 * ACTION: 	regula la temperatura subiendola o bajándola en función de la información recibida con 
	 * la temperatura del termostato. Se llama desde handlePOSTResponse() bajo peticion POST. Se le 
	 * añade un retardo para poder comprobarlo solicitando la temperatura con un GET.
	 * INPUT:	Strings con Temperatura actual y del termostato.
	 * OUTPUT:  Nada. Modifica la temperatura.
	 * */
	private void regularTemperatura(String actual, String termostato) throws InterruptedException {

		int tActual = Integer.parseInt(actual);
		int tTermostato = Integer.parseInt(termostato);
		int difGrados = Math.abs(tActual - tTermostato);
		System.out.println("Termostato: " + tTermostato);

		for (int i = 0; i < difGrados; i++) {
			if (tActual < tTermostato) {
				if (i == 0)
					System.out.println("Subiendo la temperatura " + difGrados + " grados");
				tActual += 1;
			} else if (tActual > tTermostato) {
				if (i == 0)
					System.out.println("Bajando la temperatura " + difGrados + " grados");
				tActual -= 1;
			} else {
				System.out.println("Temperatura ajustada correctamente");
			}
			temperaturaActual = String.valueOf(tActual);
			System.out.println("Actual: " + tActual);

			Thread.sleep(5000);
		}
	}

	
	/*Function: envioMail()
	 * ACTION: cliente de correo que genera un correo de aviso al técnico cuando es requerido desde 
	 * handlePOSTResponse(). Envia el mensaje al técnico con copia a Lord Stark incluyendo dos adjuntos.
	 * INPUT: dirección de correo y password del remitente obtenidos en handlePOSTResponse()
	 * OUTPUT: Confirmación por consola. Envía el correo.*/
	private void envioMail(String email_remitente, String email_remitente_pass)
			throws MessagingException, UnsupportedEncodingException, InterruptedException {

		String host_email = "smtp.gmail.com";
		String port_email = "587";

		// String[] email_destino = { "mantenimientoinvernalia@gmail.com","megustaelfresquito@gmail.com"};
		String[] email_destino = { "jodohe@floridauniversitaria.es", "josem.dominguez@hotmail.com" };

		String asunto = "Alerta por avería. Solicitud de intervención";
		String mensaje = "\n    Por la presente,\n    Lord Eddard Stark, Señor de Invernalia y Guardián del Norte, requiere que"
				+ " \"el cuervo\" Donal Noye (herrero y reparador de estufas) abandone el Muro de inmediato y se presente en el castillo para proceder a la reparación"
				+ " de una avería en el sistema central de calefacción de Invernalia.\n\nAtentamente,\nWolf-Huargo Systems\nDelegación Norte";

		String[] anexo = { "./adjuntos/logo.jpg", "./adjuntos/parte.pdf" };

		Properties props = System.getProperties();
		props.put("mail.smtp.host", host_email);
		props.put("mail.smtp.user", correoAverias);
		props.put("mail.smtp.clave", passCorreoAverias);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", port_email);

		Session session = Session.getDefaultInstance(props);

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(correoAverias));

		for (int i = 0; i < email_destino.length; i++) {
			message.addRecipients(Message.RecipientType.TO, email_destino[i]);
		}
		message.setSubject(asunto);

		BodyPart messageBodyPart1 = new MimeBodyPart();
		messageBodyPart1.setText(mensaje);

		ArrayList<BodyPart> adjuntos = new ArrayList<BodyPart>();
		for (int i = 0; i < anexo.length; i++) {
			BodyPart messageBodyPart = new MimeBodyPart();
			DataSource src = new FileDataSource(anexo[i]);
			messageBodyPart.setDataHandler(new DataHandler(src));
			messageBodyPart.setFileName(anexo[i]);
			adjuntos.add(messageBodyPart);
		}

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart1);
		for (BodyPart adjunto : adjuntos) {
			multipart.addBodyPart(adjunto);
		}
		message.setContent(multipart);

		Transport transport = session.getTransport("smtp");
		transport.connect(host_email, email_remitente, email_remitente_pass);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();

		for (int i = 0; i < 3; i++) {
			Thread.sleep(1000);
			System.out.print(".");
		}

		Thread.sleep(1000);
		System.out.println(" solicitud enviada:");
		Thread.sleep(500);
		for (int i = 0; i < email_destino.length; i++) {
			if (i == 0) {
				System.out.println("\n > Para:	" + email_destino[i]);
			} else {
				System.out.println(" > CC:		" + email_destino[i]);
			}
		}
		System.out.println(" > Asunto:	" + asunto);
	}
}
