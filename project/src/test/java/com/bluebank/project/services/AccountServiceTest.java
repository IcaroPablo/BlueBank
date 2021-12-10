package com.bluebank.project.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bluebank.project.enums.AccountStatusEnum;
import com.bluebank.project.enums.AccountTypeEnum;
import com.bluebank.project.exception.ConstraintException;
import com.bluebank.project.exception.ResourceNotFoundException;
import com.bluebank.project.models.Account;
import com.bluebank.project.models.Client;
import com.bluebank.project.repositories.AccountRepository;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class AccountServiceTest {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	AccountService accountService;

	@Autowired
	ClientService clientService;

	Client client2 = new Client();
	Client client = new Client();
	Account account = new Account();

	@BeforeEach
	public void setUp() {
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
	}

	@Test
	@DisplayName("Uma conta deverá ser criada e associada a um cliente")
	@Order(1)
	public void testRegisterNewAccount() throws ConstraintException, ResourceNotFoundException {
		clientService.registerNewClient(client);
		accountService.registerNewAccount(client.getCpfcnpj(), account);
		Account accountAux = accountRepository.findById(1L).get();
		assertNotNull(accountAux);
		assertEquals(account, accountAux);
	}

	@Test
	@DisplayName("Uma conta deverá ser encontrada pelo ID")
	@Order(2)
	public void testShowAccountById() throws ResourceNotFoundException {
		assertNotNull(accountService.showAccountById(1L));
	}
	
	@Test
	@DisplayName("Uma exceção de conta inexistente deverá lançada ao buscar pelo ID")
	@Order(3)
	public void testShowAccountById_Exception() {
		assertThrows(ResourceNotFoundException.class, () -> accountService.showAccountById(3L));
	}

	@Test
	@DisplayName("Conta(s) deverá(ão) ser encontrada(s) pelo CPF/CNPJ do seu titular")
	@Order(4)
	public void testShowAccountsByClientCpfcnpj() throws ResourceNotFoundException {
		assertNotNull(accountService.showAccountsByClientCpfcnpj(client.getCpfcnpj()));
	}
	
	@Test
	@DisplayName("Uma exceção de conta inexistente deverá lançada ao buscar pelo CPF/CNPJ do seu titular")
	@Order(5)
	public void testShowAccountsByClientCpfcnpj_Exception() {
		assertThrows(ResourceNotFoundException.class, () -> accountService.showAccountsByClientCpfcnpj("1111111111"));
	}

	@Test
	@DisplayName("Uma atualização do cliente de uma conta deverá ser feita")
	@Order(6)
	@Transactional
	public void testChangeAccountHolder() throws ConstraintException, ResourceNotFoundException {
		// Para alteração de conta
		client2.setName("BlueBankTest");
		client2.setCpfcnpj("55522211106");
		client2.setEmail("bluebank@gmail.com");
		client2.setCep("64007-800");
		client2.setPhoneNumber("(86) 98119-8357");
		client2.setPassword("minhasenha123");
		client2.setType("PJ");

		clientService.registerNewClient(client2);
		accountService.changeAccountHolder(1L, client2.getCpfcnpj());

		String cpfcnpj2 = client2.getCpfcnpj();
		String cpfcnpj = accountRepository.getById(1L).getClient().getCpfcnpj();
		
		assertEquals(cpfcnpj2, cpfcnpj); 
	}
	
	@Test
	@DisplayName("Uma exceção de conta inexistente deverá lançada ao alterar o titular de uma conta")
	@Order(7)
	public void testChangeAccountHolder_Exception() {
		assertThrows(ResourceNotFoundException.class, () -> accountService.changeAccountHolder(3L, client2.getCpfcnpj()));
	}
	
	@Test
	@DisplayName("Uma exceção cliente com CPF/CNPJ inexistente deverá lançada ao alterar o titular de uma conta")
	@Order(8)
	public void testChangeAccountHolder_Exception2() {
		assertThrows(ResourceNotFoundException.class, () -> accountService.changeAccountHolder(1L, "111111111"));
	}
	
	@Test
	@DisplayName("A desativação de uma conta deverá ser feita pelo seu Id")
	@Order(9)
	public void testDeactivateAccountById() throws ResourceNotFoundException {
		AccountStatusEnum status = AccountStatusEnum.Inativa;
		accountService.deactivateAccountById(1L);
		AccountStatusEnum accountStatus = accountRepository.findById(1L).get().getStatus();
		assertEquals(status, accountStatus);
	}
	
	@Test
	@DisplayName("Uma exceção de conta inexistente deverá lançada ao desativar uma conta pelo Id")
	@Order(10)
	public void testDeactivateAccountById_Exception() {
		assertThrows(ResourceNotFoundException.class, () -> accountService.deactivateAccountById(3L));
	}

	@Test
	@DisplayName("A desativação de uma conta deverá ser feita pelo CPF/CNPJ do seu titular")
	@Order(11)
	@Transactional
	public void testDeactivateAccountsByClientCpfcnpj() throws ResourceNotFoundException {
		AccountStatusEnum status = AccountStatusEnum.Inativa;
		accountService.deactivateAccountsByClientCpfcnpj(accountRepository.getById(1L).getClient().getCpfcnpj());
		AccountStatusEnum accountStatus = accountRepository.findById(1L).get().getStatus();
		assertEquals(status, accountStatus);
	}
	
	@Test
	@DisplayName("Uma exceção de conta inexistente deverá lançada ao desativar uma conta pelo CPF/CNPJ")
	@Order(12)
	public void testDeactivateAccountsByClientCpfcnpj_Exception() {
		assertThrows(ResourceNotFoundException.class, () -> accountService.deactivateAccountsByClientCpfcnpj("11111111"));
	}
}
