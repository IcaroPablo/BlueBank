package com.bluebank.project.notifications;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.AmazonSNSException;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sns.model.UnsubscribeRequest;
import com.amazonaws.services.sns.model.UnsubscribeResult;

@Service
public class NotificationService {

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
    
    public void unsubscribeEmail(AmazonSNSClient snsClient, String topicArn, String email) {

    	String subscriptionARN = "";
        try {
        	List<Subscription> subscriptionList = snsClient.listSubscriptionsByTopic(topicArn).getSubscriptions();
        	
        	for (Subscription subscription : subscriptionList) {
        		if (subscription.getEndpoint().equals(email)) {
        			subscriptionARN = subscription.getSubscriptionArn();
        		}
        	}
        	
//        	subscriptionARN = 
        	UnsubscribeRequest unsubscribeRequest = new UnsubscribeRequest(subscriptionARN);
        	UnsubscribeResult result = snsClient.unsubscribe(unsubscribeRequest);
            System.out.println("Subscription ARN: " + 
            				   subscriptionARN + 
            				   "\n\n Status is " + result.getSdkHttpMetadata());

        } catch (AmazonSNSException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }
    
    public void sendMessageToTopic(AmazonSNSClient snsClient, String topicARN, String message) {
    	PublishRequest publishRequest = new PublishRequest(topicARN, message);
    	snsClient.publish(publishRequest);	
    }

}
