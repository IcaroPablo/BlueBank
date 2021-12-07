package com.bluebank.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bluebank.project.dtos.LoanDTO;
import com.bluebank.project.dtos.TransferenceDTO;
import com.bluebank.project.exception.ConstraintException;
import com.bluebank.project.exception.PersistenceException;
import com.bluebank.project.exception.ResourceNotFoundException;
import com.bluebank.project.exception.TransactionException;
import com.bluebank.project.models.Loan;
import com.bluebank.project.services.LoanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/emprestimo")
@Api(value="API REST empréstimo")
@CrossOrigin(origins="*")

public class LoanController {
  
  @Autowired
  LoanService loanService;

  @PostMapping("/{cpfcnpj}")
  @ApiOperation(value="Este método cria um empréstimo")
  @ResponseStatus(HttpStatus.CREATED)
	public LoanDTO registerLoan(@PathVariable("cpfcnpj") String cpfcnpj, @Validated @RequestBody Loan emprestimo, BindingResult br) throws ResourceNotFoundException, ConstraintException, PersistenceException {
    if(br.hasErrors()) throw new ConstraintException("Não foi possível criar o empréstimo: " + br.getAllErrors().get(0).getDefaultMessage());			
    try {
      return loanService.createLoan(cpfcnpj, emprestimo);
		} catch (ConstraintException e){
			throw new ConstraintException(e.getMessage());
		} catch (Exception e) {
			throw new PersistenceException("Um erro ocorrou ao cadastrar o cliente: " + e.getMessage());
		}
  }

  @GetMapping("/id/{emprestimoId}")
  @ApiOperation(value="Este método consulta um empréstimo pelo id")
  @ResponseStatus(HttpStatus.OK)
  public LoanDTO consultLoanRegistryById(@PathVariable("emprestimoId") Long emprestimoId) throws ResourceNotFoundException{
    return loanService.showLoanById(emprestimoId);
  }

  @GetMapping("/cpfcnpj/{cpfcnpj}")
  @ApiOperation(value="Este método consulta um empréstimo pelo cpf/cnpj")
  @ResponseStatus(HttpStatus.OK)
  public List<LoanDTO> consultLoanRegistryByCpfcnpj(@PathVariable("cpfcnpj") String cpfcnpj) throws ResourceNotFoundException{
    return loanService.showLoanByClientCpfcnpj(cpfcnpj);
  }

  @PostMapping("/pagamento/{emprestimoId}/{contaId}")
  @ApiOperation(value="Este método faz o pagamento de um empréstimo")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public TransferenceDTO payLoan(@PathVariable("emprestimoId") Long loanId, @PathVariable("contaId") Long accountId) throws ResourceNotFoundException, TransactionException {
    return loanService.payLoan(loanId, accountId);
  }
}
