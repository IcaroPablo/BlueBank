package com.bluebank.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.bluebank.project.dtos.SNSMessageDTO;
import com.bluebank.project.services.NotificationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/emailsns")
@Api(value="Simples sistema para gerenciamento de notificações")
@CrossOrigin(origins="*")
public class NotificationController {
	
	@Autowired
	NotificationService topicRegister;
	
	@PostMapping("/subscribe/{email}")
	@ApiOperation(value="Cadastra um email para que receba notificações")
	@ResponseStatus(HttpStatus.CREATED)
	public void subscribeEmail(@PathVariable("email") String email) {// throws ResourceNotFoundException, ConstraintException, PersistenceException{
		
		topicRegister.subscribeEmail(email);

	}
	
	@PostMapping("/send")
	@ApiOperation(value="Envia uma mensagem para os emails cadastrados")
	@ResponseStatus(HttpStatus.OK)
	public void registerEmail(@RequestBody SNSMessageDTO snsMessageDTO) {// throws ResourceNotFoundException, ConstraintException, PersistenceException{

		String message = snsMessageDTO.getMessage();
		topicRegister.sendMessageToTopic(message);

	}
	
	@DeleteMapping("/unsubscribe/{email}")
	@ApiOperation(value="Descadastra um email para que pare de receber notificações")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void unsubscribeEmail(@PathVariable("email") String email) {// throws ResourceNotFoundException, ConstraintException, PersistenceException{
		
		topicRegister.unsubscribeEmail(email);

	}

}
