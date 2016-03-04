package jp.mts.base.config;

import jp.mts.base.infrastructure.jdbc.JdbcMqEventProcessTracker;
import jp.mts.libs.event.mq.MqEventProcessFilter;
import jp.mts.libs.event.mq.MqEventProcessTracker;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
@EnableRabbit
public class MqConfig {
	
	@Autowired
	private RabbitMqSettings rabbitMqSettings;

    @Bean //Listenerの雛形になるファクトリ
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(3);
        factory.setAdviceChain(retryOperationsInterceptor());
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
    @Bean
    public RetryOperationsInterceptor retryOperationsInterceptor() {
    	return RetryInterceptorBuilder.stateless()
    			.maxAttempts(5)
    			.backOffOptions(1000, 2, 10000)
    			.build();
    }
    @Bean
    public MqEventProcessTracker mqEventProcessTracker() {
    	return new JdbcMqEventProcessTracker();
    }
    @Bean
    public MqEventProcessFilter mqEventProcessFilter() {
    	return new MqEventProcessFilter(mqEventProcessTracker());
    }
	
    //TODO ミドルのセットアップとして行う
//	@Bean
//	AmqpAdmin amqpAdmin(){
//		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
//
//		rabbitAdmin.deleteExchange("auth-access");
//		rabbitAdmin.deleteQueue("auth-access");
//		rabbitAdmin.deleteExchange("task-manage");
//		rabbitAdmin.deleteQueue("task-manage");
//		rabbitAdmin.deleteExchange("widget-store");
//		rabbitAdmin.deleteQueue("widget-store");
//		
//		Queue aa_queue = new Queue("auth-access", true);
//		FanoutExchange aa_exchange = new FanoutExchange("auth-access", true, false);
//		rabbitAdmin.declareQueue(aa_queue);
//		rabbitAdmin.declareExchange(aa_exchange);
//
//		Queue tm_queue = new Queue("task-manage", true);
//		FanoutExchange tm_exchange = new FanoutExchange("task-manage", true, false);
//		rabbitAdmin.declareQueue(tm_queue);
//		rabbitAdmin.declareExchange(tm_exchange);
//
//		Queue wm_queue = new Queue("widget-store", true);
//		FanoutExchange wm_exchange = new FanoutExchange("widget-store", true, false);
//		rabbitAdmin.declareQueue(wm_queue);
//		rabbitAdmin.declareExchange(wm_exchange);
//
//		rabbitAdmin.declareBinding(BindingBuilder.bind(aa_queue).to(aa_exchange));
//		rabbitAdmin.declareBinding(BindingBuilder.bind(tm_queue).to(aa_exchange));
//		rabbitAdmin.declareBinding(BindingBuilder.bind(tm_queue).to(tm_exchange));
//		rabbitAdmin.declareBinding(BindingBuilder.bind(wm_queue).to(wm_exchange));
//		
//		return rabbitAdmin;
//	}

}
