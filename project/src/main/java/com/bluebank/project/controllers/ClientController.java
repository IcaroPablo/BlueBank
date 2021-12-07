package com.bluebank.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bluebank.project.dtos.ClientDTO;
import com.bluebank.project.exception.ConstraintException;
import com.bluebank.project.exception.PersistenceException;
import com.bluebank.project.exception.ResourceNotFoundException;
import com.bluebank.project.models.Client;
import com.bluebank.project.services.ClientService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/cliente")
@Api(value="API BlueBank")
@CrossOrigin(origins="*")
public class ClientController {

	@Autowired
	ClientService clienteService;
	
	@PostMapping()
	@ApiOperation(value="Cadastra um novo cliente com os dados pessoais")
	@ResponseStatus(HttpStatus.CREATED)
	public ClientDTO registerClient(@Validated @RequestBody Client cliente, BindingResult br) throws ConstraintException, PersistenceException{
		if(br.hasErrors()) throw new ConstraintException(br.getAllErrors().get(0).getDefaultMessage());			
		try {
			return clienteService.registerNewClient(cliente);
		} catch (ConstraintException e){
			throw new ConstraintException(e.getMessage());
		} catch (Exception e) {
			throw new PersistenceException("Um erro ocorrou ao cadastrar o cliente: " + e.getMessage());
		}
	}
	
	@GetMapping("/{cpfcnpj}")
	@ApiOperation(value="Consulta os dados de um cliente atraves do CPF ou CNPJ")
	@ResponseStatus(HttpStatus.OK)
	public ClientDTO showClientByCpfcnpj(@PathVariable("cpfcnpj") String cpfcnpj) throws ResourceNotFoundException, Exception{
		return clienteService.showClientByCpfcnpj(cpfcnpj);
	}
	
	@GetMapping("/nome/{nome}")
	@ApiOperation(value="Consulta os dados de um cliente atraves do nome")
	@ResponseStatus(HttpStatus.OK)
	public List<ClientDTO> showClientByName(@PathVariable("nome") String nome) throws ResourceNotFoundException {
		return clienteService.showClientByName(nome);
	}
	
	@PutMapping("/{cpfcnpj}")
	@ApiOperation(value="Atualiza o cadastro do cliente atraves do CPF ou CNPJ")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ClientDTO updateClientRegistry(@PathVariable("cpfcnpj") String cpfcnpj, @RequestBody ClientDTO clientDTO) throws ResourceNotFoundException {//, BindingResult br) throws DataIntegrityViolationException, Exception {
//		if(br.hasErrors()) throw new ConstraintException(br.getAllErrors().get(0).getDefaultMessage());
		return clienteService.updateClientRegistry(cpfcnpj, clientDTO);
	}
	
	@DeleteMapping("/{cpfcnpj}")
	@ApiOperation(value="Desativa o cadastro do cliente")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deactivateClientRegistry(@PathVariable("cpfcnpj") String cpfcnpj) throws ResourceNotFoundException {
		clienteService.deactivateClientRegistry(cpfcnpj);
	}
}
