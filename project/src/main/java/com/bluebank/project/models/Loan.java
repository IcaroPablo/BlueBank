package com.bluebank.project.models;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Loan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  private Client client;

  @NotNull
  @JsonFormat(pattern = "dd/MM/yyyy")
  private Date startDate;

  @NotNull
  @JsonFormat(pattern = "dd/MM/yyyy")
  private Date endDate;

  @NotNull
  private double borrowedAmount;

  @NotNull
  private double fees;

  @NotNull
  private int installments;

  public Loan() {
    super();
  }

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Client getClient() {
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
//	public String getNumeroContrato() {
//		return numeroContrato;
//	}
//	
//	public void setNumeroContrato(String numeroContrato) {
//		this.numeroContrato = numeroContrato;
//	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public double getBorrowedAmount() {
		return borrowedAmount;
	}
	
	public void setBorrowedAmount(double borrowedAmount) {
		this.borrowedAmount = borrowedAmount;
	}
	
	public double getFees() {
		return fees;
	}
	
	public void setFees(double fees) {
		this.fees = fees;
	}
	
	public int getInstallments() {
		return installments;
	}
	
	public void setInstallments(int installments) {
		this.installments = installments;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Loan other = (Loan) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Loan [id=" + id + ", client=" + client + ", numeroContrato=" + ", startDate="
				+ startDate + ", endDate=" + endDate + ", borrowedAmount=" + borrowedAmount + ", fees=" + fees
				+ ", installments=" + installments + "]";
	}
  
}
