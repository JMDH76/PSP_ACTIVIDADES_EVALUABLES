package es.florida.AE04;

import java.io.Serializable;

//Clase Password con sus constructores, getters y setters.
//Implementa Sererializable.

public class Password implements Serializable {

	String textoPlano, encrypt, tipoEncriptado;

	public Password(String textoPlano, String encrypt) {
		super();
		this.textoPlano = textoPlano;
		this.encrypt = encrypt;
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

	public String passwordToString() {
		return "\"" + textoPlano + "\" - \"" + encrypt + "\"";
	}
}
