package es.florida.AE01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Ejercicio05 {

	public static void main(String[] args) {

		System.out.println(
				"5. Escribe un método que acepte un array o una lista de elementos y devuelva el mayor de ellos.\r\n");

		ArrayList<Integer> numeros = new ArrayList<Integer>();
		Scanner teclado = new Scanner(System.in);

		// BUCLE entrada datos
		boolean bandera = false;

		while (bandera == false) {
			System.out.print("Inserta un número ('f' para finalizar): ");
			String numero = teclado.nextLine();

			if (!numero.equals("f")) {
				int num = Integer.parseInt(numero);
				numeros.add(num);
			} else
				bandera = true;
		}
		teclado.close();

		System.out.print("El número mayor es el " + maxNumero(numeros));
	}

/*	Comment
	INPUT: 	ArrayList de numeros
	OUTPUT:	valor más alto dentro del ArrayList        */
	public static int maxNumero(ArrayList<Integer> numeros) {

		return Collections.max(numeros);
	}
}
