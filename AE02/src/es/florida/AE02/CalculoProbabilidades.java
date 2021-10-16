package es.florida.AE02;

public class CalculoProbabilidades {

	public static void main(String[] args) {
		
		double posNEO = 1.1;
		double velNEO = 10.1;
		double probabilidadImpacto = calculoProbabilidad(posNEO, velNEO);

		System.out.println(probabilidadImpacto);
	}
	
	
	/*Metodo calculoProbabilidad()
	 * ACTION:	recibe posicion y velocidad del NEO y calcula la probabilidad siguiendo el 
	 * algoritmo suminidstrado por el proyecto.
	 * INPUT:	Doubles con velocidad y posicion del NEO.
	 * OUTPUT: double con probabilidad de impacto formateada a dos dígitos.
	 */
	public static double calculoProbabilidad (double posicionNEO, double velocidadNEO) {
		
		double posicionTierra = 1;
		double velocidadTierra = 100;
		for (int i = 0; i< (50*365*24*60*60); i++) {
			posicionNEO = posicionNEO + velocidadNEO * i;
			posicionTierra = posicionTierra + velocidadTierra * i;
		}
		double probabilidad = 100 * Math.random() * Math.pow((posicionNEO - posicionTierra)/(posicionNEO + posicionTierra), 2);
		double probabilidadFormat = Math.round(probabilidad * 100.0) / 100.0;
		
		return probabilidadFormat;
	}

}