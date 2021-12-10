package com.bluebank.project.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/emailsns")
@Api(value="API REST Conta")
@CrossOrigin(origins="*")
public class SubscriptionManager {

	AmazonSNSClient snsClient;
	
	@Autowired
	NotificationService topicRegister;
	
	SubscriptionManager() {
		String aws_id = System.getenv("AWS_ID");
		String aws_secret = System.getenv("AWS_SECRET");
		snsClient = (AmazonSNSClient)AmazonSNSClient.builder()
													.withRegion(Regions.US_EAST_2)
													.withCredentials(new AWSStaticCredentialsProvider
																	(new BasicAWSCredentials(aws_id, aws_secret)))
													.build();
	}
	
	@PostMapping("/subscribe/{email}")
	@ApiOperation(value="Cadastra um email para notificações")
	@ResponseStatus(HttpStatus.CREATED)
	public void subscribeEmail(@PathVariable("email") String email) {// throws ResourceNotFoundException, ConstraintException, PersistenceException{
		
		topicRegister.subscribeEmail(this.snsClient, "arn:aws:sns:us-east-2:965934840569:startrek", email);

	}
	
	@PostMapping("/unsubscribe/{email}")
	@ApiOperation(value="Descadastra um email para notificações")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void unsubscribeEmail(@PathVariable("email") String email) {// throws ResourceNotFoundException, ConstraintException, PersistenceException{
		
		topicRegister.unsubscribeEmail(this.snsClient, "arn:aws:sns:us-east-2:965934840569:startrek", email);

	}
	
	@PostMapping("/send/{message}")
	@ApiOperation(value="Cadastra um email para notificações")
	@ResponseStatus(HttpStatus.OK)
	public void registerEmail(@PathVariable("message") String message, @RequestBody SNSMessageDTO snsMessageDTO) {// throws ResourceNotFoundException, ConstraintException, PersistenceException{

		String messageFromBody = snsMessageDTO.getMessage();
		if (!messageFromBody.isBlank()) message = messageFromBody;
		topicRegister.sendMessageToTopic(snsClient, "arn:aws:sns:us-east-2:965934840569:startrek", message);

	}
}
