package fingerbook.domain;

import java.io.Serializable;

public class UserInfo implements Serializable {

	private String user;
	private String mail;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	
}
