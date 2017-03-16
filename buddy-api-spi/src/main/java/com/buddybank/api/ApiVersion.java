package com.buddybank.api;

public class ApiVersion {

	private int major;
	private int minor;
	private int build;

	public ApiVersion(int major, int minor, int build) {
		this.major = major;
		this.minor = minor;
		this.build = build;
	}
	
	public int getMajor() {
		return major;
	}
	public void setMajor(int major) {
		this.major = major;
	}
	public int getMinor() {
		return minor;
	}
	public void setMinor(int minor) {
		this.minor = minor;
	}
	public int getBuild() {
		return build;
	}
	public void setBuild(int build) {
		this.build = build;
	}
}
