package com.bluebank.project.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluebank.project.dtos.LoanDTO;
import com.bluebank.project.dtos.TransferenceDTO;
import com.bluebank.project.exception.ConstraintException;
import com.bluebank.project.exception.ResourceNotFoundException;
import com.bluebank.project.exception.TransactionException;
import com.bluebank.project.mappers.LoanMapper;
import com.bluebank.project.mappers.TransactionMapper;
import com.bluebank.project.models.Loan;
import com.bluebank.project.models.Transaction;
import com.bluebank.project.repositories.AccountRepository;
import com.bluebank.project.repositories.LoanRepository;

@Service
public class LoanService {

  @Autowired
  LoanRepository loanRepository;

  @Autowired
  ClientService clientService;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  LoanMapper loanMapper;

  @Autowired
  TransactionMapper transactionMapper;

  @Autowired
  TransactionService transactionService;

  @Transactional
  public LoanDTO createLoan(String cpfcnpj, Loan loan) throws ResourceNotFoundException, ConstraintException{
    loan.setClient(clientService.simpleSearchByCpfcnpj(cpfcnpj));

    LoanDTO loanDTO = new LoanDTO();
    loanMapper.updateEmprestimoDtoFromEmprestimo(loanRepository.save(loan), loanDTO);
    return loanDTO;
  }

  @Transactional
  public LoanDTO showLoanById(Long loanId) throws ResourceNotFoundException{
    Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new ResourceNotFoundException("O emprestímo não foi encontrado"));
    return loanMapper.updateEmprestimoDtoFromEmprestimo(loan, new LoanDTO());
  }

  @Transactional
  public List<LoanDTO> showLoanByClientCpfcnpj(String cpfcnpj) throws ResourceNotFoundException{
    List<LoanDTO> listLoanDTO = new ArrayList<LoanDTO>();
    clientService.simpleSearchByCpfcnpj(cpfcnpj);
    for(Loan loan : loanRepository.findByClientId_Cpfcnpj(cpfcnpj)){
      listLoanDTO.add(loanMapper.updateEmprestimoDtoFromEmprestimo(loan, new LoanDTO()));
    }
    return listLoanDTO;
  }

  @Transactional
  public TransferenceDTO payLoan(Long loanId, Long accountId) throws ResourceNotFoundException, TransactionException{
    Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new ResourceNotFoundException("O empréstimo não encontrado"));
    double moneyAmount = (loan.getBorrowedAmount() / loan.getInstallments()) + (loan.getBorrowedAmount() * loan.getFees());
    
    Transaction transaction = new Transaction();
    transaction.setValue(moneyAmount);

    return transactionService.transferAmount(accountId, 1L, transaction);
  }
  
}
