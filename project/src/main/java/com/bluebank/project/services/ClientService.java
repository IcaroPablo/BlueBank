package com.bluebank.project.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluebank.project.dtos.ClientDTO;
import com.bluebank.project.enums.ClientStatusEnum;
import com.bluebank.project.exception.ResourceNotFoundException;
import com.bluebank.project.mappers.ClientMapper;
import com.bluebank.project.models.Client;
import com.bluebank.project.repositories.ClientRepository;

@Service
public class ClientService {

	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	ClientMapper clientMapper;
	
	@Autowired
	AccountService accountService;
	
	@Transactional
	public ClientDTO registerNewClient(Client client) {
		ClientDTO clientDTO = new ClientDTO();
		clientMapper.updateDtoFromClient(client, clientDTO);
		clientRepository.save(client);
		return clientDTO;
	}
	
	public Client simpleSearchByCpfcnpj(String cpfcnpj) throws ResourceNotFoundException {
		return clientRepository.findByCpfcnpj(cpfcnpj).orElseThrow(() -> new ResourceNotFoundException("tem não otario, errou"));
	}
	
	@Transactional
	public ClientDTO showClientByCpfcnpj(String cpfcnpj) throws ResourceNotFoundException, Exception{
		Client client = new Client();
		ClientDTO clientDTO = new ClientDTO();
		
		simpleSearchByCpfcnpj(cpfcnpj);
		clientMapper.updateDtoFromClient(client, clientDTO);
		return clientDTO;
	}
	
	@Transactional
	public List<ClientDTO> showClientByName(String name) throws ResourceNotFoundException {
		List<Client> listClientAux = clientRepository.findByNameContaining(name);
		List<ClientDTO> listClientDTOAux = new ArrayList<>();
		for(Client clientAux : listClientAux) {
			listClientDTOAux.add(clientMapper.updateDtoFromClient(clientAux, new ClientDTO()));
		}
		return listClientDTOAux;
	}
	
	@Transactional
	public ClientDTO updateClientRegistry(String cpfcnpj, ClientDTO clientDTO) throws ResourceNotFoundException {
	    Client clientAux = simpleSearchByCpfcnpj(cpfcnpj);
	    clientMapper.updateClientFromDto(clientDTO, clientAux);
	    clientMapper.updateDtoFromClient(clientAux, clientDTO);
	    clientRepository.save(clientAux);
		return clientDTO;
	}

	@Transactional
	public void deactivateClientRegistry(String cpfcnpj) throws ResourceNotFoundException {
		Client clientAux = simpleSearchByCpfcnpj(cpfcnpj);
		clientAux.setStatus(ClientStatusEnum.Inativo);
		accountService.deactivateAccountsByClientCpfcnpj(cpfcnpj);
		clientRepository.save(clientAux);
	}

}
