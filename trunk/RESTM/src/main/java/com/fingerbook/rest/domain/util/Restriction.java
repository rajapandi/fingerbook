package com.fingerbook.rest.domain.util;

import org.hibernate.criterion.Criterion;

/**
 * Restricciones sobre la lista de una entidad 
 */

public class Restriction {
    
        public static enum Operation {
            EQ,NE,LT,LE,GT,GE,NOTNULL,ISNULL,LIKE,IN
        }
	        
	private String property;
	private Object value;
	private Operation operation;

	/**
	 * Constructor default, con equal como operacion.
	 * @param property La propiedad de la entidad.
	 * @param value El valor a comparar con la propiedad.
	 */
	public Restriction(String property, Object value) {
		this.property = property;
		this.value = value;
		this.operation = Restriction.Operation.EQ;
	}
	
	/**
	 * Constructor completo.
         * @param property La propiedad de la entidad.
         * @param value El valor a comparar con la propiedad.
         * @param property La operacion a realizar entre la propiedad y el valor. 
         */
	public Restriction(String property, Object value, Operation operation) {
		this.property = property;
		this.value = value;
		this.operation = operation;
	}
	
	/**
	 * 
	 * @return La operacion utilizada
	 */
	public Operation getOperation() {
		return this.operation;

	}

	/**
	 * 
	 * @return La propiedad utilizada
	 */
	public String getProperty() {
		return getOperation()==Restriction.Operation.LIKE?"upper("+ this.property+")":this.property;
	}
	
	/**
	 * 
	 * @return El valor utilizado. Si LIKE esta seteado como operacion, devuelve
	 * %value%, siendo value el string guardado.
	 */
	public Object getValue() {
		return getOperation()==Restriction.Operation.LIKE?"%"+this.value.toString().toUpperCase()+"%":this.value;
	}

	/**
	 * 
	 * @param operation La operacion a utilizar, 
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;

	}
	
	/**
	 * 
	 * @param property La propiedad de la entidad a utilizar
	 */
	public void setProperty(String property) {
		this.property = property;

	}

	/**
	 * 
	 * @param value El valor a comparar con la propiedad
	 */
	public void setValue(Object value) {
		this.value = value;
	}
}
