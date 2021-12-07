package com.bluebank.project.models;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.bluebank.project.enums.TransactionTypeEnum;

@Entity
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne
//  @JoinColumn(name = "id_conta")
  private Account account;

  @NotNull
  private TransactionTypeEnum transactionType;
  
  @NotNull
  private Date transactionDate;
  
  @NotNull
  private double previousBalance;

  @NotNull
  private double currentBalance;
  
  @OneToOne
  private Account destinationAccount;
  
  private Date dataAgendTransacao; // TODO: depende do lambda
  
  private Date dataExecTransacao; // TODO: depende do lambda

	@NotBlank
  private Double value;

  public Transaction() {
    super();
  }

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Account getAccount() {
		return account;
	}
	
	public void setAccount(Account account) {
		this.account = account;
	}
	
	public TransactionTypeEnum getTransactionType() {
		return transactionType;
	}
	
	public void setTransactionType(TransactionTypeEnum transactionType) {
		this.transactionType = transactionType;
	}
	
	public Date getTransactionDate() {
		return transactionDate;
	}
	
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public double getPreviousBalance() {
		return previousBalance;
	}
	
	public void setPreviousBalance(double previousBalance) {
		this.previousBalance = previousBalance;
	}
	
	public double getCurrentBalance() {
		return currentBalance;
	}
	
	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}
	
	public Account getDestinationAccount() {
		return destinationAccount;
	}
	
	public void setDestinationAccount(Account destinationAccount) {
		this.destinationAccount = destinationAccount;
	}
	
	public Date getDataAgendTransacao() {
		return dataAgendTransacao;
	}
	
	public void setDataAgendTransacao(Date dataAgendTransacao) {
		this.dataAgendTransacao = dataAgendTransacao;
	}
	
	public Date getDataExecTransacao() {
		return dataExecTransacao;
	}
	
	public void setDataExecTransacao(Date dataExecTransacao) {
		this.dataExecTransacao = dataExecTransacao;
	}
	
	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
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
		Transaction other = (Transaction) obj;
		return id == other.id;
	}
	
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", account=" + account + ", transactionType=" + transactionType
				+ ", transactionDate=" + transactionDate + ", previousBalance=" + previousBalance + ", currentBalance="
				+ currentBalance + ", destinationAccount=" + destinationAccount + ", dataAgendTransacao="
				+ dataAgendTransacao + ", dataExecTransacao=" + dataExecTransacao + ", value=" + value + "]";
	}  
  
}