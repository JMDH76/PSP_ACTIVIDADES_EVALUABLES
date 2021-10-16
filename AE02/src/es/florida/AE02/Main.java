package es.florida.AE02;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

	/*Metod Main()
	 * ACTION:	lee el fichero de NEOs, crea los ficheros auxiliares de cada proceso llamando a 
	 * crearFicherosProceso(). Lee cada proceso (fichero aux NEOs_n.txt) y extrae el nombre, velocidad  
	 * y distancia de cada línea y se la pasa al Process Builder para lanzar el proceso. Con la llamada 
	 * al lanzador de cálculo capturamos el tiempo de procesamiento de ese NEO que pasamos a una 
	 * Lista para calcular después el promedio y presentarlo junto al tiempo total de ejecución.
	 * Borra el fichero auxiliar con procesos.
	 * INPUT:	nombre del fichero por argumento (NEOs.txt)
	 * OUTPUT:	lista de NEOs con probailidad y mensaje por bloques de ejecucuión y tiempos medio 
	 * y total de procesamiento de los NEOs.
	 */
	public static void main(String[] args) {

		ArrayList <Long> tiempomedioproceso = new ArrayList<Long>();
		
		long tiempoinicioTotal = System.nanoTime();
		
		String nombrefichero = args[0];
		int procesos = calcularProcesos(nombrefichero);
		crearFicherosProceso(nombrefichero);
		
		for (int i = 1; i <= procesos; i++) {
			System.out.println("Proceso " + i + " lanzado...\n");
			
			int index = nombrefichero.indexOf(".");
			String prefijofichero = nombrefichero.substring(0, index);
			String nuevofichero = prefijofichero + "_" + i + ".txt";
			
			try {
				File ficheroProceso = new File(nuevofichero);
				FileReader fr = new FileReader(ficheroProceso);
				BufferedReader br = new BufferedReader(fr);
				String linea = br.readLine();
				
				while (linea != null) {
					int index2 = linea.indexOf(",");
					String nombreNEO = linea.substring(0, index2);
					linea = linea.substring(index2 + 1);
					
					int index3 = linea.indexOf(",");
					double posNEO = Double.parseDouble(linea.substring(0, index3));
					double velNEO = Double.parseDouble(linea.substring(index3 + 1));

					tiempomedioproceso.add(lanzadorCalculo(nombreNEO, posNEO, velNEO));
					linea = br.readLine();
				}
				br.close();
				fr.close();
				ficheroProceso.delete();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("\nError de lectura en el fichero "+ nuevofichero + " " + e);
				System.out.println("No se ha completado el Proceso " + i);
			}		
		}
		long tiempofinTotal = System.nanoTime();
		long duracionprocesoTotal = (tiempofinTotal - tiempoinicioTotal)/1000000;
		
		long sumatiempo = 0;
		for(long tiempo: tiempomedioproceso) {
			sumatiempo = sumatiempo + tiempo;
		}
		long tiempomedioNEO = sumatiempo/tiempomedioproceso.size();
		
		System.out.println("Tiempo medio de ejecucion por NEO: " + tiempomedioNEO + " ms");
		System.out.println("Tiempo de ejecucion de la app: " + duracionprocesoTotal + " ms");
	}

	
	/*Metodo crearFicheroProcesos()
	 * ACTION: divide el fichero original en varios ficheros auxiliares cada uno con 
	 * las líneas correspondientes a un proceso. Muestra los cores disponibles y los 
	 * procesos que va a lanzar.
	 * INPUT: recibe el nombre del fichero
	 * OUTPUT:	genera tantos ficheros como procesos con las lineas correspondientes a
	 * cada uno.
	 */
	public static void crearFicherosProceso(String nombrefichero) {

		int procesos = calcularProcesos(nombrefichero);
		int cores = Runtime.getRuntime().availableProcessors();
		System.out.println("Cores disponibles: " + cores + "\nProcesos: " + procesos + "\n");
		
		try {
			for (int i = 1; i <= procesos; i++) {

				FileInputStream f = new FileInputStream(nombrefichero);
				InputStreamReader fr = new InputStreamReader(f);
				BufferedReader br = new BufferedReader(fr);

				int index = nombrefichero.indexOf(".");
				String prefijofichero = nombrefichero.substring(0, index);
				String nuevofichero = prefijofichero + "_" + i + ".txt";
				
				FileWriter fw = new FileWriter(nuevofichero);
				BufferedWriter bw = new BufferedWriter(fw);
				String linea = br.readLine();

				int max = cores * i;
				int min = (max + 1) - cores;
				int cont = 0;
				while (linea != null) {
					cont++;
					if (cont <= max && cont >= min) {
						bw.write(linea);
						bw.newLine();
					}
					linea = br.readLine();
				}
				bw.close();
				fw.close();
				br.close();
				fr.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/*Metodo calcularProcesos()
	 *ACTION: 	lee el fichero, cuenta las líneas y en función de los cores disponibles 
	 *calcula los procesos necesarios.
	 *INPUT:	String con nombre del fichero.
	 *OUTPUT:	devuelve Integer con el numero de procesos necesarios para procesar todas
	 *las lineas.
	 */
	public static int calcularProcesos(String nombrefichero) {

		double cores = Runtime.getRuntime().availableProcessors();
		double numerolineas = 0;
		int procesos = 0;
		try {
			FileInputStream f = new FileInputStream(nombrefichero);
			InputStreamReader fr = new InputStreamReader(f);
			BufferedReader br = new BufferedReader(fr);
			String lineal = br.readLine();
			while (lineal != null) {
				numerolineas++;
				lineal = br.readLine();
			}
			procesos = (int) Math.ceil(numerolineas / cores);
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return procesos;
	}

	
	/*Metodo lanzarCalculo()
	 * ACTION:	crea el proceso con Process BUilder y hace la llamada a la clase de cálculoProbabilidades 
	 * para calcular las probabilidades de impacto de un NEO. Crea un fichero por NEO, lo lee y muestra 
	 * la probabilidad junto a un mensaje apocaliptico o tranquilizador según si la probabilidad es menor 
	 * o mayorde 10%. Captura el tiempo que tarda en procesar un NEO para pasarlo a Main().
	 * INPUT:  	recibe el nombre, poiscion y velocidad del NEO desde Main().
	 * OUTPUT:	Crea un fichero por NEO, devuelve el tiempo que ha tardado en procesar el NEO (Long) y 
	 * saca por terminal un nmensaje con el nombre, probabilidad de impacto y mensaje según esta última 
	 * sea mayor o menor a 10%.
	 */
	public static long lanzadorCalculo(String nombreNEO, Double posicionNEO, Double velocidadNEO) {

		long tiempoinicioNEO = System.nanoTime();
		try {
			File ficheroNEO = new File(nombreNEO + ".txt");
			String nombre = nombreNEO;
			String clase = "es.florida.AE02.CalculoProbabilidades";
			String javaHome = System.getProperty("java.home");
			String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
			String classpath = System.getProperty("java.class.path");
			String classname = clase;
			ArrayList<String> command = new ArrayList<String>();
			command.add(javaBin);
			command.add("-cp");
			command.add(classpath);
			command.add(classname);
			command.add(String.valueOf(posicionNEO));
			command.add(String.valueOf(velocidadNEO));
			command.add(nombre);
			
			ProcessBuilder builder = new ProcessBuilder(command);
			builder.redirectOutput(ficheroNEO);
			builder.start();	
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {}

		//Bucle para comprobar que el fichero ya está escrito y es accesible. Lee y saca mensaje.
		boolean ficheroLeido = false;
		while (ficheroLeido != true) {
			try {
				File f = new File(nombreNEO + ".txt");
				FileReader fr;

				fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String linea = br.readLine();

				double probabilidad = Double.parseDouble(linea);

				if (probabilidad <= 10) {
					System.out.println("\"" + nombreNEO + "\"	" + probabilidad
							+ "%	Baja probabilidad de colision. ¡No hay peligro!\n");
				} else
					System.err.println("\"" + nombreNEO + "\"	" + probabilidad
							+ "% 	¡ALERTA! Alta probabilidad de colision... Iniciando protocolos de evacuación del planeta...\n");

				ficheroLeido = true;
				br.close();
				fr.close();

			} catch (Exception e) {}
		}
		long tiempofinNEO = System.nanoTime();
		long duracionprocesoNEO = (tiempofinNEO - tiempoinicioNEO) / 1000000;
		return duracionprocesoNEO;
	}

}
