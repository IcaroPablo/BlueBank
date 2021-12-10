package com.bluebank.project.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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
import com.bluebank.project.models.Loan;
import com.bluebank.project.repositories.AccountRepository;
import com.bluebank.project.repositories.LoanRepository;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class LoanServiceTest {

	@Autowired
	ClientService clientService;
	@Autowired
	LoanService loanService;
	@Autowired
	LoanRepository loanRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	AccountService accountService;

	Client client = new Client();
	Client client2 = new Client();
	Account account = new Account();
	Account account2 = new Account();
	Loan loan = new Loan();

	@BeforeEach
	public void setUp() throws ConstraintException, ResourceNotFoundException {
		// Criação do banco
		client.setId(1L);
		client.setName("BlueBank");
		client.setCpfcnpj("11122333445");
		client.setEmail("bluebank@gmail.com");
		client.setCep("64007-800");
		client.setPhoneNumber("(86) 98119-6995");
		client.setPassword("minhasenha123");
		client.setType("PJ");

		// Criação de conta do banco(conta que vai receber o deposito do emprestimo)
		account.setId(1L);
		account.setAgency(1);
		account.setAccountType(AccountTypeEnum.CC);
		account.setBalance(10000.0);

		// Criação de empréstimo
		loan.setId(1L);
		loan.setStartDate(java.util.Calendar.getInstance().getTime());
		loan.setEndDate(java.util.Calendar.getInstance().getTime());
		loan.setBorrowedAmount(3000.0);
		loan.setFees(0.2);
		loan.setInstallments(3);

		// Criação do cliente
		client2.setId(2L);
		client2.setName("pedro");
		client2.setCpfcnpj("152671810");
		client2.setEmail("pedro@gmail.com");
		client2.setCep("64007-800");
		client2.setPhoneNumber("(86) 98379-8357");
		client2.setPassword("minhasenha123");
		client2.setType("PF");

		// Criação de conta do cliente
		account2.setId(2L);
		account2.setAgency(1);
		account2.setAccountType(AccountTypeEnum.CC);
		account2.setBalance(5000.0);

		clientService.registerNewClient(client);
		accountService.registerNewAccount(client.getCpfcnpj(), account);
		clientService.registerNewClient(client2);
		accountService.registerNewAccount(client2.getCpfcnpj(), account2);
		
	}

	@Test
	@DisplayName("Um empréstimo deverá ser criado")
	@Order(1)
	public void testCreateLoan() throws ResourceNotFoundException, ConstraintException {
		loanService.createLoan(client.getCpfcnpj(), loan);
		Loan loanAux = loanRepository.findById(1L).get();
		assertNotNull(loanAux);
		assertEquals(loan, loanAux);
	}

	@Test
	@DisplayName("Um empréstimo deverá ser encontrado pelo Id")
	@Order(2)
	public void testShowLoanById() throws ResourceNotFoundException {
		assertNotNull(loanService.showLoanById(1L));
	}

	@Test
	@DisplayName("Uma exceção de empréstimo não encontrado pelo Id")
	@Order(3)
	public void testShowLoanById_Exception() {
		assertThrows(ResourceNotFoundException.class, () -> loanService.showLoanById(3L));
	}

	@Test
	@DisplayName("Um empréstimo deverá ser encontrado pelo CPF/CNPJ do cliente")
	@Order(4)
	public void testShowLoanByClientCpfcnpj() throws ResourceNotFoundException {
		assertNotNull(loanService.showLoanByClientCpfcnpj(client.getCpfcnpj()));
	}

	@Test
	@DisplayName("Uma exceção de empréstimo não encontrado pelo CPF/CNPJ")
	@Order(5)
	public void testShowLoanByClientCpfcnpj_Exception() {
		assertThrows(ResourceNotFoundException.class, () -> loanService.showLoanByClientCpfcnpj("11111111"));
	}

	@Test
	@DisplayName("Um pagamento do empréstimo deverá ser realizado")
	@Order(6)
	public void testPayLoan() throws ConstraintException, ResourceNotFoundException, TransactionException {

		loanService.createLoan(client2.getCpfcnpj(), loan);
		double saldoAnterior = accountRepository.findById(2L).get().getBalance();
		loanService.payLoan(1L, 2L);
		double saldoAtual = accountRepository.findById(2L).get().getBalance();
		assertNotEquals(saldoAnterior, saldoAtual);
	}

	@Test
	@DisplayName("Uma exceção de empréstimo não encontrado deverá ser lançada ao tentar pagá-lo")
	@Order(7)
	public void testPayLoan_Exception() {
		assertThrows(ResourceNotFoundException.class, () -> loanService.payLoan(3L, 2L));
	}

	@Test
	@DisplayName("Uma exceção de conta não encontrada deverá ser lançada ao tentar pagar o empréstimo")
	@Order(8)
	public void testPayLoan_Exception2() {
		assertThrows(ResourceNotFoundException.class, () -> loanService.payLoan(1L, 3L));
	}
}
