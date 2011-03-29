package com.fingerbook.rest.domain.repository.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fingerbook.rest.domain.entities.User;
import com.fingerbook.rest.domain.exception.InvalidPasswordException;
import com.fingerbook.rest.domain.exception.InvalidUsernameException;
import com.fingerbook.rest.domain.repository.UserRepository;
import com.fingerbook.rest.domain.util.Page;
import com.fingerbook.rest.domain.util.Restriction;

@Repository
public class HibernateUserRepository  extends HibernateRepository<User> implements UserRepository {

    SessionFactory sessionFactory;
    
    @Autowired
    public HibernateUserRepository(SessionFactory sessionFactory) {
            super.setSessionFactory(sessionFactory);
            this.sessionFactory = sessionFactory;
    }

    private boolean exists(User user) {
        Page<User> users = super.list(new Restriction("username",user.getUsername()));
        return users.size()>0;
    }
    
    private boolean emailExists(String email) {
        Page<User> users = super.list(new Restriction("email",email));
        return users.size()>0;
    }

    @Override
    public User getUserForCredential(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        Page<User> users = super.list(new Restriction("username",username));
        if (users.size()==0) {
            throw new InvalidUsernameException(username);
        }
        if (!users.get(0).getPassword().equals(password)) {
            throw new InvalidPasswordException(password);
        }
        return users.get(0);
    }


 
}
