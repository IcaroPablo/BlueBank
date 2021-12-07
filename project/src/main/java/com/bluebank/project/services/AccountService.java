package com.bluebank.project.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluebank.project.dtos.AccountDTO;
import com.bluebank.project.enums.AccountStatusEnum;
import com.bluebank.project.exception.ConstraintException;
import com.bluebank.project.exception.ResourceNotFoundException;
import com.bluebank.project.mappers.AccountMapper;
import com.bluebank.project.models.Account;
import com.bluebank.project.models.Client;
import com.bluebank.project.repositories.AccountRepository;

@Service
public class AccountService {

	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	ClientService clientService;
	
	@Autowired
	AccountMapper accountMapper;
	
	public Account simpleSearchById(Long id) throws ResourceNotFoundException{
		return accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("A conta não foi encontrada"));
	}

	@Transactional
	public AccountDTO registerNewAccount(String cpfcnpj, Account account) throws ResourceNotFoundException, ConstraintException {
		account.setClient(clientService.simpleSearchByCpfcnpj(cpfcnpj));
		account.setDateForReference(java.util.Calendar.getInstance().getTime());
		account.setStatus(AccountStatusEnum.Ativa);
		
		AccountDTO accountDTOAux = new AccountDTO();
		accountMapper.updateDtoFromAccount(accountRepository.save(account), accountDTOAux);
		return accountDTOAux;
	}
	
	@Transactional
	public AccountDTO showAccountById(Long id) throws ResourceNotFoundException {
		Account accountAux = simpleSearchById(id);
		return accountMapper.updateDtoFromAccount(accountAux, new AccountDTO());
	}
	
	@Transactional
	public List<AccountDTO> showAccountsByClientCpfcnpj(String cpfcnpj) throws ResourceNotFoundException {
		clientService.simpleSearchByCpfcnpj(cpfcnpj);
		List<AccountDTO> listAccountDTOAux = new ArrayList<AccountDTO>();
		for (Account account : accountRepository.findByClientId_Cpfcnpj(cpfcnpj)) {
			listAccountDTOAux.add(accountMapper.updateDtoFromAccount(account, new AccountDTO()));
		}
		return listAccountDTOAux;
	}
	
	@Transactional
	public AccountDTO changeAccountHolder(Long id, String cpfcnpj) throws ResourceNotFoundException {
		Account accountAux = simpleSearchById(id);
		Client clientAux = clientService.simpleSearchByCpfcnpj(cpfcnpj);
		accountAux.setClient(clientAux);
		return accountMapper.updateDtoFromAccount(accountRepository.save(accountAux), new AccountDTO());
	}

	@Transactional
	public void deactivateAccountById(Long id) throws ResourceNotFoundException{
		Account accountAux = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("A conta não foi encontrada"));
		accountAux.setStatus(AccountStatusEnum.Inativa);
		accountAux.setDateForReference(java.util.Calendar.getInstance().getTime());
		accountRepository.save(accountAux);
	}
	
	@Transactional
	public void deactivateAccountsByClientCpfcnpj(String cpfcnpj) throws ResourceNotFoundException{
		clientService.simpleSearchByCpfcnpj(cpfcnpj);
		List<Account> listContaAux = accountRepository.findByClientId_Cpfcnpj(cpfcnpj);
		for (Account accountAux : listContaAux) {
			accountAux.setStatus(AccountStatusEnum.Inativa);
			accountAux.setDateForReference(java.util.Calendar.getInstance().getTime());
			accountRepository.save(accountAux);			
		}
	}
}
