package com.bluebank.project.notifications;

import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.AmazonSNSException;
import com.amazonaws.services.sns.model.SubscribeResult;

@Service
public class RegisterService {

    public void subEmail(AmazonSNSClient snsClient, String topicArn, String email) {

        try {
        	SubscribeResult result = snsClient.subscribe(topicArn, "email", email);
            System.out.println("Subscription ARN: " + 
            				   result.getSubscriptionArn() + 
            				   "\n\n Status is " + result.getSdkHttpMetadata());

        } catch (AmazonSNSException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }

}
