package com.fingerbook.client;

import org.apache.commons.codec.binary.Base64;

/*
Esta clase de encripcion y desencripcion es PESIMA.
Cambiarla por una clase que encripte/desencripte con una buena funcion.
Asi como esta agrega practicamente CERO seguridad.
*/
public class Cypher {
	public String Encode(String pass) {
		 byte[] encoded = Base64.encodeBase64(pass.getBytes());     

		return new String(encoded);
	}
	
	public String Decode(String ePass) {
		byte[] decoded = Base64.decodeBase64(ePass);
		
		return new String(decoded);
	}
}
