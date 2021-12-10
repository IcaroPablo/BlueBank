package com.bluebank.project.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bluebank.project.dtos.ClientDTO;
import com.bluebank.project.enums.ClientStatusEnum;
import com.bluebank.project.exception.ConstraintException;
import com.bluebank.project.exception.ResourceNotFoundException;
import com.bluebank.project.models.Client;
import com.bluebank.project.repositories.ClientRepository;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class ClientServiceTest {
	@Autowired
	ClientRepository clientRepository;

	@Autowired
	ClientService clientService;

	Client client = new Client();
	ClientDTO clientDTO = new ClientDTO();

	@BeforeEach
	public void setUp() {
		client.setName("BlueBank");
		client.setCpfcnpj("11122333445");
		client.setEmail("bluebank@gmail.com");
		client.setCep("64007-800");
		client.setPhoneNumber("(86) 98119-8357");
		client.setPassword("minhasenha123");
		client.setType("PJ");
		
		clientDTO.setName("");
		clientDTO.setEmail("bluebanktest@gmail.com");
		clientDTO.setCpfcnpj("");
		clientDTO.setCep("");
		clientDTO.setPhoneNumber("");
		clientDTO.setType("");
	}

	@Test
	@DisplayName("Um cliente deverá ser cadastrado no banco de dados")
	@Order(1)
	public void testRegisterNewClient() throws ConstraintException {
		clientService.registerNewClient(client);
		Client clientAux = clientRepository.findById(client.getId()).get();
		assertNotNull(clientAux);
		assertEquals(client, clientAux);
	}

	@Test
	@DisplayName("Um cliente deverá ser encontrado pelo CPF/CNPJ")
	@Order(2)
	public void testShowClientByCpfcnpj() throws ResourceNotFoundException, Exception {
		assertNotNull(clientService.showClientByCpfcnpj(client.getCpfcnpj()));
	}
	
	@Test
	@DisplayName("Uma exceção de cliente não encontrado deverá ser lançada ao buscar pelo Id")
	@Order(3)
	public void testShowClientByCpfcnpj_Exception() {
		assertThrows(ResourceNotFoundException.class, () -> clientService.showClientByCpfcnpj("11111111"));
	}
	
	@Test
	@DisplayName("Um cliente deverá ser encontrado pelo nome")
	@Order(4)
	public void testShowClientByName() throws ResourceNotFoundException {
		assertNotNull(clientService.showClientByName(client.getName()));
	}
	
	@Test
	@DisplayName("Uma exceção de cliente não encontrado deverá ser lançada ao buscar pelo Nome")
	@Order(5)
	public void testShowClientByName_Exception() {
		assertThrows(ResourceNotFoundException.class, () -> clientService.showClientByName("joaozinho"));
	}
	
	@Test
	@DisplayName("Uma atualização de cliente deverá ser feita")
	@Order(6)
	public void testUpdateClientRegistry() throws ResourceNotFoundException {
		String emailDTO = clientDTO.getEmail();
		clientService.updateClientRegistry(client.getCpfcnpj(), clientDTO);
		String email = clientRepository.findByCpfcnpj(client.getCpfcnpj()).get().getEmail();
		assertEquals(emailDTO, email);
	}
	
	@Test
	@DisplayName("Uma exceção de cliente não encontrado deverá ser lançada atualizar um Cliente")
	@Order(7)
	public void testUpdateClientRegistry_Exception() throws ResourceNotFoundException {
		assertThrows(ResourceNotFoundException.class, () -> clientService.updateClientRegistry("111111111", clientDTO));
	}
	
	@Test
	@DisplayName("Um cliente deverá se tornar inativo")
	@Order(8)
	public void testDeactivateClientRegistry() throws ResourceNotFoundException {
		ClientStatusEnum status = ClientStatusEnum.Inativo;
		clientService.deactivateClientRegistry(client.getCpfcnpj());
		ClientStatusEnum clientStatus = clientRepository.findByCpfcnpj(client.getCpfcnpj()).get().getStatus();
		assertEquals(status, clientStatus);
	}
	
	@Test
	@DisplayName("Uma exceção de cliente não encontrado deverá ser lançada")
	@Order(9)
	public void testDeactivateClientRegistry_Exception() throws ResourceNotFoundException {
		assertThrows(ResourceNotFoundException.class, () -> clientService.deactivateClientRegistry("11111111"));
	}
}
