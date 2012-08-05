package com.fingerbook.rest.domain.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.NaturalId;

import com.fingerbook.rest.domain.exception.InconsistentEntityException;


@Entity(name = "WebUser")
public class User extends com.fingerbook.rest.domain.entities.Entity {

        public static final String ADMIN = "A";
        public static final String PUBLIC = "P";
        
        @Column(length=32)
        @NaturalId
        private String username;

        @Column(nullable=false, length=32)
        private String password;
      
        @Column(nullable=false, length=64, unique=true)
        private String email;

        @Column(nullable=false)
        private String usertype;

        @Column
        private Date birthdate;

        @Column
        private Date lastLoginDate;

        @Column
        private Date registrationDate;
        
        public User(String username, String name, String lastName, String password,
                String email, Date birthdate) {
            setUsername(username);
            setPassword(password);
            setEmail(email);
            setBirthdate(birthdate);
            this.setType(PUBLIC);
        }
        
        public User() {
            // TODO Auto-generated constructor stub
        }
      
        public String getUsername(){
                return username;
        }
        
        public void setUsername(String username){
            if (username==null) {
                throw new InconsistentEntityException("username");
            }
            this.username=username;
        }
        
        public Date getBirthdate() {
                return birthdate;
        }

        public void setBirthdate(Date birthdate) {
            if (birthdate==null) {
                throw new InconsistentEntityException("birthdate");
            }
            this.birthdate = birthdate;
        }

        public Date getLastLoginDate() {
                return lastLoginDate;
        }

        public void setLastLoginDate(Date lastLoginDate) {
            if (lastLoginDate==null) {
                throw new InconsistentEntityException("lastLoginDate");
            }
            this.lastLoginDate = lastLoginDate;
        }

        public Date getRegistrationDate() {
            if (lastLoginDate==null) {
                throw new InconsistentEntityException("lastLoginDate");
            }
            return registrationDate;
        }

        public void setRegistrationDate(Date registrationDate) {
            if (registrationDate==null) {
                throw new InconsistentEntityException("registrationDate");
            }
            this.registrationDate = registrationDate;
        }

        public String getType() {
                return usertype;
        }
        public void setType(String type) {
                if( ADMIN.equalsIgnoreCase(type))
                        this.usertype = type;
                else
                        this.usertype = PUBLIC;
        }
     
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            if (password==null) {
                throw new InconsistentEntityException("password");
            }
            this.password = password;
        }
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            if (email==null) {
                throw new InconsistentEntityException("email");
            }
            this.email = email;
        }
        
        public boolean isPublic() {
            return PUBLIC.equalsIgnoreCase(usertype);
        }

        public boolean isAdmin() {
            return ADMIN.equalsIgnoreCase(usertype);
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((email == null) ? 0 : email.hashCode());
                result = prime * result
                                + ((username == null) ? 0 : username.hashCode());
                return result;
        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                User other = (User) obj;
                if (email == null) {
                        if (other.email != null)
                                return false;
                } else if (!email.equals(other.email))
                        return false;
                if (username == null) {
                        if (other.username != null)
                                return false;
                } else if (!username.equals(other.username))
                        return false;
                return true;
        }
}

