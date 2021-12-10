package com.bluebank.project.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bluebank.project.enums.AccountTypeEnum;
import com.bluebank.project.exception.ConstraintException;
import com.bluebank.project.exception.ResourceNotFoundException;
import com.bluebank.project.exception.TransactionException;
import com.bluebank.project.models.Account;
import com.bluebank.project.models.Client;
import com.bluebank.project.models.Transaction;
import com.bluebank.project.repositories.AccountRepository;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class TransactionServiceTest {

	@Autowired
	TransactionService transactionService;
	@Autowired
	AccountService accountService;
	@Autowired
	ClientService clientService;
	@Autowired
	AccountRepository accountRepository;

	Client client = new Client();
	Client client2 = new Client();
	Account account = new Account();
	Account account2 = new Account();
	Transaction transaction = new Transaction();

	@BeforeEach
	public void setUp() throws ConstraintException, ResourceNotFoundException {
		// Para criação do cliente
		client.setId(1L);
		client.setName("BlueBank");
		client.setCpfcnpj("11122333445");
		client.setEmail("bluebank@gmail.com");
		client.setCep("64007-800");
		client.setPhoneNumber("(86) 98119-8357");
		client.setPassword("minhasenha123");
		client.setType("PJ");

		// Para criação de conta
		account.setId(1L);
		account.setAgency(1);
		account.setAccountType(AccountTypeEnum.CC);
		account.setBalance(5000.0);

		// Para criação de uma transação
		transaction.setId(1L);
		transaction.setValue(150.0);

		// Criação do cliente auxiliar
		client2.setId(2L);
		client2.setName("pedro");
		client2.setCpfcnpj("152671810");
		client2.setEmail("pedro@gmail.com");
		client2.setCep("64007-800");
		client2.setPhoneNumber("(86) 98379-8357");
		client2.setPassword("minhasenha123");
		client2.setType("PF");

		// Criação de conta auxiliar
		account2.setId(2L);
		account2.setAgency(1);
		account2.setAccountType(AccountTypeEnum.CC);
		account2.setBalance(5000.0);

		clientService.registerNewClient(client);
		clientService.registerNewClient(client2);
		accountService.registerNewAccount(client.getCpfcnpj(), account);
		accountService.registerNewAccount(client2.getCpfcnpj(), account2);

	}

	@Test
	@DisplayName("Um saque deverá ser realizado")
	@Order(1)
	public void testWithdrawAmount() throws PersistenceException, ResourceNotFoundException, TransactionException, ConstraintException {
		transactionService.withdrawAmount(1L, transaction);
		assertEquals((transaction.getPreviousBalance() - transaction.getValue()), transaction.getCurrentBalance());
	}
	
	@Test
	@DisplayName("Uma exceção de cliente inexistente na operação saque deverá ser lançada")
	@Order(2)
	public void testExceptionWithdrawAmount_Exception() {
		assertThrows(ResourceNotFoundException.class, () -> transactionService.withdrawAmount(3L, transaction));
	}
	
	@Test
	@DisplayName("Uma exceção de saldo insuficiente na operação saque deverá ser lançada")
	@Order(3)
	public void testExceptionWithdrawAmount_Exception2() {
		transaction.setId(2L);
		transaction.setValue(10000.0);
		assertThrows(TransactionException.class, () -> transactionService.withdrawAmount(2L, transaction));
	}

	@Test
	@DisplayName("Um depósito deverá ser realizado")
	@Order(4)
	public void testDepositAmount() throws PersistenceException, ResourceNotFoundException, TransactionException {
		transactionService.depositAmount(1L, transaction);
		assertEquals((transaction.getPreviousBalance() + transaction.getValue()), transaction.getCurrentBalance());
	}
	
	@Test
	@DisplayName("Uma exceção de cliente inexistente para operação depósito deverá ser lançada")
	@Order(5)
	public void testDepositAmount_Exception() {
		assertThrows(ResourceNotFoundException.class, () -> transactionService.depositAmount(3L, transaction));
	}
	
	@Test
	@DisplayName("Uma exceção de valor de depósito inválido para operação depósito deverá ser lançada")
	@Order(6)
	public void testDepositAmount_Exception2() {
		transaction.setId(2L);
		transaction.setValue(-100.0);
		assertThrows(TransactionException.class, () -> transactionService.depositAmount(2L, transaction));
	}

	@Test
	@DisplayName("O extrato de uma conta deverá ser encontrado")
	@Order(7)
	public void testShowTransactionsByAccountId() throws ResourceNotFoundException {
		assertNotNull(transactionService.showTransactionsByAccountId(1L));
	}

	@Test
	@DisplayName("Uma exceção de conta inexistente para operação extrato deverá ser lançada")
	@Order(8)
	public void testShowTransactionsByAccountId_Exception() {
		assertThrows(ResourceNotFoundException.class, () -> transactionService.showTransactionsByAccountId(3L));
	}
	
	@Test
	@DisplayName("O saldo da conta deverá ser encontrado")
	@Order(9)
	public void testShowAccountBalanceById() throws ResourceNotFoundException {
		double saldo = transactionService.showAccountBalanceById(1L);
		assertEquals(account.getBalance(), saldo);
	}
	
	@Test
	@DisplayName("Uma exceção de conta inexistente para operação saldo deverá ser lançada")
	@Order(10)
	public void testShowAccountBalanceById_Exception() {
		assertThrows(ResourceNotFoundException.class, () -> transactionService.showAccountBalanceById(3L));
	}

	@Test
	@DisplayName("Uma transferencia deverá ser realizada")
	@Order(11)
	@Transactional
	public void testTransferAmmount() throws PersistenceException, ResourceNotFoundException, TransactionException {
		double saldoAnteriorBlueBank =  accountRepository.findById(1L).get().getBalance();
		double saldoAnteriorPedro = accountRepository.findById(2L).get().getBalance();
		transactionService.transferAmount(1L, 2L, transaction);
		double saldoFinalBlueBank = accountRepository.findById(1L).get().getBalance();
		double saldoFinalPedro = accountRepository.findById(2L).get().getBalance();
		assertEquals(saldoAnteriorBlueBank - transaction.getValue() , saldoFinalBlueBank);
		assertEquals(saldoAnteriorPedro + transaction.getValue(), saldoFinalPedro);
	}
	
	@Test
	@DisplayName("Uma exceção de conta inexistente para operação transferência deverá ser lançada")
	@Order(12)
	public void testTransferAmmount_Exception() {
		assertThrows(ResourceNotFoundException.class, () -> transactionService.transferAmount(3L, 2L, transaction));
	}
	
	@Test
	@DisplayName("Uma exceção de saldo insuficiente na conta de origem para operação transferência deverá ser lançada")
	@Order(13)
	public void testTransferAmmount_Exception2() {
		transaction.setId(3L);
		transaction.setValue(10000.0);
		assertThrows(TransactionException.class, () -> transactionService.transferAmount(1L, 2L, transaction));
	}
	
	@Test
	@DisplayName("Uma exceção de valor de depósito inválido para operação transferência deverá ser lançada")
	@Order(14)
	public void testTransferAmmount_Exception3() {
		transaction.setId(4L);
		transaction.setValue(-100.0);
		assertThrows(TransactionException.class, () -> transactionService.transferAmount(1L, 2L, transaction));
	}

}
