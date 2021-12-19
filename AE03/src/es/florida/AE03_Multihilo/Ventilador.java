package es.florida.AE03_Multihilo;

public class Ventilador {

	boolean control = true;
	int temporizador = 3000;

	
	/*METODO: encenderVentilador()
	 * ACTION: enciende el ventilador de la mina durante el tiempo que indique el 
	 * temporizador cuando recibe la notificacion desde apagarVentilador(). Una vez
	 * pasado este tiempo notifica a apagarVentilador y entra en bucla hasta que 
	 * vuelva a recibir la notificación y se salga
	 * INPUT: notify() desde apagarVentilador
	 * OUTPUT: notify() hacia apagarVentilador*/
	public void encenderVentilador() throws InterruptedException {
		while (true) {
			synchronized (this) {
				while (control != true) {
					wait();
				}
				System.out.println(Color.yellow + "\nVENTILADOR >  " + Color.green + "Encendidio\n" + Color.resetcolor);
				control = false;
				notify();
				Thread.sleep(temporizador);
			}
		}
	}

	/*METODO: apagarVentilador()
	 * ACTION: idem que el anterior pero apagandolo
	 * INPUT: notify() desde encenderVentilador
	 * OUTPUT: notify() hacia encenderVentilador*/
	public void apagarVentilador() throws InterruptedException {
		while (true) {
			synchronized (this) {
				while (control == true) {
					wait();
				}
				System.out.println(Color.yellow + "\nVENTILADOR >  " + Color.red + "Apagado\n" + Color.resetcolor);
				control = true;
				notify();
				Thread.sleep(temporizador);
			}
		}
	}
}
