package es.florida.AE01;

public class Ejercicio04 {

	public static void main(String[] args) {

		System.out.println("4. Escribe el c�digo necesario para calcular el factorial del n�mero 15, \r\n"
				+ "   en menos de 5 instrucciones.\r\n");

	
		int numero = Integer.parseInt(args[0]);
		System.out.println("El valor de " + numero + "! es " + calculoFactorial(numero));
	}

	
/*	Comment
	INPUT: 	recibe el n�mero a calcular por par�metro.
	OUTPUT:	devuelve el resultado del c�lculo (long), los Factoriales altos son muy largos.         */
	public static long calculoFactorial(int numero) {
		
		long factorial = 1;
		for (int i = 1; i < numero; i++) {
			factorial = factorial * (i + 1);
		}
		return factorial;
	}
}