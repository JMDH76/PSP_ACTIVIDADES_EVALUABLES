package es.florida.AE03_Multihilo;

public class App implements Runnable {
	
	static Mina mina = new Mina();
	static int orosDisponibles = mina.stock;
	static int orosRecolectados = 0;

	
	/*METODO main()
	 * ACTION:	genera los hilos de mineros y del ventilador. Cuando los hilos de mineros
	 * finalizan presenta las cantidad disponible y la recolectada. El ventilador no termina
	 * hasta parar la aplicación ya que está en bucle infinito
	 * INPUT:	no
	 * OUTPUT:	cantidades disponibles y recolectadas*/
	public static void main(String[] args) throws InterruptedException {
		
		int numeroMineros = 27;
		App app = new App();
		Ventilador ventilador = new Ventilador();

		//Lanza los hilos de mineros
		Thread t;
		for (int i = 1; i <= numeroMineros; i++) {
			t = new Thread(app);
			t.setName("Minero_" + (i));
			t.start();
		}

		// Hilo de encendido del ventilador
		Thread ventOn = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ventilador.encenderVentilador();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		//Hilo de apagado del ventilador
		Thread ventOff = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ventilador.apagarVentilador();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		ventOn.start();
		ventOff.start();
		
		//Bucle con retardo para que pueda obtener los datos y presentarlos una vez finalizados los hilos de mineros
		boolean flag = true;
		while (flag) {
			try {
				Thread.sleep(25000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (orosDisponibles == 0) {
				System.out.println("\nRecursos iniciales:	" + mina.stock);
				System.out.println("Recursos actuales:	" + orosDisponibles);
				System.out.println(Color.cyan + "Recursos recolectados:	" + orosRecolectados + Color.resetcolor);
				flag = false;
			}
		}
		ventOn.join();
		ventOff.join();
	}

	/*METODO run()
	 * ACTION: se ejecuta para hilo de minero que se ha generado. Crea el objeto Minero, obtiene su nombre del hilo
	 * e inicia un contador para indicar el turno de trabajo del minero. Mientras haya recursos disponibles  llamará
	 * en bucle a extraerRecurso en la clase Minero y le pasará el nombre y turno. Al principio del bucle aplica un 
	 * retardo que reprenta el tiempo que dura un turno. Una vez terminado el bucle obtiene el valor de la bolsa 
	 * de ese minero y la suma a la variable de recursos recogidos
	 * INPUT: 	no
	 * OUTPUT:	no	*/
	@Override
	public void run() {

		Minero minero = new Minero();
		String nombre = Thread.currentThread().getName();
		int contador = 1;
			while (orosDisponibles > 0) {
				try {
					Thread.sleep(minero.tiempoExtraccion);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				minero.extraerRecurso(nombre, contador);
				contador++;
			}
			orosRecolectados += minero.bolsa;
	}
}
