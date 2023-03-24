package pojo;
public class Client {
	private String ClientName;
	private String passId;
	private String TrueName;
	private String Country;
	private String CreStyle;
	private String CreId;
	private String PhoneId;
	private String addif;
	public Client(String clientName, String passId, String trueName, String country, String creStyle, String creId,
			String phoneId, String addif) {
		super();
		ClientName = clientName;
		this.passId = passId;
		TrueName = trueName;
		Country = country;
		CreStyle = creStyle;
		CreId = creId;
		PhoneId = phoneId;
		this.addif = addif;
	}
	public Client(String clientName, String passId, String trueName, String creStyle, String creId) {
		super();
		ClientName = clientName;
		this.passId = passId;
		TrueName = trueName;
		CreStyle = creStyle;
		CreId = creId;
	}
	public String getClientName() {
		return ClientName;
	}
	public void setClientName(String clientName) {
		ClientName = clientName;
	}
	public String getPassId() {
		return passId;
	}
	public void setPassId(String passId) {
		this.passId = passId;
	}
	public String getTrueName() {
		return TrueName;
	}
	public void setTrueName(String trueName) {
		TrueName = trueName;
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	public String getCreStyle() {
		return CreStyle;
	}
	public void setCreStyle(String creStyle) {
		CreStyle = creStyle;
	}
	public String getCreId() {
		return CreId;
	}
	public void setCreId(String creId) {
		CreId = creId;
	}
	public String getPhoneId() {
		return PhoneId;
	}
	public void setPhoneId(String phoneId) {
		PhoneId = phoneId;
	}
	public String getAddif() {
		return addif;
	}
	public void setAddif(String addif) {
		this.addif = addif;
	}
	
	@Override
	public String toString() {
		return "Client [ClientName=" + ClientName + ", passId=" + passId + ", TrueName=" + TrueName + ", Country="
				+ Country + ", CreStyle=" + CreStyle + ", CreId=" + CreId + ", PhoneId=" + PhoneId + ", addif=" + addif
				+ "]";
	}

}
