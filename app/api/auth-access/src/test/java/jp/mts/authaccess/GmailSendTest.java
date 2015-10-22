package jp.mts.authaccess;

import jdk.nashorn.internal.ir.annotations.Ignore;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

//@Configuration
@EnableAutoConfiguration
public class GmailSendTest {

	@Autowired
	private JavaMailSender javaMailSender;

	@Test
	@Ignore
	public void test() {
		SpringApplication.run(GmailSendTest.class);
	}
	
	@Bean 
	public GmailSendRunner gmailSender() {
		return new GmailSendRunner(javaMailSender);
	}
	
	public static class GmailSendRunner implements CommandLineRunner {

		private JavaMailSender javaMailSender;
		
		public GmailSendRunner(JavaMailSender javaMailSender) {
			this.javaMailSender = javaMailSender;
		}

		@Override
		public void run(String... args) throws Exception {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo("nantsuka2011@gmail.com");
			message.setText("test");
			javaMailSender.send(message);
		}
	}
	
}
