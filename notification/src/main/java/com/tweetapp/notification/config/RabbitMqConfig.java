package com.tweetapp.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;

import com.rabbitmq.client.ConnectionFactory;

@Configuration
public class RabbitMqConfig {

@Value("${queue.name}")
private String notificationDetailsQueue;

@Bean
public RabbitTemplate messageTemplate(ConnectionFactory connectionFactory) {
	RabbitTemplate rabbitTemplate=new RabbitTemplate();
	rabbitTemplate.setChannelTransacted(true);
	rabbitTemplate.setMessageConverter(converter());
	return rabbitTemplate;
}

@Bean
public Jackson2JsonMessageConverter converter() {
	
	return new Jackson2JsonMessageConverter();
}

@Bean
public Queue notificationDetailsQueue() {
	
	return new Queue(this.notificationDetailsQueue,true);
}

}
