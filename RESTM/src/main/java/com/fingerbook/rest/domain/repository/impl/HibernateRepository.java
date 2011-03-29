package com.fingerbook.rest.domain.repository.impl;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.fingerbook.rest.domain.repository.Repository;
import com.fingerbook.rest.domain.util.Order;
import com.fingerbook.rest.domain.util.Page;
import com.fingerbook.rest.domain.util.Restriction;

/**
 * 
 * Implementacion en Hibernate de la interfaz generica DAO
 * 
 * @param <T> Clase del modelo utilizado en el DAO correspondiente a una entidad
 */
public class HibernateRepository<T> implements Repository<T> {

    /*
     * Guardo la clase de la entidad utilizada por el DAO
     */
    private Class<T> domainClass = getDomainClass();
    
    /*
     * Sesion de Hibernate
     */
    private SessionFactory sessionFactory;

    /**
     * Seteo la sesion a utilizar
     * @param sessionFactory La sesion de Hibernate a utilizar
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
    }
    
    protected Session getSession() {
            return sessionFactory.getCurrentSession();
    }
    
    @SuppressWarnings("unchecked")
    protected Class<T> getDomainClass() {
        if (domainClass == null) {
            ParameterizedType thisType = (ParameterizedType) getClass().getGenericSuperclass();
            domainClass = (Class<T>) thisType.getActualTypeArguments()[0];
        }
        return domainClass;
    }
    
    @Override
    public Page<T> list(int page, int size, Collection<Restriction> restrictions,
            Collection<Order> orders) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(getDomainClass());
        
        /* Mapeo las restricciones y ordenes a criterios de busqueda de Hibernate */        
        if (restrictions!=null) {

            for (Restriction restriction : restrictions) {
                criteria.add(RestrictionMapping.getCriterion(restriction));
            }
        }
        if (orders!=null) {
            for (Order order : orders) {
                criteria.addOrder(RestrictionMapping.getOrder(order));
            }
        }
        /* Fijo el elemento inicial del resultado */
        criteria.setFirstResult(page*size);
        /* Fijo el tama�o del resultado */
        criteria.setMaxResults(size);
        
        return new Page<T>(criteria.list(), page*size, size);
    }
    
    @Override
    public Page<T> list(int page, int size) {
        return list(page,size,null,null);
    }

    @Override
    public void remove(T entity) {
        Session session = getSession();
        session.delete(entity);
        session.flush();
    }

    @Override
    public Page<T> list() {
        return list(0,Integer.MAX_VALUE,null,null);
    }

    @Override
    public Page<T> list(Restriction restriction) {
        Collection<Restriction> restrictions = new ArrayList<Restriction>();
        restrictions.add(restriction);
        return list(0,Integer.MAX_VALUE,restrictions,null);
    }

    @Override
    public Page<T> list(Order order) {
        Collection<Order> orders = new ArrayList<Order>();
        orders.add(order);
        return list(0,Integer.MAX_VALUE,null,orders);
    }

    @Override
    public Page<T> list(int page, int size, Order order) {
        Collection<Order> orders = new ArrayList<Order>();
        orders.add(order);
        return list(page,size,null,orders);
    }

    @Override
    public Page<T> list(Collection<Restriction> restrictions) {
        return list(0,Integer.MAX_VALUE,restrictions,null);
    }

    @Override
    public Page<T> list(int page, int size,
            Collection<Restriction> restrictions) {
        return list(page,size,restrictions,null);
    }

    @Override
    public void add(T entity) {
        Session session = getSession();
        session.save(entity);
        session.flush(); 
    }

}
