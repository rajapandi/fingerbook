package com.fingerbook.client;

import java.lang.reflect.Field;

public class ClientParams {

	// Default param values if not passed by command line:
	public String dir = new String(".");
	public String user = new String("gimi");
	public String mail = new String("ggross@gmail.com");
	public String gui = new String("yes");
	public String url = new String("http://10.6.0.37/fingerbookREST/");

	ClientParams(String[] args) throws NumberFormatException,
			IllegalArgumentException, IllegalAccessException {

		String split[] = null;
		for (String arg : args) {
			split = arg.split("=");
			if (split.length >= 2) {
				try {
					Field aField = ClientParams.class.getField(split[0]);
					if (aField.getType() == String.class) {
						aField.set(this, split[1]);
					} else
						throw new IllegalArgumentException(
								"Invalid argument type");
				} catch (NoSuchFieldException e) {
					System.out.println("No such argument : " + split[0]
							+ ", Using default");
				}
			}
		}
	}

	@Override
	public String toString() {
		return String.format("Scan Directory = " + "\"" + "%s" + "\"" + "\n" + "Username = " + "\"" + "%s" + "\"" + "\n" + "E-Mail = " + "\"" + "%s" + "\"" + "\n" + "GUI = " + "\"" + "%s" + "\"", dir, user, mail, gui);
	}
}