package com.bluebank.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluebank.project.dtos.DepositDTO;
import com.bluebank.project.dtos.TransactionDTO;
import com.bluebank.project.dtos.WithdrawDTO;
import com.bluebank.project.dtos.TransferenceDTO;
import com.bluebank.project.enums.TransactionTypeEnum;
import com.bluebank.project.mappers.TransactionMapper;
import com.bluebank.project.models.Account;
import com.bluebank.project.models.Transaction;
import com.bluebank.project.repositories.ClientRepository;
import com.bluebank.project.repositories.AccountRepository;
import com.bluebank.project.repositories.TransactionRepository;

@Service
public class TransactionService {

  @Autowired
  TransactionRepository transactionRepository;
  
  @Autowired
  AccountRepository accountRepository;

  @Autowired
  ClientRepository clientRepository;

  @Autowired
  TransactionMapper transactionMapper;

  @Transactional
  public Transaction createTransaction(Transaction transaction) {
	  return this.transactionRepository.save(transaction);
  }

  @Transactional
  public List<Transaction> showAllTransactions() {
    return this.transactionRepository.findAll();
  }

  @Transactional
    public List<TransactionDTO> showTransactionsByClientCpfCnpj(String cpfcnpj) {
	    List <Transaction> transactions = transactionRepository.findByAccountId_ClientId_Cpfcnpj(cpfcnpj);
	    List <Transaction> receivedTransferences = transactionRepository.findByDestinationAccountId_ClientId_Cpfcnpj(cpfcnpj);
	    transactions.addAll(receivedTransferences);
	return transactionMapper.updateDtoFromTransacoes(transactions);
    
  }  
  
  	public List<TransactionDTO> showTransactionsByAccountId(Long id) {
		List <Transaction> transactions = transactionRepository.findByAccountId(id);
		List <Transaction> receivedTransferences = transactionRepository.findByDestinationAccountId(id);
		transactions.addAll(receivedTransferences);
		return transactionMapper.updateDtoFromTransacoes(transactions);
	}
	
	public double showAccountBalanceById(Long id) {
		Account account = accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conta Inexistente"));
		return account.getBalance();
	}

	public WithdrawDTO withdrawAmount(Long id, Transaction transaction) {
		transaction.setAccount(accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conta Inexistente")));
		transaction.setTransactionType(TransactionTypeEnum.SAQ);
		transaction.setTransactionDate(java.util.Calendar.getInstance().getTime());
		transaction.setPreviousBalance(transaction.getAccount().getBalance());
		transaction.setCurrentBalance(transaction.getAccount().getBalance());
		transaction.setValue(transaction.getValue());
		
		WithdrawDTO withdrawDTO = new WithdrawDTO();
		double valorSaque = transaction.getValue();
		if (valorSaque >= transaction.getCurrentBalance()) {
			throw new IllegalArgumentException("Valor de saque maior que o saldo disponível");
		} else {
			Account conta = transaction.getAccount();
			conta.setBalance(conta.getBalance() - valorSaque);
			transaction.setCurrentBalance(conta.getBalance());
		}
		transactionMapper.updateWithdrawDtoFromTransaction(transaction, withdrawDTO);
		transactionRepository.save(transaction);
		return withdrawDTO;
	}

	public DepositDTO depositAmount(Long id, Transaction transaction) {
		transaction.setAccount(accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conta Inexistente")));
		transaction.setTransactionType(TransactionTypeEnum.DEP);
		transaction.setTransactionDate(java.util.Calendar.getInstance().getTime());
		transaction.setPreviousBalance(transaction.getAccount().getBalance());
		transaction.setCurrentBalance(transaction.getAccount().getBalance());
		transaction.setValue(transaction.getValue());
		
		DepositDTO depositDTO = new DepositDTO();
		double moneyAmount = transaction.getValue();
		if (moneyAmount < 0.0) {
			throw new IllegalArgumentException("Valor de depósito inválido");
		} else {
			Account account = transaction.getAccount();
			account.setBalance(account.getBalance() + moneyAmount);
			transaction.setCurrentBalance(account.getBalance());
		}
		transactionMapper.updateDepositDtoFromTransaction(transaction, depositDTO);
		transactionRepository.save(transaction);
		return depositDTO;
	}

	public TransferenceDTO transferAmount(Long accountId, Long destinationAccountId, Transaction transaction){
		transaction.setAccount(accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("Conta Inexistente")));
		transaction.setTransactionType(TransactionTypeEnum.TRA);
		transaction.setTransactionDate(java.util.Calendar.getInstance().getTime());
		transaction.setPreviousBalance(transaction.getAccount().getBalance());
		transaction.setCurrentBalance(transaction.getAccount().getBalance());
		transaction.setValue(transaction.getValue());
		transaction.setDestinationAccount(accountRepository.findById(destinationAccountId).orElseThrow(() -> new IllegalArgumentException("Conta inexistente")));

		TransferenceDTO transferenceDTO = new TransferenceDTO();
		double moneyAmount = transaction.getValue();
		if (moneyAmount <= 0.0) {
			throw new IllegalArgumentException("Valor de transferência inválido");
		} else if (moneyAmount >= transaction.getCurrentBalance()) {
			throw new IllegalArgumentException("Valor de saque maior que o saldo disponível");
		} else {
			Account account = transaction.getAccount();
			Account destinationAccount = transaction.getDestinationAccount();
			account.setBalance(account.getBalance() - moneyAmount);
			destinationAccount.setBalance(destinationAccount.getBalance() + moneyAmount);
			transaction.setCurrentBalance(account.getBalance());
		}
		transactionMapper.updateTransferenceDtoFromTransaction(transaction, transferenceDTO);
		transactionRepository.save(transaction);
		return transferenceDTO;
	}
}

