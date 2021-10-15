package es.florida.AE01;

import java.util.Scanner;

public class Ejercicio03 {

	public static void main(String[] args) {

		System.out.println("3. Escribe un m�todo que sume los n�meros pares hasta el n�mero que acepta como \r\n"
				+ "   par�metro y devuelva el resultado de la suma.\r\n");

		Scanner teclado = new Scanner(System.in);
		System.out.print("Inserte un n�mero: ");
		int numero = Integer.parseInt(teclado.nextLine());
		teclado.close();

		System.out.print("La suma de todos los pares hasta " + numero + " es:  " + sumaPares(numero));
	}

/*	Comment
	// INPUT:	n�mero entrada por consola (int)
	// OUTPUT: 	valor de la suma de los pares         */
	public static int sumaPares(int numero) {
		
		int suma = 0;
		for (int i = 0; i <= numero; i = i + 2) 
			suma = suma + i;
		return suma;
		}
}