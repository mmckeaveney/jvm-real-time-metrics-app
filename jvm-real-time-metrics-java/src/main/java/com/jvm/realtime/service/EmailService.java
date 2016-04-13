package com.jvm.realtime.service;

import com.amazonaws.services.simpleemail.*;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.regions.*;
import com.jvm.realtime.model.AlertModel;
import com.jvm.realtime.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    static final String FROM = "martinmckeaveney@gmail.com";  // Replace with your "From" address. This address must be verified.

    public void sendAlertEmail(UserModel userToAlert, AlertModel alertModel) {

        // Construct an object to contain the recipient address.
        Destination destination = new Destination().withToAddresses(new String[]{userToAlert.getEmail()});

        // Create the subject and body of the message.
        Content subject = new Content().withData("JVM Real Time Metrics System - Alert triggered for User " +
                userToAlert.getUsername());
        Content textBody = new Content().withData("An alert has been triggered in your environment. The alert is as follows -\n" +
                alertModel.getAppName() + " " + alertModel.getMetric() + " " + alertModel.getCondition() + " " + alertModel.getCriteria());
        Body body = new Body().withText(textBody);

        // Create a message with the specified subject and body.
        Message message = new Message().withSubject(subject).withBody(body);
        try {

            // Assemble the service.
            SendEmailRequest request = new SendEmailRequest().withSource(FROM).withDestination(destination).withMessage(message);

            System.out.println("Attempting to send an service through Amazon SES by using the AWS SDK for Java...");

            // Instantiate an Amazon SES client, which will make the service call. The service call requires your AWS credentials.
            // Because we're not providing an argument when instantiating the client, the SDK will attempt to find your AWS credentials
            // using the default credential provider chain. The first place the chain looks for the credentials is in environment variables
            // AWS_ACCESS_KEY_ID and AWS_SECRET_KEY.
            // For more information, see http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/credentials.html
            AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient();

            // Choose the AWS region of the Amazon SES endpoint you want to connect to. Note that your sandbox
            // status, sending limits, and Amazon SES identity-related settings are specific to a given AWS
            // region, so be sure to select an AWS region in which you set up Amazon SES. Here, we are using
            // the US West (Oregon) region. Examples of other regions that Amazon SES supports are US_EAST_1
            // and EU_WEST_1. For a complete list, see http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html
            Region REGION = Region.getRegion(Regions.US_WEST_2);
            client.setRegion(REGION);

            // Send the service.
            client.sendEmail(request);
            LOGGER.info("Email sent to {} for alert triggered", userToAlert.getEmail());
        } catch (Exception ex) {
            LOGGER.error("Error sending alert email", ex);
        }
    }

}
