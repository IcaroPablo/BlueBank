package com.bluebank.project.models;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import com.bluebank.project.enums.ClientStatusEnum;

@Entity
public class Client {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String name;
	
	@NotBlank
	@Column(unique = true)
	private String cpfcnpj;
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String cep;
	
	@NotBlank
	private String phoneNumber;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String type;
	
	private ClientStatusEnum status;
	
	public Client() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpfcnpj() {
		return cpfcnpj;
	}

	public void setCpfcnpj(String cpfcnpj) {
		this.cpfcnpj = cpfcnpj;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ClientStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ClientStatusEnum status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cep, cpfcnpj, email, name, password, phoneNumber, status, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return Objects.equals(cep, other.cep) && Objects.equals(cpfcnpj, other.cpfcnpj)
				&& Objects.equals(email, other.email) && Objects.equals(name, other.name)
				&& Objects.equals(password, other.password) && Objects.equals(phoneNumber, other.phoneNumber)
				&& status == other.status && Objects.equals(type, other.type);
	}

	
}
