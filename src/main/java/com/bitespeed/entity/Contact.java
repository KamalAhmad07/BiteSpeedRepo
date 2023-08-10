package com.bitespeed.entity;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

// i can use lombok also.
@Entity
@Table(name = "contact")
public class Contact {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @Column(nullable = true)
 private String phoneNumber;

 @Column(nullable = true)
 private String email;

 @Column(nullable = true)
 private Long linkedId;

 @Column(nullable = false)
 private String linkPrecedence;

 @Column(nullable = false, updatable = false)
 private LocalDateTime createdAt;

 @Column(nullable = false)
 private LocalDateTime updatedAt;

 @Column(nullable = true)
 private LocalDateTime deletedAt;

 public Contact() {
     this.createdAt = LocalDateTime.now();
     this.updatedAt = LocalDateTime.now();
     this.linkPrecedence = "primary";
 }

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getPhoneNumber() {
	return phoneNumber;
}

public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public Long getLinkedId() {
	return linkedId;
}

public void setLinkedId(Long linkedId) {
	this.linkedId = linkedId;
}

public String getLinkPrecedence() {
	return linkPrecedence;
}

public void setLinkPrecedence(String linkPrecedence) {
	this.linkPrecedence = linkPrecedence;
}

public LocalDateTime getCreatedAt() {
	return createdAt;
}

public void setCreatedAt(LocalDateTime createdAt) {
	this.createdAt = createdAt;
}

public LocalDateTime getUpdatedAt() {
	return updatedAt;
}

public void setUpdatedAt(LocalDateTime updatedAt) {
	this.updatedAt = updatedAt;
}

public LocalDateTime getDeletedAt() {
	return deletedAt;
}

public void setDeletedAt(LocalDateTime deletedAt) {
	this.deletedAt = deletedAt;
}

@Override
public String toString() {
	return "Contact [id=" + id + ", phoneNumber=" + phoneNumber + ", email=" + email + ", linkedId=" + linkedId
			+ ", linkPrecedence=" + linkPrecedence + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
			+ ", deletedAt=" + deletedAt + "]";
}

 // Getters and Setters
 // Other methods as needed
}
