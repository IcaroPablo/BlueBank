package com.bluebank.project.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.AmazonSNSException;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sns.model.UnsubscribeRequest;
import com.amazonaws.services.sns.model.UnsubscribeResult;

@Service
public class NotificationService {
	
	private AmazonSNSClient snsClient;
	private String topicARN;
	
	NotificationService() {
		String aws_id = System.getenv("AWS_ID");
		String aws_secret = System.getenv("AWS_SECRET");
		snsClient = (AmazonSNSClient)AmazonSNSClient.builder()
													.withRegion(Regions.US_EAST_2)
													.withCredentials(new AWSStaticCredentialsProvider
																	(new BasicAWSCredentials(aws_id, aws_secret)))
													.build();
		topicARN = "arn:aws:sns:us-east-2:965934840569:startrek";
	}

    public void subscribeEmail(String email) {

        try {
        	SubscribeResult result = snsClient.subscribe(topicARN, "email", email);
            System.out.println("Subscription ARN: " + 
            				   result.getSubscriptionArn() + 
            				   "\n\n Status is " + result.getSdkHttpMetadata());

        } catch (AmazonSNSException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }
    
    public void unsubscribeEmail(String email) {

    	String subscriptionARN = "";
        try {
        	List<Subscription> subscriptionList = snsClient.listSubscriptionsByTopic(topicARN).getSubscriptions();
        	
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
    
    public void sendMessageToTopic(String message) {
    	PublishRequest publishRequest = new PublishRequest(topicARN, message);
    	snsClient.publish(publishRequest);	
    }

}
