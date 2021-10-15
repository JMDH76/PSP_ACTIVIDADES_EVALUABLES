package es.florida.AE01;

import java.util.Scanner;

public class Ejercicio07 {

	public static void main(String[] args) {

		System.out.println("7. Escribe un m�todo que pida por teclado el nombre y los a�os de experiencia como \r\n"
				+ "   desarrollador de software y muestre el nivel y el salario en base al siguiente criterio:\r\n\n"
				+ "	a. Si lleva menos de 1 a�o --> �Desarrollador Junior L1 � 15000-18000�\r\n"
				+ "	b. Si lleva entre 1 y 2 a�os --> �Desarrollador Junior L2 � 18000-22000�\r\n"
				+ "	c. Si lleva entre 3 y 5 a�os --> �Desarrollador Senior L1 � 22000-28000�\r\n"
				+ "	d. Si lleva entre 6 y 8 a�os --> �Desarrollador Senior L2 � 28000-36000�\r\n"
				+ "	e. Si lleva m�s de 8 a�os --> �Analista / Arquitecto. Salario a convenir en base a rol�\n");

		String[] categorias = { "Desarrollador Junior L1 � 15000-18000", "Desarrollador Junior L2 � 18000-22000",
				"Desarrollador Senior L1 � 22000-28000", "Desarrollador Senior L2 � 28000-36000",
				"Analista / Arquitecto. Salario a convenir en base a rol" };

		Scanner teclado = new Scanner(System.in);

		System.out.print("�Cu�l es tu nombre? ");
		String nombre = teclado.nextLine();
		System.out.print("�Cuantos a�os de experiencia tienes? ");
		int experiencia = Integer.parseInt(teclado.nextLine());

		teclado.close();

		String categoria = calculaCategoria(experiencia, categorias);
		String sueldo = calculaSueldo(experiencia, categorias);
		System.out.println("\n" + nombre + " con " + experiencia + " a�os de experiencia como desarrollador de software te corresponde:\n\n	- "
				+ "Categor�a profesional:	" + categoria + "\n	- Salario: 			" + sueldo);	
	}
	
/*	Comment 
	NPUT: 	experiencia y String caracter�sticas
	OUTPUT: String con texto correspondiente a la categor�a    */ 
	public static String calculaCategoria(int experiencia, String[] categorias) {

		int index = buscaIndice(experiencia);
		String categoria = categorias[index].substring(0, 23);
		return categoria;
	}

/*	Comment
 	NPUT: experiencia y String caracter�sticas 
	OUTPUT: String con texto correspondiente al sueldo      */	
	public static String calculaSueldo(int experiencia, String[] categorias) {

		int index = buscaIndice(experiencia);
		String sueldo = "";
		if (index < 4)
			sueldo = categorias[index].substring(26, 37) + " �";
		else if (index == 4) {
			sueldo = categorias[index].substring(23, 55);
		}
		return sueldo;
	}
	
/*	Comment 
	INPUT: 	a�os experiencia 
	OUTPUT: indice correspondiente en el array   */
	public static int buscaIndice(int experiencia) {

		int indice = 0;
		if (experiencia < 1) indice = 0;
		else if (experiencia >= 1 && experiencia <= 2) indice = 1;
		else if (experiencia >= 3 && experiencia <= 5) indice = 2;
		else if (experiencia >= 6 && experiencia <= 8) indice = 3;
		else if (experiencia > 8) indice = 4;

		return indice;
	}
}