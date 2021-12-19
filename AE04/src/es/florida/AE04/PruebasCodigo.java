package es.florida.AE04;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

public class PruebasCodigo {

	public static void main(String[] args) {
		
//		Scanner teclado = new Scanner(System.in);
//		System.out.print("Indique una contraseña: ");
//		String contrasenya = teclado.nextLine();
//		
//		ArrayList<String> passEncrypted = new ArrayList<String>();
//		
//		int letra=0;
//		for (int i = 0; i < contrasenya.length(); i++) {
//			letra = contrasenya.charAt(i) + 1;
//			passEncrypted.add(String.valueOf((char)letra));
//		}
//	
//		String paswordEncryted = String.join("", passEncrypted);
//
//		System.out.println("Contraseña: " + contrasenya);
//		System.out.println("Longitud: " + contrasenya.length());
//		System.out.println(paswordEncryted);
//		
//		teclado.close();
		
		String s = encriptarMD5("123456j");
		System.out.println(s);
		

	}
	
	
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
