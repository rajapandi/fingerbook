package com.fingerbook.rest.domain.repository;

import com.fingerbook.rest.domain.entities.User;
import com.fingerbook.rest.domain.exception.InvalidPasswordException;
import com.fingerbook.rest.domain.exception.InvalidUsernameException;


public interface UserRepository extends Repository<User> {
    

    /**
     * Valida un usuario con su contrasenia
     * @param username El nombre de usuario
     * @param password La contrasenia
     * @return El usuario correspondiente a ese usuario y contrasenia. Null
     * en caso de que la contrasenia sea invalida o el usuario no exista
     */
    public User getUserForCredential(String username,String password) 
                    throws InvalidUsernameException, InvalidPasswordException;
   
}
