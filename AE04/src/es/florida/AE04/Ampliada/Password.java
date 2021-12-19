package es.florida.AE04.Ampliada;

import java.io.Serializable;

/*	Clase Password con sus constructores, getters y setters.
	Implementa Sererializable.
	Añadimos 'String tipo' al objeto para identificar el tipo de cifrado que puede elegir el usuario. */

public class Password implements Serializable {

	String textoPlano, encrypt, tipo;

	public Password(String textoPlano, String encrypt, String tipo) {
		super();
		this.textoPlano = textoPlano;
		this.encrypt = encrypt;
		this.tipo = tipo;
	}


	public Password() {
		super();
	}

	public String getTextoPlano() {
		return textoPlano;
	}

	public void setTextoPlano(String textoPlano) {
		this.textoPlano = textoPlano;
	}

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	
	public String passwordToString() {
		return "\"" + textoPlano + "\" - \"" + encrypt + "\" - \"" + tipo + "\"";
	}
}
