package com.bluebank.project.dtos;


public class TransferenceDTO implements TransactionDTO {

	private Long account;
	private String transactionType;
	private String transactionDate;
	private double previousBalance;
	private double currentBalance;
	private Long destinationAccount;
	private Double value;
	  
	public TransferenceDTO() {
	}

	public Long getAccount() {
		return account;
	}
	
	public void setAccount(Long account) {
		this.account = account;
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public String getTransactionDate() {
		return transactionDate;
	}
	
	public void setTransactionDate(String transactionDate) {
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
	
	public Long getDestinationAccount() {
		return destinationAccount;
	}
	
	public void setDestinationAccount(Long destinationAccount) {
		this.destinationAccount = destinationAccount;
	}
	
	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
  
}
