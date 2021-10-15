package es.florida.AE01;

import java.util.ArrayList;
import java.util.Scanner;

public class Ejercicio02 {

	public static void main(String[] args) {

		System.out.println("2. Crea un array de elementos que contenga el nombre de 6 compañeros de clase y…\n");

		// ---------------------------------------------------------------------------------
		// A.
		System.out.println("a) Haz que se escriban por la consola en líneas consecutivas.\n");
		String[] arraynombres = new String[6];
		Scanner teclado = new Scanner(System.in);

		//Bucle INPUT por consola de nombres en Array
		for (int i = 0; i < arraynombres.length; i++) {

			System.out.print("	Inserta nombre " + (i + 1) + ":  ");
			String nombrea = teclado.nextLine();
			arraynombres[i] = nombrea;
		}

		// OUTPUT: contenido Array para verificar
		int contarray = 0;
		System.out.println("\n	- Verificación del array:");
		for (String nombre : arraynombres) {
			contarray++;
			System.out.println("		Nombre " + contarray + ": " + nombre);
		}

		
		
		// ---------------------------------------------------------------------------------
		// B.
		System.out.println("\n---------------------------------------------------------------------------------\n");

		System.out.println("b) Haz lo mismo, pero empleando un objeto de tipo lista.\n");
		ArrayList<String> listanombres = new ArrayList<String>();

		//Bucle INPUT por consola de nombres en ArrayList
		int contbucle = 0;
		while (contbucle != 6) {
			System.out.print("	Inserta nombre " + (contbucle + 1) + ":  ");
			String nombreb = teclado.nextLine();
			listanombres.add(nombreb);
			contbucle++;
		}

		// OUTPUT: contenido ArrayList para verificar
		int contlist = 0;
		System.out.println("\n	- Verificación de la lista:");
		for (String nombre : listanombres) {
			contlist++;
			System.out.println("		Nombre " + contlist + ": " + nombre);
		}

		teclado.close();
	}
}