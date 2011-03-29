package com.fingerbook.rest.domain.util;

public class Order {

        /**
         * Criterio de ordenamiento sobre la coleccion de entidades 
         *
         */
	public static enum OrderType {
	    DESC,ASC
	}
	
	private String property;
	private OrderType order;
	
	/**
         * @PARAM PROPERTY Propiedad para usar como criterio de ordeamiento.
         * @PARAM ORDER Order descendente o ascendente.
         */
	
	public Order(String property, OrderType order) {
		this.setProperty(property);
		this.setOrder(order);	
	}
	
	/**
         * CONSTRUCTOR DEFAULT. SETEA ASC COMO ORDEN DEL RESULTADO.
         * @PARAM PROPERTY Propiedad para usar como criterio de ordeamiento.
         * @PARAM ORDER Order descendente o ascendente.
         */
	public Order(String property) {
		this.setProperty(property);
		this.setOrder(Order.OrderType.ASC);
	}
	
	/**
	 * 
	 * @return La propiedad utilizada como criterio de ordenamiento.
	 */
	public String getProperty() {
		return property;
	}
	
	/**
         * 
         * @param La propiedad utilizada como criterio de ordenamiento.
         */	
	public void setProperty(String property) {
		this.property = property;
	}
	
	/**
	 * 
	 * @return El orden utilizado
	 */
	public OrderType getOrder() {
		return order;
	}
	
	/**
	 * 
	 * @param order El orden utilizado
	 */
	public void setOrder(OrderType order) {
	    this.order = order;
	}
}
