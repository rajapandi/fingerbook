package com.fingerbook.rest.domain.repository.impl;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.fingerbook.rest.domain.util.Order;
import com.fingerbook.rest.domain.util.Restriction;

public class RestrictionMapping {

    /**
     * Devuelve un criterio de busqueda de Hibernate para una instancia de
     * restriction.
     * @param restriction El objeto con la restriccion a mapear.
     * @return El criterio de busqueda para hibernate
     */
    public static Criterion getCriterion(Restriction restriction) {
        
        Criterion ret;
        
        if (restriction == null) {
            return null;
        }
        switch(restriction.getOperation()) {
            case GE:
                ret = Restrictions.ge(
                        restriction.getProperty(), 
                        restriction.getValue());
                break;
            case GT:
                ret = Restrictions.gt(
                        restriction.getProperty(), 
                        restriction.getValue());
                break;
            case ISNULL:
                ret = Restrictions.isNull(
                        restriction.getProperty());
                break;
            case LE:
                ret = Restrictions.le(
                                restriction.getProperty(), 
                                restriction.getValue());
                break;
            case LIKE:
                ret = Restrictions.like(
                                restriction.getProperty(), 
                                (String)restriction.getValue(), 
                                MatchMode.ANYWHERE);
                break;                
            case NE:
                ret = Restrictions.ne(
                        restriction.getProperty(), 
                        restriction.getValue());
                break;                
            case NOTNULL:
                ret = Restrictions.isNotNull(restriction.getProperty());
                break;
            case EQ: default:
                ret = Restrictions.eq(
                        restriction.getProperty(), 
                        restriction.getValue());
                break;
        }
        return ret;
    }
    
    /**
     * Devuelve un criterio de ordenamiento para Hibernate.
     * @param order El criterio de ordenamiento.
     * @return El criterio de ordenamiento para Hibernate.
     */
    public static org.hibernate.criterion.Order getOrder(Order order) {
        
        if (order==null) {
            return null;
        }
        
        org.hibernate.criterion.Order ret = null;
        
        switch (order.getOrder()) {
            case DESC:
                ret = org.hibernate.criterion.Order.desc(order.getProperty());
                break;             
            case ASC: default:
                ret = org.hibernate.criterion.Order.asc(order.getProperty());
                break;                
        }
        return ret;
    }
    
}
