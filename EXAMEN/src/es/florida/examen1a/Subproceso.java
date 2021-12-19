package es.florida.examen1a;

import java.util.ArrayList;

public class Subproceso implements Runnable {

	static ArrayList<String> coloresFavoritos = new ArrayList<String>();
	static String arraycolores[] = { "amarillo", "verde", "rojo", "azul", "naranja" };

	public static void main(String[] args) {

		String numeropersonas;

		if (args[0] == null)
			numeropersonas = "100";
		else
			numeropersonas = args[0];

		int personas = Integer.parseInt(numeropersonas);

		Subproceso subproceso = new Subproceso();

		Thread t;
		for (int i = 1; i <= personas; i++) {
			t = new Thread(subproceso);
			t.setName("Persona_" + (i));
			t.start();
		}

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		boolean flag = false;
		while (!flag) {
			if (personas == coloresFavoritos.size()) {
				System.out.println("\nHan participado " + numeropersonas + " personas");
				System.out.println("La lista contiene " + coloresFavoritos.size() + " colores");
				flag = true;
			}
		}
	}

	
	synchronized static void anyadirColor(String nombre, String color) {
		coloresFavoritos.add(color);
		System.out.println(nombre + "	>>  " + color);
	}

	
	@SuppressWarnings("unused")
	@Override
	public void run() {

		for (int i = 0; i < 1e8; i++) {
			double sobrecargaCPU = Math.sqrt((double) (i));
		}

		String nombrePersona = Thread.currentThread().getName();
		int aleatorio = (int) Math.floor(Math.random() * arraycolores.length);
		String color = arraycolores[aleatorio];

		anyadirColor(nombrePersona, color);
	}

}
