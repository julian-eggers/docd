package com.itelg.docker.docd.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.config.annotation.SqsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.itelg.docker.docd.listener.AwsSqsWebHookEventListener;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnExpression("'${webhookevent.queue.awssqs.access-key}' != ''")
@Import(SqsConfiguration.class)
@Slf4j
@Setter
public class AwsSqsConfiguration
{
    @Value("${webhookevent.queue.awssqs.access-key}")
    private String accessKey;

    @Value("${webhookevent.queue.awssqs.secret-key}")
    private String secretKey;

    @Value("${webhookevent.queue.awssqs.region}")
    private String region;

    @Value("${webhookevent.queue.awssqs.queue-name}")
    private String queueName;

    @PostConstruct
    public void init()
    {
        log.info("AwsSqs-Listener activated");
    }

    @Bean
    public AmazonSQSAsync awsSqsAsync()
    {
        AWSCredentialsProvider credentials = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
        return AmazonSQSAsyncClientBuilder.standard().withRegion(resolveRegions(region)).withCredentials(credentials).build();
    }

    private static Regions resolveRegions(String region)
    {
        try
        {
            return Regions.valueOf(region);
        }
        catch (Exception e)
        {
            return Regions.fromName(region);
        }
    }

    @Bean
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSqs)
    {
        SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
        factory.setAmazonSqs(amazonSqs);
        factory.setWaitTimeOut(Integer.valueOf(10));

        return factory;
    }

    @Bean
    public AwsSqsWebHookEventListener awsSqsWebHookEventListener()
    {
        return new AwsSqsWebHookEventListener();
    }
}
