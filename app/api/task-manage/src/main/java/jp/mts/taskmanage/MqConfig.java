package jp.mts.taskmanage;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class MqConfig {

	@Bean
	ConnectionFactory connectionFactory(){
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("192.168.77.11");
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");	
		return connectionFactory;
	}
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setExchange("task-manage");
        return template;
    }	
	
    //TODO ミドルのセットアップとして行う
	@Bean
	AmqpAdmin amqpAdmin(){
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());

		Queue queue = new Queue("task-manage", true);
		FanoutExchange exchange = new FanoutExchange("task-manage", true, false);
		Binding binding = BindingBuilder.bind(queue).to(exchange);

		rabbitAdmin.declareQueue(queue);
		rabbitAdmin.declareExchange(exchange);
		rabbitAdmin.declareBinding(binding);

		return rabbitAdmin;
	}

//	@Bean
//	Queue queue() {
//		return new Queue("test-queue", true);
//	}
//
//	@Bean
//	FanoutExchange exchange() {
//		return new FanoutExchange("spring-boot-exchange", true, false);
//	}
//
//	@Bean
//	Binding binding(Queue queue, TopicExchange exchange) {
//		return BindingBuilder.bind(queue).to(exchange).with("");
//	}
	
}
