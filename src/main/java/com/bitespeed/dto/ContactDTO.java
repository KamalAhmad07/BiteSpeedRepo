// ContactDTO.java
package com.bitespeed.dto;

import java.util.LinkedHashSet;
import java.util.List;

public class ContactDTO {
    private Long primaryContatctId;
    private String email;
    private String phoneNumber;
    private LinkedHashSet<String> emails;
    private LinkedHashSet<String> phoneNumbers;
    private List<Long> secondaryContactIds;
	public Long getPrimaryContatctId() {
		return primaryContatctId;
	}
	
	public void setPrimaryContatctId(Long primaryContatctId) {
		this.primaryContatctId = primaryContatctId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public LinkedHashSet<String> getEmails() {
		return emails;
	}
	public void setEmails(LinkedHashSet<String> emails) {
		this.emails = emails;
	}
	public LinkedHashSet<String> getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(LinkedHashSet<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
	public List<Long> getSecondaryContactIds() {
		return secondaryContactIds;
	}
	public void setSecondaryContactIds(List<Long> secondaryContactIds) {
		this.secondaryContactIds = secondaryContactIds;
	}  
}
