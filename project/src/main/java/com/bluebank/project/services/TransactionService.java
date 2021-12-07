package com.bluebank.project.services;

import java.util.List;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluebank.project.dtos.DepositDTO;
import com.bluebank.project.dtos.TransactionDTO;
import com.bluebank.project.dtos.WithdrawDTO;
import com.bluebank.project.dtos.TransferenceDTO;
import com.bluebank.project.enums.TransactionTypeEnum;
import com.bluebank.project.exception.ConstraintException;
import com.bluebank.project.exception.ResourceNotFoundException;
import com.bluebank.project.exception.TransactionException;
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

	@Autowired
	ClientService clientService;

	@Autowired
	AccountService accountService;

	public Transaction createTransaction(Transaction transaction) {
		return this.transactionRepository.save(transaction);
	}

	@Transactional
	public List<Transaction> showAllTransactions() {
		return this.transactionRepository.findAll();
	}

	@Transactional
	public List<TransactionDTO> showTransactionsByClientCpfCnpj(String cpfcnpj) throws ResourceNotFoundException{
		clientService.simpleSearchByCpfcnpj(cpfcnpj);
		List <Transaction> transactions = transactionRepository.findByAccountId_ClientId_Cpfcnpj(cpfcnpj);
		List <Transaction> receivedTransferences = transactionRepository.findByDestinationAccountId_ClientId_Cpfcnpj(cpfcnpj);
		transactions.addAll(receivedTransferences);
		return transactionMapper.updateDtoFromTransacoes(transactions);
	}  
  
	@Transactional
  public List<TransactionDTO> showTransactionsByAccountId(Long id) throws ResourceNotFoundException{
		accountService.simpleSearchById(id);
		List <Transaction> transactions = transactionRepository.findByAccountId(id);
		List <Transaction> receivedTransferences = transactionRepository.findByDestinationAccountId(id);
		transactions.addAll(receivedTransferences);
		return transactionMapper.updateDtoFromTransacoes(transactions);
	}
	
	public double showAccountBalanceById(Long id) throws ResourceNotFoundException{
		Account account = accountService.simpleSearchById(id);
		return account.getBalance();
	}

	@Transactional
	public WithdrawDTO withdrawAmount(Long id, Transaction transaction) throws ResourceNotFoundException, TransactionException, PersistenceException, ConstraintException{
		transaction.setAccount(accountService.simpleSearchById(id));
		transaction.setTransactionType(TransactionTypeEnum.SAQ);
		transaction.setTransactionDate(java.util.Calendar.getInstance().getTime());
		transaction.setPreviousBalance(transaction.getAccount().getBalance());
		transaction.setCurrentBalance(transaction.getAccount().getBalance());
		transaction.setValue(transaction.getValue());
		
		WithdrawDTO withdrawDTO = new WithdrawDTO();
		double valorSaque = transaction.getValue();
		if (valorSaque >= transaction.getCurrentBalance()) {
			throw new TransactionException("Valor de saque maior que o saldo disponível");
		} else {
			Account conta = transaction.getAccount();
			conta.setBalance(conta.getBalance() - valorSaque);
			transaction.setCurrentBalance(conta.getBalance());
		}
		transactionMapper.updateWithdrawDtoFromTransaction(transaction, withdrawDTO);
		createTransaction(transaction);
		return withdrawDTO;
	}

	@Transactional
	public DepositDTO depositAmount(Long id, Transaction transaction) throws ResourceNotFoundException, TransactionException, PersistenceException{
		transaction.setAccount(accountService.simpleSearchById(id));
		transaction.setTransactionType(TransactionTypeEnum.DEP);
		transaction.setTransactionDate(java.util.Calendar.getInstance().getTime());
		transaction.setPreviousBalance(transaction.getAccount().getBalance());
		transaction.setCurrentBalance(transaction.getAccount().getBalance());
		transaction.setValue(transaction.getValue());
		
		DepositDTO depositDTO = new DepositDTO();
		double moneyAmount = transaction.getValue();
		if (moneyAmount < 0.0) {
			throw new TransactionException("Valor de depósito inválido");
		} else {
			Account account = transaction.getAccount();
			account.setBalance(account.getBalance() + moneyAmount);
			transaction.setCurrentBalance(account.getBalance());
		}
		transactionMapper.updateDepositDtoFromTransaction(transaction, depositDTO);
		createTransaction(transaction);
		return depositDTO;
	}

	@Transactional
	public TransferenceDTO transferAmount(Long accountId, Long destinationAccountId, Transaction transaction) throws ResourceNotFoundException, TransactionException, PersistenceException{
		transaction.setAccount(accountService.simpleSearchById(accountId));
		transaction.setTransactionType(TransactionTypeEnum.TRA);
		transaction.setTransactionDate(java.util.Calendar.getInstance().getTime());
		transaction.setPreviousBalance(transaction.getAccount().getBalance());
		transaction.setCurrentBalance(transaction.getAccount().getBalance());
		transaction.setValue(transaction.getValue());
		transaction.setDestinationAccount(accountService.simpleSearchById(destinationAccountId));

		TransferenceDTO transferenceDTO = new TransferenceDTO();
		double moneyAmount = transaction.getValue();
		if (moneyAmount <= 0.0) {
			throw new TransactionException("Valor de transferência inválido");
		} else if (moneyAmount >= transaction.getCurrentBalance()) {
			throw new TransactionException("Valor de saque maior que o saldo disponível");
		} else {
			Account account = transaction.getAccount();
			Account destinationAccount = transaction.getDestinationAccount();
			account.setBalance(account.getBalance() - moneyAmount);
			destinationAccount.setBalance(destinationAccount.getBalance() + moneyAmount);
			transaction.setCurrentBalance(account.getBalance());
		}
		transactionMapper.updateTransferenceDtoFromTransaction(transaction, transferenceDTO);
		createTransaction(transaction);
		return transferenceDTO;
	}
}

