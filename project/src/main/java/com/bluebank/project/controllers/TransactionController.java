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

import com.bluebank.project.dtos.DepositDTO;
import com.bluebank.project.dtos.TransactionDTO;
import com.bluebank.project.dtos.WithdrawDTO;
import com.bluebank.project.exception.ConstraintException;
import com.bluebank.project.exception.PersistenceException;
import com.bluebank.project.exception.ResourceNotFoundException;
import com.bluebank.project.exception.TransactionException;
import com.bluebank.project.models.Transaction;
import com.bluebank.project.services.TransactionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/transacao")
@Api(value="API REST transação")
@CrossOrigin(origins="*")
public class TransactionController {
  
  @Autowired
  TransactionService transacaoService;

  @PostMapping("/saque/{id}")
  @ApiOperation(value="Este método faz um saque atraves de uma transação")
	@ResponseStatus(HttpStatus.CREATED)
	public WithdrawDTO withdraw(@Validated @PathVariable("id") Long id, @RequestBody Transaction transacao, BindingResult br) throws ResourceNotFoundException, TransactionException, PersistenceException, ConstraintException{
		if(br.hasErrors()) throw new ConstraintException("Não foi possível fazer o saque: " + br.getAllErrors().get(0).getDefaultMessage());			
		try {
			return transacaoService.withdrawAmount(id, transacao);
		} catch (TransactionException e){
			throw new TransactionException(e.getMessage());
		} catch (ConstraintException e){
			throw new ConstraintException(e.getMessage());
		} catch (Exception e) {
			throw new PersistenceException("Um erro ocorreu ao fazer o saque: " + e.getMessage());
		}
	}
	
	@PostMapping("/deposito/{id}")
	@ApiOperation(value="Este método faz um depósito atraves de uma transação")
	@ResponseStatus(HttpStatus.CREATED)
	public DepositDTO deposit(@PathVariable("id") Long id, @RequestBody Transaction transacao, BindingResult br) throws ResourceNotFoundException, TransactionException, ConstraintException, PersistenceException{
		if(br.hasErrors()) throw new ConstraintException("Náo foi possível fazer o depósito: " + br.getAllErrors().get(0).getDefaultMessage());			
		try {
			return transacaoService.depositAmount(id, transacao);
		} catch (Exception e) {
			throw new PersistenceException("Um erro ocorrou ao fazer o depósito: " + e.getMessage());
		}
	}
	
	@GetMapping("/saldo/{id}")
	@ApiOperation(value="Este método consulta o saldo de uma conta")
	@ResponseStatus(HttpStatus.OK)
	public double balance(@PathVariable("id") Long id) throws ResourceNotFoundException, TransactionException{
		return transacaoService.showAccountBalanceById(id);
	}
	
	@GetMapping("/extrato/{id}")
	@ApiOperation(value="Este método consulta o extrato de uma conta")
	@ResponseStatus(HttpStatus.OK)
	public List<TransactionDTO> extract(@PathVariable("id") Long id) throws ResourceNotFoundException, TransactionException{
		return transacaoService.showTransactionsByAccountId(id);
	}

}