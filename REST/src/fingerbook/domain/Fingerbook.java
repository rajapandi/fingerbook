package fingerbook.domain;

import java.io.Serializable;

public class Fingerbook implements Serializable {

	private FingerprintsTest fingerPrints;
	private UserInfo userInfo;
	
	public FingerprintsTest getFingerPrints() {
		return fingerPrints;
	}
	public void setFingerPrints(FingerprintsTest fingerPrints) {
		this.fingerPrints = fingerPrints;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	
}
