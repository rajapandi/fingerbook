package com.fingerbook.client;

import java.lang.reflect.Field;

public class ClientParams {
	/* Default param values if not passed by command line */
	public String path = new String(".");
	public String action = new String("put");
	public String user = new String("gimi");
	public String mail = new String("ggross@gmail.com");
	public String gui = new String("yes");
	public String url = new String("http://client:8080/fingerbookRESTM/");

	ClientParams(String[] args) throws NumberFormatException,
			IllegalArgumentException, IllegalAccessException {
		String split[] = null;
		
		/* For each argument, split in '='. Use reflection to find left part
		 * variable, and set it with the right part value */
		for (String arg : args) {
			split = arg.split("=");
			if (split.length >= 2) {
				try {
					Field aField = ClientParams.class.getField(split[0]);
					/* If argument is valid, set it */
					if (aField.getType() == String.class)
						aField.set(this, split[1]);
					else
						throw new IllegalArgumentException(
								"Invalid argument type");
				} catch (NoSuchFieldException e) {
					/* Argument doesn't exist */
					System.out.println("No such argument : " + split[0]
							+ ", Using default");
				}
			}
		}
	}

	@Override
	public String toString() {
		/* Returns: path, user, mail, gui */
		return String.format("Scan Directory = " + "\"" + "%s" + "\"" + "\n" + "Username = " + "\"" + "%s" + "\"" + "\n" + "E-Mail = " + "\"" + "%s" + "\"" + "\n" + "GUI = " + "\"" + "%s" + "\"", path, user, mail, gui);
	}
}