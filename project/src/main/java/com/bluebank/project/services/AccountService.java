package com.bluebank.project.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluebank.project.dtos.AccountDTO;
import com.bluebank.project.enums.AccountStatusEnum;
import com.bluebank.project.mappers.AccountMapper;
import com.bluebank.project.models.Client;
import com.bluebank.project.models.Account;
import com.bluebank.project.repositories.ClientRepository;
import com.bluebank.project.repositories.AccountRepository;

@Service
public class AccountService {

	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	AccountMapper accountMapper;
	
	@Transactional
	public AccountDTO registerNewAccount(String cpfcnpj, Account account) {
		account.setClient(clientRepository.findByCpfcnpj(cpfcnpj));
		account.setDateForReference(java.util.Calendar.getInstance().getTime());
		account.setStatus(AccountStatusEnum.Ativa);
		
		AccountDTO accountDTOAux = new AccountDTO();
		accountMapper.updateDtoFromAccount(accountRepository.save(account), accountDTOAux);
		return accountDTOAux;
	}
	
	@Transactional
	public AccountDTO showAccountById(Long id) throws IllegalArgumentException {
		Account accountAux = accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conta Inexistente"));
		return accountMapper.updateDtoFromAccount(accountAux, new AccountDTO());
	}
	
	@Transactional
	public List<AccountDTO> showAccountsByClientCpfcnpj(String cpfcnpj) throws IllegalArgumentException {
		List<AccountDTO> listAccountDTOAux = new ArrayList<AccountDTO>();
		for (Account account : accountRepository.findByClientId_Cpfcnpj(cpfcnpj)) {
			listAccountDTOAux.add(accountMapper.updateDtoFromAccount(account, new AccountDTO()));
		}
		return listAccountDTOAux;
	}
	
	@Transactional
	public AccountDTO changeAccountHolder(Long id, String cpfcnpj) throws IllegalArgumentException {
		Account accountAux = accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conta Inexistente"));
		Client clientAux = clientRepository.findByCpfcnpj(cpfcnpj);
		accountAux.setClient(clientAux);
		return accountMapper.updateDtoFromAccount(accountRepository.save(accountAux), new AccountDTO());
	}

	@Transactional
	public void deactivateAccountById(Long id) {
		Account accountAux = accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conta Inexistente"));
		accountAux.setStatus(AccountStatusEnum.Inativa);
		accountAux.setDateForReference(java.util.Calendar.getInstance().getTime());
		accountRepository.save(accountAux);
	}
	
	@Transactional
	public void deactivateAccountsByClientCpfcnpj(String cpfcnpj) {
		List<Account> listContaAux = accountRepository.findByClientId_Cpfcnpj(cpfcnpj);
		for (Account accountAux : listContaAux) {
			accountAux.setStatus(AccountStatusEnum.Inativa);
			accountAux.setDateForReference(java.util.Calendar.getInstance().getTime());
			accountRepository.save(accountAux);			
		}
	}
}
