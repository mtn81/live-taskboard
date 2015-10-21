package jp.mts.base.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
@EnableConfigurationProperties(RabbitMqSettings.class)
public class MqConfig {
	
	@Autowired
	private RabbitMqSettings rabbitMqSettings;

    @Bean //Listenerの雛形になるファクトリ
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(3);
        return factory;
    }
	@Bean
	public ConnectionFactory connectionFactory(){
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMqSettings.getHost());
		connectionFactory.setUsername(rabbitMqSettings.getUsername());
		connectionFactory.setPassword(rabbitMqSettings.getPassword());	
		return connectionFactory;
	}
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setExchange(rabbitMqSettings.getExchange());
        return template;
    }	
	
    //TODO ミドルのセットアップとして行う
	@Bean
	AmqpAdmin amqpAdmin(){
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());

		Queue queue = new Queue("auth-access", true);
		FanoutExchange exchange = new FanoutExchange("auth-access", true, false);
		Binding binding = BindingBuilder.bind(queue).to(exchange);

		rabbitAdmin.declareQueue(queue);
		rabbitAdmin.declareExchange(exchange);
		rabbitAdmin.declareBinding(binding);

		return rabbitAdmin;
	}

}
