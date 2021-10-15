package es.florida.AE01;

import java.util.Scanner;

public class Ejercicio06 {

	public static void main(String[] args) {
		System.out
				.println("6. Escribe un m�todo que pida 5 n�meros enteros (sin validaci�n de momento), los muestre \r\n"
						+ "   por consola en orden inverso y devuelva la suma de todos los n�meros proporcionados.\r\n");
		
		int[] numeros = new int[5];
		Scanner teclado = new Scanner(System.in);

		// INPUT: nombres por consola
		for (int i = 0; i < numeros.length; i++) {

			System.out.print("Introduce un n�mero: ");
			int numero = Integer.parseInt(teclado.nextLine());
			numeros[i] = numero;
		}
		teclado.close();

		burbuja(numeros);
		System.out.print("N�meros en orden descendente: ");
		for (int num : numeros) {
			System.out.print(num + " ");
		}
		System.out.println("\nLos n�meros introducidos suman " + sumaArray(numeros));
	}
	
/*	Comment
	INPUT: Array de n�meros
	OUTPUT: Suma de los n�meros del Array            */
	public static int sumaArray(int[] numeros) {

		int suma = 0;
		for (int num : numeros) {
			suma = suma + num;
		}
		return suma;
	}

/*	Comment
	M�todo BURBUJA
	INPUT: 	Array desordenado
	OUTPUT: Array ordenado     */
	private static void burbuja(int[] numeros) {

		for (int x = 0; x < numeros.length; x++) {
			for (int y = 0; y < numeros.length - 1; y++) {
				int elementoActual = numeros[y];
				int elementoSiguiente = numeros[y + 1];
				if (elementoActual < elementoSiguiente) {	// -->  '>' orden ascendentemente | '<' orden descendente
					numeros[y] = elementoSiguiente;
					numeros[y + 1] = elementoActual;
				}
			}
		}
	}
}