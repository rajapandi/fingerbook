package com.fingerbook.rest.domain.repository.impl;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransientObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.fingerbook.rest.domain.entities.Entity;
import com.fingerbook.rest.domain.repository.EntityResolver;



@Component
public class HibernateEntityResolver implements EntityResolver {

    private final SessionFactory sessionFactory;

    @Autowired
    public HibernateEntityResolver(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findById(Class<T> type, Long id) {
            try {
                    return (T) getSession().get(type, id);
            } catch (HibernateException ex) {
                    throw new HibernateException("Problem while fetching (" + type.getSimpleName() + ", " + id.toString() + ")", ex);
            }
    }

    @Override
    public Long getId(final Object object) {
            Assert.isInstanceOf(Entity.class, object, "This entity resolver only hanldes objects implementing PersistentEntity");
            try {
                    getSession().flush();
                    Long id = ((Entity) object).getId();
                    if (id == null) {
                            throw new TransientObjectException("Object doesn't have an id associated!");
                    }
                    return id;
            } catch (HibernateException ex) {
                    throw new HibernateException("Problem while retrieving id for " + object.toString(), ex);
            }
    }

    private Session getSession() {
            return sessionFactory.getCurrentSession();
    }
 
}