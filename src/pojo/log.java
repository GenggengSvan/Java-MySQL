package pojo;

import java.util.Objects;

public class log {

	private String 用户名;
	private String passId;
	private String 身份;
	
	public log(String 用户名, String passId, String 身份) {
		super();
		this.用户名 = 用户名;
		this.passId = passId;
		this.身份 = 身份;
	}
	

	public log() {
		super();
	}


	public String get用户名() {
		return 用户名;
	}

	public void set用户名(String 用户名) {
		this.用户名 = 用户名;
	}

	public String getPassId() {
		return passId;
	}

	public void setPassId(String passId) {
		this.passId = passId;
	}

	public String get身份() {
		return 身份;
	}

	public void set身份(String 身份) {
		this.身份 = 身份;
	}

	@Override
	public int hashCode() {
		return Objects.hash(passId, 用户名, 身份);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		log other = (log) obj;
		return Objects.equals(passId, other.passId) && Objects.equals(用户名, other.用户名) && Objects.equals(身份, other.身份);
	}

	@Override
	public String toString() {
		return "log [用户名=" + 用户名 + ", passId=" + passId + ", 身份=" + 身份 + "]";
	};
	

}
