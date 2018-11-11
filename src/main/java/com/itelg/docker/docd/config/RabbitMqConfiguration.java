package com.itelg.docker.docd.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.itelg.docker.docd.listener.RabbitMqWebHookEventListener;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnExpression("'${webhookevent.queue.rabbitmq.hosts}' != ''")
@Slf4j
public class RabbitMqConfiguration
{
    @Value("${webhookevent.queue.rabbitmq.hosts}")
    private String rabbitmqAddresses;

    @Value("${webhookevent.queue.rabbitmq.username}")
    private String rabbitmqUsername;

    @Value("${webhookevent.queue.rabbitmq.password}")
    private String rabbitmqPassword;

    @Value("${webhookevent.queue.rabbitmq.exchange.name}")
    private String webHookEventExchangeName;

    @Value("${webhookevent.queue.rabbitmq.routing-key}")
    private String webHookEventRoutingKey;

    @Value("${webhookevent.queue.rabbitmq.queue-name}")
    private String webHookEventQueueName;

    @PostConstruct
    public void init()
    {
        log.info("RabbitMQ-Listener activated");
    }

    @Bean
    public CachingConnectionFactory connectionFactory()
    {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitmqAddresses);
        connectionFactory.setUsername(rabbitmqUsername);
        connectionFactory.setPassword(rabbitmqPassword);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin()
    {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public TopicExchange webHookEventExchange()
    {
        TopicExchange exchange = new TopicExchange(webHookEventExchangeName);
        rabbitAdmin().declareExchange(exchange);
        return exchange;
    }

    @Bean
    public Queue webHookEventQueue()
    {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "");
        arguments.put("x-dead-letter-routing-key", webHookEventQueueName + ".dlx");
        arguments.put("x-max-length", Long.valueOf(1000));
        Queue queue = new Queue(webHookEventQueueName, true, false, false, arguments);
        queue.setAdminsThatShouldDeclare(rabbitAdmin());
        rabbitAdmin().declareQueue(queue);
        rabbitAdmin().declareBinding(BindingBuilder.bind(queue).to(webHookEventExchange()).with(webHookEventRoutingKey));
        return queue;
    }

    @Bean
    public Queue webHookEventQueueDlx()
    {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-length", Long.valueOf(1000));
        arguments.put("x-queue-mode", "lazy");
        Queue queue = new Queue(webHookEventQueueName + ".dlx", true, false, false, arguments);
        queue.setAdminsThatShouldDeclare(rabbitAdmin());
        rabbitAdmin().declareQueue(queue);
        return queue;
    }

    @Bean
    public RabbitMqWebHookEventListener rabbitMqWebHookEventListener()
    {
        return new RabbitMqWebHookEventListener();
    }

    @Bean
    public SimpleMessageListenerContainer webHookEventListenerContainer(Queue webHookEventQueue, RabbitMqWebHookEventListener eventListener)
    {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames(webHookEventQueue.getName());
        container.setMessageListener(new MessageListenerAdapter(eventListener, "onMessage"));
        container.setDefaultRequeueRejected(false);
        return container;
    }
}