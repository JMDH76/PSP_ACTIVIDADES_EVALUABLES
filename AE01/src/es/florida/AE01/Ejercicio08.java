package es.florida.AE01;

import java.util.Scanner;

public class Ejercicio08 {
	
	/*
	Comment
	vbles para cambiar de color el texto y resetearlo;
	*/
	private static String green = "\033[32m"; 
	private static String reset = "\u001B[0m";

		public static void main(String[] args) {

			System.out
					.println("8. Escribe un mï¿½todo que pida por teclado 2 nï¿½meros como extremos de un intervalo. Luego \r\n"
							+ "   imprime por pantalla todos los nï¿½meros entre ese intervalo, indicando junto al nï¿½mero si es \r\n"
							+ "   un nï¿½mero primo o no. Al terminar de mostrar los nï¿½meros por pantalla, debe mostrar un \r\n"
							+ "   mensaje indicando el tiempo consumido en la ejecuciï¿½n del mï¿½todo.\n");

			Scanner teclado = new Scanner(System.in);

			int[] extremos = new int[2];
			
			for (int i = 0; i < 2; i++) {
				System.out.print("Introduce un numero: ");
				int numero = Integer.parseInt(teclado.nextLine());
				extremos[i] = numero;
			}
			teclado.close();
			
			long t_inicio = System.currentTimeMillis();
			System.out.println("");
			calculaPrimos(extremos);
			long t_final = System.currentTimeMillis();
			System.out.println("Tiempo de ejecución: " + (t_final - t_inicio) + " ms"); 
		}	
			
	/* 	Comment
		INPUT: 	Array con los extremos ya ordenados 
		
		INSIDE: Ordenamos el Array con M. Burbuja para determinar dato inicial.
				un primo sï¿½lo da resto 0 cuando se divide 
				entre si mismo o uno. Con el segundo bucle y la condiciï¿½n 
				calculamos el resto dividiendo por 2 e incrementandolo hasta 
				numero anterior al nï¿½mero n (h < n) Si hay algï¿½n 
				resto que de 0 no serï¿½a primo porque serï¿½a divisible 
				por mï¿½s de dos nï¿½nmeros (esPrimo = false).
		
		OUTPUT: Nï¿½meros indicando si son primos o no           */
		public static void calculaPrimos(int[] extremos) {
			burbuja(extremos);
			int n = extremos[0];
			for (int i = n; i <= extremos[1]; i++) {
				boolean esPrimo = true;
				for (int h = 2; h < n; h++) { //
					if (n % h == 0) {
						esPrimo = false;
					}
				}
				if (esPrimo == true && n != 1) {
					System.out.println(green + i + " número primo" + reset);
				} else {
					System.out.println(i);
				}
				n++;
			}
		}

		/*
		Comment 
		Mï¿½todo BURBUJA 
	 	INPUT: 	Array desordenado 
		OUTPUT: Array ordenado
		 */
		private static void burbuja(int[] intervalo) {

			for (int x = 0; x < intervalo.length; x++) {
				for (int y = 0; y < intervalo.length - 1; y++) {
					int elementoActual = intervalo[y];
					int elementoSiguiente = intervalo[y + 1];
					if (elementoActual > elementoSiguiente) {
						intervalo[y] = elementoSiguiente;
						intervalo[y + 1] = elementoActual;
					}
				}
			}
		}

	}
