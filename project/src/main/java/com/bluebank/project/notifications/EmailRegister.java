package com.bluebank.project.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/emailsns")
@Api(value="API REST Conta")
@CrossOrigin(origins="*")
public class EmailRegister {

	@Autowired
	RegisterService topicRegister;
	
	@PostMapping("/{email}")
	@ApiOperation(value="Cadastra um email para notificações")
	@ResponseStatus(HttpStatus.CREATED)
	public void registerEmail(@PathVariable("email") String email) {// throws ResourceNotFoundException, ConstraintException, PersistenceException{

		String aws_id = System.getenv("AWS_ID");
		String aws_secret = System.getenv("AWS_SECRET");
		AmazonSNSClient snsClient = (AmazonSNSClient)AmazonSNSClient.builder()//.standard().build();
																	.withRegion(Regions.US_EAST_2)
																	.withCredentials(new AWSStaticCredentialsProvider
																					(new BasicAWSCredentials(aws_id, aws_secret)))
																	.build();
		
		topicRegister.subEmail(snsClient, "arn:aws:sns:us-east-2:965934840569:startrek", email);

	}
}
