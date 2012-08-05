package com.fingerbook.rest.domain.repository;

import java.util.Collection;

import com.fingerbook.rest.domain.util.Order;
import com.fingerbook.rest.domain.util.Page;
import com.fingerbook.rest.domain.util.Restriction;

/**
 * Esta interface define el comportamiento de los servicios para
 * manipular colecciones de entidades.
 */
public interface Repository<T> {
	
        /**
         * 
         * @param entity La entidad a agregar
         */
        public void add(T entity);
    
	/**
	 * 
	 * @param entity La entidad a borrar
	 */
	public void remove(T entity);
	
	/**
         * @return Toda la coleccion de entidades
         */
        public Page<T> list();
        
        /**
         * @param first Numero de pagina
         * @param page Tama�o de la busqueda
         * @return Una coleccion de objectos entity
         */
        public Page<T> list(int page, int size);      
        
        /**
         * @param restrictions Restricciones sobre las propiedades 
         * @return Una coleccion de objectos entity
         */
        public Page<T> list(Restriction restriction);
        
        /**
         * @param order Order by property
         * @return Una coleccion de objectos entity
         */
        public Page<T> list(Order order);

        /**
         * @param page Numero de pagina
         * @param size Tama�o de la busqueda
         * @param order Order by property
         * @return Una coleccion de objectos entity
         */
        public Page<T> list(int page, int size, Order order);
        
        /**
         * @param restrictions Restricciones sobre las propiedades 
         * @return Una coleccion de objectos entity
         */
        public Page<T> list(Collection<Restriction> restrictions);
        
        /**
         * @param page Numero de la pagina
         * @param size Tama�o de la busqueda
         * @param restrictions Restricciones sobre las propiedades 
         * @return Una coleccion de objectos entity
         */
        public Page<T> list(int page, int size, 
                Collection<Restriction> restrictions);
        
	/**
	 * @param page Numero de la pagina
	 * @param size Tama�o de la busqueda
	 * @param restrictions Restricciones sobre las propiedades 
	 * @param order Order by property
	 * @return Una coleccion de objectos entity
	 */
	public Page<T> list(int page,int size,
	        Collection<Restriction> restrictions, Collection<Order> orders);
	
	
}

