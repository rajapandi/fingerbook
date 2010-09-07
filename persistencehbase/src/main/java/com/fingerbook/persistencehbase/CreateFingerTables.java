package com.fingerbook.persistencehbase;

import java.io.IOException;

public class CreateFingerTables {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		PersistentFingerbook.createFingerTables();
	}

}
