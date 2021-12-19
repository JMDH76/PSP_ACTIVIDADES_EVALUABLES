package es.florida.examen1a;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Principal {

	public static void main(String[] args) {

		Scanner teclado = new Scanner(System.in);
		System.out.print("\nIndica el número de personas: ");
		String personas = teclado.nextLine();
		teclado.close();
		lanzadorSubproceso(personas);
	}

	
	public static void lanzadorSubproceso(String personas) {

		String clase = "es.florida.examen1a.Subproceso";
		String javaHome = System.getProperty("java.home");
		String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
		String classpath = System.getProperty("java.class.path");
		String classname = clase;
		ArrayList<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-cp");
		command.add(classpath);
		command.add(classname);
		command.add(personas);

		ProcessBuilder builder = new ProcessBuilder(command);
		try {
			builder.inheritIO().start();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
	}
}
