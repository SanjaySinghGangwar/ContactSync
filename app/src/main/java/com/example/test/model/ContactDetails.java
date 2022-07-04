package com.example.test.model;

public class ContactDetails {
	String id;
	Contacts detail;

	public ContactDetails(String id, Contacts detail) {
		this.id = id;
		this.detail = detail;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Contacts getDetail() {
		return detail;
	}

	public void setDetail(Contacts detail) {
		this.detail = detail;
	}
}
