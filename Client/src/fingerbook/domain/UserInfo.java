package fingerbook.domain;

import java.io.Serializable;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = 6747532322707458976L;
	private Long userInfoId;
	private String user;
	private String mail;
	
	public Long getUserInfoId() {
		return userInfoId;
	}
	public void setUserInfoId(Long userInfoId) {
		this.userInfoId = userInfoId;
	}
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
