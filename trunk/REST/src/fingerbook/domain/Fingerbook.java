package fingerbook.domain;

public class Fingerbook {

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
